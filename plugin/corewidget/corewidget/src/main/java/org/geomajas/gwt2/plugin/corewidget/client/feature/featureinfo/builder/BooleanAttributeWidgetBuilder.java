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

package org.geomajas.gwt2.plugin.corewidget.client.feature.featureinfo.builder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.plugin.corewidget.client.i18n.WidgetCoreInternationalization;
import org.geomajas.layer.feature.attribute.BooleanAttribute;

/**
 * Widget builder for {@link Boolean} attributes. Translates boolean values to internationalized
 * <code>yes</code> and <code>no</code> Strings.
 *
 * @author Youri Flement
 */
public class BooleanAttributeWidgetBuilder implements AttributeWidgetBuilder<BooleanAttribute> {

	private static final WidgetCoreInternationalization MSG = GWT.create(WidgetCoreInternationalization.class);

	@Override
	public Widget buildAttributeWidget(BooleanAttribute attribute) {
		Label label = new Label();

		// Simply put "yes" or "no" instead of 1 and 0.
		if (attribute.getValue()) {
			label.getElement().setInnerText(MSG.yes());
		} else {
			label.getElement().setInnerText(MSG.no());
		}
		return label;
	}
}
