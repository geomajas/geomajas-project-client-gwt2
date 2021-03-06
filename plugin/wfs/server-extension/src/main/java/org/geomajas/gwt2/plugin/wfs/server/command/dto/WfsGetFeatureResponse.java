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

package org.geomajas.gwt2.plugin.wfs.server.command.dto;

import org.geomajas.command.CommandResponse;
import org.geomajas.gwt2.plugin.wfs.server.dto.WfsFeatureCollectionDto;

/**
 * Response for the GetFeatureInfo command. It returns the features found.
 *
 * @author Pieter De Graef
 */
public class WfsGetFeatureResponse extends CommandResponse {

	private static final long serialVersionUID = 1L;

	private WfsFeatureCollectionDto featureCollection;

	public WfsGetFeatureResponse() {
	}

	public WfsFeatureCollectionDto getFeatureCollection() {
		return featureCollection;
	}

	public void setFeatureCollection(WfsFeatureCollectionDto featureCollection) {
		this.featureCollection = featureCollection;
	}

}