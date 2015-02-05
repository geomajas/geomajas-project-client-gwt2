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

package org.geomajas.gwt2.plugin.print.client.template.impl;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.plugin.print.client.layerbuilder.PrintableLayerBuilder;
import org.geomajas.gwt2.plugin.print.client.layerbuilder.PrintableLayersModelBuilder;
import org.geomajas.gwt2.plugin.print.client.layerbuilder.PrintableWidgetLayerBuilder;
import org.geomajas.gwt2.plugin.print.client.layerbuilder.RasterServerLayerBuilder;
import org.geomajas.gwt2.plugin.print.client.layerbuilder.VectorServerLayerBuilder;
import org.geomajas.gwt2.plugin.print.client.template.PrintableMapBuilder;
import org.geomajas.plugin.rasterizing.command.dto.LegendRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Default implementation of {@link org.geomajas.gwt2.plugin.print.client.template.PrintableMapBuilder}.
 * 
 * @author Jan De Moerloose
 * @author An Buyle (support for extra layer with e.g. selected geometries)
 * @author Jan Venstermans
 */
public class DefaultPrintableMapBuilder implements PrintableMapBuilder {

	private static List<PrintableLayerBuilder> defaultLayerBuilders = new ArrayList<PrintableLayerBuilder>();
	
	static {
		registerDefaultLayerBuilder(new RasterServerLayerBuilder());
		registerDefaultLayerBuilder(new VectorServerLayerBuilder());
	}

	private List<PrintableLayerBuilder> layerBuilders = new ArrayList<PrintableLayerBuilder>();

	public DefaultPrintableMapBuilder() {
		layerBuilders.addAll(defaultLayerBuilders);
	}

	@Override
	public void registerLayerBuilder(PrintableLayerBuilder layerBuilder) {
		// prepend to the list take precedence !
		layerBuilders.add(0, layerBuilder);
	}

	@Override
	public void build(MapPresenter mapPresenter, Bbox worldBounds, double rasterResolution) {
		MapRasterizingInfo mapRasterizingInfo = buildMap(mapPresenter);
		createWidgetPrintLayers(mapPresenter, mapRasterizingInfo, worldBounds, rasterResolution);
	   	createModelLayersPrintLayers(mapPresenter, worldBounds, rasterResolution);
	}
	
	public static void registerDefaultLayerBuilder(PrintableLayerBuilder layerBuilder) {
		// prepend to the list to take precedence !
		defaultLayerBuilders.add(0, layerBuilder);
	}

	/* private methods */

	private MapRasterizingInfo buildMap(MapPresenter mapPresenter) {
		MapRasterizingInfo mapRasterizingInfo = new MapRasterizingInfo();
		ViewPort viewPort = mapPresenter.getViewPort();
		mapRasterizingInfo.setBounds(viewPort.getBounds());
		mapRasterizingInfo.setScale(1 / viewPort.getResolution());
		mapRasterizingInfo.setTransparent(true);
		LegendRasterizingInfo legendRasterizingInfo = new LegendRasterizingInfo();
		legendRasterizingInfo.setTitle("Legend");
		FontStyleInfo font = new FontStyleInfo();
		font.applyDefaults();
		legendRasterizingInfo.setFont(font);

		mapRasterizingInfo.setLegendRasterizingInfo(legendRasterizingInfo);

		// Support for selection of layer object : create container for info on selected features;
		// store the selections layer per layer
		List<ClientLayerInfo> selectedLayers = new ArrayList<ClientLayerInfo>();
		mapRasterizingInfo.setExtraLayers(selectedLayers);

		ClientMapInfo mapInfo = mapPresenter.getConfiguration().getHintValue(GeomajasServerExtension.MAPINFO);
		mapInfo.getWidgetInfo().put(MapRasterizingInfo.WIDGET_KEY, mapRasterizingInfo);
		// Note: mapRasterizingInfo at this time is pretty empty (rastering info for
		// layers not yet filled in)
		return mapRasterizingInfo;
	}

	private void createWidgetPrintLayers(MapPresenter mapPresenter, MapRasterizingInfo mapRasterizingInfo,
										 Bbox worldBounds, double rasterResolution) {
		AbsolutePanel mapPresenterAsAbsolutePanel = ((AbsolutePanel) mapPresenter.asWidget());
		for (int i = 0 ; i <  mapPresenterAsAbsolutePanel.getWidgetCount(); i++) {
			Widget widget = mapPresenterAsAbsolutePanel.getWidget(i);
			for (PrintableWidgetLayerBuilder widgetLayerPrintBuilder : getWidgetLayerBuilders()) {
				if (widgetLayerPrintBuilder.supports(widget)) {
					mapRasterizingInfo.getExtraLayers().add(
							widgetLayerPrintBuilder.build(mapPresenter, widget, worldBounds, rasterResolution));
					break;
				}
			}
		}
	}

	private void createModelLayersPrintLayers(MapPresenter mapPresenter,
										 Bbox worldBounds, double rasterResolution) {
		List<ClientLayerInfo> clientLayers = new ArrayList<ClientLayerInfo>();
		for (int i = 0; i < mapPresenter.getLayersModel().getLayerCount(); i++) {
			Layer layer = mapPresenter.getLayersModel().getLayer(i);
			for (PrintableLayersModelBuilder layerBuilder : getLayersModelBuilders()) {
				if (layerBuilder.supports(layer)) {
					clientLayers.add(layerBuilder.build(mapPresenter, layer, worldBounds, rasterResolution));
					break;
				}
			}
		}
		ClientMapInfo mapInfo = mapPresenter.getConfiguration().getHintValue(GeomajasServerExtension.MAPINFO);
		mapInfo.setLayers(clientLayers);
	}

	private List<PrintableLayersModelBuilder> getLayersModelBuilders() {
		List<PrintableLayersModelBuilder> layersModelPrintBuilders = new ArrayList<PrintableLayersModelBuilder>();
		for (PrintableLayerBuilder printableLayerBuilder : layerBuilders) {
			if (printableLayerBuilder instanceof PrintableLayersModelBuilder) {
				layersModelPrintBuilders.add((PrintableLayersModelBuilder) printableLayerBuilder);
			}
		}
		return layersModelPrintBuilders;
	}

	private List<PrintableWidgetLayerBuilder> getWidgetLayerBuilders() {
		List<PrintableWidgetLayerBuilder> widgetLayerPrintBuilders = new ArrayList<PrintableWidgetLayerBuilder>();
		for (PrintableLayerBuilder printableLayerBuilder : layerBuilders) {
			if (printableLayerBuilder instanceof PrintableWidgetLayerBuilder) {
				widgetLayerPrintBuilders.add((PrintableWidgetLayerBuilder) printableLayerBuilder);
			}
		}
		return widgetLayerPrintBuilders;
	}
}
