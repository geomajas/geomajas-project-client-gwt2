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

import java.util.List;


/**
 * DTO object for attribute criteria.
 * 
 * @author Jan De Moerloose
 *
 * @param <T>
 */
public abstract class ArrayCriterionDto<T> implements CriterionDto {

	private static final long serialVersionUID = 1L;

	private String attributeName;

	private String operation;

	private List<T> values;

	protected ArrayCriterionDto() {
	}

	protected ArrayCriterionDto(String attributeName, String operation, List<T> values) {
		this.attributeName = attributeName;
		this.operation = operation;
		this.values = values;
	}

	@Override
	public void accept(CriterionDtoVisitor visitor, Object context) {
		//visitor.visit(this, context);
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }

    public abstract ArrayCriterionDto<T> clone();
}
