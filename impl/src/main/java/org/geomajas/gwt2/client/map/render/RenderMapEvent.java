/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.client.map.render;

import com.google.web.bindery.event.shared.Event;

/**
 * Internal event that triggers rendering of the map.
 * 
 * @author Jan De Moerloose
 */
public class RenderMapEvent extends Event<RenderMapHandler> {

	@Override
	public Type<RenderMapHandler> getAssociatedType() {
		return RenderMapHandler.TYPE;
	}

	@Override
	protected void dispatch(RenderMapHandler handler) {
		handler.onRender(this);
	}
}