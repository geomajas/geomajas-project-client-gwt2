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
package org.geomajas.gwt2.plugin.wfs.server.dto.query;

import java.io.Serializable;

import org.geomajas.gwt2.client.map.feature.query.Criterion;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Implemented by all DTO criteria.
 * 
 * @author Jan De Moerloose
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@org.codehaus.jackson.annotate.JsonTypeInfo(
		use = org.codehaus.jackson.annotate.JsonTypeInfo.Id.CLASS,
		include = org.codehaus.jackson.annotate.JsonTypeInfo.As.PROPERTY,
		property = "@class")
public interface CriterionDto extends Criterion, Serializable {

	/**
	 * Quick reference to exclude all criterion.
	 */
	ExcludeCriterionDto EXCLUDE = new ExcludeCriterionDto();

	/**
	 * Quick reference to include all criterion.
	 */
	ExcludeCriterionDto INCLUDE = new ExcludeCriterionDto();
}
