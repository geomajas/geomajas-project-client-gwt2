/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.plugin.corewidget.example.client.sample.feature.featureinfo.control;

import com.google.gwt.core.client.GWT;

/**
 * Control widget for displaying
 * {@link org.geomajas.gwt2.plugin.corewidget.client.feature.featureinfo.FeatureInfoWidget}s.
 *
 * @author Youri Flement
 */
public final class FeatureInfoControl {

	private static FeatureInfoControl instance;

	private FeatureInfoControlViewFactory viewFactory;

	private FeatureInfoControl() {
	}

	public FeatureInfoControlViewFactory getViewFactory() {
		if (viewFactory == null) {
			viewFactory = GWT.create(FeatureInfoControlViewFactory.class);
		}
		return viewFactory;
	}

	public static FeatureInfoControl getInstance() {
		if (instance == null) {
			instance = new FeatureInfoControl();
		}
		return instance;
	}

}
