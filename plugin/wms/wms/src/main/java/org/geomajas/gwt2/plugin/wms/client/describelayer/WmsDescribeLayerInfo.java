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

package org.geomajas.gwt2.plugin.wms.client.describelayer;

import java.io.Serializable;
import java.util.List;

/**
 * Generic WMS DescribeLayer definition.
 * 
 * @author Jan De Moerloose
 */
public interface WmsDescribeLayerInfo extends Serializable {

	/**
	 * Retrieve the list of layer descriptions.
	 * 
	 * @return The list of layer descriptions.
	 */
	List<WmsLayerDescriptionInfo> getLayerDescriptions();

}
