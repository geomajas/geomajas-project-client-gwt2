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
package org.geomajas.gwt2.plugin.wfs.client.service;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.CommandResponse;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureCollectionInfo;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeDescriptionInfo;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsGetCapabilitiesInfo;
import org.geomajas.gwt2.plugin.wfs.client.query.dto.QueryDto;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsDescribeFeatureTypeRequest;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsDescribeFeatureTypeResponse;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsGetCapabilitiesRequest;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsGetCapabilitiesResponse;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsGetFeatureRequest;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsGetFeatureResponse;
import org.geomajas.gwt2.plugin.wfs.server.dto.WfsFeatureCollectionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.WfsVersionDto;

import com.google.gwt.core.client.Callback;

/**
 * Implementation of {@link WfsService} that uses the Geomajas server.
 * @author Jan De Moerloose
 *
 */
public class WfsProxyServiceImpl implements WfsService {

	@Override
	public void getCapabilities(WfsVersion version, String baseUrl,
			final Callback<WfsGetCapabilitiesInfo, String> callback) {
		WfsGetCapabilitiesRequest request = new WfsGetCapabilitiesRequest(baseUrl);
		request.setVersion(WfsVersionDto.fromString(version.toString()));
		GwtCommand command = new GwtCommand(WfsGetCapabilitiesRequest.COMMAND_NAME);
		command.setCommandRequest(request);
		GeomajasServerExtension.getInstance().getCommandService()
				.execute(command, new AbstractCommandCallback<WfsGetCapabilitiesResponse>() {

					@Override
					public void execute(WfsGetCapabilitiesResponse response) {
						callback.onSuccess(response.getGetCapabilitiesDto());
					}

					@Override
					public void onCommunicationException(Throwable error) {
						callback.onFailure(error.getMessage());
					}

					@Override
					public void onCommandException(CommandResponse response) {
						callback.onFailure(response.getErrorMessages().get(0));
					}

				});
	}

	@Override
	public void describeFeatureType(WfsVersion version, String baseUrl, String typeName,
			final Callback<WfsFeatureTypeDescriptionInfo, String> callback) {
		WfsDescribeFeatureTypeRequest request = new WfsDescribeFeatureTypeRequest(baseUrl, typeName);
		request.setVersion(WfsVersionDto.fromString(version.toString()));
		GwtCommand command = new GwtCommand(WfsDescribeFeatureTypeRequest.COMMAND_NAME);
		command.setCommandRequest(request);
		GeomajasServerExtension.getInstance().getCommandService()
				.execute(command, new AbstractCommandCallback<WfsDescribeFeatureTypeResponse>() {

					@Override
					public void execute(WfsDescribeFeatureTypeResponse response) {
						callback.onSuccess(response.getFeatureTypeDescription());
					}

					@Override
					public void onCommunicationException(Throwable error) {
						callback.onFailure(error.getMessage());
					}

					@Override
					public void onCommandException(CommandResponse response) {
						callback.onFailure(response.getErrorMessages().get(0));
					}

				});
	}

	@Override
	public void getFeatures(WfsVersion version, String baseUrl, String typeName, QueryDto query,
			Callback<WfsFeatureCollectionInfo, String> callback) {
		getFeatures(version, null, baseUrl, typeName, query, callback);
	}

	@Override
	public void getFeatures(WfsVersion version, final FeaturesSupported layer, String baseUrl, String typeName,
			QueryDto query, final Callback<WfsFeatureCollectionInfo, String> callback) {
		WfsGetFeatureRequest request = new WfsGetFeatureRequest();
		request.setBaseUrl(baseUrl);
		request.setTypeName(typeName);
		request.setCriterion(query.getCriterion());
		request.setCrs(query.getCrs());
		request.setMaxCoordsPerFeature(query.getMaxCoordsPerFeature());
		request.setMaxFeatures(query.getMaxFeatures());
		request.setSchema(query.getAttributeDescriptors());
		request.setStartIndex(query.getStartIndex());
		request.setRequestedAttributeNames(query.getRequestedAttributeNames());
		request.setVersion(WfsVersionDto.fromString(version.toString()));
		GwtCommand command = new GwtCommand(WfsGetFeatureRequest.COMMAND_NAME);
		command.setCommandRequest(request);
		GeomajasServerExtension.getInstance().getCommandService()
				.execute(command, new AbstractCommandCallback<WfsGetFeatureResponse>() {

					@Override
					public void execute(WfsGetFeatureResponse response) {
						final WfsFeatureCollectionDto collection = response.getFeatureCollection();
						callback.onSuccess(new WfsFeatureCollectionInfo() {

							@Override
							public String getTypeName() {
								return collection.getTypeName();
							}

							@Override
							public List<Feature> getFeatures() {
								List<Feature> features = new ArrayList<Feature>();
								for (org.geomajas.layer.feature.Feature feature : collection.getFeatures()) {
									Feature newFeature = GeomajasServerExtension.getInstance()
											.getServerFeatureService().create(feature, layer);
									features.add(newFeature);
								}
								return features;
							}

							@Override
							public Bbox getBoundingBox() {
								return collection.getBoundingBox();
							}

							@Override
							public String getBaseUrl() {
								return collection.getBaseUrl();
							}
						});
					}

					@Override
					public void onCommunicationException(Throwable error) {
						callback.onFailure(error.getMessage());
					}

					@Override
					public void onCommandException(CommandResponse response) {
						callback.onFailure(response.getErrorMessages().get(0));
					}

				});
	}

	@Override
	public void setWfsUrlTransformer(WfsUrlTransformer urlTransformer) {
	}

	@Override
	public WfsUrlTransformer getWfsUrlTransformer() {
		return null;
	}

}
