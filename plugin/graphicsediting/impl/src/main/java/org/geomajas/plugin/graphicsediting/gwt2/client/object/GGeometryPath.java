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
package org.geomajas.plugin.graphicsediting.gwt2.client.object;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.Resizable;
import org.geomajas.graphics.client.object.ResizableGraphicsObject;
import org.geomajas.graphics.client.object.role.Fillable;
import org.geomajas.graphics.client.object.role.Strokable;
import org.geomajas.graphics.client.util.FlipState;
import org.geomajas.gwt2.client.gfx.GeometryPath;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * {@link GGeometryPath} objects are standard Geometry Resizable objects.
 * The first G is for Graphics, like in all Graphics objects.
 * 
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public class GGeometryPath extends ResizableGraphicsObject implements GeometryEditable {

	public GGeometryPath(Geometry geometry, String text) {
		this(new ResizableGeometryPath(geometry));
	}

	public GGeometryPath(ResizableGeometryPath path) {
		super(path);
		addRole(GeometryEditable.TYPE, this);
		addRole(Strokable.TYPE, path);
		if (getPath().isClosed()) {
			addRole(Fillable.TYPE, path);
		}
	}

	@Override
	public GraphicsObject cloneObject() {
		ResizableGeometryPath path = getPath();
		GGeometryPath clone = new GGeometryPath((ResizableGeometryPath) path.cloneObject());
		copyTo(clone);
		return clone;
	}

	private ResizableGeometryPath getPath() {
		return (ResizableGeometryPath) getResizable();
	}

	@Override
	public void setGeometry(Geometry geometry) {
		getPath().setGeometry(geometry);
		update();
	}

	@Override
	public Geometry getGeometry() {
		return getPath().getGeometry();
	}

	/**
	 * Resizable implementation for path.
	 */
	static class ResizableGeometryPath implements Resizable, Fillable, Strokable, GeometryEditable {

		protected GeometryPath path;

		/**
		 * Transparent helper line object, still generating pointer events.
		 * This is only used in case of a non closed geometry path.
		 */
		private GeometryPath clickArea;

		/**
		 * Group object collecting path and clickArea.
		 * This is only used in case of a non closed geometry path.
		 */
		private Group group;

		/**
		 * Minimum mouse event buffer for a non closed geometry path.
		 */
		private static int pointerEventAreaminimumWidth = 10;

		protected Geometry geometry;

		ResizableGeometryPath(Geometry geometry) {
			path = new GeometryPath(geometry);
			if (!path.isClosed()) {
				path.setFillColor("none");
				group = new Group();
				group.add(path);
				clickArea = new GeometryPath(geometry);
				clickArea.setFillColor("none");
				clickArea.setStrokeWidth(pointerEventAreaminimumWidth);
				clickArea.setStrokeOpacity(0);  // makes it invisble, but mouse events will still be registered
				group.add(clickArea);
			}
			this.geometry = geometry;
		}

		public boolean isClosed() {
			return path.isClosed();
		}
		
		protected GeometryPath getPath() {
			return path;
		}

		@Override
		public void flip(FlipState state) {
			// TODO flip coordinates and - for FLIP_X, FLIP_Y - reverse coordinate order of closed paths
			switch (state) {
				case FLIP_X:
					break;
				case FLIP_XY:
					break;
				case FLIP_Y:
					break;
				case NONE:
					break;
			}
		}

		@Override
		public boolean isPreserveRatio() {
			return false;
		}

		@Override
		public boolean isAutoHeight() {
			return false;
		}

		@Override
		public void setUserBounds(Bbox bounds) {
			path.setUserBounds(bounds);
		}

		public Bbox getUserBounds() {
			return path.getUserbounds();
		}

		@Override
		public Bbox getBounds() {
			return path.getBounds();
		}

		@Override
		public void setPosition(Coordinate position) {
			path.setUserPosition(position);
			if (!isClosed()) {
				clickArea.setUserPosition(position);
			}
		}

		@Override
		public Coordinate getPosition() {
			return path.getUserPosition();
		}

		public VectorObject asObject() {
			return isClosed() ? path : group;
		}

		public Object cloneObject() {
			Geometry geom = (Geometry) geometry.clone();
			ResizableGeometryPath copy = new ResizableGeometryPath(geom);
			return copy;
		}

		@Override
		public void setFillColor(String fillColor) {
			path.setFillColor(fillColor);
		}

		@Override
		public void setFillOpacity(double fillOpacity) {
			path.setFillOpacity(fillOpacity);
		}

		@Override
		public String getFillColor() {
			return path.getFillColor();
		}

		@Override
		public double getFillOpacity() {
			return path.getFillOpacity();
		}

		@Override
		public String getStrokeColor() {
			return path.getStrokeColor();
		}

		@Override
		public void setStrokeColor(String strokeColor) {
			path.setStrokeColor(strokeColor);
		}

		@Override
		public int getStrokeWidth() {
			return path.getStrokeWidth();
		}

		@Override
		public void setStrokeWidth(int strokeWidth) {
			path.setStrokeWidth(strokeWidth);
		}

		@Override
		public double getStrokeOpacity() {
			return path.getStrokeOpacity();
		}

		@Override
		public void setStrokeOpacity(double strokeOpacity) {
			path.setStrokeOpacity(strokeOpacity);
		}

		@Override
		public void setGeometry(Geometry geometry) {
			path.setGeometry(geometry);
			this.geometry = geometry;
		}

		@Override
		public Geometry getGeometry() {
			return geometry;
		}

		public static int getPointerEventAreaminimumWidth() {
			return pointerEventAreaminimumWidth;
		}

		public static void setPointerEventAreaminimumWidth(int pointerEventAreaminimumWidth) {
			ResizableGeometryPath.pointerEventAreaminimumWidth = pointerEventAreaminimumWidth;
		}
	}

}
