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

import com.google.gwt.core.client.Callback;

import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.render.RenderMapEvent;
import org.geomajas.gwt2.client.map.render.TileKeyProvider;
import org.geomajas.gwt2.client.map.render.TilePriorityFunction;
import org.geomajas.gwt2.client.map.render.TileQueue;
import org.geomajas.gwt2.client.map.render.dom.LoadableTile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Implementation of a {@link TileQueue}. The queue uses an underlying map to assure O(1) on insertion and removal.
 * Tiles with the same key (which should be unique) are overridden so checking before inserting is not necessary.
 * Prioritizing (sorting) the map is O(n log n).
 * 
 * @param <T> The type of the keys.
 * @author Youri Flement
 */
public class TileQueueImpl<T> implements TileQueue {

	private LinkedHashMap<T, LoadableTile> map;

	private TilePriorityFunction priorityFunction;

	private TileKeyProvider<T> provider;

	private List<LoadableTile> loadingTiles = new ArrayList<LoadableTile>();

	private MapEventBus eventBus;

	/**
	 * Create a new tile queue with the given priority function and a provider to generate keys for tiles.
	 * 
	 * @param priorityFunction The priority function.
	 * @param provider The key provider.
	 */
	public TileQueueImpl(TilePriorityFunction priorityFunction, TileKeyProvider<T> provider, MapEventBus eventBus) {
		this.priorityFunction = priorityFunction;
		this.provider = provider;
		this.eventBus = eventBus;
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
	public void removeLayer(Layer layer) {
		List<T> matches = new ArrayList<T>();
		for (Map.Entry<T, LoadableTile> entry : map.entrySet()) {
			if (entry.getValue().getLayer() == layer) {
				matches.add(entry.getKey());
			}
		}
		for (T t : matches) {
			LoadableTile tile = map.remove(t);
			if (tile.isLoaded()) {

			}
		}
		ListIterator<LoadableTile> it = loadingTiles.listIterator();
		while (it.hasNext()) {
			if (it.next().getLayer() == layer) {
				it.remove();
			}
		}

	}

	@Override
	public LoadableTile poll() {
		return map.remove(map.keySet().iterator().next());
	}

	@Override
	public void prioritize(View view) {
		sortByValue(view);
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
	 * Sort the map on its values. We need a {@link LinkedHashMap} for this otherwise the order will not be preserved.
	 * 
	 * @param view The current view.
	 */
	public void sortByValue(final View view) {
		List<Entry<T, LoadableTile>> list = new LinkedList<Entry<T, LoadableTile>>(map.entrySet());

		Collections.sort(list, new Comparator<Entry<T, LoadableTile>>() {

			@Override
			public int compare(Entry<T, LoadableTile> o1, Entry<T, LoadableTile> o2) {
				// Get the priority of the first tile and the second tile and compare:
				Double priorityA = priorityFunction.getPriority(o1.getValue(), view);
				Double priorityB = priorityFunction.getPriority(o2.getValue(), view);
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

	public int getLoadingCount() {
		return loadingTiles.size();
	}

	@Override
	public void load(int count, int maxTotal) {
		int nrToLoad = Math.min(maxTotal - this.getLoadingCount(), Math.min(size(), count));
		for (int i = 0; i < nrToLoad; ++i) {
			final LoadableTile tile = poll();
			tile.load(new Callback<String, String>() {

				@Override
				public void onSuccess(String result) {
					loadingTiles.remove(tile);
					fireEvent();
				}

				@Override
				public void onFailure(String reason) {
					loadingTiles.remove(tile);
					fireEvent();
				}

				private void fireEvent() {
					if (eventBus != null) {
						eventBus.fireEvent(new RenderMapEvent());
					}
				}
			});
			loadingTiles.add(tile);
		}
	}
}
