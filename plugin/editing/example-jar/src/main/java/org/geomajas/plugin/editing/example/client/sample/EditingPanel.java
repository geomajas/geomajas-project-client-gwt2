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

package org.geomajas.plugin.editing.example.client.sample;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.event.MapResizedEvent;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.widget.MapLayoutPanel;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.geomajas.plugin.editing.example.client.sample.button.BufferAllButton;
import org.geomajas.plugin.editing.example.client.sample.button.CancelButton;
import org.geomajas.plugin.editing.example.client.sample.button.RedoButton;
import org.geomajas.plugin.editing.example.client.sample.button.UndoButton;
import org.geomajas.plugin.editing.gwt.client.Editing;
import org.geomajas.plugin.editing.gwt.client.GeometryEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates rendering abilities in world space with a map that supports resizing.
 * 
 * @author Pieter De Graef
 */
public class EditingPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, EditingPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	@UiField
	protected HorizontalPanel buttonPanel;

	private CancelButton cancelButton;

	private UndoButton undoButton;

	private RedoButton redoButton;

	private GeometryToShapeConverter geometryToShapeConverter;

	private GeometryEditService editService;

	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Create the mapPresenter and add an InitializationHandler:
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter();
		mapPresenter.setSize(480, 480);

		// Define the whole layout:
		MapLayoutPanel mapLayout = new MapLayoutPanel();
		mapLayout.setPresenter(mapPresenter);
		mapPanel.add(mapLayout);

		// Initialize the map, and return the layout:
		GeomajasServerExtension.getInstance().initializeMap(mapPresenter, "gwt-app", "mapOsm");

		// Prepare editing:
		GeometryEditor editor = Editing.getInstance().createGeometryEditor(mapPresenter);
		editService = editor.getEditService();
		geometryToShapeConverter = new GeometryToShapeConverter(editService, mapPresenter);

		addButtons();

		return layout;
	}

	private void addButtons() {
		// Add buttons to the button panel:
		buttonPanel.add(getBtnFreePoint());
		buttonPanel.add(getBtnFreeLine());
		buttonPanel.add(getBtnFreePolygon());
		buttonPanel.add(getBtnHugePolygon());
		buttonPanel.add(new Label());
		cancelButton = new CancelButton();
		buttonPanel.add(cancelButton);
		cancelButton.setEditService(editService);
		undoButton = new UndoButton();
		buttonPanel.add(undoButton);
		undoButton.setEditService(editService);
		redoButton = new RedoButton();
		buttonPanel.add(redoButton);
		redoButton.setEditService(editService);
		TextBox bufferDistance = new TextBox();
		bufferDistance.setValue("50000");
		buttonPanel.add(bufferDistance);
		buttonPanel.add(new BufferAllButton(geometryToShapeConverter, bufferDistance));
	}

	private Widget getBtnFreePoint() {
		Button btn = new Button("Draw point");
		btn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				geometryToShapeConverter.processCurrentGeometry();

				Geometry point = new Geometry(Geometry.POINT, 0, -1);
				editService.start(point);

				GeometryIndex index = editService.getIndexService().create(GeometryIndexType.TYPE_VERTEX, 0);
				editService.setEditingState(GeometryEditState.INSERTING);
				editService.setInsertIndex(index);
			}
		});
		return btn;
	}

	private Widget getBtnFreeLine() {
		Button btn = new Button("Draw LineString");
		btn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				geometryToShapeConverter.processCurrentGeometry();

				Geometry line = new Geometry(Geometry.LINE_STRING, 0, -1);
				editService.start(line);

				GeometryIndex index = editService.getIndexService().create(GeometryIndexType.TYPE_VERTEX, 0);
				editService.setEditingState(GeometryEditState.INSERTING);
				editService.setInsertIndex(index);
			}
		});
		return btn;
	}

	private Widget getBtnFreePolygon() {
		Button btn = new Button("Draw Polygon");
		btn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				geometryToShapeConverter.processCurrentGeometry();
				Geometry polygon = new Geometry(Geometry.POLYGON, 0, -1);
				editService.start(polygon);

				try {
					GeometryIndex index = editService.addEmptyChild();
					index = editService.getIndexService().addChildren(index, GeometryIndexType.TYPE_VERTEX, 0);
					editService.setEditingState(GeometryEditState.INSERTING);
					editService.setInsertIndex(index);
				} catch (GeometryOperationFailedException e) {
					e.printStackTrace();
				}
			}
		});
		return btn;
	}

	private Widget getBtnHugePolygon() {
		Button btn = new Button("1000 points polygon");
		btn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				geometryToShapeConverter.processCurrentGeometry();
				Coordinate[] coords = new Coordinate[1001];
				for (int i = 0; i < 1000; i++) {
					double angle = 2 * Math.PI * i / 1000.0;
					coords[i] = new Coordinate(Math.cos(angle) * 1000000, Math.sin(angle) * 1000000);
				}
				coords[1000] = (Coordinate) coords[0].clone();
				Geometry ring = new Geometry(Geometry.LINEAR_RING, 0, 5);
				ring.setCoordinates(coords);
				Geometry polygon = new Geometry(Geometry.POLYGON, 0, 5);
				polygon.setGeometries(new Geometry[] { ring });
				editService.start(polygon);
			}
		});
		return btn;
	}

	public void onMapResized(MapResizedEvent event) {
		// TODO Auto-generated method stub

	}
}