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
package org.geomajas.gwt2.widget.example.client.sample.feature.tooltip;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.widget.example.client.sample.feature.tooltip.i18n.ToolTipMessages;
import org.geomajas.gwt2.widget.example.client.sample.feature.tooltip.resource.ToolTipResource;

import java.util.List;

/**
 * Simple tooltip with an auto hide implementation.
 *
 * @author David Debuck
 *
 */
public class ToolTip implements IsWidget {

	private ToolTipMessages msg = GWT.create(ToolTipMessages.class);

	private PopupPanel toolTip;

	private Timer timer;

	private int delay = 1500; // 1.5s

	@UiField
	protected VerticalPanel contentPanel;

	private static final ToolTipBoxUiBinder UIBINDER = GWT.create(ToolTipBoxUiBinder.class);

	/**
	 * UI binder interface.
	 *
	 * @author David Debuck
	 *
	 */
	interface ToolTipBoxUiBinder extends UiBinder<Widget, ToolTip> {
	}

	/**
	 * Default constructor.
	 */
	public ToolTip() {
		toolTip = (PopupPanel) UIBINDER.createAndBindUi(this);

		ToolTipResource.INSTANCE.css().ensureInjected();
		toolTip.addStyleName(ToolTipResource.INSTANCE.css().toolTip());
	}

	/**
	 * Hide the tooltip.
	 */
	public void hide() {
		toolTip.hide();
	}

	/**
	 * Add content to the tooltip and show it with the given parameters.
	 *
	 * @param content a list of Strings
	 * @param left the left position of the tooltip
	 * @param top the top position of the tooltip
	 * @param autoHide hide the tooltip automatically after a certain time.
	 */
	public void addContentAndShow(List<String> content, int left, int top, boolean autoHide) {

		for (String s : content) {
			Label label = new Label(s);
			label.addStyleName(ToolTipResource.INSTANCE.css().toolTipLine());
			contentPanel.add(label);
		}

		// Create a timer for hiding the tooltip when autoHide is true.
		if (autoHide) {

			if (timer == null) {
				timer = new Timer() {

					public void run() {

						toolTip.hide();

					}
				};
				timer.schedule(delay);

			} else {
				timer.cancel();
				// extend the timer when this is run within the time of another.
				timer.schedule(delay);
			}

		} else {

			contentPanel.add(
					getCloseButton()
			);

		}

		// Finally set position of the tooltip and show it.
		toolTip.setPopupPosition(left, top);
		toolTip.show();

	}

	/**
	 * Clear the content of the tooltip.
	 */
	public void clearContent() {
		contentPanel.clear();
	}

	/**
	 * Set the delay for hiding the tooltip.
	 * The default is 1500 milliseconds.
	 *
	 * @param delay the time in milliseconds.
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}

	/**
	 * Create the closeButton for the tooltip.
	 *
	 * @return the closeButton
	 */
	private Button getCloseButton() {

		Button closeButton = new Button(msg.toolTipCloseButtonTitle());
		closeButton.addStyleName(ToolTipResource.INSTANCE.css().toolTipCloseButton());

		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				toolTip.hide();

			}
		});

		return closeButton;

	}

	@Override
	public Widget asWidget() {
		return toolTip;
	}

}