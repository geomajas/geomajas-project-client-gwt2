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

package org.geomajas.gwt2.plugin.geocoder.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringAlternative;

import java.util.List;

/**
 * Event which is used when the geocoder returned alternatives.
 * <p/>
 * Purpose it to allow the use to choose which one to select.
 *
 * @author Joachim Van der Auwera
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api()
public class SelectAlternativeEvent extends GeocoderEvent<SelectAlternativeHandler> {

	private MapPresenter mapPresenter;
	private List<GetLocationForStringAlternative> alternatives;

	@Override
	public Type<SelectAlternativeHandler> getAssociatedType() {
		return SelectAlternativeHandler.TYPE;
	}

	@Override
	protected void dispatch(SelectAlternativeHandler handler) {
		handler.onSelectAlternative(this);
	}

	/**
	 * Constructor which passes the map and alternatives.
	 *
	 * @param mapPresenter map presenter
	 * @param alternatives alternatives
	 */
	public SelectAlternativeEvent(MapPresenter mapPresenter, List<GetLocationForStringAlternative> alternatives) {
		this.mapPresenter = mapPresenter;
		this.alternatives = alternatives;
	}

	/**
	 * Get map widget the geocoder applies to.
	 *
	 * @return map widget
	 */
	@Api
	public MapPresenter getMapWidget() {
		return mapPresenter;
	}

	/**
	 * Get alternatives.
	 *
	 * @return alternatives
	 */
	@Api
	public List<GetLocationForStringAlternative> getAlternatives() {
		return alternatives;
	}
}
