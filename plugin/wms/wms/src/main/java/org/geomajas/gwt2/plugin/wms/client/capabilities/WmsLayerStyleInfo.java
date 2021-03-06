/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.plugin.wms.client.capabilities;

import org.geomajas.annotation.Api;

import java.io.Serializable;

/**
 * Style information for a layer in a WMS GetCapabilities file.
 *
 * @author Pieter De Graef
 * @since 2.1.0
 */
@Api
public interface WmsLayerStyleInfo extends Serializable {

	/**
	 * Get the style name. This name can be used in WMS GetMap requests.
	 *
	 * @return The style name.
	 */
	String getName();

	/**
	 * Get a human readable title for the style.
	 *
	 * @return The style title.
	 */
	String getTitle();

	/**
	 * Get a description for this style.
	 *
	 * @return A style description.
	 */
	String getAbstract();

	/**
	 * Get the default LegendGraphic information for this style.
	 *
	 * @return The default legend information for this style.
	 */
	WmsLayerLegendUrlInfo getLegendUrl();
}
