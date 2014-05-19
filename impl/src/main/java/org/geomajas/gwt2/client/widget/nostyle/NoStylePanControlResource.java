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

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.widget.control.pan.PanControlCssResource;
import org.geomajas.gwt2.client.widget.control.pan.PanControlResource;

/**
 * Empty client resource bundle for the {@link org.geomajas.gwt2.client.widget.control.pan.PanControl} widget.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public interface NoStylePanControlResource extends PanControlResource {

	/**
	 * Get the default CSS resource.
	 * 
	 * @return The CSS resource.
	 */
	@Source("geomajas-pan-control-empty.css")
	PanControlCssResource css();
}