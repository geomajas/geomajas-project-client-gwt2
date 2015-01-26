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

package org.geomajas.gwt2.client.event;

import com.google.gwt.event.shared.GwtEvent;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.render.TileLevelRenderer;

/**
 * Event that is fired when a tile level has been rendered. This is used by resolution-based layer renderers, and it is
 * up to them to determine when that is.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public class TileLevelRenderedEvent extends GwtEvent<TileLevelRenderedHandler> {

	private final double resolution;

	private final TileLevelRenderer renderer;

	/**
	 * Create an event for the specified resolution.
	 * 
	 * @param resolution the resolution that was rendered
	 * @deprecated use {@link #TileLevelRenderedEvent(TileLevelRenderer)} instead
	 */
	@Deprecated
	public TileLevelRenderedEvent(double resolution) {
		this.resolution = resolution;
		this.renderer = null;
	}

	/**
	 * Create an event for the specified renderer.
	 * 
	 * @param renderer the renderer that has just rendered.
	 * @since 2.1.0
	 */
	public TileLevelRenderedEvent(TileLevelRenderer renderer) {
		this.renderer = renderer;
		this.resolution = -1;
	}

	@Override
	public Type<TileLevelRenderedHandler> getAssociatedType() {
		return TileLevelRenderedHandler.TYPE;
	}

	@Override
	protected void dispatch(TileLevelRenderedHandler handler) {
		handler.onScaleLevelRendered(this);
	}

	/**
	 * Get the resolution that was just rendered.
	 * 
	 * @return The resolution that was just rendered.
	 * @deprecated use {@link #getRenderer()} and get the resolution from the tileLevel.
	 */
	@Deprecated
	public double getResolution() {
		if (resolution == -1) {
			throw new UnsupportedOperationException(
					"Trying to get the resolution, but this event only provides the renderer");
		}
		return resolution;
	}

	/**
	 * Get the renderer that has just rendered.
	 * 
	 * @return the renderer.
	 * @since 2.1.0
	 */
	public TileLevelRenderer getRenderer() {
		return renderer;
	}

}
