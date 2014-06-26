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
package org.geomajas.gwt2.widget.client;

import org.geomajas.gwt2.widget.client.feature.featureselectbox.FeatureSelectBoxView;
import org.geomajas.gwt2.widget.client.feature.featureselectbox.FeatureSelectBoxViewImpl;
import org.geomajas.gwt2.widget.client.feature.featureselectbox.resource.FeatureSelectBoxResource;
import org.geomajas.gwt2.widget.client.featureinfo.FeatureInfoView;
import org.geomajas.gwt2.widget.client.featureinfo.FeatureInfoViewImpl;
import org.geomajas.gwt2.widget.client.featureinfo.resource.FeatureInfoResource;

/**
 * MVP view factory for this plugin.
 *
 * @author Jan De Moerloose
 */
public class CoreWidgetViewFactory {

    /**
     * Create a new {@link FeatureSelectBoxView}.
     *
     * @return the view
     */
    public FeatureSelectBoxView createFeatureSelectBox(FeatureSelectBoxResource resource) {
        return new FeatureSelectBoxViewImpl(resource);
    }

    /**
     * Create a new {@link org.geomajas.gwt2.widget.client.featureinfo.FeatureInfoView}.
     *
     * @param resource the resource to use.
     * @return a new feature info view.
     */
    public FeatureInfoView createFeatureInfoView(FeatureInfoResource resource) {
        return new FeatureInfoViewImpl(resource);
    }
}
