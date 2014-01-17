/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.gwt2.widget.client.itemselect;

import java.util.List;

/**
 * View for item selection.
 *
 * @author Oliver May
 * @param <T> type of item in this view.
 *
 */
public interface ItemSelectView<T> {

	/**
	 * Add all these items to the view.
	 *
	 * @param data the items.
	 */
	void setItems(List<T> data);

	/**
	 * Add an item select handler.
	 *
	 * @param handler the handler
	 */
	void addItemSelectHandler(ItemSelectHandler<T> handler);

	/**
	 * Hide the view.
	 */
	void hide();

	/**
	 * Show the view on the current position.
	 */
	void show(int x, int y);

	/**
	 * The item select handler.
	 *
	 * @param <T> the type of item
	 */
	interface ItemSelectHandler<T> {

		/**
		 * Called when an item is selected.
		 *
		 * @param item the selected item
		 */
		void itemSelected(T item);
	}

}
