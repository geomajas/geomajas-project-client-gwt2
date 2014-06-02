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

package org.geomajas.plugin.geocoder.client.widget;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.plugin.geocoder.client.event.GeocoderEvent;
import org.geomajas.plugin.geocoder.client.event.LocationNotFoundEvent;
import org.geomajas.plugin.geocoder.client.event.LocationNotFoundHandler;
import org.geomajas.plugin.geocoder.client.event.SelectAlternativeEvent;
import org.geomajas.plugin.geocoder.client.event.SelectAlternativeHandler;
import org.geomajas.plugin.geocoder.client.event.SelectLocationEvent;
import org.geomajas.plugin.geocoder.client.event.SelectLocationHandler;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringAlternative;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringRequest;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringResponse;

import java.util.List;

/**
 * Default implementation of {@link GeocoderWidgetPresenter} and {@link GeocoderWidgetAlternativesPresenter}.
 *
 * @author Pieter De Graef
 * @author Jan Venstermans
 */
public class GeocoderWidgetPresenterImpl implements GeocoderWidgetPresenter, GeocoderWidgetAlternativesPresenter,
		SelectLocationHandler, SelectAlternativeHandler, LocationNotFoundHandler {

	private MapPresenter mapPresenter;

	private GeocoderWidgetView geocoderWidgetView;

	private GeocoderWidgetAlternativesView alternativesView;

	private String servicePattern = ".*";

	private HandlerManager handlerManager;

	/**
	 * Create geocoder widget which allows searching a location from a string.
	 *
	 * @param mapPresenter map to apply search results
	 * @param geocoderWidgetView geocoder widget
	 */
	public GeocoderWidgetPresenterImpl(MapPresenter mapPresenter, GeocoderWidgetView geocoderWidgetView,
									   GeocoderWidgetAlternativesView alternativesView) {
		this.mapPresenter = mapPresenter;
		this.geocoderWidgetView = geocoderWidgetView;
		this.alternativesView = alternativesView;
		handlerManager = new HandlerManager(this);
		bind();
	}

	private void bind() {
		geocoderWidgetView.setPresenter(this);
		alternativesView.setPresenter(this);
		// set default handlers
		setSelectLocationHandler(this);
		setSelectAlternativeHandler(this);
		setLocationNotFoundHandler(this);
	}

	/* getters and setters */

	/**
	 * Get the regular expression which is used to select which geocoder services to use.
	 *
	 * @return geocoder selection regular expression
	 */
	public String getServicePattern() {
		return servicePattern;
	}

	/**
	 * Set the regular expression which is used to select which geocoder services to use.
	 *
	 * @param servicePattern geocoder selection regular expression
	 */
	public void setServicePattern(String servicePattern) {
		this.servicePattern = servicePattern;
	}

	/* location methods*/

	@Override
	public void findLocation(final String location) {
		showAlternativesView(false);
		GwtCommand command = new GwtCommand(GetLocationForStringRequest.COMMAND);
		GetLocationForStringRequest request = new GetLocationForStringRequest();
		request.setCrs(mapPresenter.getViewPort().getCrs());
		request.setLocation(location);
		request.setServicePattern(servicePattern);
		command.setCommandRequest(request);
		GeomajasServerExtension.getInstance().getCommandService().execute(command,
				new AbstractCommandCallback<GetLocationForStringResponse>() {
			public void execute(GetLocationForStringResponse response) {
				goToLocation(response, location);
			}
		});
	}

	@Override
	public void goToLocation(final GetLocationForStringResponse response, final String location) {
		if (response.isLocationFound()) {
			fireGeocoderEvent(new SelectLocationEvent(mapPresenter, response));
		} else {
			List<GetLocationForStringAlternative> alternatives = response.getAlternatives();
			if (null != alternatives && alternatives.size() > 0) {
				fireGeocoderEvent(new SelectAlternativeEvent(mapPresenter, alternatives));
			} else {
				fireGeocoderEvent(new LocationNotFoundEvent(location));
			}
		}
	}

	/* handler methods */

	@Override
	public void onSelectLocation(SelectLocationEvent event) {
		mapPresenter.getViewPort().applyBounds(event.getBbox());
		geocoderWidgetView.setValue(event.getCanonicalLocation());
	}

	@Override
	public void onSelectAlternative(SelectAlternativeEvent event) {
		alternativesView.setAlternatives(event.getAlternatives());
		showAlternativesView(true);
	}

	@Override
	public void onLocationNotFound(LocationNotFoundEvent event) {
		// if geocoderWidgetView is default view, there is also a method for displaying locationNotFoundMessage
		if (geocoderWidgetView instanceof GeocoderWidgetViewImpl) {
			((GeocoderWidgetViewImpl) geocoderWidgetView).showLocationNotFoundMessage(event.getLocation());
		}
	}

	/* Hanlder registration methods */

	/**
	 * Set the select alternative handler.
	 * <p/>
	 * There can only be one handler, the default displays the alternatives in a window on the map widget.
	 *
	 * @param handler select alternative handler
	 * @return handler registration.
	 */
	public HandlerRegistration setSelectAlternativeHandler(SelectAlternativeHandler handler) {
		if (handlerManager.getHandlerCount(SelectAlternativeHandler.TYPE) > 0) {
			SelectAlternativeHandler previous = handlerManager.getHandler(SelectAlternativeHandler.TYPE, 0);
			handlerManager.removeHandler(SelectAlternativeHandler.TYPE, previous);
		}
		return handlerManager.addHandler(SelectAlternativeHandler.TYPE, handler);
	}

	/**
	 * Set the select location handler.
	 * <p/>
	 * There can only be one handler, the default zooms the map widget to the selected location.
	 *
	 * @param handler select location handler
	 * @return handler registration.
	 */
	public HandlerRegistration setSelectLocationHandler(SelectLocationHandler handler) {
		if (handlerManager.getHandlerCount(SelectLocationHandler.TYPE) > 0) {
			SelectLocationHandler previous = handlerManager.getHandler(SelectLocationHandler.TYPE, 0);
			handlerManager.removeHandler(SelectLocationHandler.TYPE, previous);
		}
		return handlerManager.addHandler(SelectLocationHandler.TYPE, handler);
	}

	/**
	 * Set the {@link LocationNotFoundHandler} handler.
	 * <p/>
	 * There can only be one handler, the default shows a Js warning message.
	 *
	 * @param handler "no location found" handler
	 * @return handler registration.
	 */
	public HandlerRegistration setLocationNotFoundHandler(LocationNotFoundHandler handler) {
		if (handlerManager.getHandlerCount(LocationNotFoundHandler.TYPE) > 0) {
			LocationNotFoundHandler previous = handlerManager.getHandler(LocationNotFoundHandler.TYPE, 0);
			handlerManager.removeHandler(LocationNotFoundHandler.TYPE, previous);
		}
		return handlerManager.addHandler(LocationNotFoundHandler.TYPE, handler);
	}

	/* view handler methods: methods invoked by the view(s) */

	@Override
	public void onAlternativesViewClosed() {
		showAlternativesView(false);
	}

	public Bbox getAlternativesViewShowBbox() {
		// if geocoderWidgetView is default view, there is a method for getting the bbox of the widget
		if (geocoderWidgetView instanceof GeocoderWidgetViewImpl) {
			return ((GeocoderWidgetViewImpl) geocoderWidgetView).getWidgetViewBbox();
		}
		return null;
	}

	@Override
	public void fireGeocoderEvent(GeocoderEvent event) {
		handlerManager.fireEvent(event);
	}

	/* methods to change the view */

	/**
	 * Clear the current location.
	 */
	public void clearLocation() {
		geocoderWidgetView.setValue("");
	}

	/* private method */

	private void showAlternativesView(boolean show) {
		if (show) {
			alternativesView.setPosition(getAlternativesViewShowBbox());
			alternativesView.show();
		} else {
			alternativesView.hide();
		}
		geocoderWidgetView.alternativesViewIsShown(show);
	}
}