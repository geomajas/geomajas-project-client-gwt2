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
import org.geomajas.gwt2.client.widget.map.MapWidgetCssResource;
import org.geomajas.gwt2.client.widget.map.MapWidgetResource;

/**
 * Empty client resource bundle for the {@link org.geomajas.gwt2.client.widget.map.MapWidgetImpl}.
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public interface NoStyleMapWidgetResource extends MapWidgetResource {

	/**
	 * Get the default CSS resource.
	 *
	 * @return The CSS resource.
	 */
	@Source("MapWidget-empty.css")
	MapWidgetCssResource css();
}