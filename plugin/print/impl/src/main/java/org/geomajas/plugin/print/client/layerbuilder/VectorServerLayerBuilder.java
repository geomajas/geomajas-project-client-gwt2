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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt.client.util.StyleUtil;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.layer.VectorServerLayer;
import org.geomajas.plugin.rasterizing.command.dto.VectorLayerRasterizingInfo;
import org.geomajas.sld.RuleInfo;

/**
 * {@link PrintableLayersModelBuilder} for {@link VectorServerLayer} instances.
 * 
 * @author Jan De Moerloose
 */
public class VectorServerLayerBuilder implements PrintableLayersModelBuilder {

	@Override
	public ClientLayerInfo build(MapPresenter mapPresenter, Layer layer, Bbox worldBounds, double resolution) {
		VectorServerLayer vectorLayer = (VectorServerLayer) layer;
		VectorLayerRasterizingInfo vectorRasterizingInfo = new VectorLayerRasterizingInfo();
		vectorRasterizingInfo.setPaintGeometries(true);
		vectorRasterizingInfo.setPaintLabels(vectorLayer.isLabeled());
		vectorRasterizingInfo.setShowing(layer.isShowing());
		ClientVectorLayerInfo layerInfo = (ClientVectorLayerInfo) vectorLayer.getLayerInfo();
		vectorRasterizingInfo.setStyle(layerInfo.getNamedStyleInfo());
		if (!vectorLayer.getSelectedFeatures().isEmpty()) {
			Collection<Feature> selectedFeatures = vectorLayer.getSelectedFeatures();
			Iterator<Feature> iterator = selectedFeatures.iterator();
			List<String> featureIds = new ArrayList<String>(selectedFeatures.size());
			while (iterator.hasNext()) {
				Feature feature = iterator.next();
				featureIds.add(feature.getId());
			}
			vectorRasterizingInfo.setSelectedFeatureIds(featureIds.toArray(new String[selectedFeatures.size()]));
			ClientMapInfo mapInfo = mapPresenter.getConfiguration().getHintValue(GeomajasServerExtension.MAPINFO);
			FeatureStyleInfo selectStyle;
			switch (layerInfo.getLayerType()) {
				case GEOMETRY:
				case LINESTRING:
				case MULTILINESTRING:
					selectStyle = mapInfo.getLineSelectStyle();
					break;
				case MULTIPOINT:
				case POINT:
					selectStyle = mapInfo.getPointSelectStyle();
					break;
				case MULTIPOLYGON:
				case POLYGON:
					selectStyle = mapInfo.getPolygonSelectStyle();
					break;
				default:
					throw new IllegalArgumentException("Unknown layer type " + layerInfo.getLayerType());
			}
			selectStyle.applyDefaults();
			RuleInfo selectionRule = StyleUtil.createRule(layerInfo.getLayerType(), selectStyle);
			vectorRasterizingInfo.setSelectionRule(selectionRule);
		}
		layerInfo.getWidgetInfo().put(VectorLayerRasterizingInfo.WIDGET_KEY, vectorRasterizingInfo);
		return layerInfo;
	}

	@Override
	public boolean supports(Layer layer) {
		return layer instanceof VectorServerLayer;
	}
}
