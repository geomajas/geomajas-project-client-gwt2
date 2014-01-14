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
package org.geomajas.gwt2.tools.client.tool.zoomin.resource;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Resource bundle for the zoom in button.
 *
 * @author Oliver May
 */
public interface ZoomInToolWidgetResource extends ClientBundle {

	@Source("geomajas-widget-zoom-in.css")
	ZoomInToolWidgetCssResource css();

	@Source("image/map-zoom-to-rect.png")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource zoomInImage();


}
