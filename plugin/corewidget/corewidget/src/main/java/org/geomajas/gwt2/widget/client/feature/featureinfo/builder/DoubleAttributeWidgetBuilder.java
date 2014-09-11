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

package org.geomajas.gwt2.widget.client.feature.featureinfo.builder;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.layer.feature.attribute.DoubleAttribute;

/**
 * Widget builder for {@link Double} attributes.
 *
 * @author Youri Flement
 */
public class DoubleAttributeWidgetBuilder implements AttributeWidgetBuilder<DoubleAttribute> {

	@Override
	public Widget buildAttributeWidget(DoubleAttribute attribute) {
		return new Label(attribute.getValue() + "");
	}
}
