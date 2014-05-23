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
package org.geomajas.plugin.jsapi.gwt2.client.exporter.map;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.map.ZoomOption;
import org.geomajas.plugin.jsapi.client.map.ViewPort;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Javascript exportable facade for a map's ViewPort. The Central view port definition that determines and influences
 * the position and current view of the map.
 * 
 * @author Oliver May
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export("ViewPort")
@ExportPackage("org.geomajas.jsapi.map")
public class ViewPortImpl implements ViewPort, Exportable {

	private static final String WORLD = "world";

	private static final String SCREEN = "screen";

	private org.geomajas.gwt2.client.map.ViewPort viewPort;

	private ZoomOption zoomOption = ZoomOption.LEVEL_FIT;

	public ViewPortImpl() {
	}

	/**
	 * Create a new ViewPort.
	 * 
	 * @param viewPort The ViewPort behind a map in the GWT face.
	 */
	public ViewPortImpl(org.geomajas.gwt2.client.map.ViewPort viewPort) {
		this.viewPort = viewPort;
	}

	/**
	 * Re-centers the map to a new position.
	 * 
	 * @param coordinate the new center position
	 */
	public void applyPosition(Coordinate coordinate) {
		viewPort.applyPosition(coordinate);
	}

	/**
	 * Apply a new scale level on the map. In case the are fixed resolutions defined on this ViewPort, it will
	 * automatically snap to the nearest resolution. In case the maximum extents are exceeded, it will pan to avoid
	 * this.
	 * 
	 * @param scale The preferred new scale.
	 */
	public void applyScale(final double scale) {
		viewPort.applyResolution(1 / scale, zoomOption);
	}

	/**
	 * <p>
	 * Change the view on the map by applying a bounding box (world coordinates!). Since the width/height ratio of the
	 * bounding box may differ from that of the map, the fit is "as good as possible".
	 * </p>
	 * <p>
	 * Also this function will almost certainly change the scale on the map, so if there have been resolutions defined,
	 * it will snap to them.
	 * </p>
	 * 
	 * @param bounds A bounding box in world coordinates that determines the view from now on.
	 */
	public void applyBounds(final Bbox bounds) {
		viewPort.applyBounds(bounds, zoomOption);
	}

	/**
	 * Return the currently visible bounds on the map. These bounds are expressed in the CRS of the map.
	 * 
	 * @return Returns the maps bounding box.
	 */
	public Bbox getBounds() {
		return viewPort.getBounds();
	}

	/**
	 * Get the current center position expressed in world space.
	 * 
	 * @return The current center position expressed in world space.
	 */
	public Coordinate getPosition() {
		return new Coordinate(viewPort.getPosition());
	}

	/**
	 * Return the current scale on the map.
	 */
	public double getScale() {
		return 1 / viewPort.getResolution();
	}

	/**
	 * Get the maximum zooming extent that is allowed on this view port. These bounds are determined by the map
	 * configuration.
	 * 
	 * @return The maximum zooming extent that is allowed on this view port.
	 */
	public Bbox getMaximumBounds() {
		return viewPort.getMaximumBounds();
	}

	/**
	 * Apply a new scale level on the map. In case the are fixed resolutions defined on this ViewPort, it will
	 * automatically snap to the nearest resolution. In case the maximum extents are exceeded, it will pan to avoid
	 * this.
	 * 
	 * @param scale The preferred new scale.
	 * @param rescalePoint After zooming, this point will still be on the same position in the view as before. Makes for
	 *        easy double clicking on the map without it moving away.
	 */
	public void applyScale(double scale, Coordinate rescalePoint) {
		// put this calculation in a static method ?
		double resolution = 1 / scale;
		double factor = viewPort.getResolution() / resolution;
		double dX = (rescalePoint.getX() - rescalePoint.getX()) * (1 - 1 / factor);
		double dY = (rescalePoint.getY() - rescalePoint.getY()) * (1 - 1 / factor);
		Coordinate p = viewPort.getPosition();
		Coordinate position = new Coordinate(p.getX() + dX, p.getY() + dY);
		viewPort.applyResolution(resolution);
		viewPort.applyPosition(position);
	}

	/**
	 * Transform the given coordinate from a certain rendering space to another.
	 * 
	 * @param coordinate The coordinate to transform. The X and Y ordinates are expected to be expressed in the 'from'
	 *        rendering space.
	 * @param from The rendering space that expresses the X and Y ordinates of the given coordinate. Options are:
	 *        <ul>
	 *        <li>screen</li>
	 *        <li>world</li>
	 *        </ul>
	 * @param to The rendering space where to the coordinate should be transformed. Options are:
	 *        <ul>
	 *        <li>screen</li>
	 *        <li>world</li>
	 *        </ul>
	 * @return The transformed coordinate.
	 */
	public Coordinate transform(Coordinate coordinate, String from, String to) {
		if (from.equalsIgnoreCase(to)) {
			return coordinate;
		}
		if (SCREEN.equalsIgnoreCase(from) && WORLD.equalsIgnoreCase(to)) {
			return viewPort.getTransformationService().transform(coordinate, RenderSpace.SCREEN, RenderSpace.WORLD);
		} else if (WORLD.equalsIgnoreCase(from) && SCREEN.equalsIgnoreCase(to)) {
			return viewPort.getTransformationService().transform(coordinate, RenderSpace.WORLD, RenderSpace.SCREEN);
		}
		return coordinate;
	}
}