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

package org.geomajas.gwt2.client.widget.map;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import org.geomajas.annotation.Api;

/**
 * Client resource bundle for the {@link MapWidgetImpl}.
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public interface MapWidgetResource extends ClientBundle {

	/**
	 * Get the default CSS resource.
	 *
	 * @return The CSS resource.
	 */
	@Source("MapWidget.css")
	MapWidgetCssResource css();

	// ------------------------------------------------------------------------
	// Images:
	// ------------------------------------------------------------------------

	/**
	 * Return an image used as background for the map.
	 *
	 * @return The map background image.
	 */
	@Source("image/map-background.png")
	@ImageOptions(repeatStyle = RepeatStyle.Both)
	ImageResource mapBackground();
}