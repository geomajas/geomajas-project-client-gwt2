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

package org.geomajas.gwt2.plugin.editing.example.client.sample;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.plugin.editing.client.event.GeometryEditChangeStateEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditChangeStateHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStartEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStartHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.geomajas.gwt2.plugin.editing.client.Editing;
import org.geomajas.gwt2.plugin.editing.client.GeometryEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Sample that demonstrates LineString editing.
 * 
 * @author Pieter De Graef
 */
public class EditPolygonPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, EditPolygonPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	@UiField
	protected Button createBtn;

	@UiField
	protected Button editBtn;

	@UiField
	protected Button stopBtn;

	@UiField
	protected Button addRingBtn;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	private GeometryEditService editService;

	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Create the mapPresenter and add an InitializationHandler:
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter();
		mapPresenter.setSize(480, 480);

		// Define the whole layout:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		// Initialize the map, and return the layout:
		GeomajasServerExtension.getInstance().initializeMap(mapPresenter, "gwt-app", "mapOsm");

		// Prepare editing:
		GeometryEditor editor = Editing.getInstance().createGeometryEditor(mapPresenter);
		editService = editor.getEditService();

		// Add editing handlers that change the enabled state of the buttons:
		editService.addGeometryEditStartHandler(new GeometryEditStartHandler() {

			@Override
			public void onGeometryEditStart(GeometryEditStartEvent event) {
				createBtn.setEnabled(false);
				editBtn.setEnabled(false);
				stopBtn.setEnabled(true);

				addRingBtn.setVisible(true);
			}
		});
		editService.addGeometryEditStopHandler(new GeometryEditStopHandler() {

			@Override
			public void onGeometryEditStop(GeometryEditStopEvent event) {
				createBtn.setEnabled(true);
				editBtn.setEnabled(true);
				stopBtn.setEnabled(false);
				addRingBtn.setVisible(false);
			}
		});
		editService.addGeometryEditChangeStateHandler(new GeometryEditChangeStateHandler() {

			@Override
			public void onChangeEditingState(GeometryEditChangeStateEvent event) {
				// Only enable the "add ring" button when the user is not busy creating a new polygon (state INSERTING).
				addRingBtn.setEnabled(editService.getEditingState() == GeometryEditState.IDLE);
			}
		});

		return layout;
	}

	@UiHandler("createBtn")
	protected void onCreateButtonClicked(ClickEvent event) {
		// Create an empty point geometry. It has no coordinate yet. That is up to the user...
		Geometry polygon = new Geometry(Geometry.POLYGON, 0, -1);
		editService.start(polygon);
		// Set state to "inserting". The user must start clicking on the map to insert additional points:
		editService.setEditingState(GeometryEditState.INSERTING);
		// Et voila, the use may now click on the map...
	}

	@UiHandler("editBtn")
	protected void onEditButtonClicked(ClickEvent event) {
		// Create a point geometry in the center of the map:
		Geometry ring = new Geometry(Geometry.LINEAR_RING, 0, -1);
		Bbox bounds = mapPresenter.getViewPort().getBounds();
		double x1 = bounds.getX() + bounds.getWidth() / 4;
		double x2 = bounds.getMaxX() - bounds.getWidth() / 4;
		double y1 = bounds.getY() + bounds.getHeight() / 4;
		double y2 = bounds.getMaxY() - bounds.getHeight() / 4;

		ring.setCoordinates(new Coordinate[] { new Coordinate(x1, y1), new Coordinate(x2, y1), new Coordinate(x2, y2),
				new Coordinate(x1, y2), new Coordinate(x1, y1) });
		Geometry polygon = new Geometry(Geometry.POLYGON, 0, 5);
		polygon.setGeometries(new Geometry[] { ring });

		// Now start editing it:
		editService.start(polygon);
	}

	@UiHandler("addRingBtn")
	protected void onAddRingButtonClicked(ClickEvent event) {
		// Set state to "inserting". The user must start clicking on the map to insert additional points:
		editService.setEditingState(GeometryEditState.INSERTING);
	}

	@UiHandler("stopBtn")
	protected void onStopButtonClicked(ClickEvent event) {
		editService.stop();
	}
}