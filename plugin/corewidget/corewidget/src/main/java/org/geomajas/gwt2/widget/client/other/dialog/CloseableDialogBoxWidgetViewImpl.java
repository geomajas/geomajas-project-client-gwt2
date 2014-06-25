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
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.widget.client.CoreWidget;
import org.geomajas.gwt2.widget.client.other.dialog.caption.CloseableDialogBoxCaption;
import org.geomajas.gwt2.widget.client.other.dialog.resource.CloseableDialogBoxWidgetResource;

/**
 * Implementation of the CloseableDialogBoxWidgetView.
 *
 * @author David Debuck.
 */
public class CloseableDialogBoxWidgetViewImpl implements CloseableDialogBoxWidgetView {

	private CloseableDialogBoxWidgetPresenter presenter;

	private DialogBox widget;

	private boolean isSizeSet;

	@UiField
	protected CloseableDialogBoxCaption customCaption;

	@UiField
	protected ScrollPanel contentScrollPanel;

	private static final MyUiBinder UIBINDER = GWT.create(MyUiBinder.class);

	/**
	 * UI binder interface.
	 *
	 * @author David Debuck
	 */
	interface MyUiBinder extends UiBinder<Widget, CloseableDialogBoxWidgetViewImpl> {
	}

	public CloseableDialogBoxWidgetViewImpl() {
		this(CoreWidget.getInstance().getClientBundleFactory().createCloseableDialogBoxWidgetResource());
	}

	public CloseableDialogBoxWidgetViewImpl(CloseableDialogBoxWidgetResource resource) {
		resource.css().ensureInjected();

		widget = (DialogBox) UIBINDER.createAndBindUi(this);

		customCaption.setParentView(this);

	}

	@Override
	public void setPresenter(CloseableDialogBoxWidgetPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setTitle(String title) {
		widget.setText(title);
	}

	@Override
	public void addContent(Widget content) {
		contentScrollPanel.add(content);
	}

	@Override
	public void show() {

		// Set the CloseableDialogBox to half the client size when nothing has been set.
		if (!isSizeSet) {
			widget.setWidth(
					Window.getClientWidth() / 2 + "px"
			);
			contentScrollPanel.setHeight(
					Window.getClientHeight() / 2 + "px"
			);
			widget.center();
			isSizeSet = true;
		}

		widget.show();

	}

	@Override
	public void hide() {
		widget.hide();
	}

	@Override
	public void center() {
		widget.center();
	}

	@Override
	public void setSize(int width, int height) {
		widget.setWidth(width + "px");
		contentScrollPanel.setHeight(height + "px");
		isSizeSet = true;
	}

	@Override
	public void setGlassEnabled(boolean isGlassEnabled) {
		widget.setGlassEnabled(isGlassEnabled);
	}

	@Override
	public void setModal(boolean isModal) {
		widget.setModal(isModal);
	}

	@Override
	public void setOnCloseHandler(ClickHandler closeHandler) {
		customCaption.setOnCloseHandler(closeHandler);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

}
