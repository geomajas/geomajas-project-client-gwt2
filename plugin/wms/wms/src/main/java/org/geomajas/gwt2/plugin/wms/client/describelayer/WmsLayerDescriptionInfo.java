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
