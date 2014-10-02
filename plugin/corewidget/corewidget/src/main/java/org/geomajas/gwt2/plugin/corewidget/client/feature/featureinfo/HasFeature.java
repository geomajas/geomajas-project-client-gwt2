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

package org.geomajas.gwt2.plugin.corewidget.client.feature.featureinfo;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.feature.Feature;

/**
 * Interface for a class that has a {@link Feature}.
 * <p/>
 *
 * @author Youri Flement
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface HasFeature {

	/**
	 * Set the feature associated with the action.
	 *
	 * @param feature    The feature.
	 */
	void setFeature(Feature feature);

}
