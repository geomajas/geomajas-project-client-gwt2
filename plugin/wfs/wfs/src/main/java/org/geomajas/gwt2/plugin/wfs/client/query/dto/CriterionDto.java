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
package org.geomajas.gwt2.plugin.wfs.client.query.dto;

import java.io.Serializable;
/**
 * Common interface for criterion DTO objects.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface CriterionDto extends Serializable {

	CriterionDto EXCLUDE = new ExcludeCriterionDto();

	CriterionDto INCLUDE = new IncludeCriterionDto();

	String EQ = "=";

	String LT = "<";

	String LTEQ = "<=";

	String GT = ">";

	String GTEQ = ">=";

	String LIKE = "like";

    String EMPTY = "empty";
    
    String NE = "!=";
    
	void accept(CriterionDtoVisitor visitor, Object context);

}
