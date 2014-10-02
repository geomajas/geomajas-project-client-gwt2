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
package org.geomajas.gwt2.plugin.corewidget.example.client.sample.dialog;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * Interface for the CloseableDialogBox Presenter.
 *
 * @author David Debuck.
 */
public interface CloseableDialogBoxWidgetPresenter {

	/**
	 * Set the title of the Closeable dialog box.
	 *
	 * @param title String
	 */
	void setTitle(String title);

	/**
	 * Add content to the Closeable dialog box.
	 *
	 * @param content Widget
	 */
	void addContent(Widget content);

	/**
	 * Show the Closeable dialog box.
	 */
	void show();

	/**
	 * Hide the Closeable dialog box.
	 */
	void hide();

	/**
	 * Center the Closeable dialog box.
	 */
	void center();

	/**
	 * Set the size of the Closeable dialog box.
	 *
	 * @param width integer
	 * @param height integer
	 */
	void setSize(int width, int height);

	/**
	 * Set GlassEnabled state in the Closeable dialog box.
	 *
	 * @param isGlassEnabled boolean
	 */
	void setGlassEnabled(boolean isGlassEnabled);

	/**
	 * Set the modal state of the Closeable dialog box.
	 *
	 * @param isModal boolean
	 */
	void setModal(boolean isModal);

	/**
	 * Set an onCloseHandler to the widget so we can check outside
	 * the widget when it actually gets closed.
	 *
	 * @param closeHandler ClickHandler
	 */
	void setOnCloseHandler(ClickHandler closeHandler);

}
