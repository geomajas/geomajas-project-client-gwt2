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

package org.geomajas.gwt2.widget.client.other.dialog.caption;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.widget.client.other.dialog.CloseableDialogBoxWidgetView;

/**
 * Caption implementation for the CloseableDialogBoxWidget.
 *
 * @author David Debuck
 */
public class CloseableDialogBoxCaption extends FocusPanel implements DialogBox.Caption {

	private CloseableDialogBoxWidgetView view;

	private ClickHandler onCloseHandler;

	@UiField
	protected InlineLabel titleElement;

	@UiField
	protected Button closeButton;

	/**
	 * UI binder interface.
	 *
	 * @author David Debuck
	 */
	interface MyUiBinder extends UiBinder<Widget, CloseableDialogBoxCaption> {
	}

	private static final MyUiBinder UIBINDER = GWT.create(MyUiBinder.class);

	/**
	 * CloseableDialogBoxCaption constructor.
	 */
	public CloseableDialogBoxCaption() {

		add(UIBINDER.createAndBindUi(this));

		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				view.hide();
				if (onCloseHandler != null) {
					onCloseHandler.onClick(event);
				}
			}
		});

		addStyleName("Caption");
	}

	/**
	 * Set the parent view for this caption.
	 *
	 * @param view CloseableDialogBoxWidgetView
	 */
	public void setParentView(CloseableDialogBoxWidgetView view) {
		this.view = view;
	}

	/**
	 * Get the closeHandler set for this caption.
	 *
	 * @return ClickHandler
	 */
	public ClickHandler getOnCloseHandler() {
		return onCloseHandler;
	}

	/**
	 * Set the closeHandler for this caption.
	 *
	 * @param onCloseHandler ClickHandler
	 */
	public void setOnCloseHandler(ClickHandler onCloseHandler) {
		this.onCloseHandler = onCloseHandler;
	}

	@Override
	public String getHTML() {
		return titleElement.getElement().getInnerHTML();
	}

	@Override
	public void setHTML(String html) {
		titleElement.getElement().setInnerHTML(html);
	}

	@Override
	public String getText() {
		return titleElement.getElement().getInnerText();
	}

	@Override
	public void setText(String text) {
		titleElement.getElement().setInnerText(text);
	}

	@Override
	public void setHTML(SafeHtml html) {
		titleElement.getElement().setInnerHTML(html.asString());
	}

}
