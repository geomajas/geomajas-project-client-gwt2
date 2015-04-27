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
import org.geomajas.gwt2.plugin.wfs.server.dto.WfsFeatureTypeDescriptionDto;

/**
 * Response for the {@link org.geomajas.gwt2.plugin.wfs.server.command.WfsDescribeFeatureTypeCommand} command.
 *
 * @author Jan De Moerloose
 */
public class WfsDescribeFeatureTypeResponse extends CommandResponse {

	private static final long serialVersionUID = 100L;

	private WfsFeatureTypeDescriptionDto featureTypeDescription;

	public WfsDescribeFeatureTypeResponse() {
	}

	public WfsFeatureTypeDescriptionDto getFeatureTypeDescription() {
		return featureTypeDescription;
	}

	public void setFeatureTypeDescription(WfsFeatureTypeDescriptionDto featureTypeDescription) {
		this.featureTypeDescription = featureTypeDescription;
	}

}
