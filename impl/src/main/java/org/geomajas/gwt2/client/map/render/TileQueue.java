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

package org.geomajas.gwt2.client.map.render;

import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.render.dom.LoadableTile;

/**
 * Interface for a queue of {@link LoadableTile}s. It should be possible to add and remove tiles from a queue and to
 * poll which tile has the highest priority to be loaded. Implementations may choose to prioritize the tiles when they
 * are added or explicitly through the {@link #prioritize(View)} method. The latter is preferable since priorities will
 * change when the {@link org.geomajas.gwt2.client.map.View} changes and priorities of added tiles will not (cannot) be
 * updated automatically.
 * 
 * @author Youri Flement
 * @author Jan De Moerloose
 */
public interface TileQueue {

	/**
	 * Clear all tiles from the queue.
	 */
	void clear();

	/**
	 * Add a tile to the queue. Implementations can choose to sort or not after insertion. Implementations can choose to
	 * allow/disallow <code>null</code> values or to duplicates.
	 * 
	 * @param tile The tile to add.
	 */
	void add(LoadableTile tile);

	/**
	 * Remove a tile from the queue.
	 * 
	 * @param tile The tile to remove.
	 * @return <code>true</code> if the tile was removed from the queue, <code>false</code> otherwise.
	 */
	boolean remove(LoadableTile tile);

	/**
	 * Remove a layer from the queue.
	 * 
	 * @param layer
	 */
	void removeLayer(Layer layer);

	/**
	 * Retrieve and remove the tile with highest priority from the queue. If the queue is empty a
	 * {@link java.util.NoSuchElementException} is thrown. If the queue has not been explicitly sorted this may be a
	 * random tile.
	 * 
	 * @return The tile with highest priority.
	 */
	LoadableTile poll();

	/**
	 * The size of the queue.
	 * 
	 * @return The size.
	 */
	int size();

	/**
	 * Check whether the tile is in the queue or not. Membership is typically checked by a given TileKeyProvider.
	 * 
	 * @param tile The tile.
	 * @return <code>true</code> if the tile is in the queue and <code>false</code> otherwise.
	 */
	boolean contains(LoadableTile tile);

	/**
	 * Prioritize the tiles that are in the queue.
	 * 
	 */
	void prioritize(View view);

	/**
	 * Load more tiles, with a maximum total to load.
	 * 
	 * @param count (maximum) number of tiles to load
	 * @param maxTotal maximum total number of tiles loading
	 * 
	 */
	void load(int count, int maxTotal);

	/**
	 * Get the number of tiles currently loading.
	 * 
	 * @return number of tiles loading
	 */
	int getLoadingCount();
}
