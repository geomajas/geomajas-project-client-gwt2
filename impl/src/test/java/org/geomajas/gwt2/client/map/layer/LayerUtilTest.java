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

package org.geomajas.gwt2.client.map.layer;

import junit.framework.Assert;
import org.geomajas.gwt2.client.map.ViewPort;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.stub;

/**
 * Testcases for {@link org.geomajas.gwt2.client.map.layer.LayerUtil}.
 *
 * @author Jan Venstermans
 */
public class LayerUtilTest {

	@Mock
	private Layer layerMock;

	@Mock
	private ViewPort viewPortMock;

	private static final double LAYER_RESOLUTION_MIN = 10.0;
	private static final double LAYER_RESOLUTION_MAX = 100.0;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		stub(layerMock.getMinResolution()).toReturn(LAYER_RESOLUTION_MIN);
		stub(layerMock.getMaxResolution()).toReturn(LAYER_RESOLUTION_MAX);
	}

	@Test
	public void testIsLayerVisibleAtViewPortResolutionLowerThanLayerResolutionMin() throws Exception {
		double viewPortResolution = LAYER_RESOLUTION_MIN / 2;
		stub(viewPortMock.getResolution()).toReturn(viewPortResolution);

		boolean result = LayerUtil.isLayerVisibleAtViewPortResolution(viewPortMock, layerMock);

		Assert.assertFalse(result);
	}

	@Test
	public void testIsLayerVisibleAtViewPortResolutionExactlyLayerResolutionMin() throws Exception {
		double viewPortResolution = LAYER_RESOLUTION_MIN;
		stub(viewPortMock.getResolution()).toReturn(viewPortResolution);

		boolean result = LayerUtil.isLayerVisibleAtViewPortResolution(viewPortMock, layerMock);

		Assert.assertFalse(result);
	}

	@Test
	public void testIsLayerVisibleAtViewPortResolutionBetweenLayerResolutionMinAndLayerResolutionMax()
			throws Exception {
		double viewPortResolution = (LAYER_RESOLUTION_MIN + LAYER_RESOLUTION_MAX) / 2;
		stub(viewPortMock.getResolution()).toReturn(viewPortResolution);

		boolean result = LayerUtil.isLayerVisibleAtViewPortResolution(viewPortMock, layerMock);

		Assert.assertTrue(result);
	}

	@Test
	public void testIsLayerVisibleAtViewPortResolutionExactlyLayerResolutionMax() throws Exception {
		double viewPortResolution = LAYER_RESOLUTION_MAX;
		stub(viewPortMock.getResolution()).toReturn(viewPortResolution);

		boolean result = LayerUtil.isLayerVisibleAtViewPortResolution(viewPortMock, layerMock);

		Assert.assertTrue(result);
	}

	@Test
	public void testIsLayerVisibleAtViewPortResolutionHigherThanLayerResolutionMax() throws Exception {
		double viewPortResolution = 2 * LAYER_RESOLUTION_MAX;
		stub(viewPortMock.getResolution()).toReturn(viewPortResolution);

		boolean result = LayerUtil.isLayerVisibleAtViewPortResolution(viewPortMock, layerMock);

		Assert.assertFalse(result);
	}
}