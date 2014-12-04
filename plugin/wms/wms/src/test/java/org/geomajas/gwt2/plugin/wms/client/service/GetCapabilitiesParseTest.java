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

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwtmockito.GwtMockitoTestRunner;

import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsGetCapabilitiesInfo;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsLayerInfo;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsLayerLegendUrlInfo;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsLayerMetadataUrlInfo;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsLayerStyleInfo;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsOnlineResourceInfo;
import org.geomajas.gwt2.plugin.wms.client.service.WmsService.WmsVersion;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import junit.framework.Assert;

/**
 * GWT test that fetches capabilities documents from a servlet and tries to parse them. We test WMS versions 1.1.1 and
 * 1.3.0.
 * 
 * @author Pieter De Graef
 */
@RunWith(GwtMockitoTestRunner.class)
public class GetCapabilitiesParseTest extends AbstractWmsServiceTest {

	private static final String CAPA_FILE = "/org/geomajas/gwt2/plugin/wms/server/GetCapabilities";

	@Test
	public void testCapabilities111() throws Exception {
		prepareResponse(CAPA_FILE+"111.xml");
		wmsService.getCapabilities(GWT.getModuleBaseURL() + CAPA_FILE, WmsVersion.V1_1_1,
				new Callback<WmsGetCapabilitiesInfo, String>() {

					public void onSuccess(WmsGetCapabilitiesInfo result) {
						Assert.assertNotNull(result);

						List<WmsLayerInfo> layers = result.getLayers();
						Assert.assertNotNull(layers);
						Assert.assertEquals(2, layers.size());

						checkLayer(1, layers.get(0), WmsVersion.V1_1_1);
						checkLayer(2, layers.get(1), WmsVersion.V1_1_1);
						
						WmsLayerInfo rootLayer = result.getRootLayer();
						checkRoot(rootLayer, WmsVersion.V1_1_1);
					}

					public void onFailure(String reason) {
						Assert.fail(reason);
					}
				});
	}

	@Test
	public void testCapabilities130() throws Exception {
		prepareResponse(CAPA_FILE+"130.xml");
		wmsService.getCapabilities(GWT.getModuleBaseURL() + CAPA_FILE, WmsVersion.V1_3_0,
				new Callback<WmsGetCapabilitiesInfo, String>() {

					public void onSuccess(WmsGetCapabilitiesInfo result) {
						Assert.assertNotNull(result);

						List<WmsLayerInfo> layers = result.getLayers();
						Assert.assertNotNull(layers);
						Assert.assertEquals(2, layers.size());

						checkLayer(1, layers.get(0), WmsVersion.V1_3_0);
						checkLayer(2, layers.get(1), WmsVersion.V1_3_0);
						
						WmsLayerInfo rootLayer = result.getRootLayer();
						checkRoot(rootLayer, WmsVersion.V1_3_0);
					}

					public void onFailure(String reason) {
						Assert.fail(reason);
					}
				});
	}

	// ------------------------------------------------------------------------
	// Private methods for testing the parsed capabilities documents:
	// ------------------------------------------------------------------------

	private void checkLayer(int index, WmsLayerInfo layer, WmsVersion version) {
		if (index == 1) {
			Assert.assertFalse(layer.isQueryable());
		} else {
			Assert.assertTrue(layer.isQueryable());
		}
		Assert.assertEquals("layer" + index + "Name", layer.getName());
		Assert.assertEquals("layer" + index + "Title", layer.getTitle());
		Assert.assertEquals("layer" + index + "Abstract", layer.getAbstract());

		List<String> keywords = layer.getKeywords();
		Assert.assertNotNull(keywords);
		Assert.assertEquals(2, keywords.size());
		Assert.assertEquals("keyword1", keywords.get(0));
		Assert.assertEquals("keyword2", keywords.get(1));

		List<String> crs = layer.getCrs();
		Assert.assertNotNull(crs);
		if (version.equals(WmsVersion.V1_1_1)) {
			Assert.assertEquals(1, crs.size());
			Assert.assertEquals("EPSG:31370", crs.get(0));
		} else {
			Assert.assertEquals(2, crs.size());
			Assert.assertEquals("EPSG:31370", crs.get(0));
			Assert.assertEquals("EPSG:4326", crs.get(1));
		}

		Bbox latlonBox = layer.getLatlonBoundingBox();
		Assert.assertEquals(0.0, latlonBox.getX());
		Assert.assertEquals(1.0, latlonBox.getMaxX());
		Assert.assertEquals(2.0, latlonBox.getY());
		Assert.assertEquals(3.0, latlonBox.getMaxY());

		Assert.assertEquals("EPSG:31370", layer.getBoundingBoxCrs());
		Bbox bounds = layer.getBoundingBox();
		Assert.assertEquals(0.0, bounds.getX());
		Assert.assertEquals(200000.0, bounds.getMaxX());
		Assert.assertEquals(0.0, bounds.getY());
		Assert.assertEquals(200000.0, bounds.getMaxY());

		checkMetadataUrl(layer.getMetadataUrls());
		List<WmsLayerStyleInfo> styles = layer.getStyleInfo();
		Assert.assertNotNull(styles);
		Assert.assertEquals(1, styles.size());
		checkLayerStyle(index, styles.get(0));
	}

	protected void checkRoot(WmsLayerInfo layer, WmsVersion version) {
		Assert.assertFalse(layer.isQueryable());
		Assert.assertNull(layer.getName());
		Assert.assertEquals("someTitle", layer.getTitle());
		Assert.assertEquals("someAbstract", layer.getAbstract());

		List<String> keywords = layer.getKeywords();
		Assert.assertNotNull(keywords);
		Assert.assertEquals(0, keywords.size());

		List<String> crs = layer.getCrs();
		Assert.assertEquals(2, crs.size());
		Assert.assertEquals("EPSG:31370", crs.get(0));
		Assert.assertEquals("EPSG:4326", crs.get(1));

		Bbox latlonBox = layer.getLatlonBoundingBox();
		Assert.assertEquals(0.0, latlonBox.getX());
		Assert.assertEquals(1.0, latlonBox.getMaxX());
		Assert.assertEquals(2.0, latlonBox.getY());
		Assert.assertEquals(3.0, latlonBox.getMaxY());
		List<WmsLayerInfo> layers = layer.getLayers();
		Assert.assertNotNull(layers);
		Assert.assertEquals(2, layers.size());

		checkLayer(1, layers.get(0), version);
		checkLayer(2, layers.get(1), version);
	}

	private void checkMetadataUrl(List<WmsLayerMetadataUrlInfo> info) {
		Assert.assertNotNull(info);
		Assert.assertTrue(info.size() > 0);
		Assert.assertEquals("ISO19115:2003", info.get(0).getType());
		Assert.assertEquals("application/xml", info.get(0).getFormat());
		checkOnlineResource(info.get(0).getOnlineResource());
	}

	private void checkOnlineResource(WmsOnlineResourceInfo info) {
		Assert.assertEquals("http://www.geomajas.org/", info.getHref());
		Assert.assertEquals("simple", info.getType());
		Assert.assertEquals("xlink", info.getXLink());
	}

	private void checkLayerStyle(int index, WmsLayerStyleInfo info) {
		Assert.assertEquals("layer" + index + "StyleName", info.getName());
		Assert.assertEquals("layer" + index + "StyleTitle", info.getTitle());
		Assert.assertEquals("layer" + index + "StyleAbstract", info.getAbstract());
		checkLayerLegendUrl(info.getLegendUrl());
	}

	private void checkLayerLegendUrl(WmsLayerLegendUrlInfo info) {
		Assert.assertEquals("image/png", info.getFormat());
		Assert.assertEquals(20, info.getWidth());
		Assert.assertEquals(20, info.getHeight());
		checkOnlineResource(info.getOnlineResource());
	}
}
