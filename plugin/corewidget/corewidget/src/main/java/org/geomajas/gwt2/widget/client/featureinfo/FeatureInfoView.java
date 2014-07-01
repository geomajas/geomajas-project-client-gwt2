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

import com.google.gwt.user.client.ui.IsWidget;
import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.feature.Feature;

/**
 * Interface for the {@link org.geomajas.gwt2.widget.client.featureinfo.FeatureInfoWidget}. The view should
 * be able to display a feature and should provide options to hide or show options to interact
 * with the feature information.
 *
 * @author Youri Flement
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface FeatureInfoView extends IsWidget {

	/**
	 * Set the feature to be displayed.
	 *
	 * @param feature the feature.
	 */
	void setFeature(Feature feature);

	/**
	 * Set the presenter of the view.
	 *
	 * @param presenter the presenter.
	 */
	void setPresenter(FeatureInfoPresenter presenter);

	/**
	 * Show or hide options of the feature info widget.
	 *
	 * @param show <code>true</code> if the options (e.g. "Zoom to object")
	 *             should be shown and <code>false</code> otherwise.
	 */
	void showOptions(boolean show);

}
