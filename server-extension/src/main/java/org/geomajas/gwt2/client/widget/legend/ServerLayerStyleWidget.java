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

package org.geomajas.gwt2.client.widget.legend;

import org.geomajas.sld.RuleInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * <p>
 * Widget that displays a single style for a {@link org.geomajas.gwt2.client.map.layer.ServerLayer}. For a
 * {@link org.geomajas.gwt2.client.map.layer.VectorServerLayer} that widget will represent a single SLD rule, for a
 * {@link org.geomajas.gwt2.client.map.layer.RasterServerLayer} this widget is used as the entire legend widget.
 * </p>
 * 
 * @author Pieter De Graef
 * @see DynamicVectorServerLayerLegend
 * @see RasterServerLayerLegend
 */
public class ServerLayerStyleWidget implements IsWidget {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface ContentWidgetViewUiBinder extends UiBinder<Widget, ServerLayerStyleWidget> {
	}

	private static final ContentWidgetViewUiBinder UI_BINDER = GWT.create(ContentWidgetViewUiBinder.class);

	protected static final String LEGEND_ICON_EXTENSION = ".png";

	private final String title;

	private final String url;

	private Widget widget;

	@UiField
	protected Image icon;

	@UiField
	protected Label label;

	private RuleInfo rule;

	protected ServerLayerStyleWidget(String url, String title, RuleInfo rule) {
		this.title = title;
		this.url = url;
		this.rule = rule;
	}

	public Widget asWidget() {
		if (widget == null) {
			widget = UI_BINDER.createAndBindUi(this);
			icon.setUrl(url);
			label.setText(title);
		}
		return widget;
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

	public RuleInfo getRule() {
		return rule;
	}

	protected static String addPath(String baseUrl, String path) {
		if (path.startsWith("/") && baseUrl.endsWith("/")) {
			baseUrl = baseUrl + path.substring(1);
		} else if (baseUrl.endsWith("/")) {
			baseUrl = baseUrl + path;
		} else {
			baseUrl = baseUrl + "/" + path;
		}
		return baseUrl;
	}
}