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
 * Legend information for a layer in the WMS Capabilities.
 *
 * @author Pieter De Graef
 * @since 2.1.0
 */
@Api
public interface WmsLayerLegendUrlInfo extends Serializable {

	/**
	 * Get the width in pixels for the style icons in the default LegendGraphic.
	 *
	 * @return The style width in pixels.
	 */
	int getWidth();

	/**
	 * Get the height in pixels for the style icons in the default LegendGraphic.
	 *
	 * @return The style height in pixels.
	 */
	int getHeight();

	/**
	 * Get the preferred format for the LegendGraphic.
	 *
	 * @return The image format.
	 */
	String getFormat();

	/**
	 * Get the default LegendGraphic URL.
	 *
	 * @return The default LegendGraphic URL.
	 */
	WmsOnlineResourceInfo getOnlineResource();
}
