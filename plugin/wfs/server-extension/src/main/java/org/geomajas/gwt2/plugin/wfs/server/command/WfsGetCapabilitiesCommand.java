/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.gwt2.plugin.wfs.server.command;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.opengis.wfs.FeatureTypeListType;
import net.opengis.wfs.FeatureTypeType;
import net.opengis.wfs.WFSCapabilitiesType;

import org.geomajas.command.CommandHasRequest;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.gwt2.plugin.wfs.client.service.WfsService.WfsVersion;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsGetCapabilitiesRequest;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsGetCapabilitiesResponse;
import org.geomajas.gwt2.plugin.wfs.server.command.factory.URLBuilder;
import org.geomajas.gwt2.plugin.wfs.server.command.factory.WfsDataStoreFactory;
import org.geomajas.gwt2.plugin.wfs.server.command.factory.WfsHttpClientFactory;
import org.geomajas.gwt2.plugin.wfs.server.command.factory.impl.DefaultWfsDataStoreFactory;
import org.geomajas.gwt2.plugin.wfs.server.command.factory.impl.DefaultWfsHttpClientFactory;
import org.geomajas.gwt2.plugin.wfs.server.dto.WfsFeatureTypeDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.WfsFeatureTypeListDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.WfsGetCapabilitiesDto;
import org.geomajas.service.DtoConverterService;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.data.wfs.internal.WFSGetCapabilities;
import org.geotools.data.wfs.internal.v1_x.FeatureTypeInfoImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Command that issues WFS GetCapabilities request.
 * 
 * @author Jan De Moerloose
 *
 */
@Component(WfsGetCapabilitiesRequest.COMMAND_NAME)
public class WfsGetCapabilitiesCommand implements
		CommandHasRequest<WfsGetCapabilitiesRequest, WfsGetCapabilitiesResponse> {

	private final Logger log = LoggerFactory.getLogger(WfsGetCapabilitiesCommand.class);

	@Autowired
	private DtoConverterService dtoConverterService;

	private WfsDataStoreFactory dataStoreFactory;

	private WfsHttpClientFactory httpClientFactory;

	public WfsGetCapabilitiesCommand() {
		dataStoreFactory = new DefaultWfsDataStoreFactory();
		httpClientFactory = new DefaultWfsHttpClientFactory();
	}

	public void setDataStoreFactory(WfsDataStoreFactory dataStoreFactory) {
		this.dataStoreFactory = dataStoreFactory;
	}

	public void setHttpClientFactory(WfsHttpClientFactory httpClientFactory) {
		this.httpClientFactory = httpClientFactory;
	}

	@Override
	public void execute(WfsGetCapabilitiesRequest request, WfsGetCapabilitiesResponse response)
			throws GeomajasException {
		try {
			String sourceUrl = request.getBaseUrl();
			URL targetUrl = httpClientFactory.getTargetUrl(sourceUrl);
			
			// Create a WFS GetCapabilities URL:
			URL url = URLBuilder.createWfsURL(targetUrl, request.getVersion(), "GetCapabilities");
			String capa = url.toExternalForm();
			
			Map<String, Serializable> connectionParameters = new HashMap<String, Serializable>();
			connectionParameters.put(WFSDataStoreFactory.URL.key, capa);
			connectionParameters.put(WFSDataStoreFactory.TIMEOUT.key, 10000);
			if (request.getStrategy() != null) {
				connectionParameters.put(WFSDataStoreFactory.WFS_STRATEGY.key, request.getStrategy());
			}
			WFSDataStore wfs = dataStoreFactory.createDataStore(connectionParameters,
					httpClientFactory.create(sourceUrl));
			// The following uses internal geotools classes, anyone knows a more generic way to get the feature types
			// ???
			WfsFeatureTypeListDto wfsFeatureTypeListDto = null;
			String vs = wfs.getWfsClient().getInfo().getVersion();
			WfsVersion version = WfsVersion.fromString(vs);
			switch (version) {
				case V1_0_0:
				case V1_1_0:
					wfsFeatureTypeListDto = create1xxFeatureTypeList(wfs);
					break;
				case V2_0_0:
				default:
					wfsFeatureTypeListDto = create200FeatureTypeList(wfs);
					break;
			}
			WfsGetCapabilitiesDto getCapabilitiesDto = new WfsGetCapabilitiesDto(wfsFeatureTypeListDto);
			response.setGetCapabilitiesDto(getCapabilitiesDto);
		} catch (Exception e) {
			log.error("GetCapabilities failed for " + request.getBaseUrl(), e);
			throw new GeomajasException(ExceptionCode.UNEXPECTED_PROBLEM, e.getMessage());
		}
	}

	private WfsFeatureTypeListDto create1xxFeatureTypeList(WFSDataStore wfs) {
		WFSGetCapabilities capabilities = wfs.getWfsClient().getCapabilities();
		WFSCapabilitiesType caps = (WFSCapabilitiesType) capabilities.getParsedCapabilities();
		FeatureTypeListType featureTypeListType = caps.getFeatureTypeList();
		WfsFeatureTypeListDto featureTypeListDto = new WfsFeatureTypeListDto();
		for (Object o : featureTypeListType.getFeatureType()) {
			FeatureTypeInfoImpl featureType = new FeatureTypeInfoImpl((FeatureTypeType) o);
			WfsFeatureTypeDto wfsFeatureTypeDto = new WfsFeatureTypeDto();
			// The name needs a prefix to be unique, use the literal name element here
			String prefix = ((FeatureTypeType) o).getName().getPrefix();
			String localPart = ((FeatureTypeType) o).getName().getLocalPart();
			String fullName = (prefix == null || prefix.isEmpty()) ? localPart : prefix + ":" + localPart;
			wfsFeatureTypeDto.setName(fullName);
			wfsFeatureTypeDto.setAbstract(featureType.getAbstract());
			wfsFeatureTypeDto.setTitle(featureType.getTitle());
			wfsFeatureTypeDto.setDefaultCrs(featureType.getDefaultSRS());
			ReferencedEnvelope bbox = featureType.getWGS84BoundingBox();
			if (bbox != null) {
				wfsFeatureTypeDto.setWGS84BoundingBox(dtoConverterService.toDto(bbox));
			} else {
				wfsFeatureTypeDto.setWGS84BoundingBox(new Bbox(-180, -90, 360, 180));
			}
			wfsFeatureTypeDto.setKeywords(new ArrayList<String>(featureType.getKeywords()));
			featureTypeListDto.add(wfsFeatureTypeDto);
		}
		return featureTypeListDto;
	}

	private WfsFeatureTypeListDto create200FeatureTypeList(WFSDataStore wfs) {
		WFSGetCapabilities capabilities = wfs.getWfsClient().getCapabilities();
		net.opengis.wfs20.WFSCapabilitiesType caps = (net.opengis.wfs20.WFSCapabilitiesType) capabilities
				.getParsedCapabilities();
		net.opengis.wfs20.FeatureTypeListType featureTypeListType = caps.getFeatureTypeList();
		WfsFeatureTypeListDto featureTypeListDto = new WfsFeatureTypeListDto();
		for (Object o : featureTypeListType.getFeatureType()) {
			org.geotools.data.wfs.internal.v2_0.FeatureTypeInfoImpl featureType = 
					new org.geotools.data.wfs.internal.v2_0.FeatureTypeInfoImpl(
					(net.opengis.wfs20.FeatureTypeType) o);
			WfsFeatureTypeDto wfsFeatureTypeDto = new WfsFeatureTypeDto();
			// The name needs a prefix to be unique, use the literal name element here
			String prefix = featureType.getQName().getPrefix();
			String localPart = featureType.getQName().getLocalPart();
			String fullName = (prefix == null || prefix.isEmpty()) ? localPart : prefix + ":" + localPart;
			wfsFeatureTypeDto.setName(fullName);
			wfsFeatureTypeDto.setAbstract(featureType.getAbstract());
			wfsFeatureTypeDto.setTitle(featureType.getTitle());
			wfsFeatureTypeDto.setDefaultCrs(featureType.getDefaultSRS());
			ReferencedEnvelope bbox = featureType.getWGS84BoundingBox();
			if (bbox != null) {
				wfsFeatureTypeDto.setWGS84BoundingBox(dtoConverterService.toDto(bbox));
			} else {
				wfsFeatureTypeDto.setWGS84BoundingBox(new Bbox(-180, -90, 360, 180));
			}
			wfsFeatureTypeDto.setKeywords(new ArrayList<String>(featureType.getKeywords()));
			featureTypeListDto.add(wfsFeatureTypeDto);
		}
		return featureTypeListDto;
	}

	@Override
	public WfsGetCapabilitiesRequest getEmptyCommandRequest() {
		return new WfsGetCapabilitiesRequest();
	}

	@Override
	public WfsGetCapabilitiesResponse getEmptyCommandResponse() {
		return new WfsGetCapabilitiesResponse();
	}

}
