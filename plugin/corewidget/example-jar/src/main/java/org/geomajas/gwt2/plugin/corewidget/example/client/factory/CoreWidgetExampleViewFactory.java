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
package org.geomajas.gwt2.plugin.corewidget.example.client.factory;

import org.geomajas.gwt2.plugin.corewidget.client.CoreWidgetViewFactory;
import org.geomajas.gwt2.plugin.corewidget.client.map.layercontrolpanel.LayerControlPanelView;
import org.geomajas.gwt2.plugin.corewidget.client.map.layercontrolpanel.resource.LayerControlPanelResource;
import org.geomajas.gwt2.plugin.corewidget.example.client.sample.map.LayerControlPanelLegendViewImpl;

/**
 * MVP view factory for core widget showcase.
 * 
 * @author Dosi Bingov
  * 
 */
public class CoreWidgetExampleViewFactory extends CoreWidgetViewFactory {

	@Override
	public LayerControlPanelView createLayerControlPanel(LayerControlPanelResource resource) {
		//change the view for LayerControl panel
		return new LayerControlPanelLegendViewImpl(resource);
	}
}
