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
package org.geomajas.gwt2.widget.client.featureselectbox.presenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.feature.FeatureMapFunction;
import org.geomajas.gwt2.client.map.feature.ServerFeatureService;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;
import org.geomajas.gwt2.widget.client.featureselectbox.event.FeatureClickedEvent;
import org.geomajas.gwt2.widget.client.featureselectbox.view.FeatureSelectBoxView;

/**
 * Feature select box interface.
 *
 * @author Jan De Moerloose
 *
 */
public class FeatureSelectBoxPresenterImpl implements FeatureSelectBoxPresenter {

	private Logger log = Logger.getLogger(FeatureSelectBoxPresenterImpl.class.getName());

	private Coordinate clickedCoordinate;

	private FeatureSelectBoxView view;

	private MapPresenter mapPresenter;

	private Map<String, org.geomajas.gwt2.client.map.feature.Feature> clickedFeatures;

	private int pixelBuffer = 10;

	public FeatureSelectBoxPresenterImpl(FeatureSelectBoxView view) {
		this.view = view;
		clickedFeatures = new HashMap<String, Feature>();
	}

	public void setPixelBuffer(int pixelBuffer) {
		this.pixelBuffer = pixelBuffer;
	}

	@Override
	public void onFeatureSelected(String label) {
		view.hide();
		Feature clickedFeature = clickedFeatures.get(label);
		mapPresenter.getEventBus().fireEvent(new FeatureClickedEvent(clickedCoordinate, clickedFeature));
	}

	@Override
	public void onActivate(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
	}

	@Override
	public void onDeactivate() {
		view.hide();
	}

	@Override
	public void onClick(Coordinate worldCoordinate) {
		clickedCoordinate = worldCoordinate;
		Geometry point = new Geometry(Geometry.POINT, 0, -1);
		point.setCoordinates(new Coordinate[] { worldCoordinate });

		GeomajasServerExtension
				.getInstance()
				.getServerFeatureService()
				.search(mapPresenter, point, calculateBufferFromPixelTolerance(),
						ServerFeatureService.QueryType.INTERSECTS,
						ServerFeatureService.SearchLayerType.SEARCH_ALL_LAYERS, -1, new FeatureMapFunction() {

							@Override
							public void execute(Map<FeaturesSupported, List<Feature>> featureMap) {
								getData(featureMap);

							}
						});

	}

	protected MapPresenter getMapPresenter() {
		return mapPresenter;
	}

	private double calculateBufferFromPixelTolerance() {
		Coordinate c1 = mapPresenter.getViewPort().getTransformationService()
				.transform(new Coordinate(0, 0), RenderSpace.SCREEN, RenderSpace.WORLD);
		Coordinate c2 = mapPresenter.getViewPort().getTransformationService()
				.transform(new Coordinate(pixelBuffer, 0), RenderSpace.SCREEN, RenderSpace.WORLD);
		return c1.distance(c2);
	}

	private void getData(Map<FeaturesSupported, List<Feature>> featureMap) {
		clickedFeatures.clear(); // clearLabels all stored features

		for (FeaturesSupported layer : featureMap.keySet()) {
			List<org.geomajas.gwt2.client.map.feature.Feature> features = featureMap.get(layer);

			if (features != null) {
				for (org.geomajas.gwt2.client.map.feature.Feature f : features) {
					log.info("Feature label =" + f.getLabel());
					log.info("Feature id =" + f.getId());

					// store features in a hashmap
					clickedFeatures.put(f.getLabel(), f);
				}

			}
		}

		showFeatureData();
	}

	private void showFeatureData() {
		// when there is more than one feature in the buffered area
		if (clickedFeatures.size() >= 2) {
			view.clearLabels();

			for (org.geomajas.gwt2.client.map.feature.Feature f : clickedFeatures.values()) {
				view.addLabel(f.getLabel());
			}

			view.show(false);
		} else if (clickedFeatures.size() == 1) {
			Feature clickedFeature = (Feature) clickedFeatures.values().toArray()[0];

			mapPresenter.getEventBus().fireEvent(new FeatureClickedEvent(clickedCoordinate, clickedFeature));
		}
	}

}
