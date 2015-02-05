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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.plugin.corewidget.example.client.i18n.SampleMessages;

/**
 * Closeable dialog widget showcase panel.
 *
 * @author Dosi Bingov
 * @author David Debuck
 */
public class CloseableDialogExample implements SamplePanel {

	private SampleMessages msg = GWT.create(SampleMessages.class);

	private DockLayoutPanel rootElement;

	private int clicked;

	@UiField
	protected Button button;

	@UiField
	protected VerticalPanel layerEventLayout;

	@UiField
	protected ScrollPanel scrollPanel;

	private static final CloseableDialogUiBinder UIBINDER = GWT.create(CloseableDialogUiBinder.class);

	/**
	 * UI binder interface.
	 */
	interface CloseableDialogUiBinder extends UiBinder<DockLayoutPanel, CloseableDialogExample> {
	}

	public CloseableDialogExample() {
		rootElement = UIBINDER.createAndBindUi(this);

		button.addMouseDownHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent mouseDownEvent) {

				final CloseableDialogBoxWidget widget = new CloseableDialogBoxWidget();

				VerticalPanel panel = new VerticalPanel();
				panel.setWidth("100%");
				panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

				HorizontalPanel top = new HorizontalPanel();
				top.setSpacing(10);
				InlineLabel labelTop = new InlineLabel(msg.closeableDialogBoxExampleLabel());
				top.add(labelTop);

				HorizontalPanel middle = new HorizontalPanel();
				middle.setSpacing(10);
				final Button middleButton = new Button(msg.closeableDialogBoxExampleButton());
				middle.add(middleButton);

				HorizontalPanel bottom = new HorizontalPanel();
				bottom.setSpacing(10);
				final InlineLabel bottomlabel = new InlineLabel(msg.loremIpsum());
				bottom.add(bottomlabel);

				middleButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {

						if (clicked < 3) {
							bottomlabel.setText(
									bottomlabel.getText() + bottomlabel.getText()
							);
							layerEventLayout.add(new Label(msg.closeableDialogBoxExampleButtonMessage()));
							scrollPanel.scrollToBottom();
							clicked++;

							if (clicked == 3) {
								middleButton.setText(msg.closeableDialogBoxExampleButtonClear());
							}
						} else {

							layerEventLayout.add(new Label(msg.closeableDialogBoxExampleButtonClearMessage()));
							scrollPanel.scrollToBottom();
							middleButton.setText(msg.closeableDialogBoxExampleButton());
							bottomlabel.setText(msg.loremIpsum());
							clicked = 0;
						}

					}
				});

				panel.add(top);
				panel.add(middle);
				panel.add(bottom);

				widget.addContent(panel);

				widget.setGlassEnabled(true);
				widget.setModal(true);

				widget.setTitle(msg.closeableDialogTitle());
				widget.setSize(380, 150);
				widget.center();
				widget.show();

				widget.setOnCloseHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {

						layerEventLayout.add(new Label(msg.closeableDialogBoxHandlerMessage()));
						scrollPanel.scrollToBottom();

					}
				});

			}
		});

	}

	@Override
	public Widget asWidget() {
		return rootElement;
	}
}