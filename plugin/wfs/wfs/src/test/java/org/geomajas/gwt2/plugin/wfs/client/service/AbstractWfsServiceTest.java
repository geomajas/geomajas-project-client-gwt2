package org.geomajas.gwt2.plugin.wfs.client.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.StringReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.xerces.parsers.DOMParser;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsGetCapabilitiesInfo;
import org.geomajas.gwt2.plugin.wfs.client.service.WfsServiceImpl.RequestBuilderFactory;
import org.geomajas.gwt2.plugin.wfs.client.service.WfsServiceImpl.UrlEncoder;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.xml.sax.InputSource;

import com.google.gwt.core.client.Callback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.impl.XMLParserImpl;
import com.google.gwtmockito.GwtMockitoTestRunner;

@RunWith(GwtMockitoTestRunner.class)
public abstract class AbstractWfsServiceTest {

	@Mock
	protected RequestBuilder requestBuilder;

	protected WfsServiceImpl wfsService;

	protected String capabilitiesXml;

	protected Document capabilitiesDoc;

	@Before
	public void init() throws Exception {
		wfsService = new WfsServiceImpl();
		wfsService.setRequestBuilderFactory(new RequestBuilderFactory() {

			@Override
			public RequestBuilder create(RequestBuilder.Method method, String url) {
				return requestBuilder;
			}
		});
		wfsService.setUrlEncoder(new UrlEncoder() {

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
				// equals() should equal the other's delegate
				if (m.getName().equals("equals")) {
					if (Proxy.isProxyClass(args[0].getClass())) {
						W3CToGoogleHandler handler = (W3CToGoogleHandler) Proxy.getInvocationHandler(args[0]);
						return delegate.equals(handler.delegate);
					}
				}
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

	class CapabilitiesCallback implements Callback<WfsGetCapabilitiesInfo, String> {

		WfsGetCapabilitiesInfo result;

		private String reason;

		@Override
		public void onSuccess(WfsGetCapabilitiesInfo result) {
			this.result = result;
		}

		@Override
		public void onFailure(String reason) {
			this.reason = reason;
		}

		public WfsGetCapabilitiesInfo getResult() {
			return result;
		}

		public String getReason() {
			return reason;
		}

	}

}
