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

package org.geomajas.gwt2.widget.client.feature.featureinfo;

import org.geomajas.gwt2.client.map.feature.Feature;

/**
 * Presenter implementation for the {@link org.geomajas.gwt2.widget.client.feature.featureinfo.FeatureInfoWidget}.
 *
 * @author Youri Flement
 */
public class FeatureInfoPresenterImpl implements FeatureInfoPresenter {

	private FeatureInfoView view;

	private Feature feature;

	public FeatureInfoPresenterImpl(FeatureInfoView view) {
		this.view = view;
	}

	@Override
	public void setFeature(Feature feature) {
		this.feature = feature;
		view.setFeature(feature);
	}

	@Override
	public Feature getFeature() {
		return feature;
	}

}
