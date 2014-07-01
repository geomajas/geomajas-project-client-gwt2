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

package org.geomajas.gwt2.widget.client.featureinfo;

import com.google.gwt.user.client.ui.Widget;
import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.attribute.Attribute;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.widget.client.featureinfo.builder.AttributeWidgetBuilder;
import org.geomajas.gwt2.widget.client.featureinfo.builder.BooleanAttributeWidgetBuilder;
import org.geomajas.gwt2.widget.client.featureinfo.builder.DefaultAttributeWidgetBuilder;
import org.geomajas.gwt2.widget.client.featureinfo.builder.DoubleAttributeWidgetBuilder;
import org.geomajas.gwt2.widget.client.featureinfo.builder.ImageAttributeWidgetBuilder;
import org.geomajas.gwt2.widget.client.featureinfo.builder.UrlAttributeWidgetBuilder;
import org.geomajas.layer.feature.attribute.BooleanAttribute;
import org.geomajas.layer.feature.attribute.DoubleAttribute;
import org.geomajas.layer.feature.attribute.ImageUrlAttribute;
import org.geomajas.layer.feature.attribute.UrlAttribute;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A factory to create widgets for feature attributes. By default a feature attribute
 * is printed as a {@link String} and there are some builders for other primitive datatypes.
 *
 * @author Youri Flement
 * @since 2.1.0
 */
@Api(allMethods = true)
public class FeatureAttributeWidgetFactory {

	private static Map<Class<?>, AttributeWidgetBuilder<?>> builders
			= new HashMap<Class<?>, AttributeWidgetBuilder<?>>();

	static {
		builders.put(DoubleAttribute.class, new DoubleAttributeWidgetBuilder());
		builders.put(BooleanAttribute.class, new BooleanAttributeWidgetBuilder());
		builders.put(ImageUrlAttribute.class, new ImageAttributeWidgetBuilder());
		builders.put(UrlAttribute.class, new UrlAttributeWidgetBuilder());
		// add more builders ...
	}

	/**
	 * Create a widget for an attribute of a {@link Feature}.
	 *
	 * @param feature    the feature of the attribute.
	 * @param descriptor the descriptor of the attribute of the feature.
	 * @return the (possible custom) widget for a feature attribute.
	 */
	public Widget createFeatureAttributeWidget(Feature feature, AttributeDescriptor descriptor) {
		// The value of the attribute: (e.g. StringAttribute, DoubleAttribute, ...)
		Attribute<?> attributeValue = feature.getAttributes().get(descriptor.getName());

		// Get a builder for the attribute:
		AttributeWidgetBuilder builder = builders.get(attributeValue.getValue().getClass());
		if (builder == null) {
			builder = new DefaultAttributeWidgetBuilder();
		}

		// Build the widget and return it:
		return builder.buildAttributeWidget(attributeValue.getValue());
	}

	/**
	 * Add an attribute builder to the collection. Attributes with the given class will
	 * have their widgets build by this builder.
	 * <p/>
	 * If there is already a builder for the given class it will be overwritten.
	 *
	 * @param clazz   the class of the attribute that is handled by the builder.
	 * @param builder the builder.
	 */
	public void addAttributeBuilder(Class<? extends Attribute> clazz, AttributeWidgetBuilder builder) {
		builders.put(clazz, builder);
	}

	/**
	 * Remove an attribute widget builder from the collection.
	 *
	 * @param builder the builder to remove.
	 */
	public void removeAttributeBuilder(AttributeWidgetBuilder<?> builder) {
		Iterator<AttributeWidgetBuilder<?>> iterator = builders.values().iterator();
		while (iterator.hasNext()) {
			AttributeWidgetBuilder<?> widgetBuilder = iterator.next();
			if (widgetBuilder.equals(builder)) {
				iterator.remove();
			}
		}
	}

	/**
	 * Remove the builder associated with the given attribute class from the collection.
	 *
	 * @param clazz the class of the attribute.
	 */
	public void removeAttributeBuilder(Class<? extends Attribute> clazz) {
		builders.remove(clazz);
	}
}
