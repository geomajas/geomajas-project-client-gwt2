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

package org.geomajas.gwt2.client.map.render;

import com.google.web.bindery.event.shared.Event;

/**
 * Event that is fired when the view on the {@link org.geomajas.gwt2.client.map.ViewPort} has been changed so that both
 * scaling and translation have occurred or the view has resized.
 * 
 * @author Pieter De Graef
 */
public class TileLevelRenderedEvent extends Event<TileLevelRenderedHandler> {

	private final FixedScaleRenderer renderer;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------
	/**
	 * Create an event for the specified view port.
	 * 
	 * @param viewPort
	 *            the view port
	 */
	public TileLevelRenderedEvent(FixedScaleRenderer renderer) {
		this.renderer = renderer;
	}

	// -------------------------------------------------------------------------
	// Event implementation:
	// -------------------------------------------------------------------------

	@Override
	public Type<TileLevelRenderedHandler> getAssociatedType() {
		return TileLevelRenderedHandler.TYPE;
	}

	// ------------------------------------------------------------------------
	// Protected methods:
	// ------------------------------------------------------------------------

	protected void dispatch(TileLevelRenderedHandler handler) {
		handler.onTileLevelRendered(this);
	}

	/**
	 * Get the renderer that fired this event.
	 * 
	 * @return The renderer.
	 */
	public FixedScaleRenderer getRenderer() {
		return renderer;
	}
}