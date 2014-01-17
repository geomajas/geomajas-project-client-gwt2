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

package org.geomajas.gwt2.widget.client.controller;

import com.google.gwt.event.dom.client.MouseEvent;
import org.geomajas.annotation.Api;
import com.google.gwt.event.dom.client.MouseDownEvent;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.controller.AbstractController;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.controller.MapController;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.feature.FeatureMapFunction;
import org.geomajas.gwt2.client.map.feature.ServerFeatureService.QueryType;
import org.geomajas.gwt2.client.map.feature.ServerFeatureService.SearchLayerType;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;
import org.geomajas.gwt2.widget.client.event.FeaturesClickedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller that provides a list of features based on a location clicked on the map.
 * The controller fires {@link FeaturesClickedEvent}s.
 * 
 * @author Dosi Bingov
 * @author Oliver May
 * 
 */
@Api(allMethods = true)
public class FeaturesClickedController extends AbstractController implements MapController {

	protected MapPresenter mapPresenter;

	private int pixelBuffer;

	/**
	 * Create a featuresclicked controller and use the default pixelBuffer of 32.
	 */
	public FeaturesClickedController() {
		// minimum distance between features to show FeatureSelectWidget.
		this(32);
	}

	/**
	 * Create a featuresclicked controller with a given pixelBuffer.
	 *
	 * @param pixelBuffer
	 *            minimum distance between features to show context menu with features labels.
	 */
	public FeaturesClickedController(int pixelBuffer) {
		super(false);
		this.pixelBuffer = pixelBuffer;
	}

	@Override
	public void onActivate(MapPresenter presenter) {
		mapPresenter = presenter;
		eventParser = mapPresenter.getMapEventParser();
	}

	@Override
	public void onDeactivate(MapPresenter presenter) {
		// Nothing to do here
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		searchAtLocation(event);
	}

	/**
	 * Search for features at the given location.
	 *
	 * @param event the mouse event
	 */
	protected void searchAtLocation(MouseEvent event) {
		final Coordinate clickedPosition = getLocation(event, RenderSpace.SCREEN);
		Coordinate location = getLocation(event, RenderSpace.WORLD);

		Geometry point = new Geometry(Geometry.POINT, 0, -1);
		point.setCoordinates(new Coordinate[] { location });

		GeomajasServerExtension.getInstance().getServerFeatureService().search(mapPresenter, point,
				calculateBufferFromPixelTolerance(), QueryType.INTERSECTS, SearchLayerType.SEARCH_ALL_LAYERS, -1,
				new FeatureMapFunction() {

					@Override
					public void execute(Map<FeaturesSupported, List<Feature>> featureMap) {
						fireFeatureClickedEvent(clickedPosition, buildFeaturesListFromMap(featureMap));
					}
				});
	}

	/**
	 * Build a list of features based on the hashmap.
	 *
	 * @param featureMap the hashmap
	 * @return a list of features
	 */
	protected List<Feature> buildFeaturesListFromMap(Map<FeaturesSupported, List<Feature>> featureMap) {
		List<Feature> allFeatures = new ArrayList<Feature>();
		for (FeaturesSupported layer : featureMap.keySet()) {
			List<Feature> features = featureMap.get(layer);

			if (features != null) {
				for (Feature f : features) {
					features.add(f);
				}

			}
		}
		return allFeatures;
	}

	private void fireFeatureClickedEvent(final Coordinate clickedPosition, final List<Feature> features) {
		mapPresenter.getEventBus().fireEvent(new FeaturesClickedEvent(clickedPosition, features));
	}

	/**
	 * Calculate the buffer for the given pixel tolerance.
	 *
	 * @return the buffer.
	 */
	protected double calculateBufferFromPixelTolerance() {
		Coordinate c1 = mapPresenter.getViewPort().getTransformationService()
				.transform(new Coordinate(0, 0), RenderSpace.SCREEN, RenderSpace.WORLD);
		Coordinate c2 = mapPresenter.getViewPort().getTransformationService()
				.transform(new Coordinate(pixelBuffer, 0), RenderSpace.SCREEN, RenderSpace.WORLD);
		return c1.distance(c2);
	}
}