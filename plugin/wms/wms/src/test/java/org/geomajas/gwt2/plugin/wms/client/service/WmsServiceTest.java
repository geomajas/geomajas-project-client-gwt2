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

package org.geomajas.gwt2.plugin.wms.client.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gwt.core.client.Callback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.impl.XMLParserImpl;
import com.google.gwtmockito.GwtMockitoTestRunner;

import net.opengis.wms.v_1_3_0.BoundingBox;
import net.opengis.wms.v_1_3_0.Layer;
import net.opengis.wms.v_1_3_0.Style;
import net.opengis.wms.v_1_3_0.WMSCapabilities;

import org.apache.commons.io.IOUtils;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsGetCapabilitiesInfo;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsLayerInfo;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsLayerStyleInfo;
import org.geomajas.gwt2.plugin.wms.client.service.WmsService.WmsVersion;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import junit.framework.Assert;

/**
 * Testcase for the {@link WmsService} interface.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
@RunWith(GwtMockitoTestRunner.class)
public class WmsServiceTest {

	@Mock
	RequestBuilderFactory requestBuilderFactory;

	@Mock
	RequestBuilder requestBuilder;

	@Before
	public void init() throws Exception {
		when(requestBuilderFactory.create(any(Method.class), anyString())).thenReturn(requestBuilder);
		when(XMLParserImpl.getInstance().parse(anyString())).thenAnswer(new Answer<Document>() {

			@Override
			public Document answer(InvocationOnMock invocation) throws Throwable {
				return new MockDocument((String) invocation.getArguments()[0]);
			}
		});
	}

	@Test
	public void testGetCapabilities() throws Exception {
		String response = IOUtils.toString(this.getClass().getResourceAsStream("capabilities_1_3_0.xml"), "UTF-8");
		prepareRequestBuilder(requestBuilder, response, 200);
		WmsServiceImpl wmsService = new WmsServiceImpl();
		wmsService.setRequestBuilderFactory(requestBuilderFactory);
		CaptureCallback callback = new CaptureCallback();
		wmsService.getCapabilities("http://test", WmsVersion.V1_3_0, callback);
		WmsGetCapabilitiesInfo info = callback.getResult();
		Assert.assertNotNull(info);
		Assert.assertEquals(8, info.getLayers().size());
		Assert.assertEquals(3, info.getRequests().size());

		// unmarshal with jaxb to compare
		JAXBContext context = JAXBContext.newInstance("net.opengis.wms.v_1_3_0");
		// Use the created JAXB context to construct an unmarshaller
		Unmarshaller unmarshaller = context.createUnmarshaller();
		// Unmarshal the given URL, retrieve WMSCapabilities element
		WMSCapabilities wmsCapabilities = (WMSCapabilities) unmarshaller.unmarshal(new InputSource(new StringReader(
				response)));

		// flatten layers
		List<Layer> ll = new ArrayList<Layer>();
		addRecursive(ll, wmsCapabilities.getCapability().getLayer());

		// Iterate over layers, check all properties
		for (int i = 0; i < ll.size(); i++) {
			Layer l = ll.get(i);
			WmsLayerInfo layerInfo = info.getLayers().get(i);
			Assert.assertEquals(l.getName(), layerInfo.getName());
			Assert.assertEquals(l.getAbstract(), layerInfo.getAbstract());
			Assert.assertEquals(l.getTitle(), layerInfo.getTitle());
			Assert.assertEquals(l.getMaxScaleDenominator(), layerInfo.getMaxScaleDenominator());
			Assert.assertEquals(l.getMinScaleDenominator(), layerInfo.getMinScaleDenominator());
			Assert.assertEquals(l.isQueryable(), layerInfo.isQueryable());
			for (int j = 0; j < layerInfo.getStyleInfo().size(); j++) {
				WmsLayerStyleInfo style = layerInfo.getStyleInfo().get(i);
				Style s = l.getStyle().get(j);
				Assert.assertEquals(s.getName(), style.getName());
				Assert.assertEquals(s.getTitle(), style.getTitle());
				Assert.assertEquals(s.getAbstract(), style.getAbstract());
				Assert.assertEquals(s.getLegendURL().get(0).getFormat(), style.getLegendUrl().getFormat());
			}
			Assert.assertTrue(BboxService.equals(toBbox(l.getBoundingBox().get(0)), layerInfo.getBoundingBox(), 0.0001));
		}
		
		

	}

	private Bbox toBbox(BoundingBox boundingBox) {
		return new Bbox(boundingBox.getMinx(), boundingBox.getMiny(), boundingBox.getMaxx() - boundingBox.getMinx(),
				boundingBox.getMaxy() - boundingBox.getMiny());
	}

	private void addRecursive(List<Layer> layers, Layer layer) {
		if (layer.getName() != null) {
			layers.add(layer);
			for (Layer child : layer.getLayer()) {
				addRecursive(layers, child);
			}
		}

	}

	private void prepareRequestBuilder(RequestBuilder requestBuilder, String responseText, int statusCode)
			throws Exception {
		final Response response = mock(Response.class);
		when(response.getStatusCode()).thenReturn(statusCode);
		when(response.getText()).thenReturn(responseText);

		when(requestBuilder.sendRequest(anyString(), any(RequestCallback.class))).thenAnswer(new Answer<Request>() {

			@Override
			public Request answer(InvocationOnMock invocation) throws Throwable {
				((RequestCallback) invocation.getArguments()[1]).onResponseReceived(null, response);
				return null;
			}
		});
	}

	private final class CaptureCallback implements Callback<WmsGetCapabilitiesInfo, String> {

		WmsGetCapabilitiesInfo result;

		private String reason;

		@Override
		public void onSuccess(WmsGetCapabilitiesInfo result) {
			this.result = result;
		}

		@Override
		public void onFailure(String reason) {
			this.reason = reason;
		}

		public WmsGetCapabilitiesInfo getResult() {
			return result;
		}

		public String getReason() {
			return reason;
		}

	}

}
