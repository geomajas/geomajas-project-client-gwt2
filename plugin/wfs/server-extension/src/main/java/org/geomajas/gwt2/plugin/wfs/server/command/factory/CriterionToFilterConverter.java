package org.geomajas.gwt2.plugin.wfs.server.command.factory;

import java.util.List;

import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.plugin.wfs.client.query.dto.CriterionDto;
import org.opengis.filter.Filter;

public interface CriterionToFilterConverter {

	Filter convert(CriterionDto criterionDto, List<AttributeDescriptor> schema);
}
