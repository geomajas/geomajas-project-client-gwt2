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
package org.geomajas.gwt2.plugin.corewidget.example.client.sample.dialog;

import com.google.gwt.core.client.GWT;

/**
 * Starting point for the Closeable dialog box widget.
 *
 * @author David Debuck
 */
public final class CloseableDialogBox {

	private static CloseableDialogBox instance;

	private CloseableDialogBoxWidgetViewFactory viewFactory;

	private CloseableDialogBoxWidgetClientBundleFactory bundleFactory;

	private CloseableDialogBox() {
	}

	/**
	 * Get a singleton instance.
	 *
	 * @return Return CloseableDialogBox
	 */
	public static CloseableDialogBox getInstance() {
		if (instance == null) {
			instance = new CloseableDialogBox();
		}
		return instance;
	}

	/**
	 * Get the view factory for this widget.
	 *
	 * @return CloseableDialogBoxWidgetClientBundleFactory
	 */
	public CloseableDialogBoxWidgetViewFactory getViewFactory() {
		if (viewFactory == null) {
			viewFactory = GWT.create(CloseableDialogBoxWidgetViewFactory.class);
		}
		return viewFactory;
	}

	/**
	 * Get the the client bundle factory for this widget.
	 *
	 * @return CloseableDialogBoxWidgetClientBundleFactory
	 */
	public CloseableDialogBoxWidgetClientBundleFactory getClientBundleFactory() {
		if (bundleFactory == null) {
			bundleFactory = GWT.create(CloseableDialogBoxWidgetClientBundleFactory.class);
		}
		return bundleFactory;
	}


}
