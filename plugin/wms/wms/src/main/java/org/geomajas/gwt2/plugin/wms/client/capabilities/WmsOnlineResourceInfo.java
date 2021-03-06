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

package org.geomajas.gwt2.plugin.wms.client.capabilities;

import org.geomajas.annotation.Api;

import java.io.Serializable;

/**
 * Definition of an online resource, used within a WMS GetCapabilities file.
 *
 * @author Pieter De Graef
 * @since 2.1.0
 */
@Api
public interface WmsOnlineResourceInfo extends Serializable {

	/**
	 * Get the online resource type.
	 *
	 * @return The online resource type.
	 */
	String getType();

	/**
	 * Get the XLink.
	 *
	 * @return The XLink.
	 */
	String getXLink();

	/**
	 * Get the actual URL to the online resource.
	 *
	 * @return The URL to the resource.
	 */
	String getHref();
}
