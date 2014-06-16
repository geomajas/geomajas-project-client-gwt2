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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.plugin.print.client.Print;
import org.geomajas.plugin.print.client.i18n.PrintMessages;
import org.geomajas.plugin.print.client.template.PageSize;
import org.geomajas.plugin.print.client.template.TemplateBuilderDataProvider;
import org.geomajas.plugin.print.client.util.PrintSettings;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link org.geomajas.plugin.print.client.widget.PrintWidgetView}, enable changing all data elements.
 *
 * @author An Buyle
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public class FullOptionsPrintPanel extends Composite implements PrintWidgetView, TemplateBuilderDataProvider {

	/**
	 * UI binder definition.
	 *
	 * @author An Buyle
	 */
	interface PrintPanelUiBinder extends UiBinder<Widget, FullOptionsPrintPanel> {
	}

	private final PrintWidgetResource resource;

	private static final PrintMessages MESSAGES = GWT.create(PrintMessages.class);

	private static final PrintPanelUiBinder UIBINDER = GWT.create(PrintPanelUiBinder.class);

	@UiField
	protected Button printButton;

	@UiField
	protected TextBox titleTextBox;

	@UiField
	protected TextBox rasterDpiTextBox;

	@UiField
	protected TextBox fileNameTextBox;

	@UiField
	protected FlowPanel totalPanel;

	@UiField
	protected ListBox pageSizeListBox;

	private PrintWidgetPresenter handler;

	//
	// private TextItem fileNameItem;
	//
	//
	@UiField
	protected RadioButton optionLandscapeOrientation;

	@UiField
	protected RadioButton optionPortraitOrientation;

	@UiField
	protected CheckBox arrowCheckBox;

	@UiField
	protected CheckBox scaleBarBox;

	@UiField
	protected VerticalPanel postPrintActionRadioGroup;

	private Map<PrintSettings.PostPrintAction, RadioButton> postPrintActionRadioButtonMap =
			new HashMap<PrintSettings.PostPrintAction, RadioButton>();

	/** Default constructor. Create an instance using the default resource bundle and layout. */
	public FullOptionsPrintPanel() {
		this((PrintWidgetResource) GWT.create(PrintWidgetResource.class));
	}

	/**
	 * Create a new instance using a custom resource bundle.
	 *
	 * @param resource The custom resource bundle to use.
	 */
	public FullOptionsPrintPanel(PrintWidgetResource resource) {
		this.resource = resource;

		// Inject the CSS and create the GUI:
		this.resource.css().ensureInjected();
		UIBINDER.createAndBindUi(this);

		// Composite.initWidget
		initWidget(totalPanel);

		printButton.setEnabled(true);

		titleTextBox.getElement().setAttribute("placeholder", MESSAGES.printPrefsTitlePlaceholder());

		// fill the pageSizeListBox
		for (String pageSizeName : PageSize.getAllNames()) {
			pageSizeListBox.addItem(pageSizeName);
		}
		// fill the postPrintActionRadioButtonMap
		postPrintActionRadioButtonMap.clear();
		for (PrintSettings.PostPrintAction postPrintAction : PrintSettings.PostPrintAction.values()) {
			RadioButton radioButton = new RadioButton("postPrintAction",
					Print.getInstance().getPrintUtil().toString(postPrintAction));
			postPrintActionRadioButtonMap.put(postPrintAction, radioButton);
			postPrintActionRadioGroup.add(radioButton);
		}

		// default values
		optionLandscapeOrientation.setValue(true);
		scaleBarBox.setValue(true);
		arrowCheckBox.setValue(false);
		rasterDpiTextBox.setText("200");
		postPrintActionRadioButtonMap.get(PrintSettings.PostPrintAction.OPEN).setValue(true);
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
		return PageSize.getByName(pageSizeListBox.getValue(pageSizeListBox.getSelectedIndex()));
	}

	@Override
	public boolean isWithArrow() {
		return arrowCheckBox.getValue();
	}

	@Override
	public boolean isWithScaleBar() {
		return scaleBarBox.getValue();
	}

	@Override
	public Integer getRasterDpi() {
		// TODO turn into a slider?: get value from (Integer) rasterDpiSlider.getValue()
		try {
			return Integer.parseInt(rasterDpiTextBox.getText());
		} catch (NumberFormatException nfe) {
			return null;
		}
	}

	@Override
	public PrintSettings.PostPrintAction getActionType() {
		for (Map.Entry<PrintSettings.PostPrintAction, RadioButton> entry : postPrintActionRadioButtonMap.entrySet()) {
			if (entry.getValue().getValue()) {
				return entry.getKey();
			}
		}
		return null;
	}

	@Override
	public String getFileName() {
		String fileName = fileNameTextBox.getText();
		if (fileName != null && !fileName.isEmpty()) {
			return fileName;
		}
		return MESSAGES.defaultPrintFileName();
	}

	@UiHandler("printButton")
	public void onClick(ClickEvent event) {
		// validation
		if (getRasterDpi() == null) {
			Window.alert("The dpi must be an integer value");
			return;
		}
		if (getActionType() == null) {
			Window.alert("Incorrect Action Type");
			return;
		}
		handler.print();
	}
}
