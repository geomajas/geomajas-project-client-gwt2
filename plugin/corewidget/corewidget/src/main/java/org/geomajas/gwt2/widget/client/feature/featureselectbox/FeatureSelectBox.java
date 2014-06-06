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
package org.geomajas.gwt2.widget.client.feature.featureselectbox;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.widget.client.CoreWidget;
import org.geomajas.gwt2.widget.client.feature.featureselectbox.resource.FeatureSelectBoxResource;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Tool tip box that displays a list of features from where one can be selected.
 * 
 * @author Dosi Bingov
 * @author Oliver May
 * @author Jan De Moerloose
 * 
 */
public class FeatureSelectBox implements IsWidget {

	private FeatureSelectBoxPresenter presenter;

	private FeatureSelectBoxView view;

	protected Coordinate clickedPosition;

	/**
	 * Default constructor.
	 */
	public FeatureSelectBox() {
		this(CoreWidget.getInstance().getClientBundleFactory().createFeatureSelectBoxResource());
	}

	/**
	 * Default constructor.
	 */
	public FeatureSelectBox(FeatureSelectBoxResource resource) {
		view = CoreWidget.getInstance().getViewFactory().createFeatureSelectBox(resource);
		presenter = new FeatureSelectBoxPresenterImpl(view);
		view.setPresenter(presenter);
	}

	public void onActivate(MapPresenter mapPresenter) {
		presenter.onActivate(mapPresenter);
	}

	public void onDeactivate() {
		presenter.onDeactivate();
	}

	public void onClick(int x, int y, Coordinate location) {
		presenter.onClick(x, y, location);
	}

	public void setSingleFeature(boolean singleFeature) {
		presenter.setSingleFeature(singleFeature);
	}

	@Override
	public Widget asWidget() {
		return view.asWidget();
	}
}
