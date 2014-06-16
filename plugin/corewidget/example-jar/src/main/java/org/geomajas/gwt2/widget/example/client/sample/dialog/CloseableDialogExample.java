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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.widget.client.other.dialog.CloseableDialogBoxWidget;
import org.geomajas.gwt2.widget.example.client.i18n.SampleMessages;

/**
 * Closeable dialog widget showcase panel.
 *
 * @author Dosi Bingov
 * @author David Debuck
 */
public class CloseableDialogExample implements SamplePanel {

	private SampleMessages msg = GWT.create(SampleMessages.class);

	private DockLayoutPanel rootElement;

	@UiField
	protected Button button;

	@UiField
	protected VerticalPanel layerEventLayout;

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

				InlineLabel label = new InlineLabel(msg.closeableDialogDescription());

				widget.addContent(label);

				widget.setGlassEnabled(true);
				widget.setModal(true);

				widget.setTitle(msg.closeableDialogTitle());
				widget.setSize(400, 200);
				widget.center();
				widget.show();

				widget.setOnCloseHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {

						layerEventLayout.add(new Label(msg.closeableDialogBoxHandlerMessage()));

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