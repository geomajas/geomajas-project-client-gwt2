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

package org.geomajas.gwt2.plugin.geocoder.client;

import com.google.gwt.core.client.GWT;
import org.geomajas.annotation.Api;

/**
 * Central class, starting point for getting e.g. view and resource factories.
 *
 * @author Jan Venstermans
 * @since 2.1.0
 */
@Api(allMethods = true)
public final class Geocoder {

	private static Geocoder instance;

	private GeocoderViewFactory viewFactory;

	private GeocoderClientBundleFactory bundleFactory;

	private Geocoder() {
	}

	/**
	 *  Returns the singleton {@link Geocoder} class.
	 *
	 * @return the geocoder singleton base class
	 */
	public static Geocoder getInstance() {
		if (instance == null) {
			instance = new Geocoder();
		}
		return instance;
	}

	/**
	 * Returns the current active resource bundle factory.
	 * In case no custom resource bundle factory has been provided, the default will be created
	 * and returned. Default is {@link GeocoderClientBundleFactory} instance.
	 *
	 * @return the active view factory
	 */
	public GeocoderClientBundleFactory getBundleFactory() {
		if (bundleFactory == null) {
			bundleFactory = GWT.create(GeocoderClientBundleFactory.class);
		}
		return bundleFactory;
	}

	/**
	 * Returns the current active view factory.
	 * In case no custom factory has been provided, the default will be created
	 * and returned. Default is {@link GeocoderViewFactory} instance.
	 *
	 * @return the active view factory
	 */
	public GeocoderViewFactory getViewFactory() {
		if (viewFactory == null) {
			viewFactory = GWT.create(GeocoderViewFactory.class);
		}
		return viewFactory;
	}

	/**
	 * Set a custom view factory.
	 * @param viewFactory the custom view factory
	 */
	public void setViewFactory(GeocoderViewFactory viewFactory) {
		this.viewFactory = viewFactory;
	}

	/**
	 * Set a custom resource bundle factory.
	 * @param bundleFactory the custom resource bundle facotry
	 */
	public void setBundleFactory(GeocoderClientBundleFactory bundleFactory) {
		this.bundleFactory = bundleFactory;
	}

}