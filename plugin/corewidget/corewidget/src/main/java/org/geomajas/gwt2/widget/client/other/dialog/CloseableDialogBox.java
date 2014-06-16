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
package org.geomajas.gwt2.widget.client.other.dialog;

import com.google.gwt.core.client.GWT;

/**
 * Initialization class for the CloseableDialogBoxWidget.
 *
 * @author David Debuck.
 */
public final class CloseableDialogBox {

	private static CloseableDialogBox instance;

	private CloseableDialogBoxViewFactory viewFactory;

	private CloseableDialogBoxClientBundleFactory bundleFactory;

	/**
	 * CloseableDialogBox default constructor.
	 */
	private CloseableDialogBox() {
	}

	/**
	 * CloseableDialogBox Constructor.
	 *
	 * @return instance of CloseableDialogBox
	 */
	public static CloseableDialogBox getInstance() {
		if (instance == null) {
			instance = new CloseableDialogBox();
		}
		return instance;
	}

	/**
	 * Factory for the CloseableDialogBox Client Bundle.
	 *
	 * @return CloseableDialogBoxClientBundleFactory
	 */
	public CloseableDialogBoxClientBundleFactory getBundleFactory() {
		if (bundleFactory == null) {
			bundleFactory = GWT.create(CloseableDialogBoxClientBundleFactory.class);
		}
		return bundleFactory;
	}

	/**
	 * Factory for the CloseableDialogBox View.
	 *
	 * @return CloseableDialogBoxViewFactory
	 */
	public CloseableDialogBoxViewFactory getViewFactory() {
		if (viewFactory == null) {
			viewFactory = GWT.create(CloseableDialogBoxViewFactory.class);
		}
		return viewFactory;
	}

}
