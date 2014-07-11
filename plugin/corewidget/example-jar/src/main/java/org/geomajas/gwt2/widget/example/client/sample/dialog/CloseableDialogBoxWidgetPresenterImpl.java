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
package org.geomajas.gwt2.widget.example.client.sample.dialog;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * Implementation of the CloseableDialogBoxWidgetPresenter interface.
 *
 * @author David Debuck.
 */
public class CloseableDialogBoxWidgetPresenterImpl implements CloseableDialogBoxWidgetPresenter {

	private CloseableDialogBoxWidgetView view;

	/**
	 * CloseableDialogBoxWidgetPresenterImpl constructor.
	 *
	 * @param view CloseableDialogBoxWidgetView
	 */
	public CloseableDialogBoxWidgetPresenterImpl(CloseableDialogBoxWidgetView view) {

		this.view = view;
		this.view.setPresenter(this);

	}

	@Override
	public void setTitle(String title) {
		view.setTitle(title);
	}

	@Override
	public void addContent(Widget content) {
		view.addContent(content);
	}

	@Override
	public void show() {
		view.show();
	}

	@Override
	public void hide() {
		view.hide();
	}

	@Override
	public void center() {
		view.center();
	}

	@Override
	public void setSize(int width, int height) {
		view.setSize(width, height);
	}

	@Override
	public void setGlassEnabled(boolean isGlassEnabled) {
		view.setGlassEnabled(isGlassEnabled);
	}

	@Override
	public void setModal(boolean isModal) {
		view.setModal(isModal);
	}

	@Override
	public void setOnCloseHandler(ClickHandler closeHandler) {
		view.setOnCloseHandler(closeHandler);
	}

}
