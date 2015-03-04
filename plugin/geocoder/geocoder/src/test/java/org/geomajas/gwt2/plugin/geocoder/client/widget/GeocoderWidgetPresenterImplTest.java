/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.plugin.geocoder.client.widget;

import com.google.gwtmockito.GwtMock;
import com.google.gwtmockito.GwtMockitoTestRunner;
import junit.framework.Assert;
import org.geomajas.configuration.client.ClientUserDataInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.service.CommandService;
import org.geomajas.gwt2.plugin.geocoder.client.event.LocationNotFoundEvent;
import org.geomajas.gwt2.plugin.geocoder.client.event.LocationNotFoundHandler;
import org.geomajas.gwt2.plugin.geocoder.client.event.SelectAlternativeEvent;
import org.geomajas.gwt2.plugin.geocoder.client.event.SelectAlternativeHandler;
import org.geomajas.gwt2.plugin.geocoder.client.event.SelectLocationEvent;
import org.geomajas.gwt2.plugin.geocoder.client.event.SelectLocationHandler;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringAlternative;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringRequest;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

/**
 * Widget for starting a geocoder location search.
 *
 * @author Jan Venstermans
 */
@RunWith(GwtMockitoTestRunner.class) // for the final class GeomajasServerExtension
public class GeocoderWidgetPresenterImplTest {

	private GeocoderWidgetPresenterImpl presenter;

	@Mock
	private MapPresenter mapPresenterMock;

	@Mock
	private GeocoderWidgetView geocoderWidgetViewMock;

	// used for impl specific treatment of LocationNotFoundEvent
	@GwtMock
	private GeocoderWidgetViewImpl geocoderWidgetViewImplMock;

	@Mock
	private GeocoderWidgetAlternativesView alternativesViewMock;

	@GwtMock   // used because GeomajasServerExtension is final
	private GeomajasServerExtension serverExtensionMock;

	@Mock
	private CommandService commandServiceMock;

	@Mock
	private ClientUserDataInfo infoMock;

	@Mock
	private ViewPort viewPortMock;

	@Mock
	private List<GetLocationForStringAlternative> alternativeListMock;

	// response dummy data
	private String location = "location";
	private String canonicalLocation = "canonicalLocation";
	private String geocoderName = "geocoderName";
	private Coordinate center = new Coordinate(2, 3);
	private Bbox bbox = new Bbox(0, 0, 5, 8);

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		presenter = new GeocoderWidgetPresenterImpl(mapPresenterMock, geocoderWidgetViewMock, alternativesViewMock);
		reset(mapPresenterMock);
		reset(geocoderWidgetViewMock);
		reset(alternativesViewMock);
		stub(serverExtensionMock.getCommandService()).toReturn(commandServiceMock);
		stub(mapPresenterMock.getViewPort()).toReturn(viewPortMock);
		GeomajasServerExtension.setInstance(serverExtensionMock);
	}

	@Test
	public void setHandlersToViewsOnConstructionTest() {
		presenter = new GeocoderWidgetPresenterImpl(mapPresenterMock, geocoderWidgetViewMock, alternativesViewMock);
		verify(geocoderWidgetViewMock).setPresenter(presenter);
		verify(alternativesViewMock).setPresenter(presenter);
	}

	@Test
	public void registerGeocoderEventHandlersOnConstruction() {
		// after construction, the default geocoder event handler should be the presenter itself
		presenter.fireGeocoderEvent(new SelectLocationEvent(mapPresenterMock, createGetLocationForStringResponse()));
		presenter.fireGeocoderEvent(new SelectAlternativeEvent(mapPresenterMock, alternativeListMock));

		verifyPresentationAsSelectLocationHandlerExecuted();
		verifyPresentationAsSelectAlternativeHandlerExecuted();

		// impl specific treatment of LocationNotFoundEvent
		presenter = new GeocoderWidgetPresenterImpl(mapPresenterMock, geocoderWidgetViewImplMock, alternativesViewMock);
		presenter.fireGeocoderEvent(new LocationNotFoundEvent(location));
		verifyPresentationImplAsLocationNotFoundHandlerExecuted();
	}

	@Test
	public void findLocationTest() {
		String crs = "crs";
		stub(viewPortMock.getCrs()).toReturn(crs);
		String servicePattern = "servicePattern";
		presenter.setServicePattern(servicePattern);

		presenter.findLocation(location);

		ArgumentCaptor<GwtCommand> commandCaptor = ArgumentCaptor.forClass(GwtCommand.class);
		ArgumentCaptor<CommandCallback> callbackCaptor = ArgumentCaptor.forClass(CommandCallback.class);
		verify(commandServiceMock).execute(commandCaptor.capture(), callbackCaptor.capture());
		// assert command
		GwtCommand command = commandCaptor.getValue();
		Assert.assertEquals(GetLocationForStringRequest.COMMAND, command.getCommandName());
		Assert.assertTrue(command.getCommandRequest() instanceof GetLocationForStringRequest);
		GetLocationForStringRequest request = (GetLocationForStringRequest) command.getCommandRequest();
		Assert.assertEquals(crs, request.getCrs());
		Assert.assertEquals(location, request.getLocation());
		Assert.assertEquals(servicePattern, request.getServicePattern());
		// assert callback
		//callbackCaptor.getValue().execute();
	}

	@Test
	public void goToLocationOneLocationFoundTest() {
		SelectLocationHandler selectLocationHandlerMock = mock(SelectLocationHandler.class);
		presenter.setSelectLocationHandler(selectLocationHandlerMock);
		GetLocationForStringResponse response = createGetLocationForStringResponse();
		response.setLocationFound(true);

		presenter.goToLocation(response, location);

		ArgumentCaptor<SelectLocationEvent> selectLocationEventArgumentCaptor = ArgumentCaptor.forClass(SelectLocationEvent.class);
		verify(selectLocationHandlerMock).onSelectLocation(selectLocationEventArgumentCaptor.capture());
		SelectLocationEvent event = selectLocationEventArgumentCaptor.getValue();
		Assert.assertEquals(response.getCanonicalLocation(), event.getCanonicalLocation());
		Assert.assertEquals(response.getCenter(), event.getCenter());
		Assert.assertEquals(response.getBbox(), event.getBbox());
		Assert.assertEquals(response.getGeocoderName(), event.getGeocoderName());
		Assert.assertEquals(response.getUserData(), event.getUserData());
		Assert.assertEquals(mapPresenterMock, event.getMapWidget());
	}

	@Test
	public void goToLocationAlternativeLocationsFoundTest() {
		SelectAlternativeHandler selectAlternativeHandlerMock = mock(SelectAlternativeHandler.class);
		stub(alternativeListMock.size()).toReturn(2);
		presenter.setSelectAlternativeHandler(selectAlternativeHandlerMock);
		GetLocationForStringResponse response = createGetLocationForStringResponse();
		response.setLocationFound(false);
		response.setAlternatives(alternativeListMock);

		presenter.goToLocation(response, location);

		ArgumentCaptor<SelectAlternativeEvent> selectAlternativeEventArgumentCaptor = ArgumentCaptor.forClass(SelectAlternativeEvent.class);
		verify(selectAlternativeHandlerMock).onSelectAlternative(selectAlternativeEventArgumentCaptor.capture());
		SelectAlternativeEvent event = selectAlternativeEventArgumentCaptor.getValue();
		Assert.assertEquals(alternativeListMock, event.getAlternatives());
		Assert.assertEquals(mapPresenterMock, event.getMapWidget());
	}

	@Test
	public void goToLocationNoLocationsFoundTest() {
		LocationNotFoundHandler locationNotFoundHandler = mock(LocationNotFoundHandler.class);
		presenter.setLocationNotFoundHandler(locationNotFoundHandler);
		GetLocationForStringResponse response = createGetLocationForStringResponse();
		response.setLocationFound(false);
		response.setAlternatives(null);

		presenter.goToLocation(response, location);

		ArgumentCaptor<LocationNotFoundEvent> locationNotFoundEventArgumentCaptor = ArgumentCaptor.forClass(LocationNotFoundEvent.class);
		verify(locationNotFoundHandler).onLocationNotFound(locationNotFoundEventArgumentCaptor.capture());
		LocationNotFoundEvent event = locationNotFoundEventArgumentCaptor.getValue();
		Assert.assertEquals(location, event.getLocation());
	}

	@Test
	public void presentationAsLocationNotFoundHandlerTest(){
		presenter = new GeocoderWidgetPresenterImpl(mapPresenterMock, geocoderWidgetViewImplMock, alternativesViewMock);
		LocationNotFoundEvent event = new LocationNotFoundEvent(location);

		((LocationNotFoundHandler) presenter).onLocationNotFound(event);

		verifyPresentationImplAsLocationNotFoundHandlerExecuted();
	}

	@Test
	public void presentationAsSelectAlternativeHandlerTest(){
		stub(alternativeListMock.size()).toReturn(2);
		SelectAlternativeEvent event = new SelectAlternativeEvent(mapPresenterMock, alternativeListMock);

		((SelectAlternativeHandler) presenter).onSelectAlternative(event);

		verifyPresentationAsSelectAlternativeHandlerExecuted();
	}

	@Test
	public void presentationAsSelectLocationHandlerTest(){
		SelectLocationEvent event = new SelectLocationEvent(mapPresenterMock, createGetLocationForStringResponse());

		((SelectLocationHandler) presenter).onSelectLocation(event);

		verifyPresentationAsSelectLocationHandlerExecuted();
	}

	private GetLocationForStringResponse createGetLocationForStringResponse() {
		GetLocationForStringResponse response = new GetLocationForStringResponse();
		response.setCanonicalLocation(canonicalLocation);
		response.setCenter(center);
		response.setBbox(bbox);
		response.setGeocoderName(geocoderName);
		response.setUserData(infoMock);
		return response;
	}

	private void verifyPresentationAsSelectLocationHandlerExecuted() {
		verify(viewPortMock).applyBounds(bbox);
		verify(geocoderWidgetViewMock).setValue(canonicalLocation);
	}

	private void verifyPresentationAsSelectAlternativeHandlerExecuted() {
		verify(alternativesViewMock).setAlternatives(alternativeListMock);
		verify(alternativesViewMock).show();
		verify(geocoderWidgetViewMock).alternativesViewIsShown(true);
	}

	private void verifyPresentationImplAsLocationNotFoundHandlerExecuted() {
		verify(geocoderWidgetViewImplMock).showLocationNotFoundMessage(location);
	}
}