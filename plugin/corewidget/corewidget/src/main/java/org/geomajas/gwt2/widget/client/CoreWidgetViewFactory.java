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

package org.geomajas.gwt2.widget.client;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.widget.client.map.layercontrolpanel.LayerControlPanelView;
import org.geomajas.gwt2.widget.client.map.layercontrolpanel.LayerControlPanelViewImpl;
import org.geomajas.gwt2.widget.client.map.layercontrolpanel.resource.LayerControlPanelResource;
import org.geomajas.gwt2.widget.client.map.mapcontrolpanel.MapControlPanelView;
import org.geomajas.gwt2.widget.client.map.mapcontrolpanel.MapControlPanelViewImpl;
import org.geomajas.gwt2.widget.client.map.mapcontrolpanel.resource.MapControlPanelResource;
import org.geomajas.gwt2.widget.client.feature.featureinfo.FeatureInfoView;
import org.geomajas.gwt2.widget.client.feature.featureinfo.FeatureInfoViewImpl;
import org.geomajas.gwt2.widget.client.feature.featureinfo.resource.FeatureInfoResource;

/**
 * MVP view factory for this plugin.
 *
 * @author Jan De Moerloose
 * @since 2.1.0
 */
@Api(allMethods = true)
public class CoreWidgetViewFactory {

	/**
	 * Create a new {@link LayerControlPanelView}.
	 *
	 * @return the view
	 */
	public LayerControlPanelView createLayerControlPanel(LayerControlPanelResource resource) {
		return new LayerControlPanelViewImpl(resource);
	}

	/**
	 * Create a new {@link MapControlPanelResource}.
	 *
	 * @return the view
	 */
	public MapControlPanelView createMapControlPanel(MapControlPanelResource resource) {
		return new MapControlPanelViewImpl(resource);
	}

	/**
	 * Create a new {@link org.geomajas.gwt2.widget.client.feature.featureinfo.FeatureInfoView}.
	 *
	 * @param resource the resource to use.
	 * @return a new feature info view.
	 */
	public FeatureInfoView createFeatureInfoView(FeatureInfoResource resource) {
		return new FeatureInfoViewImpl(resource);
	}
}
