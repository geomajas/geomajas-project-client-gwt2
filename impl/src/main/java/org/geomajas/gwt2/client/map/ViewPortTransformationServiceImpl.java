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

package org.geomajas.gwt2.client.map;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.Matrix;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.gwt.client.map.RenderSpace;

/**
 * Default implementation of the {@link ViewPortTransformationService}.
 * 
 * @author Pieter De Graef
 */
public class ViewPortTransformationServiceImpl implements ViewPortTransformationService {

	private final ViewPort viewPort;

	/**
	 * Initialize this service with the {@link ViewPort} it's supposed to provide transformations for.
	 * 
	 * @param viewPort
	 *            The {@link ViewPort} object.
	 */
	public ViewPortTransformationServiceImpl(ViewPort viewPort) {
		this.viewPort = viewPort;
	}

	/** {@inheritDoc} */
	public Coordinate transform(Coordinate coordinate, RenderSpace from, RenderSpace to) {
		switch (from) {
			case SCREEN:
				switch (to) {
					case SCREEN:
						return new Coordinate(coordinate);
					case WORLD:
						return screenToWorld(coordinate);
				}
			case WORLD:
				switch (to) {
					case SCREEN:
						return worldToScreen(coordinate);
					case WORLD:
						return new Coordinate(coordinate);
				}
		}
		return coordinate;
	}

	/** {@inheritDoc} */
	public Geometry transform(Geometry geometry, RenderSpace from, RenderSpace to) {
		switch (from) {
			case SCREEN:
				switch (to) {
					case SCREEN:
						return GeometryService.clone(geometry);
					case WORLD:
						return screenToWorld(geometry);
				}
			case WORLD:
				switch (to) {
					case SCREEN:
						return worldToScreen(geometry);
					case WORLD:
						return GeometryService.clone(geometry);
				}
		}
		return geometry;
	}

	/** {@inheritDoc} */
	public Bbox transform(Bbox bbox, RenderSpace from, RenderSpace to) {
		switch (from) {
			case SCREEN:
				switch (to) {
					case SCREEN:
						return new Bbox(bbox.getX(), bbox.getY(), bbox.getWidth(), bbox.getHeight());
					case WORLD:
						return screenToWorld(bbox);
				}
			case WORLD:
				switch (to) {
					case SCREEN:
						return worldToScreen(bbox);
					case WORLD:
						return new Bbox(bbox.getX(), bbox.getY(), bbox.getWidth(), bbox.getHeight());
				}
		}
		return bbox;
	}

	/** {@inheritDoc} */
	public Matrix getTransformationMatrix(RenderSpace from, RenderSpace to) {
		switch (from) {
			case SCREEN:
				switch (to) {
					case SCREEN:
						return Matrix.IDENTITY;
					case WORLD:
						throw new RuntimeException("Not implemented.");
				}
			case WORLD:
				switch (to) {
					case SCREEN:
						double scale = viewPort.getScale();
						if (scale > 0.0) {
							double deltaX = -(viewPort.getPosition().getX() * scale) + (double) viewPort.getMapWidth()
									/ 2.0;
							double deltaY = viewPort.getPosition().getY() * scale + (double) viewPort.getMapHeight()
									/ 2.0;
							return new Matrix(scale, 0.0, 0.0, -scale, deltaX, deltaY);
						}
						return new Matrix(1.0, 0.0, 0.0, 1.0, 0.0, 0.0);
					case WORLD:
						return Matrix.IDENTITY;
				}
		}
		return null;
	}

	/** {@inheritDoc} */
	public Matrix getTranslationMatrix(RenderSpace from, RenderSpace to) {
		switch (from) {
			case SCREEN:
				switch (to) {
					case SCREEN:
						return Matrix.IDENTITY;
					case WORLD:
						throw new RuntimeException("Not implemented.");
				}
			case WORLD:
				switch (to) {
					case SCREEN:
						double scale = viewPort.getScale();
						if (scale > 0.0) {
							double deltaX = -(viewPort.getPosition().getX() * scale) + (double) viewPort.getMapWidth()
									/ 2.0;
							double deltaY = viewPort.getPosition().getY() * scale + (double) viewPort.getMapHeight()
									/ 2.0;
							return new Matrix(1.0, 0.0, 0.0, 1.0, deltaX, deltaY);
						}
						return Matrix.IDENTITY;
					case WORLD:
						return Matrix.IDENTITY;
				}
		}
		return null;
	}

	@Override
	public Matrix getTranslationMatrix(double scale) {
		Coordinate targetDelta = getTranslationForView(new View(viewPort.getPosition(), scale));
		return new Matrix(scale, 0.0, 0.0, -scale, targetDelta.getX(), targetDelta.getY());
	}

	@Override
	public Matrix getTranslationMatrix(View view) {
		Coordinate targetDelta = getTranslationForView(view);
		return new Matrix(view.getScale(), 0.0, 0.0, -view.getScale(), targetDelta.getX(), targetDelta.getY());
	}

	// -------------------------------------------------------------------------
	// Private Transformation methods:
	// -------------------------------------------------------------------------

	private Coordinate getTranslationForView(View view) {
		double deltaX = -(view.getPosition().getX() * view.getScale()) + (double) viewPort.getMapWidth() / 2.0;
		double deltaY = view.getPosition().getY() * view.getScale() + (double) viewPort.getMapHeight() / 2.0;
		return new Coordinate(deltaX, deltaY);
	}

	private Coordinate worldToScreen(Coordinate coordinate) {
		if (coordinate != null) {
			double scale = viewPort.getScale();
			double x = coordinate.getX() * scale;
			double y = -coordinate.getY() * scale;
			double translateX = -(viewPort.getPosition().getX() * scale) + (viewPort.getMapWidth() / 2);
			double translateY = (viewPort.getPosition().getY() * scale) + (viewPort.getMapHeight() / 2);
			x += translateX;
			y += translateY;
			return new Coordinate(x, y);
		}
		return null;
	}

	private Geometry worldToScreen(Geometry geometry) {
		if (geometry != null) {
			Geometry result = new Geometry(geometry.getGeometryType(), geometry.getSrid(), geometry.getPrecision());
			if (geometry.getGeometries() != null) {
				Geometry[] transformed = new Geometry[geometry.getGeometries().length];
				for (int i = 0; i < geometry.getGeometries().length; i++) {
					transformed[i] = worldToScreen(geometry.getGeometries()[i]);
				}
				result.setGeometries(transformed);
			}
			if (geometry.getCoordinates() != null) {
				Coordinate[] transformed = new Coordinate[geometry.getCoordinates().length];
				for (int i = 0; i < geometry.getCoordinates().length; i++) {
					transformed[i] = worldToScreen(geometry.getCoordinates()[i]);
				}
				result.setCoordinates(transformed);
			}
			return result;
		}
		throw new IllegalArgumentException("Cannot transform null geometry.");
	}

	private Bbox worldToScreen(Bbox bbox) {
		if (bbox != null) {
			Coordinate c1 = worldToScreen(BboxService.getOrigin(bbox));
			Coordinate c2 = worldToScreen(BboxService.getEndPoint(bbox));
			double x = (c1.getX() < c2.getX()) ? c1.getX() : c2.getX();
			double y = (c1.getY() < c2.getY()) ? c1.getY() : c2.getY();
			return new Bbox(x, y, Math.abs(c1.getX() - c2.getX()), Math.abs(c1.getY() - c2.getY()));
		}
		return null;
	}

	private Coordinate screenToWorld(Coordinate coordinate) {
		if (coordinate != null) {
			double scale = viewPort.getScale();
			double inverseScale = 1.0 / scale;
			double x = coordinate.getX() * inverseScale;
			double y = -coordinate.getY() * inverseScale;

			double w = (double) viewPort.getMapWidth() / scale;
			double h = (double) viewPort.getMapHeight() / scale;
			// -cam: center X axis around cam. +bbox.w/2: to place the origin in the center of the screen
			double translateX = -viewPort.getPosition().getX() + (w / 2);
			double translateY = -viewPort.getPosition().getY() - (h / 2); // Inverted Y-axis here...
			x -= translateX;
			y -= translateY;
			return new Coordinate(x, y);
		}
		return null;
	}

	private Geometry screenToWorld(Geometry geometry) {
		if (geometry != null) {
			Geometry result = new Geometry(geometry.getGeometryType(), geometry.getSrid(), geometry.getPrecision());
			if (geometry.getGeometries() != null) {
				Geometry[] transformed = new Geometry[geometry.getGeometries().length];
				for (int i = 0; i < geometry.getGeometries().length; i++) {
					transformed[i] = screenToWorld(geometry.getGeometries()[i]);
				}
				result.setGeometries(transformed);
			}
			if (geometry.getCoordinates() != null) {
				Coordinate[] transformed = new Coordinate[geometry.getCoordinates().length];
				for (int i = 0; i < geometry.getCoordinates().length; i++) {
					transformed[i] = screenToWorld(geometry.getCoordinates()[i]);
				}
				result.setCoordinates(transformed);
			}
			return result;
		}
		throw new IllegalArgumentException("Cannot transform null geometry.");
	}

	private Bbox screenToWorld(Bbox bbox) {
		if (bbox != null) {
			Coordinate c1 = screenToWorld(BboxService.getOrigin(bbox));
			Coordinate c2 = screenToWorld(BboxService.getEndPoint(bbox));
			double x = (c1.getX() < c2.getX()) ? c1.getX() : c2.getX();
			double y = (c1.getY() < c2.getY()) ? c1.getY() : c2.getY();
			return new Bbox(x, y, Math.abs(c1.getX() - c2.getX()), Math.abs(c1.getY() - c2.getY()));
		}
		return null;
	}
}