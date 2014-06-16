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

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.annotation.Api;

/**
 * Entry point for the CloseableDialogBoxWidget.
 *
 * @author David Debuck.
 * @since 2.1.0
 */
@Api
public class CloseableDialogBoxWidget implements IsWidget {

	private CloseableDialogBoxWidgetView view;

	private CloseableDialogBoxWidgetPresenter presenter;

	/**
	 * CloseableDialogBoxWidget constructor.
	 */
	@Api
	public CloseableDialogBoxWidget() {
		this(CloseableDialogBox.getInstance().getViewFactory().createCloseableDialogBoxWidgetView());
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
	@Api
	public void setTitle(String title) {
		presenter.setTitle(title);
	}

	/**
	 * Add content to the Closeable dialog box.
	 *
	 * @param content Widget
	 */
	@Api
	public void addContent(Widget content) {
		presenter.addContent(content);
	}

	/**
	 * Show the Closeable dialog box.
	 */
	@Api
	public void show() {
		presenter.show();
	}

	/**
	 * Hide the Closeable dialog box.
	 */
	@Api
	public void hide() {
		presenter.hide();
	}

	/**
	 * Center the Closeable dialog box.
	 */
	@Api
	public void center() {
		presenter.center();
	}

	/**
	 * Set the width and height of the Closeable dialog box.
	 *
	 * @param width integer
	 * @param height integer
	 */
	@Api
	public void setSize(int width, int height) {
		presenter.setSize(width, height);
	}

	/**
	 * Set GlassEnabled state in the Closeable dialog box.
	 *
	 * @param isGlassEnabled boolean
	 */
	@Api
	public void setGlassEnabled(boolean isGlassEnabled) {
		presenter.setGlassEnabled(isGlassEnabled);
	}

	/**
	 * Set the modal state of the Closeable dialog box.
	 *
	 * @param isModal boolean
	 */
	@Api
	public void setModal(boolean isModal) {
		presenter.setModal(isModal);
	}

	/**
	 * Set an onCloseHandler to the widget so we can check outside
	 * the widget when it actually gets closed.
	 *
	 * @param onCloseHandler ClickHandler
	 */
	@Api
	public  void setOnCloseHandler(ClickHandler onCloseHandler) {
		presenter.setOnCloseHandler(onCloseHandler);
	}

	@Override
	public Widget asWidget() {
		return view.asWidget();
	}
}
