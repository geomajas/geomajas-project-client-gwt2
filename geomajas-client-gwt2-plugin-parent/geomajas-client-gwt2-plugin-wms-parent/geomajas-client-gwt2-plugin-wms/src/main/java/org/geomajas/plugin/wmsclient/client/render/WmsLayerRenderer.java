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

package org.geomajas.plugin.wmsclient.client.render;

import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.render.LayerRenderer;
import org.geomajas.gwt2.client.map.render.RenderingInfo;
import org.geomajas.plugin.wmsclient.client.layer.WmsLayer;

/**
 * Definition for a renderer for WMS layers.
 * 
 * @author Pieter De Graef
 */
public class WmsLayerRenderer implements LayerRenderer {

	private final WmsLayer layer;

	public WmsLayerRenderer(WmsLayer layer) {
		this.layer = layer;
	}

	@Override
	public void render(RenderingInfo renderingInfo) {
	}

	@Override
	public Layer getLayer() {
		return layer;
	}
}