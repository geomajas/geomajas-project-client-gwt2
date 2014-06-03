package org.geomajas.plugin.editing.client.service.validation;

import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.GeometryValidationState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;


public interface GeometryValidator {

	GeometryValidationState validate(Geometry geometry, GeometryIndex index);
	
	Object getValidationContext();
	
	boolean isRollBack();
	
}
