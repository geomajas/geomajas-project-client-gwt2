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
package org.geomajas.gwt2.widget.client.feature.featureselectbox;

import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

import org.geomajas.annotation.Api;

/**
 * MVP view for {@link FeatureSelectBox}.
 * 
 * @author Oliver May
 * @author Jan De Moerloose
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface FeatureSelectBoxView extends IsWidget {

	/**
	 * clears old labels and set new labels to the content panel of the widget.
	 * 
	 * @param labels
	 */
	void setLabels(List<String> labels);

	/**
	 * Add a label to the list.
	 * 
	 * @param label
	 */
	void addLabel(String label);

	/**
	 * Show the list of labels.
	 * 
	 * @param animated
	 */
	void show(boolean animated);

	/**
	 * Set the position of the popup list.
	 * 
	 * @param xPos
	 * @param yPos
	 */
	void setShowPosition(int xPos, int yPos);

	/**
	 * Hide the list of labels.
	 */
	void hide();

	/**
	 * Remove all labels from the list.
	 */
	void clearLabels();

	/**
	 * Is the view visible ?
	 * 
	 * @return
	 */
	boolean isVisible();

	/**
	 * Set the presenter for callback.
	 * 
	 * @param presenter
	 */
	void setPresenter(FeatureSelectBoxPresenter presenter);

}
