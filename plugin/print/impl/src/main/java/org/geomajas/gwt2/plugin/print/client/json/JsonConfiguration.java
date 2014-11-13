/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.gwt2.plugin.print.client.json;

import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.LayerExtraInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientUserDataInfo;
import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.configuration.validation.ConstraintInfo;
import org.geomajas.plugin.printing.component.dto.PrintComponentInfo;
import org.geomajas.sld.SymbolizerTypeInfo;
import org.geomajas.sld.expression.ExpressionInfo;
import org.geomajas.sld.expression.ExpressionTypeInfo;
import org.geomajas.sld.filter.ComparisonOpsTypeInfo;
import org.geomajas.sld.filter.LogicOpsTypeInfo;
import org.geomajas.sld.filter.SpatialOpsTypeInfo;
import org.geomajas.sld.geometry.AbstractGeometryCollectionInfo;
import org.geomajas.sld.geometry.AbstractGeometryInfo;

import com.github.nmorel.gwtjackson.client.AbstractConfiguration;

/**
 * GWT jackson configuration. Adds type info to our DTO classes.
 * 
 * @author Jan De Moerloose
 *
 */
public class JsonConfiguration extends AbstractConfiguration {

	@Override
	protected void configure() {
		// abstract info classes
		addMixInAnnotations(AbstractAttributeInfo.class, JsonTypeInfoMixin.class);
		addMixInAnnotations(ClientLayerInfo.class, JsonTypeInfoMixin.class);
		addMixInAnnotations(ClientUserDataInfo.class, JsonTypeInfoMixin.class);
		addMixInAnnotations(ClientWidgetInfo.class, JsonTypeInfoMixin.class);
		addMixInAnnotations(ConstraintInfo.class, JsonTypeInfoMixin.class);
		addMixInAnnotations(LayerExtraInfo.class, JsonTypeInfoMixin.class);
		addMixInAnnotations(PrintComponentInfo.class, JsonTypeInfoMixin.class);
		
		// sld
		addMixInAnnotations(ExpressionInfo.class, JsonTypeInfoMixin.class);
		addMixInAnnotations(ExpressionTypeInfo.class, JsonTypeInfoMixin.class);
		addMixInAnnotations(ComparisonOpsTypeInfo.class, JsonTypeInfoMixin.class);
		addMixInAnnotations(LogicOpsTypeInfo.class, JsonTypeInfoMixin.class);
		addMixInAnnotations(SpatialOpsTypeInfo.class, JsonTypeInfoMixin.class);
		addMixInAnnotations(AbstractGeometryCollectionInfo.class, JsonTypeInfoMixin.class);
		addMixInAnnotations(AbstractGeometryInfo.class, JsonTypeInfoMixin.class);
		addMixInAnnotations(SymbolizerTypeInfo.class, JsonTypeInfoMixin.class);
	}

}
