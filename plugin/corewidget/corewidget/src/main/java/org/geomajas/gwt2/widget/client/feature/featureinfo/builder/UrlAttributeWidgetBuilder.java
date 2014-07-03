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

import com.google.gwt.user.client.ui.Anchor;
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
		return new Anchor(url.getValue());
	}
}
