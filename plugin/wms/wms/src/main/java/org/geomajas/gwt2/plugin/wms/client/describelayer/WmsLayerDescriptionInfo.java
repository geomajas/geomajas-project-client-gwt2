// Copyright (C) 2010-2011 DOV, http://dov.vlaanderen.be/
// All rights reserved

package org.geomajas.gwt2.plugin.wms.client.describelayer;

/**
 * Generic WMS LayerDescription definition.
 * 
 * @author Jan De Moerloose
 */
public interface WmsLayerDescriptionInfo {

	/**
	 * Ows type for WFS.
	 */
	String WFS = "WFS";

	/**
	 * Ows type for WCS.
	 */
	String WCS = "WCS";

	/**
	 * Get the name of the layer.
	 * 
	 * @return the name of the layer
	 */
	String getName();

	/**
	 * Get the URL prefix for the WFS containing the feature data (redundant with owsUrl/owsType=WFS).
	 * 
	 * @return the url.
	 */
	String getWfs();

	/**
	 * Get the OGC Web Service type (WFS, WCS).
	 * 
	 * @return the type
	 */
	String getOwsType();

	/**
	 * Get the OGC Web Service base URL.
	 * 
	 * @return the url
	 */
	String getOwsUrl();

}
