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

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.layer.feature.attribute.StringAttribute;

/**
 * Widget builder for an attribute whose values is an <code>URL</code>.
 *
 * @author Youri Flement
 */
public class UrlAttributeWidgetBuilder implements AttributeWidgetBuilder<StringAttribute> {

	@Override
	public Widget buildAttributeWidget(StringAttribute url) {
		VerticalPanel widget = new VerticalPanel();
		Anchor link = new Anchor(url.getValue());
		widget.add(link);
		return widget;
	}
}
