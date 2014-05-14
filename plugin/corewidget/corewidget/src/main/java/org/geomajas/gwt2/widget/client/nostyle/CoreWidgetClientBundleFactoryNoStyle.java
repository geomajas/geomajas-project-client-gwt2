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

package org.geomajas.gwt2.widget.client.nostyle;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.widget.client.CoreWidgetClientBundleFactory;
import org.geomajas.gwt2.widget.client.featureselectbox.resource.FeatureSelectBoxResource;

import com.google.gwt.core.client.GWT;

/**
 * No style factory for client bundles defined within this artifact. This factory wipes out all css.
 * 
 * @author Jan De Moerloose
 * @since 2.1.0
 */
@Api(allMethods = true)
public class CoreWidgetClientBundleFactoryNoStyle extends CoreWidgetClientBundleFactory {

	/**
	 * Create an empty resource bundle for the
	 * {@link org.geomajas.gwt2.widget.client.featureselectbox.FeatureSelectListener} widget.
	 * 
	 * @return A new resource bundle.
	 */
	public FeatureSelectBoxResource createFeatureSelectBoxResource() {
		return GWT.create(FeatureSelectBoxResourceNoStyle.class);
	}

}
