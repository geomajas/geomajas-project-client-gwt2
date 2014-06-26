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

package org.geomajas.gwt2.widget.client.featureinfo.resource;

import org.geomajas.gwt2.widget.client.feature.featureselectbox.resource.FeatureSelectBoxCssResource;
import org.geomajas.gwt2.widget.client.feature.featureselectbox.resource.FeatureSelectBoxResource;

/**
 * CSS resource to remove style of the {@link org.geomajas.gwt2.widget.client.featureinfo.FeatureInfoWidget}.
 *
 * @author Youri Flement
 */
public interface FeatureInfoResourceNoStyle extends FeatureSelectBoxResource {

    @Source("featureInfo-nostyle.css")
    FeatureSelectBoxCssResource css();
}
