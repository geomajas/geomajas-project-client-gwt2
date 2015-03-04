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
package org.geomajas.gwt2.plugin.corewidget.client.map.mapcontrolpanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.plugin.corewidget.client.map.mapcontrolpanel.resource.MapControlPanelResource;

import java.util.logging.Logger;

/**
 * Default implementation of {@link MapControlPanelView}.
 *
 * @author Dosi Bingov
 */
public class MapControlPanelViewImpl implements MapControlPanelView {

	private Logger log = Logger.getLogger(MapControlPanelViewImpl.class.getName());

	private MapControlPanelPresenter presenter;

	private VerticalPanel contentPanel;

	private MapControlPanelResource resource;

	private static final MapControlPanelUiBinder UIBINDER = GWT.create(MapControlPanelUiBinder.class);

	/**
	 * UI binder interface.
	 *
	 * @author Dosi Bingov
	 *
	 */
	interface MapControlPanelUiBinder extends UiBinder<Widget, MapControlPanelViewImpl> {
	}

	public MapControlPanelViewImpl(MapControlPanelResource mapControlPanelResource) {
		resource = mapControlPanelResource;
		contentPanel = (VerticalPanel) UIBINDER.createAndBindUi(this);
		contentPanel.setStyleName(mapControlPanelResource.css().mapControlPanel());
		resource.css().ensureInjected();
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	@Override
	public Widget asWidget() {
		return contentPanel;
	}

	@Override
	public void setPresenter(MapControlPanelPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void moveWidget(int fromIndex, int toIndex) {
		Widget widget = contentPanel.getWidget(fromIndex);
		contentPanel.remove(fromIndex);
		contentPanel.insert(widget, toIndex);
	}

	@Override
	public void add(Widget widget) {
		contentPanel.add(widget);
	}

	@Override
	public boolean removeWidget(int index) {
		return contentPanel.remove(index);
	}

	@Override
	public int getWidgetCount() {
		return contentPanel.getWidgetCount();
	}

	@Override
	public Widget getWidgetAt(int index) {
		return contentPanel.getWidget(index);
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------


}
