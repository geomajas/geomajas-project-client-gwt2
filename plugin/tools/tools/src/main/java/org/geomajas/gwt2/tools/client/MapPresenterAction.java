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
package org.geomajas.gwt2.tools.client;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.MapPresenter;

/**
 * Action aware of the map presenter.
 *
 * @author Oliver May
 *
 * @param <T> The type returned on success
 * @param <F> The type returned on failure
 * @since 2.0.0
 */
@Api(allMethods = true)
public abstract class MapPresenterAction<T, F> extends Action<T,F> {

	protected MapPresenter mapPresenter;

	public MapPresenterAction(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
	}

}
