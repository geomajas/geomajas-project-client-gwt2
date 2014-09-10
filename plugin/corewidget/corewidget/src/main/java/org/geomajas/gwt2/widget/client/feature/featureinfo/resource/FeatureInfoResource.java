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

package org.geomajas.gwt2.widget.client.feature.featureinfo.resource;

import com.google.gwt.resources.client.ClientBundle;
import org.geomajas.gwt2.widget.client.CoreWidget;

/**
 * Resource bundle for the {@link org.geomajas.gwt2.widget.client.feature.featureinfo.FeatureInfoWidget}.
 *
 * @author Youri Flement
 */
public interface FeatureInfoResource extends ClientBundle {

	FeatureInfoResource INSTANCE = CoreWidget.getInstance().getClientBundleFactory()
			.createFeatureInfoResource();

	@Source("featureInfo.css")
	FeatureInfoCssResource css();

}
