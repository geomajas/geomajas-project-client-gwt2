package org.geomajas.gwt2.plugin.wfs.server.command;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.opengis.wfs.FeatureTypeListType;
import net.opengis.wfs.FeatureTypeType;
import net.opengis.wfs.WFSCapabilitiesType;

import org.geomajas.command.Command;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsGetCapabilitiesRequest;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsGetCapabilitiesResponse;
import org.geomajas.gwt2.plugin.wfs.server.command.factory.HttpClientFactory;
import org.geomajas.gwt2.plugin.wfs.server.command.factory.WfsDataStoreFactory;
import org.geomajas.gwt2.plugin.wfs.server.command.factory.impl.DefaultHttpClientFactory;
import org.geomajas.gwt2.plugin.wfs.server.command.factory.impl.DefaultWfsDataStoreFactory;
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

@Component(WfsGetCapabilitiesRequest.COMMAND_NAME)
public class WfsGetCapabilitiesCommand implements Command<WfsGetCapabilitiesRequest, WfsGetCapabilitiesResponse> {

	private final Logger log = LoggerFactory.getLogger(WfsGetCapabilitiesCommand.class);

	@Autowired
	private DtoConverterService dtoConverterService;

	private WfsDataStoreFactory dataStoreFactory;

	private HttpClientFactory httpClientFactory;

	public WfsGetCapabilitiesCommand() {
		dataStoreFactory = new DefaultWfsDataStoreFactory();
		httpClientFactory = new DefaultHttpClientFactory();
	}

	public void setDataStoreFactory(WfsDataStoreFactory dataStoreFactory) {
		this.dataStoreFactory = dataStoreFactory;
	}

	public void setHttpClientFactory(HttpClientFactory httpClientFactory) {
		this.httpClientFactory = httpClientFactory;
	}

	public void execute(WfsGetCapabilitiesRequest request, WfsGetCapabilitiesResponse response)
			throws GeomajasException {
		// Create a WFS GetCapabilities URL:
		String capa = request.getBaseUrl() + "?service=wfs&version=1.0.0&request=GetCapabilities";

		Map<String, Serializable> connectionParameters = new HashMap<String, Serializable>();
		connectionParameters.put(WFSDataStoreFactory.URL.key, capa);
		connectionParameters.put(WFSDataStoreFactory.TIMEOUT.key, 10000);
		try {
			WFSDataStore wfs = dataStoreFactory.createDataStore(connectionParameters,
					httpClientFactory.getClientForUrl(capa));
			// The following uses internal geotools classes, but no other obvious way to get the feature types ???
			WFSGetCapabilities capabilities = wfs.getWfsClient().getCapabilities();
			WFSCapabilitiesType caps = (WFSCapabilitiesType) capabilities.getParsedCapabilities();
			FeatureTypeListType featureTypeListType = caps.getFeatureTypeList();
			WfsFeatureTypeListDto featureTypeListDto = new WfsFeatureTypeListDto();
			for (Object o : featureTypeListType.getFeatureType()) {
				FeatureTypeInfoImpl featureType = new FeatureTypeInfoImpl((FeatureTypeType) o);
				WfsFeatureTypeDto wfsFeatureTypeDto = new WfsFeatureTypeDto();
				// The name needs a prefix to be unique, use the literal name element here
				String fullName = ((FeatureTypeType) o).getName().getPrefix() + ":"
						+ ((FeatureTypeType) o).getName().getLocalPart();
				wfsFeatureTypeDto.setName(fullName);
				wfsFeatureTypeDto.setAbstract(featureType.getAbstract());
				wfsFeatureTypeDto.setTitle(featureType.getTitle());
				wfsFeatureTypeDto.setDefaultCrs(featureType.getDefaultSRS());
				ReferencedEnvelope bbox = featureType.getWGS84BoundingBox();
				wfsFeatureTypeDto.setWGS84BoundingBox(dtoConverterService.toDto(bbox));
				wfsFeatureTypeDto.setKeywords(new ArrayList<String>(featureType.getKeywords()));
				featureTypeListDto.add(wfsFeatureTypeDto);
			}
			WfsGetCapabilitiesDto getCapabilitiesDto = new WfsGetCapabilitiesDto(featureTypeListDto);
			response.setGetCapabilitiesDto(getCapabilitiesDto);
		} catch (IOException e) {
			log.error("GetCapabilities failed for " + request.getBaseUrl(), e);
			throw new GeomajasException(ExceptionCode.UNEXPECTED_PROBLEM, e.getMessage());
		}
	}

	public WfsGetCapabilitiesResponse getEmptyCommandResponse() {
		return new WfsGetCapabilitiesResponse();
	}

}
