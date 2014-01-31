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

import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Renderer for a specific tile level for a layer based on fixed scales.
 * 
 * @author Pieter De Graef
 */
public interface FixedScaleRenderer {

	/**
	 * Get the fixed scale index for this scale renderer.
	 * 
	 * @return The fixed scale index.
	 */
	int getTileLevel();

	/**
	 * Render the required tiles for the given view. If the scale in this view is not the scale represented by this
	 * renderer, it will calculate the bounding box of the view on it's own scale level. It other words, the renderer
	 * will make sure the requested view will be rendered (but in it's own scale level).
	 * 
	 * @param view
	 *            The view to visualize.
	 */
	void render(View view);

	/** Cancel rendering. If there are any requests underway to the server, these can all be canceled. */
	void cancel();

	/**
	 * Is the requested view rendered or not?
	 * 
	 * @param view
	 *            The view to check. It may be that the view has a different scale level than what is rendered in this
	 *            renderer. In that case, this renderer will calculate how that view applies on it's own scale level.
	 * 
	 * @return true or false.
	 */
	boolean isRendered(View view);

	/**
	 * Add a handler for catching {@link TileLevelRenderedEvent}s.
	 * 
	 * @param handler
	 *            The handler to catch the events.
	 * @return A registration to the handler.
	 */
	HandlerRegistration addTileLevelRenderedHandler(TileLevelRenderedHandler handler);
}