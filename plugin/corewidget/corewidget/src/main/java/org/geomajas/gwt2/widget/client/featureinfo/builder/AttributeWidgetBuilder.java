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

package org.geomajas.gwt2.widget.client.featureinfo.builder;

import com.google.gwt.user.client.ui.Widget;

/**
 * Interface for an attribute widget builder. A widget builder should be able to build
 * a widget for a specific attribute of a {@link org.geomajas.gwt2.client.map.feature.Feature}.
 *
 * @param <ATTRIBUTE_TYPE> The type of the attribute (StringAttribute, DoubleAttribute, ...)
 * @author Youri Flement
 */
public interface AttributeWidgetBuilder<ATTRIBUTE_TYPE> {

	/**
	 * Build a custom widget for a specific attribute.
	 *
	 * @param attributeValue the value of the attribute.
	 * @return the custom widget.
	 */
	Widget buildAttributeWidget(ATTRIBUTE_TYPE attributeValue);
}
