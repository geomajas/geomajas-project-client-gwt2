package org.geomajas.gwt2.plugin.wms.client.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.impl.XMLParserImpl;
import com.google.gwtmockito.GwtMockitoTestRunner;

import org.apache.commons.io.IOUtils;
import org.apache.xerces.parsers.DOMParser;
import org.geomajas.gwt2.plugin.wms.client.service.WmsServiceImpl.CoordinateFormatter;
import org.geomajas.gwt2.plugin.wms.client.service.WmsServiceImpl.RequestBuilderFactory;
import org.geomajas.gwt2.plugin.wms.client.service.WmsServiceImpl.UrlEncoder;
import org.junit.Before;
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

@RunWith(GwtMockitoTestRunner.class)
public abstract class AbstractWmsServiceTest {

	@Mock
	protected RequestBuilder requestBuilder;

	protected WmsServiceImpl wmsService;

	protected String capabilitiesXml;

	protected Document capabilitiesDoc;

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
		when(XMLParserImpl.getInstance().parse(anyString())).thenAnswer(new Answer<Document>() {

			@Override
			public Document answer(InvocationOnMock invocation) throws Throwable {
				return capabilitiesDoc;
			}
		});
	}

	protected void prepareResponse(String resource) throws Exception {
		capabilitiesXml = IOUtils.toString(this.getClass().getResourceAsStream(resource), "UTF-8");
		DOMParser parser = new DOMParser();
		parser.parse(new InputSource(new StringReader(capabilitiesXml)));
		capabilitiesDoc = w3cToGoogle(parser.getDocument());
		final Response response = mock(Response.class);
		when(response.getStatusCode()).thenReturn(200);
		when(response.getText()).thenReturn(capabilitiesXml);

		when(requestBuilder.sendRequest(anyString(), any(RequestCallback.class))).thenAnswer(new Answer<Request>() {

			@Override
			public Request answer(InvocationOnMock invocation) throws Throwable {
				((RequestCallback) invocation.getArguments()[1]).onResponseReceived(null, response);
				return null;
			}
		});
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
