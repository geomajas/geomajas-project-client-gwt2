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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.widget.client.other.dialog.CloseableDialogBoxWidget;

/**
 * Closeable dialog widget showcase panel.
 *
 * @author Dosi Bingov
 * @author David Debuck
 */
public class CloseableDialogExample implements SamplePanel {

	private DockLayoutPanel rootElement;

	@UiField
	protected Button button;

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

				HTMLPanel temp = new HTMLPanel("");
				InlineLabel label = new InlineLabel("Sample text.");
				Button button = new Button("test");
				temp.add(label);
				temp.add(button);

				widget.addContent(label);

				widget.setGlassEnabled(true);
				widget.setModal(true);

				widget.setTitle("Window title");
				widget.setSize(400, 200);
				widget.center();
				widget.show();

				widget.setOnCloseHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {

						Window.alert("Close handler action.");

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