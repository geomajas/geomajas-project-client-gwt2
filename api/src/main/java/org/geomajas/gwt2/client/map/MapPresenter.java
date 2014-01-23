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

package org.geomajas.gwt2.client.map;

import java.util.Collection;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.gwt2.client.controller.MapController;
import org.geomajas.gwt2.client.map.layer.LayersModel;
import org.geomajas.gwt2.client.map.render.LayersModelRenderer;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Central map definition.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface MapPresenter extends IsWidget {

	/**
	 * Get the event bus for this map. All events regarding this map, it's layers and it's features will originate from
	 * this event bus. In other words, this is where you need to register your handlers.
	 * 
	 * @return The event bus for this map.
	 */
	MapEventBus getEventBus();

	/** Return the widget that displays the map in the HTML page. */
	Widget asWidget();

	/**
	 * Get the renderer for the {@link LayersModel}. This renderer is responsible for rendering the list of layers on
	 * the map.
	 * 
	 * @return The renderer.
	 */
	LayersModelRenderer getLayersModelRenderer();

	/**
	 * Apply a new width and height on the map. Both parameters are expressed in pixels.
	 * 
	 * @param width
	 *            The new pixel width for the map.
	 * @param height
	 *            The new pixel height for the map.
	 */
	void setSize(int width, int height);

	/**
	 * Returns the layers model for this presenter. This model is the central layer handler for the map, with methods
	 * for getting layers, moving them up and down, adding or removing layers, ..
	 * 
	 * @return The layers model.
	 */
	LayersModel getLayersModel();

	/**
	 * Returns the {@link ViewPort} associated with this map. The view port regulates zooming and panning around the
	 * map, but also presents transformation methods for transforming vector objects between the different render
	 * spaces.
	 * 
	 * @return Returns the view port.
	 */
	ViewPort getViewPort();

	/**
	 * Apply a new {@link MapController} on the map. This controller will handle all mouse-events that are global for
	 * the map. Only one controller can be set at any given time. When a controller is active on the map, using this
	 * method, any fall-back controller is automatically disabled.
	 * 
	 * @param controller
	 *            The new {@link MapController} object. If null is passed, then the active controller is again disabled.
	 *            At that time the fall-back controller is again activated.
	 */
	void setMapController(MapController controller);

	/**
	 * Return the currently active controller on the map.
	 * 
	 * @return The currently active controller.
	 */
	MapController getMapController();

	/**
	 * Add a new listener to the map. These listeners passively listen to mouse events on the map, without actually
	 * interfering with these events. It is up to the developer to make sure these listeners do not interfere with the
	 * primary controller on the map. In this regard it is considered bad practice to let these listeners interfere with
	 * the mouse events.
	 * 
	 * @param mapListener
	 *            The listener to try and remove again.
	 * @return Returns true of removal was successful, false otherwise (i.e. if the listener could not be found).
	 */
	boolean addMapListener(MapController mapListener);

	/**
	 * Remove one of the currently active listeners on the map. These listeners passively listen to mouse events on the
	 * map, without actually interfering with these events. It is up to the developer to make sure these listeners do
	 * not interfere with the primary controller on the map. In this regard it is considered bad practice to let these
	 * listeners interfere with the mouse events.
	 * 
	 * @param mapListener
	 *            The listener to try and remove again.
	 * @return Returns true of removal was successful, false otherwise (i.e. if the listener could not be found).
	 */
	boolean removeMapListener(MapController mapListener);

	/**
	 * Get the currently active set of listeners on the map. These listeners passively listen to mouse events on the
	 * map, without actually interfering with these events. It is up to the developer to make sure these listeners do
	 * not interfere with the primary controller on the map. In this regard it is considered bad practice to let these
	 * listeners interfere with the mouse events.
	 * 
	 * @return Returns the full collection of currently active listeners.
	 */
	Collection<MapController> getMapListeners();

	/**
	 * Apply a new mouse cursor when hovering above the map.
	 * 
	 * @param cursor
	 *            The new cursor to apply.
	 */
	void setCursor(String cursor);

	/**
	 * Get an event parser specific for this map. This object can derive locations from mouse or touch events etc.
	 * 
	 * @return The event parser specific for this map.
	 */
	MapEventParser getMapEventParser();

	/**
	 * Get a panel onto which widgets can be freely added. This panel is always of the same size of the map, and resizes
	 * with the map.
	 * 
	 * @return A panel onto which widgets can be added.
	 */
	HasWidgets getWidgetPane();

	/**
	 * Get the map configuration object. This object contains the server-side configuration and a series of map hints.
	 * 
	 * @return The configuration object for this map.
	 */
	MapConfiguration getConfiguration();

	/**
	 * Manages different types of containers that can be added to the map. These containers are used to overlay
	 * drawings/widgets/etc on the map.
	 * 
	 * @return The container manager.
	 */
	ContainerManager getContainerManager();
}