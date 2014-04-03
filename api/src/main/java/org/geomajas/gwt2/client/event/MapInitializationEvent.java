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
package org.geomajas.gwt2.client.event;

import com.google.web.bindery.event.shared.Event;
import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.MapPresenter;

/**
 * Event that reports the initialization of the map. This is when the actual configuration has been loaded and when
 * layers become available.
 *
 * @author Jan De Moerloose
 * @since 2.0.0
 */
@Api(allMethods = true)
public class MapInitializationEvent extends Event<MapInitializationHandler> {

	private MapPresenter mapPresenter;

	/**
	 * MapInitializationEvent constructor that takes as argument the map.
	 *
	 * @param mapPresenter the map.
	 * @since 2.1.0
	 */
	public MapInitializationEvent(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
	}

	@Override
	public Type<MapInitializationHandler> getAssociatedType() {
		return MapInitializationHandler.TYPE;
	}

	@Override
	protected void dispatch(MapInitializationHandler mapInitializationHandler) {
		mapInitializationHandler.onMapInitialized(this);
	}

	/**
	 * Get the map.
	 *
	 * @return The map.
	 * @since 2.1.0
	 */
	public MapPresenter getMapPresenter() {
		return mapPresenter;
	}
}