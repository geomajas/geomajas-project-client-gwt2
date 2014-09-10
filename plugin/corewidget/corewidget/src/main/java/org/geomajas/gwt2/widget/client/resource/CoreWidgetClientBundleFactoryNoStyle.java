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

package org.geomajas.gwt2.widget.client.resource;

import com.google.gwt.core.client.GWT;
import org.geomajas.gwt2.widget.client.feature.featureinfo.resource.FeatureInfoResource;
import org.geomajas.gwt2.widget.client.feature.featureinfo.resource.FeatureInfoResourceNoStyle;
import org.geomajas.gwt2.widget.client.feature.featureselectbox.resource.FeatureSelectBoxResource;
import org.geomajas.gwt2.widget.client.feature.featureselectbox.resource.FeatureSelectBoxResourceNoStyle;

import com.google.gwt.core.client.GWT;
import org.geomajas.annotation.Api;
import org.geomajas.gwt2.widget.client.map.layercontrolpanel.resource.LayerControlPanelResource;
import org.geomajas.gwt2.widget.client.map.layercontrolpanel.resource.LayerControlPanelResourceNoStyle;
import org.geomajas.gwt2.widget.client.map.mapcontrolpanel.resource.MapControlPanelResource;
import org.geomajas.gwt2.widget.client.map.mapcontrolpanel.resource.MapControlPanelResourceNoStyle;

/**
 * No style factory for client bundles defined within this artifact. This factory wipes out all css.
 *
 * @author Jan De Moerloose
 * @since 2.1.0
 */
@Api(allMethods = true)
public class CoreWidgetClientBundleFactoryNoStyle extends CoreWidgetClientBundleFactory {

	/**
	 * Create a new resource bundle for the
	 * {@link org.geomajas.gwt2.widget.client.map.layercontrolpanel.LayerControlPanel} widget.
	 *
	 * Create an empty resource bundle for the
	 * {@link org.geomajas.gwt2.widget.client.FeatureSelectBox.FeatureSelectListener} widget.
	 *
	 * @return A new resource bundle.
	 */
	public LayerControlPanelResource createLayerControlPanelResource() {
		return GWT.create(LayerControlPanelResourceNoStyle.class);
	}

	/**
	 * Create a new resource bundle for the
	 * {@link org.geomajas.gwt2.widget.client.map.mapcontrolpanel.MapControlPanel} widget.
	 *
	 * @return A new resource bundle.
	 */
	public MapControlPanelResource createMapControlPanelResource() {
		return GWT.create(MapControlPanelResourceNoStyle.class);
	}
	/**
	 * Create an empty resource bundle for the {@link org.geomajas.gwt2.widget.client.feature.featureinfo.FeatureInfoWidget}.
	 *
	 * @return A new resource bundle.
	 */
	public FeatureInfoResource createFeatureInfoResource() {
		return GWT.create(FeatureInfoResourceNoStyle.class);
	}

}
