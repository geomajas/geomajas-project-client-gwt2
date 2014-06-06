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

package org.geomajas.gwt2.widget.client.feature.featureselectbox.resource;

/**
 * Client resource bundle interface with empty css classes.
 * 
 * @author Jan De Moerloose
 */
public interface FeatureSelectBoxResourceNoStyle extends FeatureSelectBoxResource {

	/**
	 * Get the css resource.
	 * @return the css resource
	 */
	@Source("featureSelectBox-empty.css")
	FeatureSelectBoxCssResource css();	
}