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

package org.geomajas.gwt2.plugin.corewidget.client.feature.featureinfo.builder;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Default Widget builder.
 *
 * @author Youri Flement
 */
public class DefaultAttributeWidgetBuilder implements AttributeWidgetBuilder<Object> {

	@Override
	public Widget buildAttributeWidget(Object attribute) {
		return new Label(attribute.toString());
	}
}
