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
package org.geomajas.gwt2.plugin.corewidget.client.map.mapcontrolpanel;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.annotation.Api;

/**
 * MVP view for {@link MapControlPanel}.
 *
 * @author Dosi Bingov
 *
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface MapControlPanelView extends IsWidget {

	/**
	 * Set the presenter for callback.
	 *
	 * @param presenter
	 */
	void setPresenter(MapControlPanelPresenter presenter);

	/**
	 * Move {@link  Widget} on map control view from a one given index to another.
	 *
	 * @param fromIndex
	 * @param toIndex
	 */
	void moveWidget(int fromIndex, int toIndex);

	/**
	 * Add {@link  Widget} to map control view.
	 * @param widget {@link  Widget}
	 */
	void add(Widget widget);

	/**
	 * Remove widget from given index.
	 *
	 * @param index
	 */
	boolean removeWidget(int index);

	/**
	 * Get count of the added widgets to {@link MapControlPanelView}.
	 *
	 * @return
	 */
	int getWidgetCount();

	/**
	 * Get {@link Widget} at given index.
	 *
	 * @param index
	 * @return
	 */
	Widget getWidgetAt(int index);
}
