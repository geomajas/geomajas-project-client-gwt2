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
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.impl.XMLParserImpl;
import com.google.gwtmockito.GwtMockitoTestRunner;

import net.opengis.wms.v_1_3_0.BoundingBox;
import net.opengis.wms.v_1_3_0.Layer;
import net.opengis.wms.v_1_3_0.Style;
import net.opengis.wms.v_1_3_0.WMSCapabilities;

import org.apache.commons.io.IOUtils;
import org.apache.xerces.parsers.DOMParser;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsGetCapabilitiesInfo;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsLayerInfo;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsLayerStyleInfo;
import org.geomajas.gwt2.plugin.wms.client.layer.WmsLayerConfiguration;
import org.geomajas.gwt2.plugin.wms.client.service.WmsService.WmsRequest;
import org.geomajas.gwt2.plugin.wms.client.service.WmsService.WmsUrlTransformer;
import org.geomajas.gwt2.plugin.wms.client.service.WmsService.WmsVersion;
import org.geomajas.gwt2.plugin.wms.client.service.WmsServiceImpl.CoordinateFormatter;
import org.geomajas.gwt2.plugin.wms.client.service.WmsServiceImpl.RequestBuilderFactory;
import org.geomajas.gwt2.plugin.wms.client.service.WmsServiceImpl.UrlEncoder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.DecimalFormat;
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

	private static final String VALUE_URL = "http://www.geomajas.org/";

	private static final String VALUE_LAYER = "someLayer";

	private static final String VALUE_STYLE = "someStyle";

	private static final String VALUE_CRS = "EPSG:4326";

	private static final String VALUE_CRS2 = "EPSG:31370";

	private static final int VALUE_SIZE = 342;

	private static final String HELLOWORLD = "Hello World";

	private WmsLayerConfiguration wmsConfig;

	private WmsUrlTransformer toHelloWorld;

	@Mock
	RequestBuilder requestBuilder;

	WmsServiceImpl wmsService;

	private String capabilitiesXml;

	private Document capsdoc;

	@Before
	public void init() throws Exception {
		wmsService = new WmsServiceImpl();
		wmsService.setRequestBuilderFactory(new RequestBuilderFactory() {

			@Override
			public RequestBuilder create(RequestBuilder.Method method, String url) {
				return requestBuilder;
			}
		});
		wmsService.setCoordinateFormatter(new CoordinateFormatter() {

			@Override
			public String format(double number) {
				return new DecimalFormat("#0.0#").format(number);
			}
		});
		wmsService.setUrlEncoder(new UrlEncoder() {

			@Override
			public String encodeUrl(String url) {
				return url;
			}
		});

		capabilitiesXml = IOUtils.toString(this.getClass().getResourceAsStream("capabilities_1_3_0.xml"), "UTF-8");
		DOMParser parser = new DOMParser();
		parser.parse(new InputSource(new StringReader(capabilitiesXml)));
		capsdoc = w3cToGoogle(parser.getDocument());
		when(XMLParserImpl.getInstance().parse(anyString())).thenAnswer(new Answer<Document>() {

			@Override
			public Document answer(InvocationOnMock invocation) throws Throwable {
				return capsdoc;
			}
		});
		prepareRequestBuilder(requestBuilder, capabilitiesXml, 200);
		toHelloWorld = new WmsUrlTransformer() {

			public String transform(WmsRequest request, String url) {
				return HELLOWORLD;
			}
		};
		wmsConfig = new WmsLayerConfiguration();
		wmsConfig.setBaseUrl(VALUE_URL);
		wmsConfig.setLayers(VALUE_LAYER);
		wmsConfig.setStyles(VALUE_STYLE);
		wmsConfig.setCrs(VALUE_CRS2);
	}

	@Test
	public void testGetCapabilities() throws Exception {
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
				capabilitiesXml)));

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

	@Test
	public void testGetMapUrl() {
		Bbox bounds = new Bbox(0, 1, 100, 100);
		String getMapUrl = wmsService.getMapUrl(wmsConfig, bounds, VALUE_SIZE, VALUE_SIZE);

		Assert.assertEquals(VALUE_URL, getMapUrl.substring(0, getMapUrl.indexOf('?')));
		Assert.assertTrue(hasParameter(getMapUrl, "service", "WMS"));
		Assert.assertTrue(hasParameter(getMapUrl, "layers", wmsConfig.getLayers()));
		Assert.assertTrue(hasParameter(getMapUrl, "width", VALUE_SIZE + ""));
		Assert.assertTrue(hasParameter(getMapUrl, "height", VALUE_SIZE + ""));
		Assert.assertTrue(hasParameter(getMapUrl, "bbox", "0.0,1.0,100.0,101.0"));
		Assert.assertTrue(hasParameter(getMapUrl, "format", wmsConfig.getFormat()));
		Assert.assertTrue(hasParameter(getMapUrl, "version", wmsConfig.getVersion().toString()));
		Assert.assertTrue(hasParameter(getMapUrl, "crs", VALUE_CRS2));
		Assert.assertTrue(hasParameter(getMapUrl, "styles", wmsConfig.getStyles()));
		Assert.assertTrue(hasParameter(getMapUrl, "transparent", wmsConfig.isTransparent() + ""));
		Assert.assertTrue(hasParameter(getMapUrl, "request", "GetMap"));
	}

	@Test
	public void testGetMapUrlInvertedAxis() {
		Bbox bounds = new Bbox(0, 1, 100, 100);
		WmsLayerConfiguration wmsConfig = new WmsLayerConfiguration();
		wmsConfig.setBaseUrl(VALUE_URL);
		wmsConfig.setLayers(VALUE_LAYER);
		wmsConfig.setStyles(VALUE_STYLE);
		wmsConfig.setCrs(VALUE_CRS);
		wmsConfig.setUseInvertedAxis(true);
		String getMapUrl = wmsService.getMapUrl(wmsConfig, bounds, VALUE_SIZE, VALUE_SIZE);

		Assert.assertEquals(VALUE_URL, getMapUrl.substring(0, getMapUrl.indexOf('?')));
		Assert.assertTrue(hasParameter(getMapUrl, "service", "WMS"));
		Assert.assertTrue(hasParameter(getMapUrl, "layers", wmsConfig.getLayers()));
		Assert.assertTrue(hasParameter(getMapUrl, "width", VALUE_SIZE + ""));
		Assert.assertTrue(hasParameter(getMapUrl, "height", VALUE_SIZE + ""));
		Assert.assertTrue(hasParameter(getMapUrl, "bbox", "1.0,0.0,101.0,100.0"));
		Assert.assertTrue(hasParameter(getMapUrl, "format", wmsConfig.getFormat()));
		Assert.assertTrue(hasParameter(getMapUrl, "version", wmsConfig.getVersion().toString()));
		Assert.assertTrue(hasParameter(getMapUrl, "crs", VALUE_CRS));
		Assert.assertTrue(hasParameter(getMapUrl, "styles", wmsConfig.getStyles()));
		Assert.assertTrue(hasParameter(getMapUrl, "transparent", wmsConfig.isTransparent() + ""));
		Assert.assertTrue(hasParameter(getMapUrl, "request", "GetMap"));
	}

	@Test
	public void testGetLegendUrl() {
		String getLegendUrl = wmsService.getLegendGraphicUrl(wmsConfig);

		Assert.assertEquals(VALUE_URL, getLegendUrl.substring(0, getLegendUrl.indexOf('?')));
		Assert.assertTrue(hasParameter(getLegendUrl, "service", "WMS"));
		Assert.assertTrue(hasParameter(getLegendUrl, "layer", wmsConfig.getLayers()));
		Assert.assertTrue(hasParameter(getLegendUrl, "request", "GetLegendGraphic"));
		Assert.assertTrue(hasParameter(getLegendUrl, "format", wmsConfig.getFormat()));
		Assert.assertTrue(hasParameter(getLegendUrl, "width", wmsConfig.getLegendConfig().getIconWidth() + ""));
		Assert.assertTrue(hasParameter(getLegendUrl, "height", wmsConfig.getLegendConfig().getIconHeight() + ""));
	}

	@Test
	public void testWmsUrlTransformer1() {
		Assert.assertNull(wmsService.getWmsUrlTransformer());
		wmsService.setWmsUrlTransformer(toHelloWorld);
		Assert.assertEquals(toHelloWorld, wmsService.getWmsUrlTransformer());
	}

	@Test
	public void testWmsUrlTransformer4GetMap() {
		Assert.assertNull(wmsService.getWmsUrlTransformer());
		String getLegendUrl = wmsService.getLegendGraphicUrl(wmsConfig);
		Assert.assertEquals(VALUE_URL, getLegendUrl.substring(0, getLegendUrl.indexOf('?')));
		Assert.assertTrue(hasParameter(getLegendUrl, "service", "WMS"));

		wmsService.setWmsUrlTransformer(toHelloWorld);
		Bbox bounds = new Bbox(0, 1, 100, 100);
		String getMapUrl = wmsService.getMapUrl(wmsConfig, bounds, VALUE_SIZE, VALUE_SIZE);
		Assert.assertEquals(HELLOWORLD, getMapUrl);
	}

	@Test
	public void testWmsUrlTransformer4GetLegend() {
		String getLegendUrl = wmsService.getLegendGraphicUrl(wmsConfig);
		Assert.assertEquals(VALUE_URL, getLegendUrl.substring(0, getLegendUrl.indexOf('?')));
		Assert.assertTrue(hasParameter(getLegendUrl, "service", "WMS"));

		wmsService.setWmsUrlTransformer(toHelloWorld);
		getLegendUrl = wmsService.getLegendGraphicUrl(wmsConfig);
		Assert.assertEquals(HELLOWORLD, getLegendUrl);
	}

	private boolean hasParameter(String url, String parameter, String value) {
		String paramString = url.substring(url.indexOf('?') + 1);
		String[] parameters = paramString.split("&");

		for (String param : parameters) {
			String paramName = param.substring(0, param.indexOf('='));
			if (paramName.equalsIgnoreCase(parameter)) {
				String paramValue = param.substring(param.indexOf('=') + 1);
				return paramValue.equals(value);
			}
		}
		return false;
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

	private Document w3cToGoogle(final org.w3c.dom.Document document) {
		return (Document) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[] { Document.class },
				new W3CToGoogleHandler(document));
	}

	/**
	 * Recursive proxy to expose w3c dom classes as gwt dom classes.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public class W3CToGoogleHandler implements InvocationHandler {

		private Object delegate;

		public W3CToGoogleHandler(Object delegate) {
			this.delegate = delegate;
		}

		@Override
		public Object invoke(Object target, Method method, Object[] args) throws Throwable {
			try {
				Method m = delegate.getClass().getMethod(method.getName(), method.getParameterTypes());
				Object result = m.invoke(delegate, args == null ? null : new Object[] { args[0] });
				if (result == null) {
					return null;
				}
				// find the matching interfaces for the return type and create a new proxy for those interfaces
				Class<?>[] c = getClasses(result, m.getReturnType());
				if (c.length > 0) {
					return Proxy.newProxyInstance(getClass().getClassLoader(), c, new W3CToGoogleHandler(result));
				} else {
					return result;
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}

		private Class[] getClasses(Object result, Class type) {
			List<Class> matches = new ArrayList<Class>();
			if (result instanceof org.w3c.dom.NamedNodeMap) {
				matches.add(com.google.gwt.xml.client.NamedNodeMap.class);
			}
			if (result instanceof org.w3c.dom.NodeList) {
				matches.add(com.google.gwt.xml.client.NodeList.class);
			}
			if (result instanceof org.w3c.dom.Document) {
				matches.add(com.google.gwt.xml.client.Document.class);
			}
			if (result instanceof org.w3c.dom.Element) {
				matches.add(com.google.gwt.xml.client.Element.class);
			}
			if (result instanceof org.w3c.dom.Node) {
				matches.add(com.google.gwt.xml.client.Node.class);
			}
			return matches.toArray(new Class[0]);
		}

	}

}
