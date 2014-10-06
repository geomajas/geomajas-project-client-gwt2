/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.plugin.wms.client.service;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.CommandResponse;
import org.geomajas.geometry.Coordinate;
import org.geomajas.global.ExceptionDto;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;
import org.geomajas.gwt2.plugin.wms.client.WmsServerExtension;
import org.geomajas.gwt2.plugin.wms.client.layer.WmsLayer;
import org.geomajas.gwt2.plugin.wms.server.command.dto.WmsGetFeatureInfoRequest;
import org.geomajas.gwt2.plugin.wms.server.command.dto.WmsGetFeatureInfoResponse;

import com.google.gwt.core.client.Callback;

/**
 * Default implementation of the {@link WmsFeatureService}.
 *
 * @author Pieter De Graef
 * @author An Buyle
 */
public class WmsFeatureServiceImpl extends WmsServiceImpl implements WmsService {

	@Override
	public void getFeatureInfo(ViewPort viewPort, final WmsLayer layer, Coordinate location, String format,
			final Callback<List<Feature>, String> callback) {
		final String url = getFeatureInfoUrl(viewPort, layer, location, format);

		Integer max = WmsServerExtension.getInstance().getHintValue(WmsServerExtension.GET_FEATUREINFO_MAX_COORDS);

		GwtCommand command = new GwtCommand(WmsGetFeatureInfoRequest.COMMAND_NAME);
		command.setCommandRequest(new WmsGetFeatureInfoRequest(url, max));
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<WmsGetFeatureInfoResponse>() {

			@Override
			public void execute(WmsGetFeatureInfoResponse response) {
				List<Feature> features = new ArrayList<Feature>();
				for (org.geomajas.layer.feature.Feature feature : response.getFeatures()) {
					Feature newFeature = GeomajasServerExtension.getInstance().getServerFeatureService()
							.create(feature, (FeaturesSupported) layer);
					features.add(newFeature);
				}
				callback.onSuccess(features);
			}

			@Override
			public void onCommunicationException(Throwable error) {
				callback.onFailure(error.toString());
				super.onCommunicationException(error);
			}

			@Override
			public void onCommandException(CommandResponse response) {
				String msg = "";
				for (ExceptionDto error : response.getExceptions()) {
					msg = error.getClassName() + ": " + error.getMessage();
					break;
				}
				callback.onFailure(msg);
				super.onCommandException(response);
			}
		});
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

}
