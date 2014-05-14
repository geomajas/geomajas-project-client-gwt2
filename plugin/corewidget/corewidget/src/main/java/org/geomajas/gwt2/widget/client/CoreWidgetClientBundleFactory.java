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
import org.geomajas.gwt2.widget.client.featureselectbox.resource.FeatureSelectBoxResource;

import com.google.gwt.core.client.GWT;

/**
 * Default factory for client bundles defined within this artifact. By using such a factory, it is possible to easily
 * override the default client bundles using deferred binding.
 *
 * @author Jan De Moerloose
 * @since 2.1.0
 */
@Api(allMethods = true)
public class CoreWidgetClientBundleFactory {

	/**
	 * Create a new resource bundle for the {@link org.geomajas.gwt2.widget.client.featureselectbox.FeatureSelectListener} widget.
	 *
	 * @return A new resource bundle.
	 */
	public FeatureSelectBoxResource createFeatureSelectBoxResource() {
		return GWT.create(FeatureSelectBoxResource.class);
	}

}
