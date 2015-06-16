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

package org.geomajas.gwt2.client.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;

/**
 * Defines a view on the map. XYZ in the form of position and resolution.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public class View {

	private final Coordinate position;

	private final double resolution;

	private Map<Hint<?>, Object> hintValues = new HashMap<Hint<?>, Object>();

	/**
	 * {@link Hint} used to indicate that the view is part of an animation.
	 *
	 * @since 2.4.0
	 */
	public static final Hint<Boolean> ANIMATION = new Hint<Boolean>("animation");

	/**
	 * {@link Hint} used to indicate that the view belongs to a user interaction (with unpredictable end state, so
	 * renderers should not proactively load tiles).
	 *
	 * @since 2.4.0
	 */
	public static final Hint<Boolean> INTERACTIVE = new Hint<Boolean>("interactive");

	/**
	 * {@link Hint} used to indicate that the user is dragging.
	 *
	 * @since 2.4.0
	 */
	public static final Hint<Boolean> DRAGGING = new Hint<Boolean>("dragging");

	/**
	 * Construct a view for the parameters given.
	 * 
	 * @param position The position of the view.
	 * @param resolution The resolution of the view.
	 */
	public View(Coordinate position, double resolution) {
		this.position = position;
		this.resolution = resolution;
		setHint(INTERACTIVE, false);
		setHint(ANIMATION, false);
		setHint(DRAGGING, false);
	}

	/**
	 * Set a hint value.
	 * 
	 * @param hint
	 * @param value
	 * @since 2.4.0
	 */
	public <T> void setHint(Hint<T> hint, T value) {
		if (value == null) {
			throw new IllegalArgumentException("Null value passed.");
		}
		hintValues.put(hint, value);
	}

	/**
	 * Get a hint value.
	 * 
	 * @param hint
	 * @return the value
	 * @since 2.4.0
	 */
	@SuppressWarnings("unchecked")
	public <T> T getHint(Hint<T> hint) {
		return (T) hintValues.get(hint);
	}

	/**
	 * Get all hint keys.
	 * @return
	 * 
	 * @since 2.4.0
	 */
	public Set<Hint<?>> getHints() {
		return hintValues.keySet();
	}

	/**
	 * Set when the user is dragging.
	 * 
	 * @param dragging
	 * @since 2.4.0
	 */
	public void setDragging(boolean dragging) {
		setHint(DRAGGING, dragging);
	}

	/**
	 * Is the user dragging ?
	 * 
	 * @return
	 * @since 2.4.0
	 */
	public boolean isDragging() {
		return getHint(DRAGGING);
	}

	/**
	 * Set whether this view belongs to a user interaction (with unpredictable end state, so renderers should not
	 * proactively load tiles).
	 * 
	 * @param interactive
	 * @since 2.4.0
	 */
	public void setInteractive(boolean interactive) {
		setHint(INTERACTIVE, interactive);
	}

	/**
	 * Does this view belong to a user interaction (with unpredictable end state, so renderers should not proactively
	 * load tiles) ?
	 * 
	 * @return
	 * @since 2.4.0
	 */
	public boolean isInteractive() {
		return getHint(INTERACTIVE);
	}

	/**
	 * Set whether this view is part of an animation.
	 * 
	 * @param animation
	 * @since 2.4.0
	 */
	public void setAnimation(boolean animation) {
		setHint(ANIMATION, animation);
	}

	/**
	 * Is this view part of an animation ?
	 * 
	 * @return
	 * @since 2.4.0
	 */
	public boolean isAnimation() {
		return getHint(ANIMATION);
	}

	/**
	 * Get the position for a certain view.
	 * 
	 * @return The position
	 */
	public Coordinate getPosition() {
		return position;
	}

	/**
	 * Get the resolution for a certain view.
	 * 
	 * @return The resolution
	 */
	public double getResolution() {
		return resolution;
	}

	@Override
	public String toString() {
		return "NavigationView: " + position + ", resolution=" + resolution;
	}

	@Override
	public boolean equals(Object object) {
		if (object != null && object instanceof View) {
			View other = (View) object;
			// We don't compare bounds, because a view may come from a map with a different size. It's resolution and
			// position that matter.
			return other.getPosition().equals(position) && Math.abs(other.getResolution() - resolution) < 1e-10
					&& hintValues.equals(other.hintValues);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return position.hashCode() + (int) Math.round(resolution);
	}
}