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
package org.geomajas.gwt2.widget.client.layercontrolpanel;

import com.google.gwt.user.client.ui.Composite;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.widget.client.CoreWidget;

import org.geomajas.gwt2.widget.client.layercontrolpanel.resource.LayerControlPanelResource;

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
 */
public class LayerControlPanel extends Composite {

	private LayerControlPanelPresenter presenter;

	private LayerControlPanelView view;

	/**
	 * Constructor.
	 *
	 * @param eventBus
	 * @param layer
	 */
	public LayerControlPanel(MapEventBus eventBus, Layer layer) {
		this(CoreWidget.getInstance().getClientBundleFactory().createLayerControlPanelResource(), eventBus, layer);
	}

	/**
	 * Constructor.
	 *
	 * @param resource
	 * @param eventBus
	 * @param layer
	 */
	public LayerControlPanel(LayerControlPanelResource resource, MapEventBus eventBus, Layer layer) {
		view = CoreWidget.getInstance().getViewFactory().createLayerControlPanel(resource);
		presenter = new LayerControlPanelPresenterImpl(view, layer, eventBus);
		view.setPresenter(presenter);
		initWidget(view.asWidget());
	}


	/**
	 * Return the target layer for this legend panel.
	 *
	 * @return The layer who's styles are displayed within this panel.
	 */
	public Layer getLayer() {
		return presenter.getLayer();
	}
}
