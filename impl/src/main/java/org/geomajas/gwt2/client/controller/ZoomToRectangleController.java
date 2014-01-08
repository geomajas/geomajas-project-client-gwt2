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

package org.geomajas.gwt2.client.controller;

import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.animation.NavigationAnimationFactory;
import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.ZoomOption;

/**
 * Map controller that lets the user drag a rectangle on the map, after which it zooms to that rectangle.
 * 
 * @author Pieter De Graef
 */
public class ZoomToRectangleController extends AbstractRectangleController {

	/**
	 * Effectively zoom to the bounds that the user drew on the map.
	 * 
	 * @param worldBounds
	 *            The result of the user's dragging on the map, expressed in world space.
	 */
	public void execute(Bbox worldBounds) {
		View endView = mapPresenter.getViewPort().asView(worldBounds, ZoomOption.LEVEL_CLOSEST);
		mapPresenter.getViewPort().registerAnimation(NavigationAnimationFactory.createZoomIn(mapPresenter, endView));
	}
}