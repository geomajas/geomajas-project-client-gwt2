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
import org.geomajas.gwt2.client.map.Hint;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.MapPresenterImpl;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.feature.FeatureSelectionRenderer;
import org.geomajas.gwt2.client.map.feature.ServerFeatureService;
import org.geomajas.gwt2.client.map.feature.ServerFeatureServiceImpl;
import org.geomajas.gwt2.client.map.layer.RasterServerLayerImpl;
import org.geomajas.gwt2.client.map.layer.ServerLayer;
import org.geomajas.gwt2.client.map.layer.VectorServerLayerImpl;
import org.geomajas.gwt2.client.map.render.LayersModelRenderer;
import org.geomajas.gwt2.client.service.CommandService;
import org.geomajas.gwt2.client.service.CommandServiceImpl;
import org.geomajas.gwt2.client.service.EndPointService;
import org.geomajas.gwt2.client.service.EndPointServiceImpl;
import org.geomajas.gwt2.client.widget.DefaultMapWidget;

/**
 * Singleton service that provides access to other services within this artifact.
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public final class GeomajasServerExtension {

	/**
	 * {@link org.geomajas.gwt2.client.map.Hint} used to save the server-side configuration object into the {@link
	 * MapConfiguration}.
	 */
	public static final Hint<ClientMapInfo> MAPINFO = new Hint<ClientMapInfo>("mapInfo");

	private static GeomajasServerExtension instance;

	private final CommandService commandService;

	private final EndPointService endPointService;

	private final ServerFeatureService featureService;

	// ------------------------------------------------------------------------
	// Constructor & getInstance:
	// ------------------------------------------------------------------------

	/**
	 * No-argument constructor. No touchy!
	 */
	private GeomajasServerExtension() {
		commandService = new CommandServiceImpl();
		endPointService = new EndPointServiceImpl();
		featureService = new ServerFeatureServiceImpl();
	}

	/**
	 * Get the GeomajasServerExtension instance.
	 *
	 * @return The GeomajasServerExtension instance.
	 */
	public static GeomajasServerExtension getInstance() {
		if (instance == null) {
			instance = new GeomajasServerExtension();
		}
		return instance;
	}

	/**
	 * Override the GeomajasServerExtension instance.
	 *
	 * @param instance The new GeomajasServerExtension instance. You could override this class. On the other hand, to
	 *                 reset to the default implementation (i.e. this class), you can provide null.
	 */
	public static void setInstance(GeomajasServerExtension instance) {
		GeomajasServerExtension.instance = instance;
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	/**
	 * Get the {@link CommandService} singleton. This service allows for executing commands on the back-end. It is the
	 * base for all Geomajas client-server communication.
	 *
	 * @return The {@link EndPointService} singleton.
	 */
	public CommandService getCommandService() {
		return commandService;
	}

	/**
	 * Get the {@link EndPointService} singleton. Has pointers to the Geomajas services on the back-end, and allows
	 * those end-points to be altered in case your server is somewhere else (for example behind a proxy).
	 *
	 * @return The {@link EndPointService} singleton.
	 */
	public EndPointService getEndPointService() {
		return endPointService;
	}

	/**
	 * Get a service for working with or searching for features in server-side layers.
	 *
	 * @return The feature service.
	 */
	public ServerFeatureService getServerFeatureService() {
		return featureService;
	}

	/**
	 * Initialize the map by fetching a configuration on the server. This method will create a map and add the default
	 * map widgets (zoom in/out, zoom to rectangle and scale bar).
	 *
	 * @param mapPresenter  The map to initialize.
	 * @param applicationId The application ID in the backend configuration.
	 * @param id            The map ID in the backend configuration.
	 */
	public void initializeMap(final MapPresenter mapPresenter, String applicationId, String id) {
		initializeMap(mapPresenter, applicationId, id, new DefaultMapWidget[] {
				DefaultMapWidget.ZOOM_CONTROL, DefaultMapWidget.ZOOM_TO_RECTANGLE_CONTROL,
				DefaultMapWidget.SCALEBAR });
	}

	/**
	 * Initialize the map by fetching a configuration on the server.
	 *
	 * @param mapPresenter  The map to initialize.
	 * @param applicationId The application ID in the backend configuration.
	 * @param id            The map ID in the backend configuration.
	 * @param mapWidgets    A set of widgets that should be added to the map by default.
	 */
	public void initializeMap(final MapPresenter mapPresenter, String applicationId, String id,
			final DefaultMapWidget... mapWidgets) {
		GwtCommand commandRequest = new GwtCommand(GetMapConfigurationRequest.COMMAND);
		commandRequest.setCommandRequest(new GetMapConfigurationRequest(id, applicationId));
		commandService.execute(commandRequest, new AbstractCommandCallback<GetMapConfigurationResponse>() {

			public void execute(GetMapConfigurationResponse response) {
				// Initialize the MapModel and ViewPort:
				ClientMapInfo mapInfo = response.getMapInfo();
				
				// Create the map configuration
				MapConfiguration configuration = createMapConfiguration(mapInfo, mapPresenter);
				
				// Add all layers:
				for (ClientLayerInfo layerInfo : mapInfo.getLayers()) {
					ServerLayer<?> layer = createLayer(configuration, layerInfo, mapPresenter.getViewPort(),
							mapPresenter.getEventBus());
					mapPresenter.getLayersModel().addLayer(layer);
				}
				
				
				// Initialize the map:
				((MapPresenterImpl) mapPresenter).initialize(configuration, mapWidgets);
				
				// All layers animated
				LayersModelRenderer modelRenderer = mapPresenter.getLayersModelRenderer();
				for (int i = 0; i < mapPresenter.getLayersModel().getLayerCount(); i++) {
					modelRenderer.setAnimated(mapPresenter.getLayersModel().getLayer(i), true);
				}

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
	 * @param layerInfo The server-side configuration object.
	 * @param viewPort  The map viewport.
	 * @param eventBus  The map eventBus.
	 * @return The new layer object. It has NOT been added to the map just yet.
	 */
	public ServerLayer<?> createLayer(MapConfiguration mapConfiguration, ClientLayerInfo layerInfo, ViewPort viewPort, MapEventBus eventBus) {
		ServerLayer<?> layer = null;
		switch (layerInfo.getLayerType()) {
			case RASTER:
				layer = new RasterServerLayerImpl(mapConfiguration, (ClientRasterLayerInfo) layerInfo, viewPort, eventBus);
				break;
			default:
				layer = new VectorServerLayerImpl(mapConfiguration, (ClientVectorLayerInfo) layerInfo, viewPort, eventBus);
				break;
		}
		return layer;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private MapConfiguration createMapConfiguration(ClientMapInfo mapInfo, MapPresenter mapPresenter) {
		MapConfiguration configuration = mapPresenter.getConfiguration();
		configuration.setCrs(mapInfo.getCrs(), mapInfo.getUnitLength());
		configuration.setHintValue(MapConfiguration.INITIAL_BOUNDS, mapInfo.getInitialBounds());
		configuration.setMaxBounds(mapInfo.getMaxBounds());
		configuration.setMinimumResolution(1 / mapInfo.getScaleConfiguration().getMaximumScale().getPixelPerUnit());
		List<Double> resolutions = new ArrayList<Double>();
		for (ScaleInfo scale : mapInfo.getScaleConfiguration().getZoomLevels()) {
			resolutions.add(1 / scale.getPixelPerUnit());
		}
		configuration.setResolutions(resolutions);
		configuration.setHintValue(MAPINFO, mapInfo);
		return configuration;
	}
}
