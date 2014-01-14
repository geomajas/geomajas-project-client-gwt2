/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.gwt2.tools;

import com.google.gwt.core.client.Callback;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.GestureChangeHandler;
import com.google.gwt.event.dom.client.GestureEndHandler;
import com.google.gwt.event.dom.client.GestureStartHandler;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import junit.framework.Assert;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.gfx.CanvasContainer;
import org.geomajas.gwt2.client.gfx.TransformableWidgetContainer;
import org.geomajas.gwt2.client.gfx.VectorContainer;
import org.geomajas.gwt2.client.map.ConfigurationHelper;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.MapPresenterImpl;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.ZoomOption;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlContainer;
import org.geomajas.gwt2.tools.client.tool.zoomin.ZoomInAction;
import org.junit.Before;
import org.junit.Test;
import org.vaadin.gwtgraphics.client.Transformable;

import java.util.List;

/**
 * @author Oliver May
 */
public class TestZoomInAction {

	private final MapEventBus eventBus;

	private final ViewPort viewPort;

	private MapPresenterImpl mapPresenter;

	public TestZoomInAction() {
		mapPresenter = new MapPresenterImpl(new SimpleEventBus(), new MapWidgetTestImpl());
		mapPresenter.initialize(ConfigurationHelper.createTestMapConfig().getMapOptions());

		eventBus = mapPresenter.getEventBus();
		viewPort = mapPresenter.getViewPort();
		viewPort.setMapSize(1000, 1000);
	}

	@Before
	public void prepareTest() {
		viewPort.applyBounds(viewPort.getMaximumBounds(), ZoomOption.LEVEL_CLOSEST);
	}

	@Test
	public void testZoomIn() {
		ZoomInAction action = new ZoomInAction(mapPresenter);
		viewPort.applyBounds(viewPort.getMaximumBounds(), ZoomOption.LEVEL_CLOSEST);

		Assert.assertEquals(4.0, viewPort.getScale());

	    action.actionPerformed(new Callback<Boolean, Boolean>() {
			@Override
			public void onFailure(Boolean reason) {

			}

			@Override
			public void onSuccess(Boolean result) {

			}
		});
		Assert.assertEquals(2.0, viewPort.getScale());
	}


}
