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

package org.geomajas.gwt2.plugin.wfs.server.command.converter;

import java.math.BigDecimal;
import java.util.HashMap;

import org.geomajas.geometry.conversion.jts.GeometryConverterService;
import org.geomajas.geometry.conversion.jts.JtsConversionException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.attribute.BooleanAttribute;
import org.geomajas.layer.feature.attribute.DateAttribute;
import org.geomajas.layer.feature.attribute.DoubleAttribute;
import org.geomajas.layer.feature.attribute.FloatAttribute;
import org.geomajas.layer.feature.attribute.IntegerAttribute;
import org.geomajas.layer.feature.attribute.LongAttribute;
import org.geomajas.layer.feature.attribute.ShortAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.simplify.DouglasPeuckerSimplifier;

/**
 * Converts GeoTools simple features to Geomajas features. Extension of {@link FeatureConverter} to solve problem with
 * java.sql.Date versus java.util.Date.
 *
 * @author Pieter De Graef
 * @author An Buyle (fix problem with sql.Date versus java.util.Date)
 */
public class FeatureConverter {

	private final Logger log = LoggerFactory.getLogger(FeatureConverter.class);

	@SuppressWarnings("rawtypes")
	public Feature toDto(SimpleFeature feature, int maxCoordsPerFeature) throws IllegalArgumentException {
		if (feature == null) {
			throw new IllegalArgumentException("No feature was passed.");
		}
		Feature dto = new Feature(feature.getID());
		HashMap<String, Attribute> attributeMap = new HashMap<String, Attribute>();
		GeometryDescriptor geometryDescr = feature.getFeatureType().getGeometryDescriptor();
		for (org.opengis.feature.type.AttributeDescriptor desc : feature.getType().getAttributeDescriptors()) {
			if (null == geometryDescr || !geometryDescr.getName().getLocalPart().equals(desc.getLocalName())) {
				Object obj = feature.getAttribute(desc.getName());
				Attribute<?> value = toPrimitive(obj, desc.getType());
				attributeMap.put(desc.getLocalName(), value);
			}
		}

		dto.setAttributes(attributeMap);
		dto.setId(feature.getID());
		dto.setUpdatable(false);
		dto.setDeletable(false);

		Object defaultGeometry = feature.getDefaultGeometry();
		if (defaultGeometry instanceof Geometry) {
			Geometry geometry = (Geometry) defaultGeometry;
			if (maxCoordsPerFeature > 0) {
				// we take the tolerance from the 1st geometry to make sure we don't end up with an empty geometry !
				Envelope firstEnvelope = geometry.getGeometryN(0).getEnvelopeInternal();
				double distanceTolerance = (firstEnvelope.getWidth() + firstEnvelope.getHeight()) / maxCoordsPerFeature;
				while (geometry.getNumPoints() > maxCoordsPerFeature) {
					geometry = DouglasPeuckerSimplifier.simplify(geometry, distanceTolerance);
					distanceTolerance *= 2;
				}
			}
			try {
				dto.setGeometry(GeometryConverterService.fromJts(geometry));
			} catch (JtsConversionException e) {
				// OK then, no geometry for you...
				log.error("Error while parsing geometry from GML: " + e.getMessage());
			}
		}
		if(dto.getGeometry().getCoordinates() == null) {
			System.out.println();
		}
		return dto;
	}

	private Attribute<?> toPrimitive(Object value, AttributeType type) {
		// String attribute?
		if (type.getBinding().equals(String.class)) {
			return new StringAttribute(value == null ? null : value.toString());
		}
		// Number attributes:
		try {
			if ((Integer.class).equals(type.getBinding())) {
				return new IntegerAttribute((Integer) convertToClass(value, Integer.class));
			}
			if ((Short.class).equals(type.getBinding())) {
				return new ShortAttribute((Short) convertToClass(value, Short.class));
			} else if (Long.class.equals(type.getBinding())) {
				return new LongAttribute((Long) convertToClass(value, Long.class));
			} else if (Float.class.equals(type.getBinding())) {
				return new FloatAttribute((Float) convertToClass(value, Float.class));
			} else if (Double.class.equals(type.getBinding())) {
				return new DoubleAttribute((Double) convertToClass(value, Double.class));
			} else if (BigDecimal.class.equals(type.getBinding())) {
				return new DoubleAttribute((Double) convertToClass(
						null == value ? null : Double.valueOf(value.toString()), Double.class));
			}
		} catch (NumberFormatException e) {
			return new StringAttribute(value == null ? null : value.toString());
		}

		// Boolean and date attributes:
		if (Boolean.class.equals(type.getBinding())) {
			return new BooleanAttribute((Boolean) convertToClass(value, Boolean.class));
		} else if (java.sql.Date.class.equals(type.getBinding())) {
			// note: java.sql.Date extends java.util.Date but takes care that time part is of no importance
			return new DateAttribute((java.util.Date) convertToClass(value, java.sql.Date.class));
		} else if (java.sql.Timestamp.class.equals(type.getBinding())) {
			// java.sql.Timestamp extends java.util.Date
			return new DateAttribute((java.util.Date) convertToClass(value, java.sql.Timestamp.class));
		} else if (java.util.Date.class.equals(type.getBinding())) {
			// a branch with test on java.util.Date is still needed!
			return new DateAttribute((java.util.Date) convertToClass(value, java.util.Date.class));
		}

		// Last resort...
		return new StringAttribute(value == null ? null : value.toString());
	}

	private Object convertToClass(Object object, Class<?> c) {
		if (object == null) {
			return null;
		} else if (c.isInstance(object)) {
			return object;
		} else {
			return fromString(object.toString(), c);
		}
	}

	private Object fromString(String str, Class<?> c) {
		if (c.equals(Integer.class)) {
			return Integer.parseInt(str);
		} else if (c.equals(Short.class)) {
			return Short.parseShort(str);
		} else if (c.equals(Long.class)) {
			return Long.parseLong(str);
		} else if (c.equals(Float.class)) {
			return Float.parseFloat(str);
		} else if (c.equals(Double.class)) {
			return Double.parseDouble(str);
		} else if (c.equals(Boolean.class)) {
			return Boolean.valueOf(str);
		}
		return null;
	}
}
