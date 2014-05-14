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
package org.geomajas.gwt2.widget.client;

import org.geomajas.annotation.Api;

import com.google.gwt.core.client.GWT;

/**
 * Starting point for the core widget plugin.
 * 
 * @author Jan De Moerloose
 * @since 2.0.0
 */
@Api(allMethods = true)
public final class CoreWidget {

	private static CoreWidget instance;

	private ViewFactory viewFactory;
	
	private CoreWidgetClientBundleFactory bundleFactory;

	private CoreWidget() {
		viewFactory = new ViewFactory();
	}

	/**
	 * Get a singleton instance.
	 * 
	 * @return Return CoreWidget!
	 */
	public static CoreWidget getInstance() {
		if (instance == null) {
			instance = new CoreWidget();
		}
		return instance;
	}

	/**
	 * Replace the instance for testing.
	 * 
	 * @param coreWidget
	 */
	public static void setInstance(CoreWidget coreWidget) {
		instance = coreWidget;
	}

	/**
	 * Get the MVP view factory for the widgets of this plugin.
	 * 
	 * @return the factory
	 */
	public ViewFactory getViewFactory() {
		return viewFactory;
	}

	/**
	 * Replace the default MVP view factory for the widgets of this plugin.
	 * 
	 * @param viewFactory the new factory
	 */
	public void setViewFactory(ViewFactory viewFactory) {
		this.viewFactory = viewFactory;
	}
	
	/**
	 * Get a factory for creating resource bundles for this artifact. All widgets make use of this factory. If you want
	 * to override the default styles, then override this factory through deferred binding.
	 *
	 * @return A factory for creating resource bundles for this artifact.
	 * @since 2.1.0
	 */
	public CoreWidgetClientBundleFactory getClientBundleFactory() {
		if (bundleFactory == null) {
			bundleFactory = GWT.create(CoreWidgetClientBundleFactory.class);
		}
		return bundleFactory;
	}


}
