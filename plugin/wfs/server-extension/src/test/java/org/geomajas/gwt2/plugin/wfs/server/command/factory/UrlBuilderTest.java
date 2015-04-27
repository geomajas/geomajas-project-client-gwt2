package org.geomajas.gwt2.plugin.wfs.server.command.factory;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import junit.framework.Assert;

import org.geomajas.gwt2.plugin.wfs.server.dto.WfsVersionDto;
import org.junit.Test;

public class UrlBuilderTest {

	@Test
	public void testUserInfo() throws MalformedURLException, URISyntaxException {
		URL url = URLBuilder
		        .createWfsURL(
		                new URL(
		                        "http://dov-bodemverkenner:depattatenzijnzocht@msb-on-101.mmis.be:9880/"
		                        + "raadpleegdienstenmercatorintern/wfs"),
		                WfsVersionDto.V1_0_0, "GetCapabilities");
		Assert.assertEquals(
		        "http://dov-bodemverkenner:depattatenzijnzocht@msb-on-101.mmis.be:9880/"
		        + "raadpleegdienstenmercatorintern/wfs?service=WFS&version=1.0.0&request=GetCapabilities",
		        url.toExternalForm());
	}
}
