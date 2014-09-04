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

package org.geomajas.gwt2.plugin.wms.client.capabilities;

import org.geomajas.annotation.Api;

import java.io.Serializable;
import java.util.List;

/**
 * Generic WMS GetCapabilities definition.
 *
 * @author Pieter De Graef
 * @since 2.1.0
 */
@Api
public interface WmsGetCapabilitiesInfo extends Serializable {

	/**
	 * Retrieve the list of supported request types for this WMS server.
	 *
	 * @return The list of supported requests.
	 */
	List<WmsRequestInfo> getRequests();

	/**
	 * Retrieve the list of layers defined in the capabilities file.
	 *
	 * @return The full list of layers.
	 */
	List<WmsLayerInfo> getLayers();
}
