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

package org.geomajas.gwt2.widget.client.featureinfo;

import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.feature.Feature;

/**
 * Presenter interface for the {@link org.geomajas.gwt2.widget.client.featureinfo.FeatureInfoWidget}.
 * The presenter should be able to show a feature, change the visibility of options and zoom
 * to a specific feature on the map.
 *
 * @author Youri Flement
 */
public interface FeatureInfoPresenter {

    void setMapPresenter(MapPresenter mapPresenter);

    /**
     * Zoom to a specific feature on the map.
     *
     * @param feature the feature to zoom to.
     */
    void zoomToObject(Feature feature);

    /**
     * Set the feature to be displayed.
     *
     * @param feature the feature to display.
     */
    void setFeature(Feature feature);

    /**
     * Hide or show the options to interact with the feature information.
     *
     * @param show <code>true</code> if the options should be shown,
     *             <code>false</code> otherwise.
     */
    void showOptions(boolean show);
}
