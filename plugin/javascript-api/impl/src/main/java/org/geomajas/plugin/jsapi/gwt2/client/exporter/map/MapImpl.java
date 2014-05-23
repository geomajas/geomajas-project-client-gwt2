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
package org.geomajas.plugin.jsapi.gwt2.client.exporter.map;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.controller.AbstractMapController;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.plugin.jsapi.client.event.JsEventBus;
import org.geomajas.plugin.jsapi.client.map.ExportableFunction;
import org.geomajas.plugin.jsapi.client.map.LayersModel;
import org.geomajas.plugin.jsapi.client.map.Map;
import org.geomajas.plugin.jsapi.client.map.ViewPort;
import org.geomajas.plugin.jsapi.client.map.controller.MapController;
import org.geomajas.plugin.jsapi.client.map.feature.FeatureSearchService;
import org.geomajas.plugin.jsapi.gwt2.client.exporter.event.JsEventBusImpl;
import org.geomajas.plugin.jsapi.gwt2.client.exporter.map.feature.FeatureSearchServiceImpl;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.NoExport;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Javascript exportable facade of the Map presentation. See the specific implementation for details how to initialize.
 * 
 * The implementation should make sure the newly created Map is registered in a
 * {@link org.geomajas.plugin.jsapi.client.GeomajasService}! This way created maps are guaranteed available trough
 * JavaScript.
 * 
 * @author Pieter De Graef
 * @author Oliver May
 */
@Export("Map")
@ExportPackage("org.geomajas.jsapi.map")
public class MapImpl implements Exportable, Map {

	private JsEventBus eventBus;

	private MapPresenter mapPresenter;

	private ViewPort viewPort;

	private LayersModel layersModel;

	private FeatureSearchService featureSearchService;

	private String htmlElementId;

	/**
	 * No-arguments constructor. If this is removed, we get errors from the GWT exporter...
	 */
	public MapImpl() {
	}

	/**
	 * Create a facade for the given {@link MapPresenter}, available trough JavaScript.
	 * 
	 * @param mapPresenter the {@link MapPresenter} object.
	 * @since 1.0.0
	 */
	@Api
	public MapImpl(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
		eventBus = new JsEventBusImpl(this);
		viewPort = new ViewPortImpl(mapPresenter.getViewPort());
		layersModel = new LayersModelImpl(mapPresenter.getLayersModel());
		featureSearchService = new FeatureSearchServiceImpl(mapPresenter);
	}

	// ------------------------------------------------------------------------
	// Map implementation:
	// ------------------------------------------------------------------------

	/**
	 * Couples this map to an existing HTML element (div or span).
	 * 
	 * @param id id of the element
	 */
	public void setHtmlElementId(String id) {
		htmlElementId = id;
		// Is there a better way ?
		HTMLPanel div = HTMLPanel.wrap(Document.get().getElementById(id));
		div.add(mapPresenter);
	}

	/**
	 * Returns the layers model for this presenter. This model is the central layer handler for the map, with methods
	 * for getting layers, moving them up and down, adding or removing layers, ..
	 * 
	 * @return The layers model.
	 */
	public LayersModel getLayersModel() {
		return layersModel;
	}

	/**
	 * Returns the {@link ViewPort} associated with this map. The view port regulates zooming and panning around the
	 * map, but also presents transformation methods for transforming vector objects between the different render
	 * spaces.
	 * 
	 * @return Returns the view port.
	 */
	public ViewPort getViewPort() {
		return viewPort;
	}

	/**
	 * Apply a new {@link MapController} on the map. This controller will handle all mouse-events that are global for
	 * the map. Only one controller can be set at any given time. When a controller is active on the map, using this
	 * method, any fall-back controller is automatically disabled.
	 * 
	 * @param mapController The new {@link MapController} object. If null is passed, then the active controller is again
	 *        disabled. At that time the fall-back controller is again activated.
	 */
	public void setMapController(MapController mapController) {
		if (mapController != null) {
			mapController.setMap(this);
			mapPresenter.setMapController(new JsController(mapController));
		} else {
			mapPresenter.setMapController(null);
		}
	}

	/**
	 * Return the currently active controller on the map.
	 * 
	 * @return The currently active controller.
	 */
	public MapController getMapController() {
		final org.geomajas.gwt2.client.controller.MapController controller = mapPresenter.getMapController();
		MapController mapController = new MapController(this, controller);
		mapController.setActivationHandler(new ExportableFunction() {

			public void execute() {
				controller.onActivate(mapPresenter);
			}
		});
		mapController.setDeactivationHandler(new ExportableFunction() {

			public void execute() {
				controller.onDeactivate(mapPresenter);
			}
		});
		return mapController;
	}

	/**
	 * Apply a new width and height on the map. Both parameters are expressed in pixels.
	 * 
	 * @param width The new pixel width for the map.
	 * @param height The new pixel height for the map.
	 */
	public void setSize(int width, int height) {
		mapPresenter.setSize(width, height);
	}

	/**
	 * Apply a new mouse cursor when hovering above the map.
	 * 
	 * @param cursor The new cursor to apply.
	 */
	public void setCursor(String cursor) {
		mapPresenter.setCursor(cursor);
	}

	/**
	 * Apply a new default cursor to the map.
	 * 
	 * @param cursor The new cursor to apply.
	 */
	public void setDefaultCursor(String cursor) {
		// how ?
	}

	public String getHtmlElementId() {
		return htmlElementId;
	}

	/**
	 * Get the event bus that handles all event handlers and event firing for this map.
	 * 
	 * @return The event bus that manages all event related to this map.
	 */
	public JsEventBus getEventBus() {
		return eventBus;
	}

	/**
	 * Return a service that can search for features.
	 * 
	 * @return A service that can search for features.
	 */
	public FeatureSearchService getFeatureSearchService() {
		return featureSearchService;
	}

	// ------------------------------------------------------------------------
	// Other public methods:
	// ------------------------------------------------------------------------

	@NoExport
	public MapPresenter getMapPresenter() {
		return (MapPresenter) mapPresenter;
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * JavaScript to GWT controller wrapper.
	 * 
	 * @author Pieter De Graef
	 */
	private class JsController extends AbstractMapController {

		private MapController mapController;

		public JsController(MapController mapController) {
			super(false);
			this.mapController = mapController;
		}

		public void onActivate(MapPresenter presenter) {
			if (mapController.getActivationHandler() != null) {
				mapController.getActivationHandler().execute();
			}
		}

		public void onDeactivate(MapPresenter presenter) {
			if (mapController.getDeactivationHandler() != null) {
				mapController.getDeactivationHandler().execute();
			}
		}

		public void onMouseMove(MouseMoveEvent event) {
			if (mapController.getMouseMoveHandler() != null) {
				mapController.getMouseMoveHandler().onMouseMove(event);
			}
		}

		public void onMouseOver(MouseOverEvent event) {
			if (mapController.getMouseOverHandler() != null) {
				mapController.getMouseOverHandler().onMouseOver(event);
			}
		}

		public void onMouseOut(MouseOutEvent event) {
			if (mapController.getMouseOutHandler() != null) {
				mapController.getMouseOutHandler().onMouseOut(event);
			}
		}

		public void onDown(HumanInputEvent<?> event) {
			if (mapController.getDownHandler() != null) {
				mapController.getDownHandler().onDown(event);
			}
		}

		public void onUp(HumanInputEvent<?> event) {
			if (mapController.getUpHandler() != null) {
				mapController.getUpHandler().onUp(event);
			}
		}

		public void onDrag(HumanInputEvent<?> event) {
			if (mapController.getDragHandler() != null) {
				mapController.getDragHandler().onDrag(event);
			}
		}

		public void onDoubleClick(DoubleClickEvent event) {
			if (mapController.getDoubleClickHandler() != null) {
				mapController.getDoubleClickHandler().onDoubleClick(event);
			}
		}
	}
}