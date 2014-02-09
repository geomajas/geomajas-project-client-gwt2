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

package org.geomajas.gwt2.client.widget.nostyle;

import com.google.gwt.core.client.GWT;
import org.geomajas.gwt2.client.widget.GeomajasImplClientBundleFactory;
import org.geomajas.gwt2.client.widget.control.pan.PanControlResource;
import org.geomajas.gwt2.client.widget.control.scalebar.ScalebarResource;
import org.geomajas.gwt2.client.widget.control.zoom.ZoomControlResource;
import org.geomajas.gwt2.client.widget.control.zoom.ZoomStepControlResource;
import org.geomajas.gwt2.client.widget.control.zoomtorect.ZoomToRectangleControlResource;
import org.geomajas.gwt2.client.widget.map.MapWidgetResource;

/**
 * Overrides the default factory to return resource bundles with empty CSS classes. This way no real CSS is injected
 * into the application and designers are free to provide their own CSS files.
 *
 * @author Pieter De Graef
 */
public final class GeomajasImplClientBundleFactoryNoStyle extends GeomajasImplClientBundleFactory {

	public PanControlResource createPanControlResource() {
		return GWT.create(NoStylePanControlResource.class);
	}

	public ScalebarResource createScalebarResource() {
		return GWT.create(NoStyleScalebarResource.class);
	}

	public ZoomControlResource createZoomControlResource() {
		return GWT.create(NoStyleZoomControlResource.class);
	}

	public ZoomStepControlResource createZoomStepControlResource() {
		return GWT.create(NoStyleZoomStepControlResource.class);
	}

	public ZoomToRectangleControlResource createZoomToRectangleControlResource() {
		return GWT.create(NoStyleZoomToRectangleControlResource.class);
	}

	public MapWidgetResource createMapWidgetResource() {
		return GWT.create(NoStyleMapWidgetResource.class);
	}
}
