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
package org.geomajas.gwt2.plugin.wfs.client.protocol;

import java.io.Serializable;
import java.util.List;

import org.geomajas.annotation.Api;

/**
 * Generic WFS feature type list definition.
 *
 * @author Jan De Moerloose
 * @since 2.2.1
 */
@Api(allMethods = true)
public interface WfsFeatureTypeListInfo extends Serializable {

	/**
	 * Get the feature types.
	 * 
	 * @return
	 */
	List<WfsFeatureTypeInfo> getFeatureTypes();
}
