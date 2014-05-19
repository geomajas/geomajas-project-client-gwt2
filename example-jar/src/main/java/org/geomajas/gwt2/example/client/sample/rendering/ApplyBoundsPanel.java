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

package org.geomajas.gwt2.example.client.sample.rendering;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.Matrix;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.geometry.service.WktException;
import org.geomajas.geometry.service.WktService;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.event.MapInitializationEvent;
import org.geomajas.gwt2.client.event.MapInitializationHandler;
import org.geomajas.gwt2.client.gfx.GfxUtil;
import org.geomajas.gwt2.client.gfx.VectorContainer;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.ZoomOption;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

/**
 * ContentPanel that demonstrates rendering abilities in world space.
 * 
 * @author Jan Venstermans
 */
public class ApplyBoundsPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Jan Venstermans
	 */
	interface MyUiBinder extends UiBinder<Widget, ApplyBoundsPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	private VectorContainer container;

	private Bbox rectangleWideBbox;
	private Bbox rectangleHighBbox;
	private Bbox geometriesUnion;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter();
		mapPresenter.setSize(480, 480);
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());

		// Define the whole layout:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		// Initialize the map, and return the layout:
		GeomajasServerExtension.getInstance().initializeMap(mapPresenter, "gwt-app", "mapOsm");
		return layout;
	}

	@UiHandler("rectangleWideBtn")
	public void onRectangleWideBtnClicked(ClickEvent event) {
		mapPresenter.getViewPort().applyBounds(rectangleWideBbox, ZoomOption.LEVEL_FIT);
	}

	@UiHandler("rectangleHighBtn")
	public void onRectangleHighBtnClicked(ClickEvent event) {
		mapPresenter.getViewPort().applyBounds(rectangleHighBbox, ZoomOption.LEVEL_FIT);
	}

	@UiHandler("geometryBtn")
	public void onGeometryBtnClicked(ClickEvent event) {
		mapPresenter.getViewPort().applyBounds(geometriesUnion, ZoomOption.LEVEL_FIT);
	}

	private Geometry scale(Geometry geom) {
		Matrix scale = new Matrix(1000000, 0, 0, 1000000, 0, 0);
		return transform(geom, scale);
	}

	private VectorObject style(Geometry geom) {
		GfxUtil util = GeomajasImpl.getInstance().getGfxUtil();
		VectorObject shape = util.toShape(geom);
		util.applyStroke(shape, "#CC9900", 0.8, 1, "2 5");
		util.applyFill(shape, "#CC9900", geom.getGeometryType().endsWith("String") ? 0f : 0.5f);
		return shape;
	}

	public Geometry transform(Geometry geometry, Matrix matrix) {
		Geometry copy = GeometryService.clone(geometry);
		transformInplace(copy, matrix);
		return copy;
	}

	private void transformInplace(Geometry geometry, Matrix matrix) {
		if (geometry.getGeometries() != null) {
			for (Geometry g : geometry.getGeometries()) {
				transformInplace(g, matrix);
			}
		} else if (geometry.getCoordinates() != null) {
			for (Coordinate c : geometry.getCoordinates()) {
				double x = c.getX() * matrix.getXx() + c.getY() * matrix.getXy() + matrix.getDx();
				double y = c.getX() * matrix.getYx() + c.getY() * matrix.getYy() + matrix.getDy();
				c.setX(x);
				c.setY(y);
			}
		}
	}

	private void fillContainerAndMakeBboxes() {
		//add rectangleWide
		Rectangle rectangleWide = new Rectangle(1000000, 1000000, 2000000, 1000000);
		rectangleWide.setFillColor("#CC9900");
		rectangleWide.setFillOpacity(0.4);
		container.add(rectangleWide);
		rectangleWideBbox = new Bbox(rectangleWide.getUserX(), rectangleWide.getUserY(),
				rectangleWide.getUserWidth(), rectangleWide.getUserHeight());

		Rectangle rectangleHigh = new Rectangle(-2000000, 1000000, 1000000, 2000000);
		rectangleHigh.setFillColor("#66CC66");
		rectangleHigh.setFillOpacity(0.4);
		container.add(rectangleHigh);
		rectangleHighBbox = new Bbox(rectangleHigh.getUserX(), rectangleHigh.getUserY(),
				rectangleHigh.getUserWidth(), rectangleHigh.getUserHeight());

		//add geometries
		try {
			Geometry polygon = WktService
					.toGeometry("POLYGON ((0 0, 0 0.7, 0.7 0.7, 0 0),(0.1 0.2, 0.1 0.4, 0.3 0.4, 0.1 0.2))");
			Geometry line = WktService.toGeometry("LINESTRING (-2 0, -2 0.7, -1.3 0, -1.3 0.7)");
			Geometry point = WktService.toGeometry("POINT (-3.5 0.5)");
			Geometry multiPolygon = new Geometry(Geometry.MULTI_POLYGON, 0, 5);
			Matrix m1 = new Matrix(1, 0, 0, 1, -0.5, -2);
			Matrix m2 = new Matrix(1, 0, 0, 1, 0.5, -2);
			Matrix m3 = new Matrix(1, 0, 0, 1, 0, -1);
			multiPolygon.setGeometries(new Geometry[] { transform(polygon, m1), transform(polygon, m2),
					transform(polygon, m3), });
			Geometry multiLinestring = new Geometry(Geometry.MULTI_LINE_STRING, 0, 5);
			multiLinestring.setGeometries(new Geometry[] { transform(line, m1), transform(line, m2),
					transform(line, m3), });
			Geometry mp = new Geometry(Geometry.MULTI_POINT, 0, 5);
			mp.setGeometries(new Geometry[] { transform(point, m1), transform(point, m2), transform(point, m3), });

			//scale
			polygon = scale(polygon);
			line = scale(line);
			point = scale(point);
			multiPolygon = scale(multiPolygon);
			multiLinestring = scale(multiLinestring);
			mp = scale(mp);

			//style and add to container
			container.add(style(polygon));
			container.add(style(line));
			container.add(style(point));
			container.add(style(multiPolygon));
			container.add(style(multiLinestring));
			container.add(style(mp));

			//create general bounding box
			geometriesUnion = GeometryService.getBounds(polygon);
			geometriesUnion = BboxService.union(geometriesUnion, GeometryService.getBounds(line));
			geometriesUnion = BboxService.union(geometriesUnion, GeometryService.getBounds(point));
			geometriesUnion = BboxService.union(geometriesUnion, GeometryService.getBounds(multiPolygon));
			geometriesUnion = BboxService.union(geometriesUnion, GeometryService.getBounds(multiLinestring));
			geometriesUnion = BboxService.union(geometriesUnion, GeometryService.getBounds(mp));

		} catch (WktException e) {
			// not possible
		}
	}

	@UiHandler("deleteBtn")
	public void onDeleteAllBtnClicked(ClickEvent event) {
		Bbox union = BboxService.union(rectangleWideBbox, rectangleHighBbox);
		union = BboxService.union(union, rectangleHighBbox);
		union = BboxService.union(union, geometriesUnion);
		mapPresenter.getViewPort().applyBounds(union, ZoomOption.LEVEL_FIT);
	}

	/**
	 * Map initialization handler that adds a CheckBox to the layout for every layer. With these CheckBoxes, the user
	 * can toggle the layer's visibility.
	 * 
	 * @author Pieter De Graef
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			container = mapPresenter.getContainerManager().addWorldContainer();
			fillContainerAndMakeBboxes();
		}
	}
}