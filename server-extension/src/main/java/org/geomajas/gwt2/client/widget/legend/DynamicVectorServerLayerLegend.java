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

package org.geomajas.gwt2.client.widget.legend;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.event.LayerStyleChangedEvent;
import org.geomajas.gwt2.client.event.LayerStyleChangedHandler;
import org.geomajas.gwt2.client.event.ViewPortChangedEvent;
import org.geomajas.gwt2.client.event.ViewPortChangedHandler;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.VectorServerLayer;
import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.RuleInfo;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Widget that represents the legend for a {@link VectorServerLayer}. This legend will only display the currently
 * visible styles and update automatically. For example, every style rule may have a minimum and/or maximum scale range.
 * Should the map be out of that range, the corresponding style will be made invisible.<br/>
 * This legend will also react to {@link LayerStyleChangedEvent}s to update automatically.
 * 
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public class DynamicVectorServerLayerLegend implements IsWidget {

	private final List<ServerLayerStyleWidget> ruleWidgets = new ArrayList<ServerLayerStyleWidget>();

	private final ViewPort viewPort;

	private final VectorServerLayer layer;

	private VerticalPanel layout;

	/**
	 * Create a new legend widget.
	 * 
	 * @param eventBus
	 *            The map eventbus. Used to react to the correct events to update the legend content.
	 * @param viewPort
	 *            The viewport.
	 * @param layer
	 *            The layer for which to build a legend.
	 */
	protected DynamicVectorServerLayerLegend(MapEventBus eventBus, ViewPort viewPort, VectorServerLayer layer) {
		this.viewPort = viewPort;
		this.layer = layer;

		// Zooming in or out may cause some styles to become visible/invisible:
		eventBus.addViewPortChangedHandler(new ViewPortChangedHandler() {

			public void onViewPortChanged(ViewPortChangedEvent event) {
				updateVisibility();
			}
		});

		// Update the legend widget when the style on this layer changes:
		eventBus.addLayerStyleChangedHandler(new LayerStyleChangedHandler() {

			@Override
			public void onLayerStyleChanged(LayerStyleChangedEvent event) {
				layout.clear();
				buildLegend(layout);
			}
		}, layer);
	}

	@Override
	public Widget asWidget() {
		if (layout == null) {
			layout = new VerticalPanel();
			buildLegend(layout);
		}
		return layout;
	}

	/**
	 * Get the layer for this legend.
	 * 
	 * @return The layer.s
	 */
	public VectorServerLayer getLayer() {
		return layer;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void buildLegend(VerticalPanel layout) {
		NamedStyleInfo styleInfo = layer.getLayerInfo().getNamedStyleInfo();
		int i = 0;
		for (FeatureTypeStyleInfo sfi : styleInfo.getUserStyle().getFeatureTypeStyleList()) {
			for (RuleInfo rInfo : sfi.getRuleList()) {
				String url = GeomajasServerExtension.getInstance().getEndPointService().getLegendServiceUrl();
				ServerLayerStyleWidget.addPath(url, layer.getServerLayerId());
				ServerLayerStyleWidget.addPath(url, styleInfo.getName());
				ServerLayerStyleWidget.addPath(url, i + ".png");
				ServerLayerStyleWidget widget = new ServerLayerStyleWidget(URL.encode(url), rInfo.getName(), rInfo);
				ruleWidgets.add(widget);
				layout.add(widget);
				i++;
			}
		}
	}

	private void updateVisibility() {
		for (ServerLayerStyleWidget ruleWidget : ruleWidgets) {
			ruleWidget.asWidget().setVisible(isVisible(ruleWidget.getRule(), viewPort));
		}
	}

	/** TODO This method might be interesting as a static method in some SLD utility class. */
	private boolean isVisible(RuleInfo rule, ViewPort viewPort) {
		if (rule == null) {
			return true;
		}
		double minScale = Double.MAX_VALUE;
		double maxScale = Double.MIN_VALUE;

		if (rule.getMinScaleDenominator() != null && rule.getMinScaleDenominator().getMinScaleDenominator() != 0) {
			minScale = viewPort.toScale(rule.getMinScaleDenominator().getMinScaleDenominator());
		}

		if (rule.getMaxScaleDenominator() != null && rule.getMaxScaleDenominator().getMaxScaleDenominator() != 0) {
			maxScale = viewPort.toScale(rule.getMaxScaleDenominator().getMaxScaleDenominator());
		}

		return (maxScale <= viewPort.getScale() && viewPort.getScale() < minScale);
	}
}