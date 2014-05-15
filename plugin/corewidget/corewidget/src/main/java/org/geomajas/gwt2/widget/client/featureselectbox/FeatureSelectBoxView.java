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
package org.geomajas.gwt2.widget.client.featureselectbox;

import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

/**
 * @author Oliver May
 * @author Jan De Moerloose
 */
public interface FeatureSelectBoxView extends IsWidget {

	/**
	 * clears old labels and set new labels to the content panel of the widget.
	 *
	 * @param labels
	 */
	void setLabels(List<String> labels);

	void addLabel(String label);

	void show(boolean animated);

	void setShowPosition(int xPos, int yPos);

	void hide();

	void clearLabels();

	boolean isVisible();

	void setPresenter(FeatureSelectBoxPresenter presenter);

}
