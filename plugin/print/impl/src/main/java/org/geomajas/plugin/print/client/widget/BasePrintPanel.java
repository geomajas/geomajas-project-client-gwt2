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
package org.geomajas.plugin.print.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.plugin.print.client.util.PrintSettings;
import org.geomajas.plugin.print.client.i18n.PrintMessages;
import org.geomajas.plugin.print.client.template.PageSize;
import org.geomajas.plugin.print.client.template.TemplateBuilderDataProvider;

/**
 * Default implementation of {@link PrintWidgetView}.
 * 
 * @author An Buyle
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public class BasePrintPanel extends Composite implements PrintWidgetView, TemplateBuilderDataProvider {

	/**
	 * UI binder definition for the {@link } widget.
	 * 
	 * @author An Buyle
	 */
	interface PrintPanelUiBinder extends UiBinder<Widget, BasePrintPanel> {
	}

	private final PrintWidgetResource resource;

	private static final PrintMessages MESSAGES = GWT.create(PrintMessages.class);

	private static final PrintPanelUiBinder UIBINDER = GWT.create(PrintPanelUiBinder.class);

	@UiField
	protected Button printButton;

	@UiField
	protected TextBox titleTextBox;

	@UiField
	protected FlowPanel totalPanel;

	private PrintWidgetPresenter handler;

	@UiField
	protected RadioButton optionLandscapeOrientation;

	@UiField
	protected RadioButton optionPortraitOrientation;

	/** Default constructor. Create an instance using the default resource bundle and layout. */
	public BasePrintPanel() {
		this((PrintWidgetResource) GWT.create(PrintWidgetResource.class));
	}

	/**
	 * Create a new instance using a custom resource bundle.
	 * 
	 * @param resource The custom resource bundle to use.
	 */
	public BasePrintPanel(PrintWidgetResource resource) {
		this.resource = resource;

		// Inject the CSS and create the GUI:
		this.resource.css().ensureInjected();
		UIBINDER.createAndBindUi(this);

		// Composite.initWidget
		initWidget(totalPanel);

		printButton.setEnabled(true);

		titleTextBox.getElement().setAttribute("placeholder", MESSAGES.printPrefsTitlePlaceholder());

		final ClickHandler orientationOptionClickedHandler = new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (event != null) {
					optionLandscapeOrientation.setValue(event.getSource().equals(optionLandscapeOrientation));
					optionPortraitOrientation.setValue(event.getSource().equals(optionPortraitOrientation));
				}
			}
		};
		optionLandscapeOrientation.addClickHandler(orientationOptionClickedHandler);
		optionPortraitOrientation.addClickHandler(orientationOptionClickedHandler);

		// Defayult = Landscape
		optionLandscapeOrientation.setValue(true);
		optionPortraitOrientation.setValue(false);
	}

	@Override
	public void setHandler(PrintWidgetPresenter handler) {
		this.handler = handler;
	}

	@Override
	public TemplateBuilderDataProvider getTemplateBuilderDataProvider() {
		return this;
	}

	@Override
	public String getTitle() {
		String title = titleTextBox.getText().trim();
		if (title == null || title.isEmpty()) {
			title = MESSAGES.defaultPrintTitle();
		}
		return title;
	}

	@Override
	public boolean isLandscape() {
		return optionLandscapeOrientation.getValue();
	}

	@Override
	public PageSize getPageSize() {
		// default value
		return PageSize.A4;
		// TODO: get value from view element
	}

	@Override
	public boolean isWithArrow() {
		return true;
	}

	@Override
	public boolean isWithScaleBar() {
		return true;
	}

	@Override
	public Integer getRasterDpi() {
		return 200;
	}

	@Override
	public PrintSettings.PostPrintAction getActionType() {
		return PrintSettings.PostPrintAction.OPEN;
	}

	@Override
	public String getFileName() {
		return MESSAGES.defaultPrintFileName();
	}

	@UiHandler("printButton")
	public void onClick(ClickEvent event) {
		handler.print();
	}
}
