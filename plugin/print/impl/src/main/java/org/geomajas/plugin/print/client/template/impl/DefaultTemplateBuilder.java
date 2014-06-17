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

package org.geomajas.plugin.print.client.template.impl;

import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.layer.LayersModel;
import org.geomajas.gwt2.client.map.layer.RasterServerLayer;
import org.geomajas.gwt2.client.map.layer.VectorServerLayer;
import org.geomajas.plugin.print.client.template.PrintableMapBuilder;
import org.geomajas.plugin.print.client.template.TemplateBuilder;
import org.geomajas.plugin.print.client.util.PrintLayout;
import org.geomajas.plugin.print.command.dto.PrintTemplateInfo;
import org.geomajas.plugin.print.component.dto.ImageComponentInfo;
import org.geomajas.plugin.print.component.dto.LabelComponentInfo;
import org.geomajas.plugin.print.component.dto.LayoutConstraintInfo;
import org.geomajas.plugin.print.component.dto.LegendComponentInfo;
import org.geomajas.plugin.print.component.dto.LegendGraphicComponentInfo;
import org.geomajas.plugin.print.component.dto.LegendIconComponentInfo;
import org.geomajas.plugin.print.component.dto.LegendItemComponentInfo;
import org.geomajas.plugin.print.component.dto.MapComponentInfo;
import org.geomajas.plugin.print.component.dto.PageComponentInfo;
import org.geomajas.plugin.print.component.dto.PrintComponentInfo;
import org.geomajas.plugin.print.component.dto.RasterizedLayersComponentInfo;
import org.geomajas.plugin.print.component.dto.ScaleBarComponentInfo;
import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.RuleInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Default print template builder, parameters include title, size, raster DPI, orientation, etc...
 * 
 * @author Jan De Moerloose (smartGWT version)
 * @author An Buyle (GWT2 version)
 */
public class DefaultTemplateBuilder extends AbstractTemplateBuilder implements TemplateBuilder {

	protected double pageWidth;

	protected double pageHeight;

	protected int marginX;

	protected int marginY;

	protected String titleText;

	protected int rasterDpi;

	protected boolean withScaleBar;

	protected boolean withArrow;

	protected MapPresenter mapPresenter;

	protected String applicationId;

	private PrintableMapBuilder mapBuilder;

	public DefaultTemplateBuilder() {
		this(new DefaultPrintableMapBuilder());
	}

	public DefaultTemplateBuilder(PrintableMapBuilder mapBuilder) {
		this.mapBuilder = mapBuilder;
	}

	@Override
	public PrintTemplateInfo buildTemplate() {
		PrintTemplateInfo template = super.buildTemplate();
		template.setId(1L);
		template.setName("default");
		return template;
	}

	@Override
	protected PageComponentInfo buildPage() {
		PageComponentInfo page = super.buildPage();
		page.getLayoutConstraint().setWidth((float) pageWidth);
		page.getLayoutConstraint().setHeight((float) pageHeight);
		return page;
	}

	@Override
	protected MapComponentInfo buildMap() {
		ViewPort viewPort = mapPresenter.getViewPort();
		ClientMapInfo mapInfo = mapPresenter.getConfiguration().getHintValue(GeomajasServerExtension.MAPINFO);
		double printWidth = getPageWidth() - 2 * marginX;
		double printHeight = getPageHeight() - 2 * marginY;

		Bbox fittingBox = createFittingBox(viewPort.getBounds(), printWidth / printHeight);

		MapComponentInfo map = super.buildMap(fittingBox);
		map.getLayoutConstraint().setMarginX(marginX);
		map.getLayoutConstraint().setMarginY(marginY);

		map.setLocation(new org.geomajas.geometry.Coordinate(fittingBox.getX(), fittingBox.getY()));
		map.setPpUnit((float) (printWidth / fittingBox.getWidth()));

		map.setTag("map");
		map.setMapId(mapInfo.getId());

		map.setApplicationId(applicationId);
		map.setRasterResolution(rasterDpi);

		// use rasterized layers for pure GWT
		double rasterScale = map.getPpUnit() * map.getRasterResolution() / 72.0;
		// map.getPpUnit() = number of pixels per map unit at 72 dpi

		mapBuilder.build(mapPresenter, fittingBox, 1 / rasterScale);

		List<PrintComponentInfo> layers = new ArrayList<PrintComponentInfo>();
		RasterizedLayersComponentInfo rasterizedLayersComponentInfo = new RasterizedLayersComponentInfo();
		rasterizedLayersComponentInfo.setMapInfo(mapInfo);

		layers.add(rasterizedLayersComponentInfo);
		map.getChildren().addAll(0, layers);
		return map;
	}

	@Override
	protected ImageComponentInfo buildArrow() {
		if (isWithArrow()) {
			ImageComponentInfo northarrow = super.buildArrow();
			northarrow.setImagePath("/images/northarrow.gif");
			northarrow.getLayoutConstraint().setAlignmentX(LayoutConstraintInfo.RIGHT);
			northarrow.getLayoutConstraint().setAlignmentY(LayoutConstraintInfo.TOP);
			northarrow.getLayoutConstraint().setMarginX((float) PrintLayout.templateMarginX);
			northarrow.getLayoutConstraint().setMarginY((float) PrintLayout.templateMarginY);
			northarrow.getLayoutConstraint().setWidth((float) PrintLayout.templateNorthArrowWidth);
			northarrow.setTag("arrow");
			return northarrow;
		} else {
			return null;
		}
	}

	@Override
	protected LegendComponentInfo buildLegend() {
		LegendComponentInfo legend = super.buildLegend();
		FontStyleInfo style = new FontStyleInfo();
		style.setFamily(PrintLayout.templateDefaultFontFamily);
		style.setStyle(PrintLayout.templateDefaultFontStyle);
		style.setSize((int) PrintLayout.templateDefaultFontSize);
		legend.setFont(style);
		ClientMapInfo mapInfo = mapPresenter.getConfiguration().getHintValue(GeomajasServerExtension.MAPINFO);
		legend.setMapId(mapInfo.getId());

		legend.setTag("legend");
		LayersModel layersModel = this.mapPresenter.getLayersModel();
		for (int i = 0; i < layersModel.getLayerCount(); i++) {
			Layer layer = layersModel.getLayer(i);
			if (layer instanceof VectorServerLayer && layer.isShowing()) {
				VectorServerLayer vectorLayer = (VectorServerLayer) layer;
				ClientVectorLayerInfo layerInfo = (ClientVectorLayerInfo) vectorLayer.getLayerInfo();
				FeatureTypeStyleInfo fts = layerInfo.getNamedStyleInfo().getUserStyle().getFeatureTypeStyleList()
						.get(0);
				for (RuleInfo rule : fts.getRuleList()) {
					// use title if present, name if not
					String title = (rule.getTitle() != null ? rule.getTitle() : rule.getName());
					// fall back to style name
					if (title == null) {
						title = layerInfo.getNamedStyleInfo().getName();
					}
					LegendItemComponentInfo item = new LegendItemComponentInfo();
					LegendGraphicComponentInfo graphic = new LegendGraphicComponentInfo();
					graphic.setLabel(title);
					graphic.setRuleInfo(rule);
					graphic.setLayerId(layerInfo.getServerLayerId());
					item.addChild(graphic);
					item.addChild(getLegendLabel(legend, title));
					legend.addChild(item);
				}
			} else if (layer instanceof RasterServerLayer && layer.isShowing()) {
				RasterServerLayer rasterLayer = (RasterServerLayer) layer;
				ClientRasterLayerInfo layerInfo = rasterLayer.getLayerInfo();
				LegendItemComponentInfo item = new LegendItemComponentInfo();
				LegendIconComponentInfo icon = new LegendIconComponentInfo();
				icon.setLabel(layerInfo.getLabel());
				icon.setLayerType(layerInfo.getLayerType());
				item.addChild(icon);
				item.addChild(getLegendLabel(legend, layerInfo.getLabel()));
				legend.addChild(item);
			}
		}
		return legend;
	}

	@Override
	protected LegendComponentInfo buildLegend(Bbox bounds) {
		return buildLegend(); // fall-back
	}

	private LabelComponentInfo getLegendLabel(LegendComponentInfo legend, String text) {
		LabelComponentInfo legendLabel = new LabelComponentInfo();
		legendLabel.setBackgroundColor(PrintLayout.templateDefaultBackgroundColor);
		legendLabel.setBorderColor(PrintLayout.templateDefaultBorderColor);
		legendLabel.setFontColor(PrintLayout.templateDefaultColor);
		legendLabel.setFont(legend.getFont());
		legendLabel.setText(text);
		legendLabel.setTextOnly(true);
		return legendLabel;
	}

	@Override
	protected ScaleBarComponentInfo buildScaleBar() {
		if (isWithScaleBar()) {
			ScaleBarComponentInfo bar = super.buildScaleBar();
			bar.setTicNumber(3);
			bar.setTag("scalebar");
			return bar;
		} else {
			return null;
		}
	}

	@Override
	protected LabelComponentInfo buildTitle() {
		if (titleText != null) {
			LabelComponentInfo title = super.buildTitle();
			title.setText(titleText);
			title.getLayoutConstraint().setMarginY(2 * marginY);
			return title;
		} else {
			return null;
		}
	}

	public double getPageWidth() {
		return pageWidth;
	}

	public void setPageWidth(double pageWidth) {
		this.pageWidth = pageWidth;
	}

	public double getPageHeight() {
		return pageHeight;
	}

	public void setPageHeight(double pageHeight) {
		this.pageHeight = pageHeight;
	}

	public String getTitleText() {
		return titleText;
	}

	public void setTitleText(String titleText) {
		this.titleText = titleText;
	}

	public int getRasterDpi() {
		return rasterDpi;
	}

	public void setRasterDpi(int rasterDpi) {
		this.rasterDpi = rasterDpi;
	}

	public boolean isWithScaleBar() {
		return withScaleBar;
	}

	public void setWithScaleBar(boolean withScaleBar) {
		this.withScaleBar = withScaleBar;
	}

	public boolean isWithArrow() {
		return withArrow;
	}

	public void setWithArrow(boolean withArrow) {
		this.withArrow = withArrow;
	}

	public int getMarginX() {
		return marginX;
	}

	public void setMarginX(int marginX) {
		this.marginX = marginX;
	}

	public int getMarginY() {
		return marginY;
	}

	public void setMarginY(int marginY) {
		this.marginY = marginY;
	}

	public MapPresenter getMapPresenter() {
		return mapPresenter;
	}

	public void setMapPresenter(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * Creates the largest possible bounding box that fits around the specified bounding box but has a different
	 * width/height ratio. and the same centerpoint.
	 * 
	 * @param bbox
	 * @param newRatio
	 *            width/height ratio
	 * @return bbox
	 */
	public Bbox createFittingBox(Bbox bbox, double newRatio) {
		double oldRatio = bbox.getWidth() / bbox.getHeight();
		double newWidth = bbox.getWidth();
		double newHeight = bbox.getHeight();
		if (newRatio < oldRatio) {
			// Keep width of bbox , decrease height to fullfill newRatio
			newHeight = newWidth / newRatio;
		} else {
			// Keep height of bbox , decrease width to fullfill newRatio
			newWidth = newHeight * newRatio;
		}
		Bbox result = new Bbox(0, 0, newWidth, newHeight);
		result.setX(bbox.getX() + (bbox.getWidth() - newWidth) / 2.0);
		result.setY(bbox.getY() + (bbox.getHeight() - newHeight) / 2.0);
		return result;
	}
}
