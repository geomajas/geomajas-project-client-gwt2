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

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.gfx.GfxUtil;
import org.geomajas.gwt2.client.gfx.GfxUtilImpl;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.MapPresenterImpl;
import org.geomajas.gwt2.client.widget.DefaultMapWidget;
import org.geomajas.gwt2.client.widget.control.pan.PanControlResource;
import org.geomajas.gwt2.client.widget.control.scalebar.ScalebarResource;
import org.geomajas.gwt2.client.widget.control.watermark.WatermarkResource;
import org.geomajas.gwt2.client.widget.control.zoom.ZoomControlResource;
import org.geomajas.gwt2.client.widget.control.zoom.ZoomStepControlResource;
import org.geomajas.gwt2.client.widget.control.zoomtorect.ZoomToRectangleControlResource;
import org.geomajas.gwt2.client.widget.map.MapWidgetResource;

/**
 * Geomajas starting point. This class allows you to request singleton services or create new instances.
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public final class GeomajasImpl implements Geomajas {

	private static Geomajas instance;

	private EventBus eventBus;

	private GeomajasImpl() {
	}

	/**
	 * Get a singleton instance.
	 *
	 * @return Return Geomajas!
	 */
	public static Geomajas getInstance() {
		if (instance == null) {
			instance = new GeomajasImpl();
		}
		return instance;
	}

	/**
	 * Apply a new singleton instance. This method provides a way to overwrite the default Geomajas implementations.
	 *
	 * @param geomajas The new Geomajas starting point.
	 */
	public static void setInstance(Geomajas geomajas) {
		instance = geomajas;
	}

	@Override
	public MapPresenter createMapPresenter() {
		return new MapPresenterImpl(getEventBus());
	}

	@Override
	public MapPresenter createMapPresenter(MapConfiguration configuration, int mapWidth, int mapHeight) {
		return createMapPresenter(configuration, mapWidth, mapHeight, new DefaultMapWidget[] {
				DefaultMapWidget.ZOOM_CONTROL, DefaultMapWidget.ZOOM_TO_RECTANGLE_CONTROL,
				DefaultMapWidget.SCALEBAR });
	}

	@Override
	public MapPresenter createMapPresenter(MapConfiguration configuration, int mapWidth, int mapHeight,
			DefaultMapWidget... mapWidgets) {
		MapPresenterImpl mapPresenter = new MapPresenterImpl(getEventBus());
		mapPresenter.setSize(mapWidth, mapHeight);
		mapPresenter.initialize(configuration, mapWidgets);
		return mapPresenter;
	}

	@Override
	public GfxUtil getGfxUtil() {
		return GfxUtilImpl.getInstance();
	}

	@Override
	public EventBus getEventBus() {
		if (eventBus == null) {
			eventBus = new SimpleEventBus();
		}
		return eventBus;
	}

	@Override
	public void loadDefaultMapStyles() {
		// The MapWidgetImpl:
		MapWidgetResource mapWidgetResource = GWT.create(MapWidgetResource.class);
		mapWidgetResource.css().ensureInjected();

		// The PanControl:
		PanControlResource panControlResource = GWT.create(PanControlResource.class);
		panControlResource.css().ensureInjected();

		// The Scalebar:
		ScalebarResource scaleBarResource = GWT.create(ScalebarResource.class);
		scaleBarResource.css().ensureInjected();

		// The Watermark:
		WatermarkResource watermarkResource = GWT.create(WatermarkResource.class);
		watermarkResource.css().ensureInjected();

		// The ZoomControl:
		ZoomControlResource zoomControlResource = GWT.create(ZoomControlResource.class);
		zoomControlResource.css().ensureInjected();

		// The ZoomStepControl:
		ZoomStepControlResource zoomStepControlResource = GWT.create(ZoomStepControlResource.class);
		zoomStepControlResource.css().ensureInjected();

		// The ZoomToRectangleControl:
		ZoomToRectangleControlResource zoomToRectControlResource = GWT.create(ZoomToRectangleControlResource.class);
		zoomToRectControlResource.css().ensureInjected();
	}
}