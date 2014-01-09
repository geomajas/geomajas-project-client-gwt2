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

package org.geomajas.gwt2.client.map.feature;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.event.FeatureDeselectedEvent;
import org.geomajas.gwt2.client.event.FeatureSelectedEvent;
import org.geomajas.gwt2.client.event.FeatureSelectionHandler;
import org.geomajas.gwt2.client.event.LayerHideEvent;
import org.geomajas.gwt2.client.event.LayerShowEvent;
import org.geomajas.gwt2.client.event.LayerVisibilityHandler;
import org.geomajas.gwt2.client.event.LayerVisibilityMarkedEvent;
import org.geomajas.gwt2.client.gfx.GfxUtil;
import org.geomajas.gwt2.client.gfx.VectorContainer;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Circle;

/**
 * This feature selection handler will render the selection on the map. It is used for maps that receive their
 * configuration from the server.
 * 
 * @author Pieter De Graef
 */
public class FeatureSelectionRenderer implements FeatureSelectionHandler, LayerVisibilityHandler {

	private final VectorContainer container;

	private final Map<String, VectorObject> shapes;

	private FeatureStyleInfo pointStyle;

	private FeatureStyleInfo lineStyle;

	private FeatureStyleInfo ringStyle;

	public FeatureSelectionRenderer(MapPresenter mapPresenter) {
		container = mapPresenter.getContainerManager().addWorldContainer();
		shapes = new HashMap<String, VectorObject>();
	}

	public void initialize(ClientMapInfo mapInfo) {
		pointStyle = mapInfo.getPointSelectStyle();
		lineStyle = mapInfo.getLineSelectStyle();
		ringStyle = mapInfo.getPolygonSelectStyle();
	}

	public void onFeatureSelected(FeatureSelectedEvent event) {
		Feature feature = event.getFeature();
		if (feature.getLayer().isShowing()) {
			render(feature);
		}
	}

	public void onFeatureDeselected(FeatureDeselectedEvent event) {
		remove(event.getFeature());
	}

	public void onShow(LayerShowEvent event) {
	}

	public void onHide(LayerHideEvent event) {
	}

	public void onVisibilityMarked(LayerVisibilityMarkedEvent event) {
	}

	private void render(Feature f) {
		GfxUtil gfxUtil = GeomajasImpl.getInstance().getGfxUtil();
		VectorObject shape = gfxUtil.toShape(f.getGeometry());
		String type = f.getGeometry().getGeometryType();
		if (Geometry.POINT.equals(type) || Geometry.MULTI_POINT.equals(type)) {

			// if no radius is defined in symbol circle it will be default 5px
			if (shape instanceof Circle && null != pointStyle.getSymbol() 
					&& null != pointStyle.getSymbol().getCircle()) {
				((Circle) shape).setUserRadius(pointStyle.getSymbol().getCircle().getR());
			}

			gfxUtil.applyStroke(shape, pointStyle.getStrokeColor(), pointStyle.getStrokeOpacity(),
					pointStyle.getStrokeWidth(), pointStyle.getDashArray());
			gfxUtil.applyFill(shape, pointStyle.getFillColor(), pointStyle.getFillOpacity());
		} else if (Geometry.LINE_STRING.equals(type) || Geometry.MULTI_LINE_STRING.equals(type)) {
			gfxUtil.applyStroke(shape, lineStyle.getStrokeColor(), lineStyle.getStrokeOpacity(),
					lineStyle.getStrokeWidth(), lineStyle.getDashArray());
			gfxUtil.applyFill(shape, lineStyle.getFillColor(), lineStyle.getFillOpacity());
		} else {
			gfxUtil.applyStroke(shape, ringStyle.getStrokeColor(), ringStyle.getStrokeOpacity(),
					ringStyle.getStrokeWidth(), ringStyle.getDashArray());
			gfxUtil.applyFill(shape, ringStyle.getFillColor(), ringStyle.getFillOpacity());
		}
		container.add(shape);
		shapes.put(f.getId(), shape);
	}

	private void remove(Feature feature) {
		VectorObject shape = shapes.get(feature.getId());
		if (shape != null) {
			container.remove(shape);
			shapes.remove(feature.getId());
		}
	}
}