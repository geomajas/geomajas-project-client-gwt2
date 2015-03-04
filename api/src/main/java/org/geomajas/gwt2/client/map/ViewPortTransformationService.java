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

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.Matrix;
import org.geomajas.gwt.client.map.RenderSpace;

/**
 * A transformation service that helps in transforming geometric objects between the different {@link RenderSpace}s.
 * This service is tightly coupled with the {@link ViewPort}, as it requires knowledge of the current map position,
 * scale, width and height for it's calculations.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public interface ViewPortTransformationService {

	/**
	 * Transform the given coordinate from a certain rendering space to another.
	 * 
	 * @param coordinate
	 *            The coordinate to transform. The X and Y ordinates are expected to be expressed in the 'from'
	 *            rendering space.
	 * @param from
	 *            The rendering space that expresses the X and Y ordinates of the given coordinate.
	 * @param to
	 *            The rendering space where to the coordinate should be transformed.
	 * @return The transformed coordinate.
	 */
	Coordinate transform(Coordinate coordinate, RenderSpace from, RenderSpace to);

	/**
	 * Transform the given geometry from a certain rendering space to another.
	 * 
	 * @param geometry
	 *            The geometry to transform. The coordinates are expected to be expressed in the 'from' rendering space.
	 * @param from
	 *            The rendering space that expresses the coordinates of the given geometry.
	 * @param to
	 *            The rendering space where to the geometry should be transformed.
	 * @return The transformed geometry.
	 */
	Geometry transform(Geometry geometry, RenderSpace from, RenderSpace to);

	/**
	 * Transform the given bounding box from a certain rendering space to another.
	 * 
	 * @param bbox
	 *            The bounding box to transform. The coordinates are expected to be expressed in the 'from' rendering
	 *            space.
	 * @param from
	 *            The rendering space that expresses the values (x, y, width, height) of the given bounding box.
	 * @param to
	 *            The rendering space where to the bounding box should be transformed.
	 * @return The transformed bounding box.
	 */
	Bbox transform(Bbox bbox, RenderSpace from, RenderSpace to);

	/**
	 * Get the transformation matrix to transform spatial objects from one render space to another. This matrix should
	 * contain both scale and translation factors.
	 * 
	 * @param from
	 *            The rendering space that describes the origin of the objects to transform.
	 * @param to
	 *            The rendering space that describes where to objects should be transformed.
	 * @return The matrix that describes the requested transformation.
	 */
	Matrix getTransformationMatrix(RenderSpace from, RenderSpace to);

	/**
	 * Get the translation matrix to transform spatial objects from one render space to another. This matrix should
	 * contain only translation factors, no scaling factors.
	 * 
	 * @param from
	 *            The rendering space that describes the origin of the objects to transform.
	 * @param to
	 *            The rendering space that describes where to objects should be transformed.
	 * @return The matrix that describes the requested transformation.
	 */
	Matrix getTranslationMatrix(RenderSpace from, RenderSpace to);

	/**
	 * Get the translation matrix to render objects on the map in a certain scale level.
	 * 
	 * @param scale
	 *            The scale for which to return the translation.
	 * @return The matrix that describes the requested transformation.
	 */
	Matrix getTranslationMatrix(double scale);

	/**
	 * Get the translation matrix to render objects on the map in a certain scale level, given a certain position.
	 * 
	 * @param view
	 *            The view to get a translation matrix for.
	 * @return The matrix that describes the requested transformation.
	 */
	Matrix getTranslationMatrix(View view);
}
