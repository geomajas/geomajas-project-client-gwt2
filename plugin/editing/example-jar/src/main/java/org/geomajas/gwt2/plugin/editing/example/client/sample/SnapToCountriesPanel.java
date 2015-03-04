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

package org.geomajas.gwt2.plugin.editing.example.client.sample;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.event.MapInitializationEvent;
import org.geomajas.gwt2.client.event.MapInitializationHandler;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.feature.FeatureMapFunction;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;
import org.geomajas.gwt2.client.map.layer.VectorServerLayer;
import org.geomajas.gwt2.example.base.client.ExampleBase;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.plugin.editing.client.GeometryArrayFunction;
import org.geomajas.plugin.editing.client.event.GeometryEditStartEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStartHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.geomajas.plugin.editing.client.snap.SnapSourceProvider;
import org.geomajas.plugin.editing.client.snap.SnappingRule;
import org.geomajas.plugin.editing.client.snap.algorithm.NearestEdgeSnapAlgorithm;
import org.geomajas.gwt2.plugin.editing.client.Editing;
import org.geomajas.gwt2.plugin.editing.client.GeometryEditor;

import java.util.List;
import java.util.Map;

/**
 * Sample that demonstrates LineString editing.
 *
 * @author Pieter De Graef
 */
public class SnapToCountriesPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 *
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, SnapToCountriesPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	@UiField
	protected Button createBtn;

	@UiField
	protected Button stopBtn;

	@UiField
	protected CheckBox snapCheckBox;

	@UiField
	protected TextBox distanceBox;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	private GeometryEditor editor;

	private GeometryEditService editService;

	private NearestEdgeSnapAlgorithm snapAlgorithm;

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
		GeomajasServerExtension.getInstance().initializeMap(mapPresenter, "appEditingExample",
				"mapEditingExampleCountries");
		mapPresenter.getEventBus().addMapInitializationHandler(new MapInitializationHandler() {

			@Override
			public void onMapInitialized(MapInitializationEvent event) {
				mapPresenter.getViewPort().applyBounds(ExampleBase.BBOX_ITALY);
			}
		});

		// Prepare editing:
		editor = Editing.getInstance().createGeometryEditor(mapPresenter);
		editService = editor.getEditService();

		// Activate snapping at startup:
		editor.setSnapOnDrag(true);
		editor.setSnapOnInsert(true);

		// Apply a snapping rule to the SnapService:
		snapAlgorithm = new NearestEdgeSnapAlgorithm();
		applySnappingRule();

		// Add editing handlers that change the enabled state of the buttons:
		editService.addGeometryEditStartHandler(new GeometryEditStartHandler() {

			@Override
			public void onGeometryEditStart(GeometryEditStartEvent event) {
				createBtn.setEnabled(false);
				stopBtn.setEnabled(true);
			}
		});
		editService.addGeometryEditStopHandler(new GeometryEditStopHandler() {

			@Override
			public void onGeometryEditStop(GeometryEditStopEvent event) {
				createBtn.setEnabled(true);
				stopBtn.setEnabled(false);
			}
		});

		distanceBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				applySnappingRule();
			}
		});

		return layout;
	}

	@UiHandler("snapCheckBox")
	protected void onSnapCheckboxClicked(ClickEvent event) {
		editor.setSnapOnDrag(snapCheckBox.getValue());
		editor.setSnapOnInsert(snapCheckBox.getValue());
	}

	@UiHandler("createBtn")
	protected void onCreateButtonClicked(ClickEvent event) {
		// Create an empty point geometry. It has no coordinate yet. That is up to the user...
		Geometry point = new Geometry(Geometry.POLYGON, 0, -1);
		editService.start(point);

		// Set the editing service in "INSERTING" mode. Make sure it starts inserting in the correct index.
		try {
			// Add an empty LinearRing to the Polygon.
			GeometryIndex index = editService.addEmptyChild();

			// Make sure we can start adding coordinates into the empty LinearRing:
			index = editService.getIndexService().addChildren(index, GeometryIndexType.TYPE_VERTEX, 0);

			// Set state to "inserting". The user must start clicking on the map to insert additional points:
			editService.setEditingState(GeometryEditState.INSERTING);

			// Make sure the service knows where to insert (in the empty LinearRing):
			editService.setInsertIndex(index);
		} catch (GeometryOperationFailedException e) {
			e.printStackTrace();
		}

		// Et voila, the use may now click on the map...
	}

	@UiHandler("stopBtn")
	protected void onStopButtonClicked(ClickEvent event) {
		editService.stop();
	}

	private void applySnappingRule() {
		// First clear any previous rules:
		editor.getSnappingService().clearSnappingRules();

		// Read the distance value. If it is not an integer, we apply the default:
		String distanceTxt = distanceBox.getValue();
		int distance;
		try {
			distance = Integer.parseInt(distanceTxt);
		} catch (Exception e) {
			distanceBox.setValue("100000");
			distance = 100000;
		}

		// Add the snapping rule, using a Nearest Edge snapping algorithm:
		editor.getSnappingService().addSnappingRule(
				new SnappingRule(snapAlgorithm, new CountriesLayerProvider(), distance));
	}

	/**
	 * Snapping source provider that provides the geometries from the countries in the Countries layer.
	 *
	 * @author Pieter De Graef
	 */
	private class CountriesLayerProvider implements SnapSourceProvider {

		private Bbox mapBounds;

		private boolean fetch;

		@Override
		public void getSnappingSources(final GeometryArrayFunction callback) {
			if (fetch) {
				final VectorServerLayer layer = (VectorServerLayer) mapPresenter.getLayersModel().getLayer(1);
				GeomajasServerExtension
						.getInstance()
						.getServerFeatureService()
						.search(mapPresenter.getViewPort().getCrs(), layer, GeometryService.toPolygon(mapBounds), 0.0,
								new FeatureMapFunction() {

									@Override
									public void execute(Map<FeaturesSupported, List<Feature>> featureMap) {
										List<Feature> features = featureMap.get(layer);
										Geometry[] geometries = new Geometry[features.size()];
										for (int i = 0; i < features.size(); i++) {
											geometries[i] = features.get(i).getGeometry();
										}
										callback.execute(geometries);
									}
								});
			}
		}

		@Override
		public void update(Bbox mapBounds) {
			// Only update the mapBounds if it's outside the region we already fetched.
			if (this.mapBounds == null || !BboxService.contains(this.mapBounds, mapBounds)) {
				this.mapBounds = BboxService.buffer(mapBounds, mapBounds.getWidth() / 2);
				fetch = true;
			} else {
				fetch = false;
			}
		}
	}
}