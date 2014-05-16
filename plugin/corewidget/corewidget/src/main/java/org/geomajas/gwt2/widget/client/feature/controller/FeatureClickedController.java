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

package org.geomajas.gwt2.widget.client.feature.controller;

import java.util.List;
import java.util.Map;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.feature.FeatureMapFunction;
import org.geomajas.gwt2.client.map.feature.ServerFeatureService.QueryType;
import org.geomajas.gwt2.client.map.feature.ServerFeatureService.SearchLayerType;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;
import org.geomajas.gwt2.widget.client.feature.event.FeatureClickedEvent;
import org.geomajas.gwt2.widget.client.itemselect.ItemSelectView;
import org.geomajas.gwt2.widget.client.itemselect.ItemSelectWidget;

import com.google.gwt.event.dom.client.MouseEvent;

/**
 * Controller that provides a feature based on a location clicked on the map. When multiple features
 * are found on the location, a drop down box is presented from where a single feature can be selected.
 * The controller fires {@link org.geomajas.gwt2.widget.client.event.event.FeatureClickedEvent}s.
 *
 * @author Dosi Bingov
 * @author Oliver May
 * @since 2.1.0
 *
 */
@Api(allMethods = true)
public class FeatureClickedController extends FeaturesClickedController implements ItemSelectView
		.ItemSelectHandler<Feature> {

	private ItemSelectView<Feature> itemSelectView;

	private Coordinate clickLocation;

	/**
	 * Construct a feature clicked controller.
	 */
	public FeatureClickedController() {
		this(new ItemSelectWidget<Feature>());
	}

	/**
	 * Construct a feature clicked controller with a selection of features.
	 * 
	 * @param itemSelectView
	 */
	public FeatureClickedController(ItemSelectView<Feature> itemSelectView) {
		super();
		this.itemSelectView = itemSelectView;
		this.itemSelectView.addItemSelectHandler(this);
	}

	protected void searchAtLocation(MouseEvent event) {
		final Coordinate clickedPosition = getLocation(event, RenderSpace.SCREEN);
		clickLocation = getLocation(event, RenderSpace.WORLD);

		Geometry point = new Geometry(Geometry.POINT, 0, -1);
		point.setCoordinates(new Coordinate[] { clickLocation });

		GeomajasServerExtension.getInstance().getServerFeatureService().search(mapPresenter, point,
				calculateBufferFromPixelTolerance(), QueryType.INTERSECTS, SearchLayerType.SEARCH_ALL_LAYERS, -1,
				new FeatureMapFunction() {

					@Override
					public void execute(Map<FeaturesSupported, List<Feature>> featureMap) {
						itemSelectView.setItems(buildFeaturesListFromMap(featureMap));
						itemSelectView.show((int) clickedPosition.getX(), (int) clickedPosition.getY());
					}
				});
	}

	@Override
	public void itemSelected(Feature item) {
		mapPresenter.getEventBus().fireEvent(new FeatureClickedEvent(clickLocation, item));
	}
}