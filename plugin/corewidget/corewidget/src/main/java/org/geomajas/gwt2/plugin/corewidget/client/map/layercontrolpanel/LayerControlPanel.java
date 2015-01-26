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
package org.geomajas.gwt2.plugin.corewidget.client.map.layercontrolpanel;

import com.google.gwt.user.client.ui.Composite;
import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.plugin.corewidget.client.CoreWidget;

import org.geomajas.gwt2.plugin.corewidget.client.map.layercontrolpanel.resource.LayerControlPanelResource;

/**
 * Layer control panel widget.
 *
 * <p>
 * A widget that displays the legend for a single layer. It provides the possibility to toggle that layer's visibility
 * through a CheckBox.
 * </p>
 * <p>
 * If the layer should become invisible because the map has zoomed in or out beyond the allowed scale range (for that
 * layer), the CheckBox in this widget will automatically become disabled. It is no use to start marking a layer visible
 * when it won't appear anyway.
 * </p>
 *
 * @author Pieter De Graef
 * @author Dosi Bingov
 *
 * @since 2.1.0
 */
@Api(allMethods = true)
public class LayerControlPanel extends Composite {

	private LayerControlPanelPresenter presenter;

	private LayerControlPanelView view;

	/**
	 *  Create {@link LayerControlPanel} widget.
	 *
	 * @param mapPresenter The {@link MapPresenter} that holds the layer.
	 * @param layer {@link Layer} The layer to display.
	 * @param disableToggleOutOfRange when false layer visibility toggle control (e.g. checkbox) stays enabled even if
	 *                                   the layer is out of range.
	 */
	public LayerControlPanel(MapPresenter mapPresenter, Layer layer, boolean disableToggleOutOfRange) {
		this(CoreWidget.getInstance().getClientBundleFactory().createLayerControlPanelResource(),
				mapPresenter, layer, disableToggleOutOfRange);
	}

	/**
	 *
	 * Create {@link LayerControlPanel} widget.
	 *
	 * @param resource GWT client resource bundle {@link LayerControlPanelResource}.
	 * @param mapPresenter {@link MapPresenter}
	 * @param layer {@link Layer}
	 * @param disableToggleOutOfRange when false layer visibility toggle control (e.g. chekcbox) stays enabled even if
	 *                                   the layer is out of range.
	 */
	public LayerControlPanel(LayerControlPanelResource resource, MapPresenter mapPresenter, Layer layer,
							 boolean disableToggleOutOfRange) {
		view = CoreWidget.getInstance().getViewFactory().createLayerControlPanel(resource);
		presenter = new LayerControlPanelPresenterImpl(view, layer, mapPresenter, disableToggleOutOfRange);
		view.setPresenter(presenter);
		view.setLayer(layer);
		initWidget(view.asWidget());
	}

	/**
	 * Return the target layer of the widget.
	 *
	 * @return The layer of this widget.
	 */
	public Layer getLayer() {
		return view.getLayer();
	}
}
