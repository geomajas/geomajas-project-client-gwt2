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
package org.geomajas.gwt2.widget.client.map.mapcontrolpanel;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.widget.client.CoreWidget;
import org.geomajas.gwt2.widget.client.map.mapcontrolpanel.resource.MapControlPanelResource;

/**
 * Map control panel widget.
 *
 * Control panel that shows the layers for an entire map. It uses the
 * {@link org.geomajas.gwt2.widget.client.map.layercontrolpanel.LayerControlPanel} to render all individual layers.
 * This widget will keep track of the layers in the map's {@link org.geomajas.gwt2.client.map.layer.LayersModel}.
 * If a new layer is added or layer is removed from the map, this widget will change accordingly.
 *
 * @author Pieter De Graef
 * @author Dosi Bingov
 *
 * @since 2.1.0
 */
@Api(allMethods = true)
public class MapControlPanel implements IsWidget {

	private MapControlPanelPresenter presenter;

	private MapControlPanelView view;

	/**
	 * Create {@link MapControlPanel} widget.
	 *
	 * @param mapPresenter {@link MapPresenter}.
	 */
	public MapControlPanel(MapPresenter mapPresenter) {
		this(CoreWidget.getInstance().getClientBundleFactory().createMapControlPanelResource(), mapPresenter, true);
	}

	/**
	 * Create {@link MapControlPanel} widget.
	 *
	 * @param mapPresenter {@link MapPresenter}.
	 * @param disableToggleOutOfRange  Should layer visibility toggle control stay enabled when its layer is out
	 *                                    of range. Default when the layer is out of range visibility toggle control is
	 *                                    disabled.
	 */
	public MapControlPanel(MapPresenter mapPresenter, boolean disableToggleOutOfRange) {
		this(CoreWidget.getInstance().getClientBundleFactory().createMapControlPanelResource(), mapPresenter,
				disableToggleOutOfRange);
	}


	/**
	 * Create {@link MapControlPanel} widget.
	 *
	 * @param resource GWT client resource bundle {@link MapControlPanelResource}.
	 * @param mapPresenter {@link MapPresenter}.
	 * @param disableToggleOutOfRange  Should layer visibility toggle control stay enabled when its layer is out
	 *                                    of range. Default when the layer is out of range visibility toggle control is
	 *                                    disabled.
	 */
	public MapControlPanel(MapControlPanelResource resource, MapPresenter mapPresenter,
						   boolean disableToggleOutOfRange) {
		view = CoreWidget.getInstance().getViewFactory().createMapControlPanel(resource);
		presenter = new MapControlPanelPresenterImpl(view, mapPresenter, disableToggleOutOfRange);
		view.setPresenter(presenter);
	}

	/**
	 * Implementation of {@link com.google.gwt.user.client.ui.IsWidget}.
	 *
	 * @return {@link Widget}.
	 */
	@Override
	public Widget asWidget() {
		return view.asWidget();
	}
}
