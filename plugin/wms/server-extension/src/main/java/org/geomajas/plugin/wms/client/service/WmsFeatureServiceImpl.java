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

package org.geomajas.plugin.wms.client.service;

import com.google.gwt.core.client.Callback;
import org.geomajas.command.CommandResponse;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.global.ExceptionDto;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.render.TileCode;
import org.geomajas.gwt2.client.service.TileService;
import org.geomajas.plugin.wms.client.WmsServerExtension;
import org.geomajas.plugin.wms.client.layer.FeaturesSupportedWmsLayer;
import org.geomajas.plugin.wms.client.layer.WmsLayer;
import org.geomajas.plugin.wms.server.command.dto.GetFeatureInfoRequest;
import org.geomajas.plugin.wms.server.command.dto.GetFeatureInfoResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of the {@link WmsFeatureService}.
 *
 * @author Pieter De Graef
 * @author An Buyle
 */
public class WmsFeatureServiceImpl extends WmsServiceImpl implements WmsFeatureService {

	private static final double DEFAULT_PIXEL_TOLERANCE = 0.0; // Default tolerance for the location

	private static final int DEFAULT_MAX_FEATURES = 20; // Default maximum number of feats returned by GetFeatureInfo

	// ------------------------------------------------------------------------
	// WmsFeatureService implementation:
	// ------------------------------------------------------------------------

	@Override
	public void getFeatureInfo(ViewPort viewPort, final FeaturesSupportedWmsLayer layer, Coordinate location,
			final Callback<List<Feature>, String> callback) {
		final String url = getFeatureInfoUrl(viewPort, layer, location, GetFeatureInfoFormat.GML2,
				DEFAULT_PIXEL_TOLERANCE, DEFAULT_MAX_FEATURES);

		Integer max = WmsServerExtension.getInstance().getHintValue(WmsServerExtension.GET_FEATUREINFO_MAX_COORDS);

		GwtCommand command = new GwtCommand(GetFeatureInfoRequest.COMMAND_NAME);
		command.setCommandRequest(new GetFeatureInfoRequest(url, max));
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<GetFeatureInfoResponse>() {

			@Override
			public void execute(GetFeatureInfoResponse response) {
				List<Feature> features = new ArrayList<Feature>();
				for (org.geomajas.layer.feature.Feature feature : response.getFeatures()) {
					Feature newFeature = GeomajasServerExtension.getInstance().getServerFeatureService()
							.create(feature, layer);
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

	@Override
	public void getFeatureInfo(ViewPort viewPort, FeaturesSupportedWmsLayer layer, Coordinate location,
			GetFeatureInfoFormat format, final Callback<Object, String> callback) {
		String url = getFeatureInfoUrl(viewPort, layer, location, format, DEFAULT_PIXEL_TOLERANCE,
				DEFAULT_MAX_FEATURES);
		GwtCommand command = new GwtCommand(GetFeatureInfoRequest.COMMAND_NAME);
		command.setCommandRequest(new GetFeatureInfoRequest(url));
		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback<GetFeatureInfoResponse>() {

			public void execute(GetFeatureInfoResponse response) {
				if (response.getFeatures() != null) {
					callback.onSuccess(response.getFeatures());
				} else {
					callback.onSuccess(response.getWmsResponse());
				}
			}
		});
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private String getFeatureInfoUrl(ViewPort viewPort, WmsLayer layer, Coordinate location,
			GetFeatureInfoFormat format, double tolerance, int maxFeatures) {
		StringBuilder url = getBaseUrlBuilder(layer.getConfiguration());

		// Calculate the denominator for tile height and width adaptation to reflect the specified tolerance in pixels
		int toleranceCorrection = (int) Math.round(tolerance * 2.0);
		if (toleranceCorrection <= 0) {
			toleranceCorrection = 1;
		}
		if (toleranceCorrection > 2.0) {
			toleranceCorrection = 2; // limit because it seems sometimes not to work if > 2
		}

		TileCode tileCode = TileService.getTileCodeForLocation(layer.getTileConfiguration(), location,
				viewPort.getResolution());
		Bbox worldBounds = TileService.getWorldBoundsForTile(layer.getTileConfiguration(), tileCode);

		Bbox screenBounds = viewPort.getTransformationService()
				.transform(worldBounds, RenderSpace.WORLD, RenderSpace.SCREEN);
		Coordinate screenLocation = viewPort.getTransformationService()
				.transform(location, RenderSpace.WORLD, RenderSpace.SCREEN);

		// Add the base parameters needed for getMap:
		addBaseParameters(url, layer.getConfiguration(), viewPort.getCrs(), worldBounds, layer.getTileConfiguration().
				getTileWidth() / toleranceCorrection, layer.getTileConfiguration().getTileHeight() /
				toleranceCorrection);

		url.append("&QUERY_LAYERS=");
		url.append(layer.getConfiguration().getLayers()); // No URL.encode here!
		url.append("&request=GetFeatureInfo");
		switch (layer.getConfiguration().getVersion()) {
			case V1_3_0:
				url.append("&I=");
				url.append((int) Math.round((screenLocation.getX() - screenBounds.getX())
						/ (double) toleranceCorrection));
				url.append("&J=");
				url.append((int) Math.round((screenLocation.getY() - screenBounds.getY())
						/ (double) toleranceCorrection));
				break;
			case V1_1_1:
			default:
				url.append("&X=");
				url.append((int) Math.round((screenLocation.getX() - screenBounds.getX())
						/ (double) toleranceCorrection));
				url.append("&Y=");
				url.append((int) Math.round((screenLocation.getY() - screenBounds.getY())
						/ (double) toleranceCorrection));
		}
		url.append("&FEATURE_COUNT=");
		url.append(maxFeatures);
		url.append("&INFO_FORMAT=");
		url.append(format.toString());

		return finishUrl(WmsRequest.GETFEATUREINFO, url);
	}
}
