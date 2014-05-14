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
package org.geomajas.gwt2.widget.client.featureselectbox;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.controller.AbstractMapController;
import org.geomajas.gwt2.client.controller.MapController;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.widget.client.CoreWidget;
import org.geomajas.gwt2.widget.client.featureselectbox.presenter.FeatureSelectBoxPresenter;
import org.geomajas.gwt2.widget.client.featureselectbox.presenter.FeatureSelectBoxPresenterImpl;
import org.geomajas.gwt2.widget.client.featureselectbox.view.FeatureSelectBoxView;

import java.util.logging.Logger;

/**
 * Tool tip box that displays a list of features from where one can be selected.
 * 
 * @author Dosi Bingov
 * @author Oliver May
 * @since 2.0.0
 */
@Api(allMethods = true)
public class FeatureSelectListener extends AbstractMapController implements MapController, IsWidget {

	private Logger log = Logger.getLogger(FeatureSelectListener.class.getName());

	private FeatureSelectBoxPresenter presenter;

	private FeatureSelectBoxView view;

	private static final int MIN_PIXEL_DISTANCE = 120;

	protected Coordinate clickedPosition;

	/**
	 * Default constructor.
	 */
	public FeatureSelectListener() {
		super(false);
		view = CoreWidget.getInstance().getFeatureSelectBoxViewFactory().create();
		presenter = new FeatureSelectBoxPresenterImpl(view);
		view.setPresenter(presenter);
	}

	@Override
	public void onActivate(MapPresenter mapPresenter) {
		super.onActivate(mapPresenter);
		presenter.onActivate(mapPresenter);
	}

	@Override
	public void onDeactivate(MapPresenter mapPresenter) {
		super.onDeactivate(mapPresenter);
		presenter.onDeactivate();
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		if (view.isVisible() && clickedPosition != null) {
			if (getLocation(event, RenderSpace.SCREEN).distance(clickedPosition) > MIN_PIXEL_DISTANCE) {
				view.hide();
			}
		}
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		log.info("FeatureSelectedListener => mouse down fired");
		view.hide();
		view.setShowPosition(event.getClientX(), event.getClientY());
		clickedPosition = getLocation(event, RenderSpace.SCREEN);
		presenter.onClick(getLocation(event, RenderSpace.WORLD));
	}

	@Override
	public Widget asWidget() {
		return view.asWidget();
	}
}
