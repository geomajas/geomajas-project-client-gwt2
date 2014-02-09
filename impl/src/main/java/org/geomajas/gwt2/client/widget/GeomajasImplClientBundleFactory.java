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

package org.geomajas.gwt2.client.widget;

import com.google.gwt.core.client.GWT;
import org.geomajas.gwt2.client.widget.control.pan.PanControlResource;
import org.geomajas.gwt2.client.widget.control.scalebar.ScalebarResource;
import org.geomajas.gwt2.client.widget.control.zoom.ZoomControlResource;
import org.geomajas.gwt2.client.widget.control.zoom.ZoomStepControlResource;
import org.geomajas.gwt2.client.widget.control.zoomtorect.ZoomToRectangleControlResource;
import org.geomajas.gwt2.client.widget.map.MapWidgetResource;

/**
 * Default factory for client bundles defined within this artifact. By using such a factory, it is possible to easily
 * override the default client bundles using deferred binding.
 *
 * @author Pieter De Graef
 */
public class GeomajasImplClientBundleFactory {

	public PanControlResource createPanControlResource() {
		return GWT.create(PanControlResource.class);
	}

	public ScalebarResource createScalebarResource() {
		return GWT.create(ScalebarResource.class);
	}

	public ZoomControlResource createZoomControlResource() {
		return GWT.create(ZoomControlResource.class);
	}

	public ZoomStepControlResource createZoomStepControlResource() {
		return GWT.create(ZoomStepControlResource.class);
	}

	public ZoomToRectangleControlResource createZoomToRectangleControlResource() {
		return GWT.create(ZoomToRectangleControlResource.class);
	}

	public MapWidgetResource createMapWidgetResource() {
		return GWT.create(MapWidgetResource.class);
	}
}
