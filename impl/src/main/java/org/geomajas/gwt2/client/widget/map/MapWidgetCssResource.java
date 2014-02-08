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

import com.google.gwt.resources.client.CssResource;
import org.geomajas.annotation.Api;

/**
 * CSS resource bundle for the {@link org.geomajas.gwt2.client.widget.control.pan.PanControl} widget.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public interface MapWidgetCssResource extends CssResource {

	/**
	 * The main style of the {@link org.geomajas.gwt2.client.widget.control.pan.PanControl}. Provides the mapBackground
	 * style.
	 */
	@ClassName("gm-mapBackground")
	String mapBackground();
}