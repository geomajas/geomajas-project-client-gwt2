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
package org.geomajas.gwt2.plugin.wfs.server.command.factory.impl;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.global.GeomajasException;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.client.map.attribute.GeometryAttributeType;
import org.geomajas.gwt2.plugin.wfs.client.query.dto.AttributeCriterionDto;
import org.geomajas.gwt2.plugin.wfs.client.query.dto.BboxCriterionDto;
import org.geomajas.gwt2.plugin.wfs.client.query.dto.CriterionDto;
import org.geomajas.gwt2.plugin.wfs.client.query.dto.CriterionDtoVisitor;
import org.geomajas.gwt2.plugin.wfs.client.query.dto.DWithinCriterionDto;
import org.geomajas.gwt2.plugin.wfs.client.query.dto.ExcludeCriterionDto;
import org.geomajas.gwt2.plugin.wfs.client.query.dto.FidCriterionDto;
import org.geomajas.gwt2.plugin.wfs.client.query.dto.FullTextCriterionDto;
import org.geomajas.gwt2.plugin.wfs.client.query.dto.GeometryCriterionDto;
import org.geomajas.gwt2.plugin.wfs.client.query.dto.IncludeCriterionDto;
import org.geomajas.gwt2.plugin.wfs.client.query.dto.LogicalCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.command.factory.CriterionToFilterConverter;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geotools.filter.text.ecql.ECQL;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.simplify.DouglasPeuckerSimplifier;

/**
 * Converts {@link CriterionDto} to a geotools {@link Filter}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class DefaultCriterionFilterConverter implements CriterionToFilterConverter, CriterionDtoVisitor {

	private final Logger log = LoggerFactory.getLogger(DefaultCriterionFilterConverter.class);

	private FilterService filterService;

	private DtoConverterService converterService;

	private int maxCoordinatesInGeometry = -1;

	private boolean roundCoordinates;

	private int roundNumberOfDecimals = 2;

	public DefaultCriterionFilterConverter(FilterService filterService, DtoConverterService converterService) {
		this.filterService = filterService;
		this.converterService = converterService;
	}

	public void setMaxCoordinatesInGeometry(int maxCoordinatesInGeometry) {
		this.maxCoordinatesInGeometry = maxCoordinatesInGeometry;
	}

	public void setRoundCoordinates(boolean roundCoordinates) {
		this.roundCoordinates = roundCoordinates;
	}
	
	public void setRoundNumberOfDecimals(int roundNumberOfDecimals) {
		this.roundNumberOfDecimals = roundNumberOfDecimals;
	}
	
	/**
	 * Convert a criterion to a filter.
	 * 
	 * @param criterionDto
	 * @param schema
	 * @return
	 */
	public Filter convert(CriterionDto criterionDto, List<AttributeDescriptor> schema) {
		FilterContext fc = new FilterContext();
		fc.setSchema(schema);
		criterionDto.accept(this, fc);
		try {
			log.debug("Filter converted : " + ECQL.toCQL(fc.getFilter()));
		} catch (Exception e) {
			// ignore
		}
		return fc.getFilter();
	}

	@Override
	public void visit(LogicalCriterionDto criterion, Object context) {
		FilterContext fc = (FilterContext) context;
		if (criterion.getChildren().size() == 0) {
			fc.setFilter(Filter.EXCLUDE);
		} else {
			Filter filter = null;
			for (CriterionDto child : criterion.getChildren()) {
				child.accept(this, fc);
				if (filter == null) {
					filter = fc.getFilter();
				} else {
					filter = filterService.createLogicFilter(filter, criterion.getOperator().name(), fc.getFilter());
				}
			}
			fc.setFilter(filter);
		}
	}

	@Override
	public void visit(AttributeCriterionDto<?> criterion, Object context) {
		FilterContext fc = (FilterContext) context;
		String operation = criterion.getOperation();
		String name = criterion.getAttributeName();
		Filter filter = null;
		if ("like".equalsIgnoreCase(operation)) {
			filter = filterService.createLikeFilter(name, criterion.getValue().toString());
		} else if ("!=".equalsIgnoreCase(operation)) {
			filter = filterService.createCompareFilter(name, "<>", criterion.getValue().toString());
		} else {
			filter = filterService.createCompareFilter(name, operation, criterion.getValue().toString());
		}
		fc.setFilter(filter);
	}

	@Override
	public void visit(BboxCriterionDto criterion, Object context) {
		FilterContext fc = (FilterContext) context;
		Bbox bbox = criterion.getBbox();
		Envelope envelope = converterService.toInternal(bbox);
		String name = criterion.getAttributeName();
		if (name == null) {
			name = fc.getDefaultGeometryName();
		}
		String crs = criterion.getCrs();
		Filter filter = filterService.createBboxFilter(crs, envelope, name);
		fc.setFilter(filter);
	}

	@Override
	public void visit(FidCriterionDto criterion, Object context) {
		FilterContext fc = (FilterContext) context;
		String[] fids = criterion.getFids();

		Filter filter = filterService.createFidFilter(fids);
		fc.setFilter(filter);

	}

	@Override
	public void visit(GeometryCriterionDto criterion, Object context) {
		FilterContext fc = (FilterContext) context;
		String operation = criterion.getOperation();
		String name = criterion.getAttributeName();
		if (name == null) {
			name = fc.getDefaultGeometryName();
		}
		Geometry geometry;
		try {
			geometry = converterService.toInternal(criterion.getValue());
			geometry = check(geometry);
			Filter filter = null;
			FilterFactory2 ff = (FilterFactory2) filterService.getFilterFactory();
			Expression nameExpression = ff.property(name);
			Literal geomLiteral = ff.literal(geometry);
			if (GeometryCriterionDto.CONTAINS.equalsIgnoreCase(operation)) {
				filter = filterService.createContainsFilter(geometry, name);
			} else if (GeometryCriterionDto.CROSSES.equalsIgnoreCase(operation)) {
				filter = ff.crosses(nameExpression, geomLiteral);
			} else if (GeometryCriterionDto.DISJOINT.equalsIgnoreCase(operation)) {
				filter = ff.disjoint(nameExpression, geomLiteral);
			} else if (GeometryCriterionDto.EQUALS.equalsIgnoreCase(operation)) {
				filter = ff.equals(nameExpression, geomLiteral);
			} else if (GeometryCriterionDto.INTERSECTS.equalsIgnoreCase(operation)) {
				filter = filterService.createIntersectsFilter(geometry, name);
			} else if (GeometryCriterionDto.OVERLAPS.equalsIgnoreCase(operation)) {
				filter = filterService.createOverlapsFilter(geometry, name);
			} else if (GeometryCriterionDto.TOUCHES.equalsIgnoreCase(operation)) {
				filter = filterService.createTouchesFilter(geometry, name);
			} else if (GeometryCriterionDto.WITHIN.equalsIgnoreCase(operation)) {
				filter = filterService.createWithinFilter(geometry, name);
			}
			fc.setFilter(filter);
		} catch (GeomajasException e) {
			throw new IllegalArgumentException("Unparseable geometry in filter");
		}
	}

	@Override
	public void visit(DWithinCriterionDto criterion, Object context) {
		FilterContext fc = (FilterContext) context;
		String name = criterion.getAttributeName();
		if (name == null) {
			name = fc.getDefaultGeometryName();
		}
		Geometry geometry;
		try {
			geometry = converterService.toInternal(criterion.getValue());
			geometry = check(geometry);
			FilterFactory2 ff = (FilterFactory2) filterService.getFilterFactory();
			Expression nameExpression = ff.property(name);
			Literal geomLiteral = ff.literal(geometry);
			Filter filter = ff.dwithin(nameExpression, geomLiteral, criterion.getDistance(), criterion.getUnits());
			fc.setFilter(filter);
		} catch (GeomajasException e) {
			throw new IllegalArgumentException("Unparseable geometry in filter");
		}
	}

	private Geometry check(Geometry geometry) {
		if (maxCoordinatesInGeometry > 0) {
			int distanceTolerance = 10;
			while (geometry.getNumPoints() > maxCoordinatesInGeometry) {
				geometry = DouglasPeuckerSimplifier.simplify(geometry, distanceTolerance);
				distanceTolerance *= 2;
			}
		}
		if (roundCoordinates) {
			final double factor = Math.pow(10, roundNumberOfDecimals);
			geometry.apply(new CoordinateFilter() {

				@Override
				public void filter(Coordinate coord) {
					coord.x = Math.round(coord.x * factor) / factor;
					coord.y = Math.round(coord.y * factor) / factor;
				}
			});
		}
		return geometry;
	}

	@Override
	public void visit(FullTextCriterionDto criterion, Object context) {
		FilterContext fc = (FilterContext) context;
		List<AttributeDescriptor> schema = fc.getSchema();
		String key = "*" + criterion.getKey() + "*";
		if (schema == null || schema.size() == 0) {
			fc.setFilter(Filter.EXCLUDE);
		} else {
			List<Filter> filters = new ArrayList<Filter>();
			for (AttributeDescriptor descriptor : schema) {
				if (descriptor.getType().getBinding().equals(String.class)) {
					filters.add(filterService.createLikeFilter(descriptor.getName(), key));
				}
			}
			if (filters.size() > 1) {
				FilterFactory2 ff = (FilterFactory2) filterService.getFilterFactory();
				fc.setFilter(ff.or(filters));
			} else if (filters.size() == 1) {
				fc.setFilter(filters.get(0));
			} else {
				fc.setFilter(Filter.EXCLUDE);
			}
		}
	}

	@Override
	public void visit(IncludeCriterionDto criterion, Object context) {
		FilterContext fc = (FilterContext) context;
		fc.setFilter(Filter.INCLUDE);
	}

	@Override
	public void visit(ExcludeCriterionDto criterion, Object context) {
		FilterContext fc = (FilterContext) context;
		fc.setFilter(Filter.EXCLUDE);
	}

	/**
	 * Context class to pass along the filter.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class FilterContext {

		private Filter filter;

		private List<AttributeDescriptor> schema;

		public Filter getFilter() {
			return filter;
		}

		public List<AttributeDescriptor> getSchema() {
			return schema;
		}

		public void setSchema(List<AttributeDescriptor> schema) {
			this.schema = schema;
		}

		public void setFilter(Filter filter) {
			this.filter = filter;
		}

		public String getDefaultGeometryName() {
			if (schema != null) {
				for (AttributeDescriptor attributeDescriptor : schema) {
					if (attributeDescriptor.getType() instanceof GeometryAttributeType) {
						return attributeDescriptor.getName();
					}
				}
			}
			return "the_geom";
		}

	}

}
