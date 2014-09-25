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

package org.geomajas.gwt2.plugin.corewidget.client.feature.featureinfo.resource;


import com.google.gwt.resources.client.ClientBundle;

/**
 * CSS resource to remove style of the
 * {@link org.geomajas.gwt2.plugin.corewidget.client.feature.featureinfo.FeatureInfoWidget}.
 *
 * @author Youri Flement
 */
public interface FeatureInfoResourceNoStyle extends ClientBundle {

	@Source("featureInfo-nostyle.css")
	FeatureInfoCssResource css();
}
