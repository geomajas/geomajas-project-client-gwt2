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
package org.geomajas.gwt2.tools.client.tool.zoomin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.tools.client.tool.zoomin.resource.ZoomInToolWidgetResource;

/**
 * Zoom in button widget.
 *
 * @author Oliver May
 */
public class ZoomInToolWidget implements ZoomInToolPresenter.View, IsWidget {

	private final MapPresenter mapPresenter;

	/**
	 * UI binder for this widget.
	 *
	 * @author Pieter De Graef
	 */
	interface ZoomInWidgetUiBinder extends UiBinder<Widget, ZoomInToolWidget> {
	}

	private ZoomInToolWidgetResource resource;

	private ZoomInWidgetUiBinder uiBinder;

	@UiField
	protected SimplePanel zoomInElement;

	public ZoomInToolWidget(MapPresenter mapPresenter) {
		this(mapPresenter, (ZoomInToolWidgetResource) GWT.create(ZoomInToolWidgetResource.class),
				(ZoomInWidgetUiBinder) GWT.create(ZoomInWidgetUiBinder.class));
	}

	public ZoomInToolWidget(MapPresenter mapPresenter, ZoomInToolWidgetResource resource,
			ZoomInWidgetUiBinder uiBinder) {
		this.resource = resource;
		this.uiBinder = uiBinder;
		this.mapPresenter = mapPresenter;

	}

	@Override
	public void setDisabled(boolean disabled) {
		//FIXME
	}

	@Override
	public void addClickedHandler(ClickHandler handler) {
		zoomInElement.addDomHandler(handler, ClickEvent.getType());
	}

	@Override
	public Widget asWidget() {
		this.resource.css().ensureInjected();
		Widget layout = this.uiBinder.createAndBindUi(this);
		ZoomInToolPresenter presenter = new ZoomInToolPresenter.ZoomInToolPresenterImpl(mapPresenter);
		presenter.setView(this);
		return layout;
	}


}
