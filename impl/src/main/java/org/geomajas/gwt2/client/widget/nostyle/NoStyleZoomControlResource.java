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
import org.geomajas.gwt2.client.widget.control.zoom.ZoomControlCssResource;
import org.geomajas.gwt2.client.widget.control.zoom.ZoomControlResource;

/**
 * Client resource bundle for the {@link org.geomajas.gwt2.client.widget.control.zoom.ZoomControl}.
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public interface NoStyleZoomControlResource extends ZoomControlResource {

	/**
	 * Get the default CSS resource.
	 * 
	 * @return The CSS resource.
	 */
	@Source("geomajas-zoom-control-empty.css")
	ZoomControlCssResource css();
}