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

import com.google.web.bindery.event.shared.EventBus;
import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.event.MapInitializationHandler;
import org.geomajas.gwt2.client.gfx.GfxUtil;
import org.geomajas.gwt2.client.widget.DefaultMapWidget;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapPresenter;

/**
 * Geomajas starting point. This class allows you to request singleton services or create new instances.
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public interface Geomajas {

	/**
	 * Create a new empty map. This map still needs to be initialized (it needs to fetch a configuration from the
	 * server, only then is it initialized, and are any layers available). Use this method if you want to fetch a
	 * configuration from the server, using the Geomajas server extensions. Otherwise it's better to use the {@link
	 * #createMapPresenter(MapConfiguration, int, int)} method.
	 *
	 * @return An empty map.
	 */
	MapPresenter createMapPresenter();

	/**
	 * Create a new empty map and immediately provide the necessary initialization configuration. This method will
	 * create a map and add the default map widgets (zoom in/out, zoom to rectangle and scale bar).
	 *
	 * @param configuration The map configuration.
	 * @param mapWidth      This initial map width in pixels.
	 * @param mapHeight     This initial map height in pixels.
	 * @return An initialized map. Time to add some layers!
	 */
	MapPresenter createMapPresenter(MapConfiguration configuration, int mapWidth, int mapHeight);


	/**
	 * Create a new empty map and immediately provide the necessary initialization configuration.
	 *
	 * <br />
	 * <b>NOTE!</b> that the map will be null while
	 * {@link MapInitializationHandler#onMapInitialized(org.geomajas.gwt2.client.event.MapInitializationEvent)} method
	 * is being executed. Use {@link org.geomajas.gwt2.client.event.MapInitializationEvent#getMapPresenter()} instead.
	 *
	 * @since 2.1.0
	 *
	 * @param configuration The map configuration.
	 * @param mapWidth      This initial map width in pixels.
	 * @param mapHeight     This initial map height in pixels.
	 * @param handler       A map initialization handler.
	 * @param mapWidgets    A set of widgets that should be added to the map by default.
	 * @return An initialized map.
	 */
	MapPresenter createMapPresenter(MapConfiguration configuration, int mapWidth, int mapHeight,
			MapInitializationHandler handler, DefaultMapWidget... mapWidgets);

	/**
	 * Create a new empty map and immediately provide the necessary initialization configuration.
	 *
	 * @param configuration The map configuration.
	 * @param mapWidth      This initial map width in pixels.
	 * @param mapHeight     This initial map height in pixels.
	 * @param mapWidgets    A set of widgets that should be added to the map by default.
	 * @return An initialized map. Time to add some layers!
	 */
	MapPresenter createMapPresenter(MapConfiguration configuration, int mapWidth, int mapHeight,
			DefaultMapWidget... mapWidgets);

	/**
	 * Get the {@link GfxUtil} singleton. Utility service that helps out when rendering custom shapes on the map.
	 *
	 * @return The {@link GfxUtil} singleton.
	 */
	GfxUtil getGfxUtil();

	/**
	 * Get a general EventBus singleton. This EventBus should should be used outside of the map, to catch application
	 * specific events.
	 *
	 * @return A general EventBus.
	 */
	EventBus getEventBus();
}