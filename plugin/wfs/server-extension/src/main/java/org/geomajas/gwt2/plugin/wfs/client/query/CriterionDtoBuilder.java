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
package org.geomajas.gwt2.plugin.wfs.client.query;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt2.client.map.attribute.PrimitiveType;
import org.geomajas.gwt2.client.map.feature.query.Criterion;
import org.geomajas.gwt2.client.map.feature.query.CriterionBuilder;
import org.geomajas.gwt2.client.map.feature.query.LogicalCriterion.Operator;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.BboxCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.BigDecimalAttributeCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.BooleanAttributeCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.CriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.DWithinCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.DateAttributeCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.DoubleAttributeCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.FidCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.FloatAttributeCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.FullTextCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.GeometryCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.IntegerAttributeCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.LogicalCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.LongAttributeCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.ShortAttributeCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.StringAttributeCriterionDto;

/**
 * Builds {@link Criterion} from DTO objects.
 * 
 * @author Jan De Moerloose
 *
 */
public class CriterionDtoBuilder implements CriterionBuilder {

	private String attributeName;

	private Object value;

	private String operation;

	private Double distance;

	private CriterionDto criterion;

	private String crs;

	private String units;

	private Bbox bbox;

	private PrimitiveType primitiveType;

	@Override
	public CriterionBuilder attribute(String attributeName) {
		this.attributeName = attributeName;
		return this;
	}

	@Override
	public CriterionBuilder operation(String operation) {
		this.operation = operation;
		return this;
	}

	@Override
	public CriterionBuilder value(Double value) {
		this.value = value;
		primitiveType = PrimitiveType.DOUBLE;
		return this;
	}

	@Override
	public CriterionBuilder value(Integer value) {
		this.value = value;
		primitiveType = PrimitiveType.INTEGER;
		return this;
	}

	@Override
	public CriterionBuilder value(Float value) {
		this.value = value;
		primitiveType = PrimitiveType.FLOAT;
		return this;
	}

	@Override
	public CriterionBuilder value(Short value) {
		this.value = value;
		primitiveType = PrimitiveType.SHORT;
		return this;
	}

	@Override
	public CriterionBuilder value(Long value) {
		this.value = value;
		primitiveType = PrimitiveType.LONG;
		return this;
	}

	@Override
	public CriterionBuilder value(BigDecimal value) {
		this.value = value;
		primitiveType = PrimitiveType.DOUBLE;
		return this;
	}

	@Override
	public CriterionBuilder value(String value) {
		this.value = value;
		return this;
	}

	@Override
	public CriterionBuilder value(Boolean value) {
		this.value = value;
		primitiveType = PrimitiveType.BOOLEAN;
		return this;
	}

	@Override
	public CriterionBuilder value(Date value) {
		this.value = value;
		primitiveType = PrimitiveType.DATE;
		return this;
	}

	@Override
	public CriterionBuilder value(Geometry value) {
		this.value = value;
		return this;
	}

	@Override
	public CriterionBuilder within(double distance, String units) {
		this.distance = distance;
		this.units = units;
		return this;
	}

	@Override
	public CriterionBuilder fid(String... fids) {
		criterion = new FidCriterionDto(fids);
		return this;
	}

	@Override
	public CriterionBuilder fullText(String key) {
		criterion = new FullTextCriterionDto(key);
		return this;
	}

	@Override
	public CriterionBuilder bbox(double xmin, double ymin, double xmax, double ymax) {
		this.bbox = new Bbox(xmin, ymin, xmax - xmin, ymax - ymin);
		return this;
	}

	@Override
	public CriterionBuilder bbox(Bbox bbox) {
		this.bbox = bbox;
		return this;
	}

	@Override
	public CriterionBuilder crs(String crs) {
		this.crs = crs;
		return this;
	}

	@Override
	public CriterionBuilder include() {
		criterion = CriterionDto.INCLUDE;
		return this;
	}

	@Override
	public CriterionBuilder exclude() {
		criterion = CriterionDto.EXCLUDE;
		return this;
	}

	@Override
	public CriterionBuilder or(List<Criterion> criteria) {
		criterion = new LogicalCriterionDto(Operator.OR, criteria.toArray(new Criterion[0]));
		return this;
	}

	@Override
	public CriterionBuilder and(List<Criterion> criteria) {
		criterion = new LogicalCriterionDto(Operator.AND, criteria.toArray(new Criterion[0]));
		return this;
	}

	@Override
	public Criterion build() {
		if (criterion == null) {
			if (value instanceof Geometry) {
				if (distance != null) {
					criterion = new DWithinCriterionDto(attributeName, distance, units, (Geometry) value);
				} else {
					criterion = new GeometryCriterionDto(attributeName, operation, (Geometry) value);
				}
			} else if (primitiveType != null) {
				switch (primitiveType) {
					case BOOLEAN:
						criterion = new BooleanAttributeCriterionDto(attributeName, operation, (Boolean) value);
						break;
					case DATE:
						criterion = new DateAttributeCriterionDto(attributeName, operation, (Date) value);
						break;
					case DOUBLE:
						if (value instanceof Double) {
							criterion = new DoubleAttributeCriterionDto(attributeName, operation, (Double) value);
						} else {
							criterion = new BigDecimalAttributeCriterionDto(attributeName, operation,
									(BigDecimal) value);
						}
						break;
					case FLOAT:
						criterion = new FloatAttributeCriterionDto(attributeName, operation, (Float) value);
						break;
					case INTEGER:
						criterion = new IntegerAttributeCriterionDto(attributeName, operation, (Integer) value);
						break;
					case LONG:
						criterion = new LongAttributeCriterionDto(attributeName, operation, (Long) value);
						break;
					case SHORT:
						criterion = new ShortAttributeCriterionDto(attributeName, operation, (Short) value);
						break;
					case STRING:
					default:
						criterion = new StringAttributeCriterionDto(attributeName, operation, (String) value);
						break;
				}
			} else if (bbox != null) {
				criterion = new BboxCriterionDto(attributeName, bbox);
				((BboxCriterionDto) criterion).setCrs(crs);
			}

		}
		return criterion;
	}
}
