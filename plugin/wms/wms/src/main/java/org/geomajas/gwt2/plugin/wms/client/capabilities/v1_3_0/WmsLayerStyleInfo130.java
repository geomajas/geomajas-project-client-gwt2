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

package org.geomajas.gwt2.plugin.wms.client.capabilities.v1_3_0;

import com.google.gwt.xml.client.Node;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsLayerLegendUrlInfo;
import org.geomajas.gwt2.plugin.wms.client.capabilities.v1_1_1.WmsLayerStyleInfo111;

/**
 * Default {@link org.geomajas.gwt2.plugin.wms.client.capabilities.WmsLayerStyleInfo} implementation for WMS 1.3.0.
 *
 * @author Pieter De Graef
 */
public class WmsLayerStyleInfo130 extends WmsLayerStyleInfo111 {

	private static final long serialVersionUID = 100L;

	public WmsLayerStyleInfo130(Node node) {
		super(node);
	}

	protected WmsLayerLegendUrlInfo createLegendInfo(Node node) {
		return new WmsLayerLegendUrlInfo130(node);
	}
}