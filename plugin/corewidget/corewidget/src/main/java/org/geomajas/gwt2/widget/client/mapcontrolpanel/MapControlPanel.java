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
package org.geomajas.gwt2.widget.client.mapcontrolpanel;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.widget.client.CoreWidget;
import org.geomajas.gwt2.widget.client.mapcontrolpanel.resource.MapControlPanelResource;

/**
 * Map control panel widget.
 *
 * Legend panel that shows the legend for an entire map. It uses the
 * {@link org.geomajas.gwt2.widget.client.layercontrolpanel.LayerLegendPanel} to render the legends for
 * individual layers. This widget will keep track of the layers in the map's
 * {@link org.geomajas.gwt2.client.map.layer.LayersModel}. If a new layer is added or layer is removed from the map,
 * this widget will change the legend accordingly. Also if the order of layers change, this widget will change
 * accordingly.
 *
 * @author Pieter De Graef
 * @author Dosi Bingov
 * 
 */
public class MapControlPanel implements IsWidget {

	private MapControlPanelPresenter presenter;

	private MapControlPanelView view;


	public MapControlPanel(MapPresenter mapPresenter) {
		this(CoreWidget.getInstance().getClientBundleFactory().createMapControlPanelResource(), mapPresenter);
	}


	public MapControlPanel(MapControlPanelResource resource, MapPresenter mapPresenter) {
		view = CoreWidget.getInstance().getViewFactory().createMapControlPanel(resource);
		presenter = new MapControlPanelPresenterImpl(view, mapPresenter);
		view.setPresenter(presenter);
	}

	@Override
	public Widget asWidget() {
		return view.asWidget();
	}
}
