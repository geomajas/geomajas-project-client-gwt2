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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import junit.framework.Assert;

import org.apache.tools.ant.filters.StringInputStream;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.plugin.wfs.client.capabilities.WfsFeatureTypeInfo;
import org.geomajas.gwt2.plugin.wfs.client.capabilities.WfsGetCapabilitiesInfo;
import org.geomajas.gwt2.plugin.wfs.client.service.WfsService.WfsRequest;
import org.geomajas.gwt2.plugin.wfs.client.service.WfsService.WfsUrlTransformer;
import org.geomajas.gwt2.plugin.wfs.client.service.WfsService.WfsVersion;
import org.geotools.data.DataSourceException;
import org.geotools.data.wfs.v1_0_0.FeatureSetDescription;
import org.geotools.data.wfs.v1_0_0.WFSCapabilities;
import org.geotools.xml.DocumentFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

import com.google.gwtmockito.GwtMockitoTestRunner;
import com.vividsolutions.jts.geom.Envelope;

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

		WFSCapabilities capabilities = parseCapabilities();
		Assert.assertEquals(capabilities.getFeatureTypes().size(), info.getFeatureTypeList().getFeatureTypes().size());
		int i = 0;
		for (FeatureSetDescription featureSetDescription : capabilities.getFeatureTypes()) {
			WfsFeatureTypeInfo wfsFeatureTypeInfo = info.getFeatureTypeList().getFeatureTypes().get(i++);
			Assert.assertEquals(featureSetDescription.getName(), wfsFeatureTypeInfo.getName());
			Assert.assertEquals(featureSetDescription.getTitle(), wfsFeatureTypeInfo.getTitle());
			Assert.assertEquals(featureSetDescription.getAbstract(), wfsFeatureTypeInfo.getAbstract());
			Assert.assertEquals(featureSetDescription.getSRS(), wfsFeatureTypeInfo.getDefaultCrs());
			Assert.assertEquals(featureSetDescription.getLatLongBoundingBox(), toEnvelope(wfsFeatureTypeInfo.getWGS84BoundingBox()));
			// geotools is parsing on spaces, but spec does not specify ??
			// Assert.assertEquals(join(featureSetDescription.getKeywords()), wfsFeatureTypeInfo.getKeywords());
		}
	}
	
	private Envelope toEnvelope(Bbox box) {
		return new Envelope(box.getX(), box.getMaxX(), box.getY(), box.getMaxY());
	}

	protected WFSCapabilities parseCapabilities() throws IOException {
        // TODO: move to some 1.0.0 specific class
        Map<String,Object> hints = new HashMap<String,Object>();
        hints.put(DocumentFactory.VALIDATION_HINT, Boolean.FALSE);

        Object parsed;
        try {
            parsed = DocumentFactory.getInstance(new StringInputStream(capabilitiesXml), hints, Level.INFO);
        } catch (SAXException e) {
            throw new DataSourceException("Error parsing WFS 1.0.0 capabilities", e);
        }

        if (parsed instanceof WFSCapabilities) {
            return (WFSCapabilities) parsed;
        } else {
            throw new DataSourceException(
                    "The specified URL Should have returned a 'WFSCapabilities' object. Returned a "
                            + ((parsed == null) ? "null value."
                                    : (parsed.getClass().getName() + " instance.")));
        }
    }

}
