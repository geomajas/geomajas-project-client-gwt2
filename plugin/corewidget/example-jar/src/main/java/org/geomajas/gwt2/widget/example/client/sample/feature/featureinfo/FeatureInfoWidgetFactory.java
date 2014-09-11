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

package org.geomajas.gwt2.widget.example.client.sample.feature.featureinfo;

import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.widget.client.feature.featureinfo.FeatureInfoWidget;

/**
 * Factory for creating feature info widgets.
 *
 * @author Youri Flement
 */
public class FeatureInfoWidgetFactory {

	/**
	 * Create a default {@link FeatureInfoWidget}. No actions or resources are
	 * given to the widget.
	 *
	 * @return The default {@link FeatureInfoWidget}.
	 */
	public FeatureInfoWidget getDefaultFeatureInfoWidget() {
		return new FeatureInfoWidget();
	}

	/**
	 * Create a default {@link FeatureInfoWidget} with a {@link ZoomToObjectAction}
	 * to allow zooming to selected features.
	 *
	 * @param mapPresenter The map presenter used by the action(s).
	 * @return A feature info widget with actions.
	 */
	public FeatureInfoWithActions getFeatureInfoWidgetWithActions(MapPresenter mapPresenter) {
		FeatureInfoWithActions widgetWithActions = new FeatureInfoWithActions();
		widgetWithActions.addHasFeature(new ZoomToObjectAction(mapPresenter));

		return widgetWithActions;
	}

}
