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

package org.geomajas.gwt2.client.controller;

import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.user.client.Timer;
import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.feature.FeatureMapFunction;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;
import org.geomajas.gwt2.client.map.layer.ServerLayer;
import org.geomajas.gwt2.client.map.layer.VectorServerLayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller that provides a feature based on a location clicked on the map. When multiple features are found on the
 * location, a drop down box is presented from where a single feature can be selected. The controller fires
 * {@link org.geomajas.gwt2.client.event.FeatureClickedEvent}s.
 *
 * @author Dosi Bingov
 * @author David Debuck
 * @since 2.1.0
 *
 */
@Api(allMethods = true)
public class FeatureMouseOverListener extends AbstractMapController {

	private static Logger logger = Logger.getLogger("");

	private Map<String, Feature> clickedFeatures = new HashMap<String, Feature>();

	private int pixelBuffer = 10;

	private static final int TIMER_DELAY = 500; // 0.5s

	private Timer timer;

	VectorServerLayer temp;

	protected Coordinate clickedPosition;

	/**
	 * Default constructor.
	 */
	public FeatureMouseOverListener() {
		super(true);
	}

	/**
	 * Constructor with parameter for the buffer.
	 *
	 * @param pixelBuffer buffer in pixels.
	 */
	public FeatureMouseOverListener(int pixelBuffer) {
		super(true);
		this.pixelBuffer = pixelBuffer;
	}

	@Override
	public void onActivate(MapPresenter mapPresenter) {
		super.onActivate(mapPresenter);
		this.mapPresenter = mapPresenter;
	}

	@Override
	public void onDeactivate(MapPresenter mapPresenter) {
		super.onDeactivate(mapPresenter);
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {

		clickedPosition = getLocation(event, RenderSpace.WORLD);

		// place new tooltip after some time
		if (timer == null) {
			timer = new Timer() {

				public void run() {

					String crs = mapPresenter.getViewPort().getCrs();

					Geometry point = new Geometry(Geometry.POINT, 0, -1);
					point.setCoordinates(new Coordinate[] { clickedPosition });

					int index = mapPresenter.getLayersModel().getLayerCount();



					for (int i = 0; i < index; i++) {
						org.geomajas.gwt2.client.map.layer.Layer layer = mapPresenter.getLayersModel().getLayer(i);

						if (layer.isShowing() && layer instanceof FeaturesSupported && layer instanceof ServerLayer) {

							temp = (VectorServerLayer) layer;

						}
					}

					GeomajasServerExtension
							.getInstance()
							.getServerFeatureService()
							.search(crs, temp , point,
									calculateBufferFromPixelTolerance(), new SelectionCallback()
							);


				}
			};
			timer.schedule(TIMER_DELAY);

		} else {
			timer.cancel();
			timer.schedule(TIMER_DELAY);
		}

	}

	/**
	 * Callback for feature searches.
	 *
	 * @author David Debuck
	 */
	private class SelectionCallback implements FeatureMapFunction {

		/**
		 * Default constructor.
		 */
		public SelectionCallback() {
			//
		}

		@Override
		public void execute(Map<FeaturesSupported, List<Feature>> featureMap) {

			clickedFeatures.clear();

			for (FeaturesSupported layer : featureMap.keySet()) {
				List<Feature> features = featureMap.get(layer);

				if (features != null) {
					for (Feature f : features) {

						logger.log(Level.INFO, "Feature: " + f.getLabel());

						clickedFeatures.put(f.getLabel(), f);
					}

				}
			}

			//mapPresenter.getEventBus().fireEvent(
					//new FeatureClickedEvent(clickedPosition, new ArrayList<Feature>(clickedFeatures.values())));

		}

	}

	/**
	 * Calculate a buffer in which the listener may include the features from the map.
	 *
	 * @return double buffer
	 */
	private double calculateBufferFromPixelTolerance() {

		Coordinate c1 = mapPresenter.getViewPort().getTransformationService()
				.transform(new Coordinate(0, 0), RenderSpace.SCREEN, RenderSpace.WORLD);
		Coordinate c2 = mapPresenter.getViewPort().getTransformationService()
				.transform(new Coordinate(pixelBuffer, 0), RenderSpace.SCREEN, RenderSpace.WORLD);
		return c1.distance(c2);

	}

}