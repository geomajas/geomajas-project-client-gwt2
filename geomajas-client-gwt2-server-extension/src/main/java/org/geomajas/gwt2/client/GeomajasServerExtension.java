/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.client;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.command.dto.GetMapConfigurationRequest;
import org.geomajas.command.dto.GetMapConfigurationResponse;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.MapOptions;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.MapPresenterImpl;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.feature.FeatureSelectionRenderer;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.layer.RasterServerLayerImpl;
import org.geomajas.gwt2.client.map.layer.ServerLayer;
import org.geomajas.gwt2.client.map.layer.VectorServerLayerImpl;
import org.geomajas.gwt2.client.service.CommandService;
import org.geomajas.gwt2.client.service.CommandServiceImpl;
import org.geomajas.gwt2.client.service.EndPointService;
import org.geomajas.gwt2.client.service.EndPointServiceImpl;

/**
 * Singleton service that provides access to other services within this artifact.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public final class GeomajasServerExtension {

	private static CommandService commandService;

	private static EndPointService endPointService;

	/** No-argument constructor. It's private, because this is meant to be a singleton service. */
	private GeomajasServerExtension() {
		commandService = new CommandServiceImpl();
		endPointService = new EndPointServiceImpl();
	}

	/**
	 * Get the {@link CommandService} singleton. This service allows for executing commands on the back-end. It is the
	 * base for all Geomajas client-server communication.
	 * 
	 * @return The {@link EndPointService} singleton.
	 */
	public static CommandService getCommandService() {
		return commandService;
	}

	/**
	 * Get the {@link EndPointService} singleton. Has pointers to the Geomajas services on the back-end, and allows
	 * those end-points to be altered in case your server is somewhere else (for example behind a proxy).
	 * 
	 * @return The {@link EndPointService} singleton.
	 */
	public static EndPointService getEndPointService() {
		return endPointService;
	}

	/**
	 * Initialize the map by fetching a configuration on the server.
	 * 
	 * @param mapPresenter
	 *            The map to initialize.
	 * @param applicationId
	 *            The application ID in the backend configuration.
	 * @param id
	 *            The map ID in the backend configuration.
	 */
	public static void initializeMap(final MapPresenter mapPresenter, String applicationId, String id) {
		GwtCommand commandRequest = new GwtCommand(GetMapConfigurationRequest.COMMAND);
		commandRequest.setCommandRequest(new GetMapConfigurationRequest(id, applicationId));
		commandService.execute(commandRequest, new AbstractCommandCallback<GetMapConfigurationResponse>() {

			public void execute(GetMapConfigurationResponse response) {
				// Initialize the MapModel and ViewPort:
				ClientMapInfo mapInfo = response.getMapInfo();

				// Now add all layers:
				for (ClientLayerInfo layerInfo : mapInfo.getLayers()) {
					mapPresenter.getLayersModel().addLayer(
							createLayer(layerInfo, mapPresenter.getViewPort(), mapPresenter.getEventBus()));
				}

				// Initialize the map:
				MapOptions mapOptions = new MapOptions();
				mapOptions.setCrs(mapInfo.getCrs());
				mapOptions.setInitialBounds(mapInfo.getInitialBounds());
				mapOptions.setMaxBounds(mapInfo.getMaxBounds());
				mapOptions.setMaximumScale(mapInfo.getScaleConfiguration().getMaximumScale().getPixelPerUnit());
				mapOptions.setPixelLength(mapInfo.getPixelLength());
				mapOptions.setUnitLength(mapInfo.getUnitLength());
				List<Double> resolutions = new ArrayList<Double>();
				for (ScaleInfo scale : mapInfo.getScaleConfiguration().getZoomLevels()) {
					resolutions.add(scale.getPixelPerUnit());
				}
				mapOptions.setResolutions(resolutions);
				((MapPresenterImpl) mapPresenter).initialize(mapOptions);

				// Also add a renderer for feature selection:
				FeatureSelectionRenderer renderer = new FeatureSelectionRenderer(mapPresenter);
				renderer.initialize(mapInfo);
				mapPresenter.getEventBus().addFeatureSelectionHandler(renderer);
				mapPresenter.getEventBus().addLayerVisibilityHandler(renderer);
			}
		});
	}

	/**
	 * Create a new layer, based upon a server-side layer configuration object.
	 * 
	 * @param layerInfo
	 *            The server-side configuration object.
	 * @param viewPort
	 *            The map viewport.
	 * @param eventBus
	 *            The map eventBus.
	 * @return The new layer object. It has NOT been added to the map just yet.
	 */
	public static Layer createLayer(ClientLayerInfo layerInfo, ViewPort viewPort, MapEventBus eventBus) {
		ServerLayer<?> layer = null;
		switch (layerInfo.getLayerType()) {
			case RASTER:
				layer = new RasterServerLayerImpl((ClientRasterLayerInfo) layerInfo, viewPort, eventBus);
				break;
			default:
				layer = new VectorServerLayerImpl((ClientVectorLayerInfo) layerInfo, viewPort, eventBus);
				break;
		}
		return layer;
	}
}