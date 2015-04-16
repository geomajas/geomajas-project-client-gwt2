package org.geomajas.gwt2.plugin.wfs.client.protocol;

import java.io.Serializable;
import java.util.List;

import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;


public interface WfsFeatureTypeDescriptionInfo  extends Serializable {

	String getBaseUrl();

	String getTypeName();

	List<AttributeDescriptor> getAttributeDescriptors();

}
