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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.controller.AbstractMapController;
import org.geomajas.gwt2.client.controller.FeatureSelectionController;
import org.geomajas.gwt2.client.controller.FeatureSelectionController.SelectionMethod;
import org.geomajas.gwt2.client.event.FeatureDeselectedEvent;
import org.geomajas.gwt2.client.event.FeatureSelectedEvent;
import org.geomajas.gwt2.client.event.FeatureSelectionHandler;
import org.geomajas.gwt2.client.event.MapInitializationEvent;
import org.geomajas.gwt2.client.event.MapInitializationHandler;
import org.geomajas.gwt2.client.gfx.VectorContainer;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;
import org.geomajas.gwt2.example.base.client.ExampleBase;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.plugin.editing.client.EditingServerExtension;
import org.geomajas.plugin.editing.client.GeometryArrayFunction;
import org.geomajas.plugin.editing.client.event.GeometryEditChangeStateEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditChangeStateHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStartEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStartHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.split.GeometrySplitService;
import org.geomajas.gwt2.plugin.editing.client.Editing;
import org.geomajas.gwt2.plugin.editing.client.GeometryEditor;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * Sample that demonstrates LineString editing.
 *
 * @author Pieter De Graef
 */
public class SplitCountryPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 *
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, SplitCountryPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	@UiField
	protected Button createBtn;

	@UiField
	protected Button clearBtn;

	@UiField
	protected Button splitBtn;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	private GeometrySplitService splitService;

	private GeometryEditService editService;

	private Geometry selected;

	private VectorContainer resultContainer;

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
		GeomajasServerExtension.getInstance().initializeMap(mapPresenter, "gwt-app", "mapCountries");
		mapPresenter.getEventBus().addMapInitializationHandler(new MapInitializationHandler() {

			@Override
			public void onMapInitialized(MapInitializationEvent event) {
				mapPresenter.getViewPort().applyBounds(ExampleBase.BBOX_ITALY);
				installSelectionController();
			}
		});

		// Keep track of the lastly select feature and save it's geometry:
		mapPresenter.getEventBus().addFeatureSelectionHandler(new FeatureSelectionHandler() {

			@Override
			public void onFeatureSelected(FeatureSelectedEvent event) {
				selected = event.getFeature().getGeometry();
				checkCreateBtnEnabledState();
			}

			@Override
			public void onFeatureDeselected(FeatureDeselectedEvent event) {
				selected = null;
				checkCreateBtnEnabledState();
			}
		});

		// Create a container wherein the result of the splitting will be shown:
		resultContainer = mapPresenter.getContainerManager().addWorldContainer();

		// Prepare editing:
		GeometryEditor editor = Editing.getInstance().createGeometryEditor(mapPresenter);
		editService = editor.getEditService();
		splitService = EditingServerExtension.getInstance().createGeometrySplitService(editService);

		// Add editing handlers that change the enabled state of the buttons:
		editService.addGeometryEditStartHandler(new GeometryEditStartHandler() {

			@Override
			public void onGeometryEditStart(GeometryEditStartEvent event) {
				checkCreateBtnEnabledState();
			}
		});
		editService.addGeometryEditStopHandler(new GeometryEditStopHandler() {

			@Override
			public void onGeometryEditStop(GeometryEditStopEvent event) {
				checkCreateBtnEnabledState();
			}
		});

		// When the user is finished adding points to the splitting line, we enable the "split" and "clear" buttons:
		editService.addGeometryEditChangeStateHandler(new GeometryEditChangeStateHandler() {

			@Override
			public void onChangeEditingState(GeometryEditChangeStateEvent event) {
				clearBtn.setEnabled(event.getEditingState() == GeometryEditState.IDLE);
				splitBtn.setEnabled(event.getEditingState() == GeometryEditState.IDLE);
			}
		});

		return layout;
	}

	@UiHandler("createBtn")
	protected void onCreateButtonClicked(ClickEvent event) {
		mapPresenter.setMapController(null);
		splitService.start(selected);
	}

	@UiHandler("clearBtn")
	protected void onClearButtonClicked(ClickEvent event) {
		// Reset button states:
		createBtn.setEnabled(false);
		clearBtn.setEnabled(false);
		splitBtn.setEnabled(false);

		// Clear selection and install the controller:
		deselectAll();
		installSelectionController();

		// Clear the result container:
		resultContainer.clear();
	}

	@UiHandler("splitBtn")
	protected void onSplitButtonClicked(ClickEvent event) {
		splitService.stop(new GeometryArrayFunction() {

			@Override
			public void execute(Geometry[] geometries) {
				// Clear selection:
				deselectAll();

				// Now draw the result:
				for (Geometry geometry : geometries) {
					final VectorObject vo = GeomajasImpl.getInstance().getGfxUtil().toShape(geometry);
					GeomajasImpl.getInstance().getGfxUtil().applyFill(vo, "#00AA00", 0.9);
					GeomajasImpl.getInstance().getGfxUtil().applyStroke(vo, "#000000", 1.0, 2, null);

					// Highlight the resulting geometries when hovering over it:
					GeomajasImpl.getInstance().getGfxUtil().applyController(vo, new AbstractMapController() {

						@Override
						public void onMouseOver(MouseOverEvent event) {
							GeomajasImpl.getInstance().getGfxUtil().applyFill(vo, "#00FF00", 0.9);
						}

						@Override
						public void onMouseOut(MouseOutEvent event) {
							GeomajasImpl.getInstance().getGfxUtil().applyFill(vo, "#00AA00", 0.9);
						}
					});

					resultContainer.add(vo);
				}
				splitBtn.setEnabled(false);
			}
		});
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void checkCreateBtnEnabledState() {
		createBtn.setEnabled(selected != null && !editService.isStarted());
	}

	private void installSelectionController() {
		mapPresenter.setMapController(new FeatureSelectionController(SelectionMethod.SINGLE_SELECTION));
	}

	private void deselectAll() {
		FeaturesSupported layer = (FeaturesSupported) mapPresenter.getLayersModel().getLayer(1);
		layer.clearSelectedFeatures();
	}
}