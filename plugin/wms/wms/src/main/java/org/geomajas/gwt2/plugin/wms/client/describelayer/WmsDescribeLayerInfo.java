// Copyright (C) 2010-2011 DOV, http://dov.vlaanderen.be/
// All rights reserved

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
