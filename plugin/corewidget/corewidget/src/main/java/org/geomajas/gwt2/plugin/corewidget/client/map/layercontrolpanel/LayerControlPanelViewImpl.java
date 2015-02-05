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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.plugin.corewidget.client.map.layercontrolpanel.resource.LayerControlPanelResource;

import java.util.logging.Logger;

/**
 * Default implementation of {@link LayerControlPanelView}.
 *
 * @author Dosi Bingov
 */
public class LayerControlPanelViewImpl implements LayerControlPanelView {

	private Logger log = Logger.getLogger(LayerControlPanelViewImpl.class.getName());

	private LayerControlPanelPresenter presenter;

	private Layer layer;

	private HorizontalPanel widget;

	@UiField
	protected CheckBox visibilityToggle;

	@UiField
	protected Label title;

	private LayerControlPanelResource resource;

	private static final LayerControlPanelUiBinder UIBINDER = GWT.create(LayerControlPanelUiBinder.class);

	/**
	 * UI binder interface.
	 *
	 * @author Dosi Bingov
	 *
	 */
	interface LayerControlPanelUiBinder extends UiBinder<Widget,  LayerControlPanelViewImpl> {
	}

	public LayerControlPanelViewImpl(LayerControlPanelResource layerControlPanelResource) {
		resource = layerControlPanelResource;
		widget = (HorizontalPanel) UIBINDER.createAndBindUi(this);
		widget.setStyleName(layerControlPanelResource.css().layerControlPanel());
		title.setStyleName(layerControlPanelResource.css().layerControlPanelTitle());
		visibilityToggle.addStyleName(layerControlPanelResource.css().layerControlPanelToggle());
		resource.css().ensureInjected();
		bindEvents();
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public Layer getLayer() {
		return this.layer;
	}

	@Override
	public void setLayer(Layer layer) {
		this.layer = layer;
	}

	@Override
	public void setPresenter(LayerControlPanelPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setLayerVisible(boolean visible) {
		visibilityToggle.setValue(visible);
	}

	@Override
	public void enableVisibilityToggle(boolean enable) {
		visibilityToggle.setEnabled(enable);
	}

	@Override
	public void setLayerTitle(String title) {
		this.title.setText(title);
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void bindEvents() {
		visibilityToggle.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (visibilityToggle.isEnabled()) {
					presenter.toggleLayerVisibility();
					visibilityToggle.setEnabled(true); // Works because JavaScript is single threaded...
				}
			}
		});
	}
}
