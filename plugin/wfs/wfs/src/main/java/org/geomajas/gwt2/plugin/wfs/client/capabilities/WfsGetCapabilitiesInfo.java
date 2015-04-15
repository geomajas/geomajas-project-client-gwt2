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

package org.geomajas.gwt2.plugin.wfs.client.capabilities;

import java.io.Serializable;

/**
 * Generic WFS GetCapabilities definition.
 *
 * @author Jan De Moerloose
 */
public interface WfsGetCapabilitiesInfo extends Serializable {

	/**
	 * Retrieve the list of the feature types for this WFS server.
	 *
	 * @return The list of the feature types.
	 */
	WfsFeatureTypeListInfo getFeatureTypeList();
}
