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

package org.geomajas.plugin.editing.client.snap;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.GeometryArrayFunction;
import org.geomajas.plugin.editing.client.snap.event.CoordinateSnapEvent;
import org.geomajas.plugin.editing.client.snap.event.CoordinateSnapHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * The central snapping service. This services works with a series of snapping rules you have to provide. The snapping
 * rules have a strict order. If the snapping service wants to see if snapping is possible, it will go over the snapping
 * rules one by one and stop if one of the rules returns a hit. That is why it is important to add your snapping rules
 * in the correct order.
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public class SnapService {

	private List<SnappingRule> snappingRules;

	private EventBus eventBus;

	private boolean hasSnapped;

	private double currentDistance;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a new service for snapping. This service can for example assist the editing process to provide a snapping
	 * functionality.
	 */
	public SnapService() {
		snappingRules = new ArrayList<SnappingRule>();
		eventBus = new SimpleEventBus();
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	/**
	 * Add a handler to be notified of snapping events. Those events are fired every time the <code>snap</code> method
	 * is called. The event itself will know whether or not snapping actually occurred.
	 *
	 * @param handler The handler to be notified of snapping events.
	 * @return Returns the registration which can remove the handler again.
	 */
	public HandlerRegistration addCoordinateSnapHandler(CoordinateSnapHandler handler) {
		return eventBus.addHandler(CoordinateSnapHandler.TYPE, handler);
	}

	/**
	 * Using the current list of snapping rules, try to snap the given coordinate. Fires a {@link CoordinateSnapEvent}
	 * in any case (whether snapping occurred or not).
	 *
	 * @param coordinate The original location.
	 * @return The returned (perhaps snapped) location. If no snapping occurred, the original location is returned.
	 */
	public Coordinate snap(Coordinate coordinate) {
		Coordinate result = coordinate;
		currentDistance = Double.MAX_VALUE;
		hasSnapped = false;

		for (SnappingRule snappingRule : snappingRules) {
			if (!hasSnapped) {
				double distance = Math.min(currentDistance, snappingRule.getDistance());
				result = snappingRule.getAlgorithm().snap(coordinate, distance);
				if (snappingRule.getAlgorithm().hasSnapped()) {
					currentDistance = snappingRule.getAlgorithm().getCalculatedDistance();
					hasSnapped = true;
				}
			}
		}
		eventBus.fireEvent(new CoordinateSnapEvent(coordinate, result));
		return result;
	}

	/**
	 * After the <code>snap</code> method has been called, this method can return the distance that was covered in the
	 * snap event.
	 */
	public double getCalculatedDistance() {
		return currentDistance;
	}

	/**
	 * Check to see whether or not snapping actually occurred. Call this after the <code>snap</code> method has been
	 * called.
	 *
	 * @return Has snapping actually occurred?
	 */
	public boolean hasSnapped() {
		return hasSnapped;
	}

	/**
	 * Update the playing field for snapping, by providing a bounding box wherein the source providers should present
	 * their geometries.
	 *
	 * @param mapBounds The bounding box wherein we expect snapping to occur. Is usually the current view on the map.
	 */
	public void update(Bbox mapBounds) {
		for (final SnappingRule condition : snappingRules) {
			condition.getSourceProvider().update(mapBounds);
			condition.getSourceProvider().getSnappingSources(new GeometryArrayFunction() {

				public void execute(Geometry[] geometries) {
					condition.getAlgorithm().setGeometries(geometries);
				}
			});
		}
	}

	/**
	 * Remove all snapping rules from this service. Without any snapping rules, snapping can not occur.
	 */
	public void clearSnappingRules() {
		snappingRules.clear();
	}

	/**
	 * Returns all current registered snapping rules.
	 *
	 * @return The list of snapping rules.
	 */
	public List<SnappingRule> getSnappingRules() {
		return snappingRules;
	}

	/**
	 * Adds a snapping rule at the back of the snapping rule list.
	 *
	 * @param rule The new snapping rule to add.
	 */
	public void addSnappingRule(SnappingRule rule) {
		snappingRules.add(rule);
	}

	/**
	 * Remove a snapping rule from the list.
	 *
	 * @param rule The snapping rule to remove.
	 * @return Indicates success.
	 */
	public boolean removeSnappingRule(SnappingRule rule) {
		return snappingRules.remove(rule);
	}

	/**
	 * Insert the snapping rule at a certain index.
	 *
	 * @param index The index to insert at.
	 * @param rule  The snapping rule to insert.
	 */
	public void insertSnappingRule(int index, SnappingRule rule) {
		snappingRules.add(index, rule);
	}
}