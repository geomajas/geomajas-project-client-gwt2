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

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.layer.feature.attribute.StringAttribute;

/**
 * Widget builder for attributes whose value are a path to an image.
 *
 * @author Youri Flement
 */
public class ImageAttributeWidgetBuilder implements AttributeWidgetBuilder<StringAttribute> {

	@Override
	public Widget buildAttributeWidget(StringAttribute imagePath) {
		return new Image(imagePath.getValue());
	}
}
