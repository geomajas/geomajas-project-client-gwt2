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

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point for the CloseableDialogBoxWidget.
 *
 * @author David Debuck.
 */
public class CloseableDialogBoxWidget implements IsWidget {

	private CloseableDialogBoxWidgetView view;

	private CloseableDialogBoxWidgetPresenter presenter;

	/**
	 * CloseableDialogBoxWidget constructor.
	 */
	public CloseableDialogBoxWidget() {
		this(CloseableDialogBox.getInstance().getViewFactory().closeableDialogBoxWidgetView());
	}

	/**
	 * CloseableDialogBoxWidget constructor.
	 *
	 * @param view CloseableDialogBoxWidgetView
	 */
	public CloseableDialogBoxWidget(CloseableDialogBoxWidgetView view) {
		this.view = view;
		presenter = new CloseableDialogBoxWidgetPresenterImpl(view);
	}

	/**
	 * Set the title of the Closeable dialog box.
	 *
	 * @param title String
	 */
	public void setTitle(String title) {
		presenter.setTitle(title);
	}

	/**
	 * Add content to the Closeable dialog box.
	 *
	 * @param content Widget
	 */
	public void addContent(Widget content) {
		presenter.addContent(content);
	}

	/**
	 * Show the Closeable dialog box.
	 */
	public void show() {
		presenter.show();
	}

	/**
	 * Hide the Closeable dialog box.
	 */
	public void hide() {
		presenter.hide();
	}

	/**
	 * Center the Closeable dialog box.
	 */
	public void center() {
		presenter.center();
	}

	/**
	 * Set the width and height of the Closeable dialog box.
	 *
	 * @param width integer
	 * @param height integer
	 */
	public void setSize(int width, int height) {
		presenter.setSize(width, height);
	}

	/**
	 * Set GlassEnabled state in the Closeable dialog box.
	 *
	 * @param isGlassEnabled boolean
	 */
	public void setGlassEnabled(boolean isGlassEnabled) {
		presenter.setGlassEnabled(isGlassEnabled);
	}

	/**
	 * Set the modal state of the Closeable dialog box.
	 *
	 * @param isModal boolean
	 */
	public void setModal(boolean isModal) {
		presenter.setModal(isModal);
	}

	/**
	 * Set an onCloseHandler to the widget so we can check outside
	 * the widget when it actually gets closed.
	 *
	 * @param onCloseHandler ClickHandler
	 */
	public  void setOnCloseHandler(ClickHandler onCloseHandler) {
		presenter.setOnCloseHandler(onCloseHandler);
	}

	@Override
	public Widget asWidget() {
		return view.asWidget();
	}
}
