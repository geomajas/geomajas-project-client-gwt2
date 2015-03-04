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

package org.geomajas.gwt2.client.map.layer;

import org.geomajas.gwt2.client.map.ViewPort;

/**
 * Util methods for {@link Layer}.
 *
 * @author Jan Venstermans
 */
public final class LayerUtil {

	private LayerUtil() {
	}
	//
	/**
	 * Check if current viewPort resolution is between the minimum (inclusive) and
	 * the maximum scale (exclusive) of the layer.
	 *  Inclusive/exclusive follows SLD convention: exclusive minResolution, inclusive maxResolution.
	 *
	 * @param viewPort the viewPort
	 * @param layer layer
	 * @return whether the layer is visible in the provided viewPort resolution
	 */
	public static boolean isLayerVisibleAtViewPortResolution(ViewPort viewPort, Layer layer) {
		if (viewPort.getResolution() > layer.getMinResolution()
				&& viewPort.getResolution() <= layer.getMaxResolution()) {
			return true;
		}
		return false;
	}
}
