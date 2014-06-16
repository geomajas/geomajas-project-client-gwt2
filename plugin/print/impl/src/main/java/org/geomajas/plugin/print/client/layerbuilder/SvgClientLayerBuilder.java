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

package org.geomajas.plugin.print.client.layerbuilder;

import com.google.gwt.user.client.ui.Widget;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.plugin.rasterizing.command.dto.ClientSvgLayerInfo;
import org.vaadin.gwtgraphics.client.VectorObjectContainer;

/**
 * Builds a printable version of a live map.
 * 
 * @author Jan De Moerloose
 * @author An Buyle (support for extra layer with e.g. selected geometries)
 * @author Jan Venstermans (turning SvgMapBuilder into a LayerBuilder)
 */
public class SvgClientLayerBuilder implements PrintableWidgetLayerBuilder {

	@Override
	public boolean supports(Widget widget) {
		return widget instanceof VectorObjectContainer;
	}

	@Override
	public ClientLayerInfo build(MapPresenter mapPresenter, Widget widget, Bbox worldBounds, double rasterResolution) {
		ClientSvgLayerInfo svg = new ClientSvgLayerInfo();
		svg.setSvgContent(widget.getElement().getInnerHTML());
		svg.setViewBoxBounds(mapPresenter.getViewPort().getBounds());
		svg.setViewBoxWidth(mapPresenter.getViewPort().getMapWidth());
		svg.setViewBoxHeight(mapPresenter.getViewPort().getMapHeight());
		return svg;
	}
}
