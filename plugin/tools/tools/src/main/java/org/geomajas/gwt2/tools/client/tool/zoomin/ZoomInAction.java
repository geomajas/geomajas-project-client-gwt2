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
package org.geomajas.gwt2.tools.client.tool.zoomin;

import com.google.gwt.core.client.Callback;
import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.animation.NavigationAnimationFactory;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.tools.client.ConfigurableAction;
import org.geomajas.gwt2.tools.client.MapPresenterAction;

/**
 * Action for zooming in on the map.
 *
 * @author Oliver May
 * @since 2.0.0
 */
@Api
public class ZoomInAction extends MapPresenterAction<Boolean, Boolean> implements ConfigurableAction {

	/**
	 * Construct a zoom in action, working on the given mapPresenter.
	 *
	 * @param mapPresenter
	 */
	public ZoomInAction(MapPresenter mapPresenter) {
		super(mapPresenter);
	}

	@Override
	public void actionPerformed(Callback<Boolean, Boolean> callback) {
		ViewPort viewPort = mapPresenter.getViewPort();
		int index = viewPort.getFixedScaleIndex(viewPort.getScale());
		if (index < viewPort.getFixedScaleCount() - 1) {
			viewPort.registerAnimation(NavigationAnimationFactory.createZoomIn(mapPresenter));
		}
		// We may add some callback information here, about the new zoomlevel for example.
	}

	@Override
	public void configure(String key, String value) {
		//Configure zoom parameters here.
	}
}
