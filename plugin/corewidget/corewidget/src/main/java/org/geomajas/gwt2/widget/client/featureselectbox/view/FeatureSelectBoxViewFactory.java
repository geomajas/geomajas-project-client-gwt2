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
package org.geomajas.gwt2.widget.client.featureselectbox.view;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

/**
 * MVP view factory for {@link FeatureSelectBoxView}.
 * 
 * @author Jan De Moerloose
 * @since 2.1.0
 * 
 */
@Api(allMethods = true)
@UserImplemented
public interface FeatureSelectBoxViewFactory {

	/**
	 * Create an instance of {@link FeatureSelectBoxView}.
	 * 
	 * @return the instance
	 */
	FeatureSelectBoxView create();
}
