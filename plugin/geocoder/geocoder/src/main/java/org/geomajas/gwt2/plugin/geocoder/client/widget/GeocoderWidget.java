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

package org.geomajas.gwt2.plugin.geocoder.client.widget;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.plugin.geocoder.client.Geocoder;

/**
 * Widget that enables searching for a location based on a String.
 * There are two view elements: the default view (the main widget) contains a text field to type the location;
 * the second view shows the alternative answers in case a String can refer to more than one location.
 *
 * @author Jan Venstermans
 * @since 2.1.0
 */
@Api(allMethods = true)
public class GeocoderWidget implements IsWidget {

	private GeocoderWidgetView geocoderWidgetView;

	private GeocoderWidgetPresenter presenter;

	/**
	 * Default constructor. The {@link org.geomajas.gwt2.client.map.MapPresenter} of the map
	 * must be provided.
	 *
	 * @param mapPresenter
	 */
	public GeocoderWidget(MapPresenter mapPresenter) {
		this(mapPresenter, Geocoder.getInstance().getViewFactory().createGeocoderWidgetView(),
				Geocoder.getInstance().getViewFactory().createGeocoderWidgetAlternativesView());
	}

	/**
	 *  Constructor that enables adding default view. Two views must be provided:<br/>
	 *   the geocoderView is the view that enables typing the location to be searched;<br/>
	 *   the alternativesView is the view that shows a list of locations in case more than
	 *    one location was found.
	 *
	 * @param mapPresenter
	 * @param geocoderView view that enables typing the location to be searched
	 * @param alternativesView view that shows a list of locations in case of multiple result
	 */
	public GeocoderWidget(MapPresenter mapPresenter, GeocoderWidgetView geocoderView,
						  GeocoderWidgetAlternativesView alternativesView) {
		this.geocoderWidgetView = geocoderView;
		presenter = new GeocoderWidgetPresenterImpl(mapPresenter, geocoderWidgetView, alternativesView);
	}

	/**
	 * Returns the {@link Widget} object of the {@link GeocoderWidget}.
	 * @return widget
	 */
	public Widget asWidget() {
		return geocoderWidgetView.asWidget();
	}

	/**
	 * Returns the presenter of the widget.
	 * @return the presenter
	 */
	public GeocoderWidgetPresenter getPresenter() {
		return presenter;
	}
}