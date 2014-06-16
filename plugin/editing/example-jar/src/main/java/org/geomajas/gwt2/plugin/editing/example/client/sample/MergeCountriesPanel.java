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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.controller.AbstractMapController;
import org.geomajas.gwt2.client.controller.FeatureSelectionController;
import org.geomajas.gwt2.client.event.FeatureDeselectedEvent;
import org.geomajas.gwt2.client.event.FeatureSelectedEvent;
import org.geomajas.gwt2.client.event.FeatureSelectionHandler;
import org.geomajas.gwt2.client.event.MapInitializationEvent;
import org.geomajas.gwt2.client.event.MapInitializationHandler;
import org.geomajas.gwt2.client.gfx.VectorContainer;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;
import org.geomajas.gwt2.example.base.client.ExampleBase;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.plugin.editing.client.EditingServerExtension;
import org.geomajas.plugin.editing.client.GeometryFunction;
import org.geomajas.plugin.editing.client.merge.GeometryMergeException;
import org.geomajas.plugin.editing.client.merge.GeometryMergeService;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * Sample that demonstrates LineString editing.
 *
 * @author Pieter De Graef
 */
public class MergeCountriesPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 *
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, MergeCountriesPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	@UiField
	protected Button clearBtn;

	@UiField
	protected Button mergeBtn;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	private GeometryMergeService mergeService;

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
		GeomajasServerExtension.getInstance().initializeMap(mapPresenter, "appEditingExample",
				"mapEditingExampleCountries");
		mapPresenter.getEventBus().addMapInitializationHandler(new MapInitializationHandler() {

			@Override
			public void onMapInitialized(MapInitializationEvent event) {
				mapPresenter.getViewPort().applyBounds(ExampleBase.BBOX_ITALY);
				installSelectionController();

				// Start the merging process:
				try {
					mergeService.start();
				} catch (GeometryMergeException e) {
				}
			}
		});

		// Keep track of feature selection and add geometries to the merging service accordingly:
		mapPresenter.getEventBus().addFeatureSelectionHandler(new FeatureSelectionHandler() {

			@Override
			public void onFeatureSelected(FeatureSelectedEvent event) {
				checkMergeBtnEnabledState();
			}

			@Override
			public void onFeatureDeselected(FeatureDeselectedEvent event) {
				checkMergeBtnEnabledState();
			}
		});

		// Create a container wherein the result of the splitting will be shown:
		resultContainer = mapPresenter.getContainerManager().addWorldContainer();

		// Prepare merging:
		mergeService = EditingServerExtension.getInstance().getGeometryMergeService();

		return layout;
	}

	@UiHandler("clearBtn")
	protected void onClearButtonClicked(ClickEvent event) {
		// Clear selection and install the controller:
		deselectAll();
		installSelectionController();

		// Clear the result container:
		resultContainer.clear();

		// Reset button states:
		clearBtn.setEnabled(false);
		mergeBtn.setEnabled(false);

		// Restart the merge service:
		try {
			mergeService.start();
		} catch (GeometryMergeException e) {
		}
	}

	@UiHandler("mergeBtn")
	protected void onMergeButtonClicked(ClickEvent event) {
		try {
			// Get all selected features and add them to the merge service:
			FeaturesSupported layer = (FeaturesSupported) mapPresenter.getLayersModel().getLayer(1);
			for (Feature feature : layer.getSelectedFeatures()) {
				mergeService.addGeometry(feature.getGeometry());
			}

			// Now all geometries are added, execute the merge:
			mergeService.stop(new GeometryFunction() {

				@Override
				public void execute(Geometry geometry) {
					resultContainer.clear();
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
					deselectAll();
					clearBtn.setEnabled(true);
					mergeBtn.setEnabled(false);
				}
			});
		} catch (GeometryMergeException e) {
			Window.alert("Somethings wrong merging stuff");
		}
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void checkMergeBtnEnabledState() {
		FeaturesSupported layer = (FeaturesSupported) mapPresenter.getLayersModel().getLayer(1);
		mergeBtn.setEnabled(layer.getSelectedFeatures() != null && layer.getSelectedFeatures().size() > 1);
	}

	private void installSelectionController() {
		mapPresenter.setMapController(new FeatureSelectionController());
	}

	private void deselectAll() {
		FeaturesSupported layer = (FeaturesSupported) mapPresenter.getLayersModel().getLayer(1);
		layer.clearSelectedFeatures();
	}
}