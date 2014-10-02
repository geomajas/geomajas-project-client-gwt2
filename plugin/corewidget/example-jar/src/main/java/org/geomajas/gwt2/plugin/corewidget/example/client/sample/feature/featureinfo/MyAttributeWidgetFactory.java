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

package org.geomajas.gwt2.plugin.corewidget.example.client.sample.feature.featureinfo;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.client.map.attribute.Attribute;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.plugin.corewidget.client.feature.featureinfo.builder.AttributeWidgetFactory;
import org.geomajas.layer.feature.attribute.DoubleAttribute;

/**
 * Factory to create attribute widgets. Overwrites the default attribute factory
 * {@link org.geomajas.gwt2.plugin.corewidget.client.feature.featureinfo.builder.AttributeWidgetFactory}.
 * Requests for building attributes with a
 * value of {@link Double} are intercepted and a custom widget for these attributes is build.
 * Other attributes are delegated to the default factory.
 *
 * @author Youri Flement
 */
public class MyAttributeWidgetFactory extends AttributeWidgetFactory {

	@Override
	public Widget createAttributeWidget(Feature feature, AttributeDescriptor descriptor) {
		Attribute<?> attributeValue = feature.getAttributes().get(descriptor.getName());

		Widget widget;
		if (attributeValue.getValue() instanceof DoubleAttribute) {
			widget = createDoubleWidget((DoubleAttribute) attributeValue.getValue());
		} else {
			widget = super.createAttributeWidget(feature, descriptor);
		}

		return widget;
	}

	/**
	 * Create a custom widget for attributes of type {@link Double}. Simply applies a number
	 * formatter on the attribute value and puts it in a panel.
	 *
	 * @param attributeValue the value of the attribute.
	 * @return the custom widget.
	 */
	private Widget createDoubleWidget(DoubleAttribute attributeValue) {
		VerticalPanel panel = new VerticalPanel();
		String formattedNumber = NumberFormat.getDecimalFormat().format(attributeValue.getValue());
		panel.getElement().setInnerText(formattedNumber);
		return panel;
	}
}
