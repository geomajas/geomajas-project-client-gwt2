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

package org.geomajas.plugin.wms.client.capabilities;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.wms.client.service.WmsService;

import java.io.Serializable;
import java.util.List;

/**
 * Definition of a supported request from the WMS Capabilities file. It contains a list of format that are supported
 * for this particular request.
 *
 * @author Pieter De Graef
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface WmsRequestInfo extends Serializable {

	/**
	 * The request type represented by this object.
	 *
	 * @return The request type.
	 */
	WmsService.WmsRequest getRequestType();

	/**
	 * Retrieve the list of formats supported for this particular WMS request.
	 *
	 * @return The full list of layers.
	 */
	List<String> getFormats();
}
