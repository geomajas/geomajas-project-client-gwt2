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

package org.geomajas.gwt2.tools;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.GestureChangeHandler;
import com.google.gwt.event.dom.client.GestureEndHandler;
import com.google.gwt.event.dom.client.GestureStartHandler;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.gfx.CanvasContainer;
import org.geomajas.gwt2.client.gfx.CanvasShape;
import org.geomajas.gwt2.client.gfx.TransformableWidget;
import org.geomajas.gwt2.client.gfx.TransformableWidgetContainer;
import org.geomajas.gwt2.client.gfx.VectorContainer;
import org.geomajas.gwt2.client.map.MapPresenterImpl.MapWidget;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlContainer;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlObject;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Transformable;
import org.vaadin.gwtgraphics.client.VectorObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Implementation of the MapWidget interface as described by the {@link org.geomajas.gwt2.client.map.MapPresenterImpl}.
 * It represents the MVP 'view' of the map's presenter (aka MapPresenter).
 * It is designed to be used in Unit Tests.
 * </p>
 *
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @author Oliver May
 */
public final class MapWidgetTestImpl implements MapWidget {

	// Container for raster layers or rasterized layers:
	private HtmlContainer layerHtmlContainer = new MyHtmlContainer();

	// Container for vector layers (SVG/VML):
	private VectorContainer layerVectorContainer = new MyVectorContainer();

	// Parent container for all SVG/VML:
//	private DrawingArea drawingArea;

	// Parent container for all canvases:
//	private AbsolutePanel canvasPanel;

	// Parent container for all transformable widget containers
//	private FlowPanel widgetPanel;

	// List of all screen containers:
	private List<VectorContainer> screenContainers = new ArrayList<VectorContainer>();

	// List of all world containers:
	private List<VectorContainer> worldContainers = new ArrayList<VectorContainer>();

	// List of all world canvas containers:
	private List<CanvasContainer> worldCanvases = new ArrayList<CanvasContainer>();

	// List of all widget containers:
	private List<TransformableWidgetContainer> widgetContainers = new ArrayList<TransformableWidgetContainer>();

	// List of all world transformables (canvas + vector):
	private List<Transformable> worldTransformables = new ArrayList<Transformable>();

	private int width;

	private int height;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public MapWidgetTestImpl() {
//		// Attach an HtmlContainer inside the clipping area (used for rendering layers):
//		layerHtmlContainer = new HtmlGroup();
//		layerHtmlContainer.asWidget().getElement().getStyle().setZIndex(0);
//
//		// Add a panel to hold the canvases (this should come before vectors or it catches all events !)
//		canvasPanel = new AbsolutePanel();
//
//		// Attach a DrawingArea inside the clipping area (used for vector rendering):
//		drawingArea = new DrawingArea(100, 100);
//
//		// First child within the vector drawing area is a group for the map to render it's non-HTML layers:
//		layerVectorContainer = new VectorGroup();
//		drawingArea.add(layerVectorContainer);
//
//		widgetPanel = new FlowPanel();
	}

	@Override
	public Widget asWidget() {
		return null;
	}

	@Override
	public HtmlContainer getMapHtmlContainer() {
		return layerHtmlContainer;
	}

	@Override
	public VectorContainer getMapVectorContainer() {
		return layerVectorContainer;
	}

	@Override
	public List<VectorContainer> getWorldVectorContainers() {
		return worldContainers;
	}

	@Override
	public List<Transformable> getWorldTransformables() {
		return worldTransformables;
	}

	@Override
	public VectorContainer getNewScreenContainer() {
		return new MyVectorContainer();
	}

	@Override
	public VectorContainer getNewWorldContainer() {
		return new MyVectorContainer();
	}

	@Override
	public CanvasContainer getNewWorldCanvas() {
		return new MyCanvasContainer();
	}

	@Override
	public TransformableWidgetContainer getNewWorldWidgetContainer() {
		return new MyTransformableWidgetContainer();
	}

	@Override
	public boolean removeVectorContainer(VectorContainer container) {
		if (container instanceof Group) {
			if (worldContainers.contains(container)) {
//				drawingArea.remove((Group) container);
				worldContainers.remove(container);
				worldTransformables.remove(container);
				return true;
			} else if (screenContainers.contains(container)) {
//				drawingArea.remove((Group) container);
				screenContainers.remove(container);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean removeWorldWidgetContainer(TransformableWidgetContainer container) {
		if (worldContainers.contains(container)) {
//			widgetPanel.remove(container);
			widgetContainers.remove(container);
			worldTransformables.remove(container);
			return true;
		}
		return false;
	}

	@Override
	public boolean bringToFront(VectorContainer container) {
		if (container instanceof Group) {
			if (worldContainers.contains(container)) {
//				drawingArea.bringToFront((Group) container);
				return true;
			} else if (screenContainers.contains(container)) {
//				drawingArea.bringToFront((Group) container);
				return true;
			}
		}
		return false;
	}

	@Override
	public AbsolutePanel getWidgetContainer() {
		return null;
	}

	@Override
	public void onResize() {
	}

	// ------------------------------------------------------------------------
	// Overriding resize methods:
	// ------------------------------------------------------------------------

	public void setPixelSize(int width, int height) {
//		drawingArea.setWidth(width);
//		drawingArea.setHeight(height);
//		canvasPanel.setPixelSize(width, height);
		for (CanvasContainer container : worldCanvases) {
			container.setPixelSize(width, height);
		}
	}

	public void setSize(String width, String height) {
//		drawingArea.setWidth(width);
//		drawingArea.setHeight(height);
//		canvasPanel.setWidth(width);
//		canvasPanel.setHeight(height);
		for (CanvasContainer container : worldCanvases) {
			container.setPixelSize(0, 0);
		}
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setWidth(String width) {
//		drawingArea.setWidth(width);
//		canvasPanel.setWidth(width);
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setHeight(String height) {
//		drawingArea.setHeight(height);
//		canvasPanel.setHeight(height);
		for (CanvasContainer container : worldCanvases) {
			container.setPixelSize(0, 0);
		}
	}

	// ------------------------------------------------------------------------
	// Add mouse event catch methods:
	// ------------------------------------------------------------------------

	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {

			}
		};
	}

	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {

			}
		};
	}

	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {

			}
		};
	}

	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {

			}
		};
	}

	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {

			}
		};
	}

	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {

			}
		};
	}

	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {

			}
		};
	}

	// ------------------------------------------------------------------------
	// Touch event catch methods:
	// ------------------------------------------------------------------------

	@Override
	public HandlerRegistration addTouchStartHandler(TouchStartHandler handler) {
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {

			}
		};
	}

	@Override
	public HandlerRegistration addTouchEndHandler(TouchEndHandler handler) {
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {

			}
		};
	}

	@Override
	public HandlerRegistration addTouchCancelHandler(TouchCancelHandler handler) {
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {

			}
		};
	}

	@Override
	public HandlerRegistration addTouchMoveHandler(TouchMoveHandler handler) {
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {

			}
		};
	}

	// ------------------------------------------------------------------------
	// Gesture event catch methods:
	// ------------------------------------------------------------------------
	@Override
	public HandlerRegistration addGestureStartHandler(GestureStartHandler handler) {
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {

			}
		};
	}

	@Override
	public HandlerRegistration addGestureChangeHandler(GestureChangeHandler handler) {
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {

			}
		};
	}

	@Override
	public HandlerRegistration addGestureEndHandler(GestureEndHandler handler) {
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {

			}
		};
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {

	}

	private static class MyVectorContainer implements VectorContainer {

		@Override
		public void setVisible(boolean visible) {

		}

		@Override
		public boolean isVisible() {
			return false;
		}

		@Override
		public HandlerRegistration addClickHandler(ClickHandler handler) {
			return null;
		}

		@Override
		public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
			return null;
		}

		@Override
		public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
			return null;
		}

		@Override
		public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
			return null;
		}

		@Override
		public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
			return null;
		}

		@Override
		public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
			return null;
		}

		@Override
		public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
			return null;
		}

		@Override
		public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
			return null;
		}

		@Override
		public void fireEvent(GwtEvent<?> event) {

		}

		@Override
		public Widget asWidget() {
			return null;
		}

		@Override
		public void setTranslation(double v, double v2) {

		}

		@Override
		public void setScale(double v, double v2) {

		}

		@Override
		public void setFixedSize(boolean b) {

		}

		@Override
		public boolean isFixedSize() {
			return false;
		}

		@Override
		public void setOpacity(double v) {

		}

		@Override
		public VectorObject add(VectorObject vectorObject) {
			return null;
		}

		@Override
		public VectorObject insert(VectorObject vectorObject, int i) {
			return null;
		}

		@Override
		public VectorObject remove(VectorObject vectorObject) {
			return null;
		}

		@Override
		public VectorObject bringToFront(VectorObject vectorObject) {
			return null;
		}

		@Override
		public VectorObject moveToBack(VectorObject vectorObject) {
			return null;
		}

		@Override
		public void clear() {

		}

		@Override
		public int getVectorObjectCount() {
			return 0;
		}

		@Override
		public VectorObject getVectorObject(int i) {
			return null;
		}

		@Override
		public int indexOf(VectorObject vectorObject) {
			return 0;
		}
	}

	private static class MyHtmlContainer implements HtmlContainer {

		@Override
		public void applyScale(double scale, int x, int y) {

		}

		@Override
		public double getScale() {
			return 0;
		}

		@Override
		public void add(HtmlObject child) {

		}

		@Override
		public void insert(HtmlObject child, int beforeIndex) {

		}

		@Override
		public boolean remove(HtmlObject child) {
			return false;
		}

		@Override
		public void bringToFront(HtmlObject child) {

		}

		@Override
		public void clear() {

		}

		@Override
		public int getChildCount() {
			return 0;
		}

		@Override
		public HtmlObject getChild(int index) {
			return null;
		}

		@Override
		public Coordinate getOrigin() {
			return null;
		}

		@Override
		public void setOrigin(Coordinate origin) {

		}

		@Override
		public HtmlObject getParent() {
			return null;
		}

		@Override
		public int getWidth() {
			return 0;
		}

		@Override
		public void setWidth(int width) {

		}

		@Override
		public int getHeight() {
			return 0;
		}

		@Override
		public void setHeight(int height) {

		}

		@Override
		public int getLeft() {
			return 0;
		}

		@Override
		public void setLeft(int left) {

		}

		@Override
		public int getTop() {
			return 0;
		}

		@Override
		public void setTop(int top) {

		}

		@Override
		public double getOpacity() {
			return 0;
		}

		@Override
		public void setOpacity(double opacity) {

		}

		@Override
		public void setVisible(boolean visible) {

		}

		@Override
		public boolean isVisible() {
			return false;
		}

		@Override
		public Widget asWidget() {
			return null;
		}
	}

	private static class MyCanvasContainer implements CanvasContainer {

		@Override
		public void addShape(CanvasShape shape) {

		}

		@Override
		public void addAll(List<CanvasShape> shapes) {

		}

		@Override
		public void removeShape(CanvasShape shape) {

		}

		@Override
		public void clear() {

		}

		@Override
		public void repaint() {

		}

		@Override
		public void setPixelSize(int width, int height) {

		}

		@Override
		public Widget asWidget() {
			return null;
		}

		@Override
		public void setTranslation(double v, double v2) {

		}

		@Override
		public void setScale(double v, double v2) {

		}

		@Override
		public void setFixedSize(boolean b) {

		}

		@Override
		public boolean isFixedSize() {
			return false;
		}

		@Override
		public void setOpacity(double v) {

		}
	}

	private static class MyTransformableWidgetContainer implements TransformableWidgetContainer {

		@Override
		public void add(TransformableWidget child) {

		}

		@Override
		public void insert(TransformableWidget child, int beforeIndex) {

		}

		@Override
		public boolean remove(TransformableWidget child) {
			return false;
		}

		@Override
		public int indexOf(TransformableWidget child) {
			return 0;
		}

		@Override
		public void bringToFront(TransformableWidget child) {

		}

		@Override
		public void clear() {

		}

		@Override
		public int getChildCount() {
			return 0;
		}

		@Override
		public TransformableWidget getChild(int index) {
			return null;
		}

		@Override
		public Widget asWidget() {
			return null;
		}

		@Override
		public void setTranslation(double v, double v2) {

		}

		@Override
		public void setScale(double v, double v2) {

		}

		@Override
		public void setFixedSize(boolean b) {

		}

		@Override
		public boolean isFixedSize() {
			return false;
		}
	}
}