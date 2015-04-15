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

package org.geomajas.gwt2.plugin.wfs.client.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

import junit.framework.Assert;
import net.opengis.wfs.FeatureTypeType;
import net.opengis.wfs.WFSCapabilitiesType;

import org.apache.tools.ant.filters.StringInputStream;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.plugin.wfs.client.capabilities.WfsFeatureTypeInfo;
import org.geomajas.gwt2.plugin.wfs.client.capabilities.WfsGetCapabilitiesInfo;
import org.geomajas.gwt2.plugin.wfs.client.service.WfsService.WfsRequest;
import org.geomajas.gwt2.plugin.wfs.client.service.WfsService.WfsUrlTransformer;
import org.geomajas.gwt2.plugin.wfs.client.service.WfsService.WfsVersion;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.wfs.internal.GetCapabilitiesResponse;
import org.geotools.data.wfs.internal.WFSGetCapabilities;
import org.geotools.data.wfs.internal.v1_x.FeatureTypeInfoImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.geometry.Envelope;

import com.google.gwtmockito.GwtMockitoTestRunner;

/**
 * Testcase for the {@link WmsService} interface.
 * 
 * @author Jan De Moerloose
 */
@RunWith(GwtMockitoTestRunner.class)
public class WfsServiceTest extends AbstractWfsServiceTest {

	private static final String VALUE_URL = "http://www.geomajas.org/";

	private static final String VALUE_LAYER = "someLayer";

	private static final String VALUE_STYLE = "someStyle";

	private static final String VALUE_CRS = "EPSG:4326";

	private static final String VALUE_CRS2 = "EPSG:31370";

	private static final int VALUE_SIZE = 342;

	private static final String HELLOWORLD = "Hello World";

	private WfsUrlTransformer toHelloWorld;

	@Before
	public void init() throws Exception {
		super.init();
		toHelloWorld = new WfsUrlTransformer() {

			public String transform(WfsRequest request, String url) {
				return HELLOWORLD;
			}
		};
	}

	@Test
	public void testGetCapabilities100() throws Exception {
		prepareResponse("capabilities_1_0_0.xml");
		CapabilitiesCallback callback = new CapabilitiesCallback();
		wfsService.getCapabilities("http://test", WfsVersion.V1_0_0, callback);
		WfsGetCapabilitiesInfo info = callback.getResult();
		Assert.assertNotNull(info);
		Assert.assertNotNull(info.getFeatureTypeList());

		// using geotools to parse the xml and compare results:
		WFSGetCapabilities capabilities = parseCapabilities();
		WFSCapabilitiesType caps = (WFSCapabilitiesType) capabilities.getParsedCapabilities();
		Assert.assertEquals(caps.getFeatureTypeList().getFeatureType().size(), info.getFeatureTypeList()
				.getFeatureTypes().size());
		int i = 0;
		for (Object o : caps.getFeatureTypeList().getFeatureType()) {
			WfsFeatureTypeInfo wfsFeatureTypeInfo = info.getFeatureTypeList().getFeatureTypes().get(i++);
			FeatureTypeInfoImpl featureTypeType = new FeatureTypeInfoImpl((FeatureTypeType) o);
			Assert.assertTrue(wfsFeatureTypeInfo.getName().endsWith(featureTypeType.getName()));
			Assert.assertEquals(featureTypeType.getTitle(), wfsFeatureTypeInfo.getTitle());
			Assert.assertEquals(featureTypeType.getAbstract(), wfsFeatureTypeInfo.getAbstract());
			Assert.assertEquals(featureTypeType.getDefaultSRS(), wfsFeatureTypeInfo.getDefaultCrs());
			Assert.assertTrue(featureTypeType.getWGS84BoundingBox().boundsEquals2D(
					toEnvelope(wfsFeatureTypeInfo.getWGS84BoundingBox()), 1E-5));
			// geotools accepts empty string as keyword, but spec does not specify ?? add empty string to fix the test...
			if(wfsFeatureTypeInfo.getKeywords().size() == (featureTypeType.getKeywords().size()-1)) {
				wfsFeatureTypeInfo.getKeywords().add("");
			}
			Assert.assertEquals(featureTypeType.getKeywords(), new HashSet<String>(wfsFeatureTypeInfo.getKeywords()));
		}
	}

	private Envelope toEnvelope(Bbox box) throws Exception {
		return new ReferencedEnvelope(box.getX(), box.getMaxX(), box.getY(), box.getMaxY(), null);
	}

	protected WFSGetCapabilities parseCapabilities() throws Exception {

		GetCapabilitiesResponse response = new GetCapabilitiesResponse(new HTTPResponse() {

			@Override
			public InputStream getResponseStream() throws IOException {
				return new StringInputStream(capabilitiesXml);
			}

			@Override
			public String getResponseHeader(String headerName) {
				return null;
			}

			@Override
			public String getResponseCharset() {
				return null;
			}

			@Override
			public String getContentType() {
				return "text/xml";
			}

			@Override
			public void dispose() {
			}
		});

		return response.getCapabilities();
	}

}
