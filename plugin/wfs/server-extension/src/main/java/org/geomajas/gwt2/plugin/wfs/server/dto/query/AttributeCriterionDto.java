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

import org.geomajas.gwt2.client.map.feature.query.AttributeCriterion;
import org.geomajas.gwt2.client.map.feature.query.CriterionVisitor;

/**
 * DTO object for attribute criteria.
 * 
 * @author Jan De Moerloose
 *
 * @param <T>
 */
public abstract class AttributeCriterionDto<T> implements AttributeCriterion, CriterionDto {

	private static final long serialVersionUID = 1L;

	private String attributeName;

	private String operation; // =,<,>,<=,>=,like

	private T value;

	protected AttributeCriterionDto() {
	}

	protected AttributeCriterionDto(String attributeName, String operation, T value) {
		this.attributeName = attributeName;
		this.operation = operation;
		this.value = value;
	}

	@Override
	public void accept(CriterionVisitor visitor, Object context) {
		visitor.visit(this, context);
	}

	/**
	 * Get the attribute name.
	 * 
	 * @return
	 */
	@Override
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * Set the attribute name.
	 * 
	 * @param attributeName
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * Get the operation (see implementations for allowed values).
	 * 
	 * @return
	 */
	@Override
	public String getOperation() {
		return operation;
	}

	/**
	 * Set the operation (see implementations for allowed values).
	 * 
	 * @param operation
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * Get the value.
	 * 
	 * @return
	 */
	@Override
	public T getValue() {
		return value;
	}

	/**
	 * Set the value to compare with.
	 * 
	 * @param value
	 */
	public void setValue(T value) {
		this.value = value;
	}

	/**
	 * Deep-clone this criterion.
	 */
	@Override
	public abstract AttributeCriterionDto<T> clone();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributeName == null) ? 0 : attributeName.hashCode());
		result = prime * result + ((operation == null) ? 0 : operation.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/**
	 * Equals/hashcode.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AttributeCriterionDto other = (AttributeCriterionDto) obj;
		if (attributeName == null) {
			if (other.attributeName != null) {
				return false;
			}
		} else if (!attributeName.equals(other.attributeName)) {
			return false;
		}
		if (operation == null) {
			if (other.operation != null) {
				return false;
			}
		} else if (!operation.equals(other.operation)) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

}
