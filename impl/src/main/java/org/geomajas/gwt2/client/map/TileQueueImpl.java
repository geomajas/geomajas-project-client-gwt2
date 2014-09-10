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

package org.geomajas.gwt2.client.map;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.render.TileKeyProvider;
import org.geomajas.gwt2.client.map.render.TilePriorityFunction;
import org.geomajas.gwt2.client.map.render.TileQueue;
import org.geomajas.gwt2.client.map.render.dom.LoadableTile;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Implementation of a {@link TileQueue}. The queue uses an underlying map to assure O(1) on insertion
 * and removal. Tiles with the same key (which should be unique) are overridden so checking before inserting
 * is not necessary. Prioritizing (sorting) the map is O(n log n).
 *
 * @param <T> The type of the keys.
 * @author Youri Flement
 */
public class TileQueueImpl<T> implements TileQueue {

	protected LinkedHashMap<T, LoadableTile> map;

	protected TilePriorityFunction priorityFunction;

	protected TileKeyProvider<T> provider;

	/**
	 * Create a new tile queue with the given priority function and a provider to generate keys for tiles.
	 *
	 * @param priorityFunction The priority function.
	 * @param provider         The key provider.
	 */
	public TileQueueImpl(TilePriorityFunction priorityFunction, TileKeyProvider<T> provider) {
		this.priorityFunction = priorityFunction;
		this.provider = provider;
		map = new LinkedHashMap<T, LoadableTile>();
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public void add(LoadableTile tile) {
		map.put(provider.getKey(tile), tile);
	}

	@Override
	public boolean remove(LoadableTile tile) {
		return map.remove(provider.getKey(tile)) != null;
	}

	@Override
	public LoadableTile poll() {
		return map.remove(map.keySet().iterator().next());
	}

	@Override
	public void prioritize(int index, double resolution, Coordinate focus) {
		sortByValue(index, resolution, focus);
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean contains(LoadableTile tile) {
		return map.containsKey(provider.getKey(tile));
	}

	/**
	 * Sort the map on its values. We need a {@link LinkedHashMap} for this otherwise the order
	 * will not be preserved.
	 *
	 * @param resolutionIndex The current resolution index.
	 * @param resolution      The current resolution.
	 * @param focus           The current position to focus on.
	 */
	public void sortByValue(final int resolutionIndex, final double resolution, final Coordinate focus) {
		List<Entry<T, LoadableTile>> list = new LinkedList<Entry<T, LoadableTile>>(map.entrySet());

		Collections.sort(list, new Comparator<Entry<T, LoadableTile>>() {
			@Override
			public int compare(Entry<T, LoadableTile> o1, Entry<T, LoadableTile> o2) {
				// Get the priority of the first tile and the second tile and compare:
				Double priorityA = priorityFunction.getPriority(o1.getValue(), resolutionIndex, resolution, focus);
				Double priorityB = priorityFunction.getPriority(o2.getValue(), resolutionIndex, resolution, focus);
				return priorityA.compareTo(priorityB);
			}
		});

		// Clear the map and put the sorted values in it:
		map.clear();
		map = new LinkedHashMap<T, LoadableTile>();
		for (Map.Entry<T, LoadableTile> entry : list) {
			map.put(entry.getKey(), entry.getValue());
		}
	}
}
