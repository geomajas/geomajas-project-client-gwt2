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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.plugin.print.client.Print;
import org.geomajas.plugin.print.client.template.PageSize;
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
public class OptionsPrintPanel extends AbstractPrintWidgetView {

	/**
	 * UI binder definition.
	 *
	 * @author An Buyle
	 */
	interface PrintPanelUiBinder extends UiBinder<Widget, OptionsPrintPanel> {
	}

	private static final PrintPanelUiBinder UIBINDER = GWT.create(PrintPanelUiBinder.class);

	@UiField
	protected VerticalPanel totalPanel;

	@UiField
	protected Button printButton;

	/* elements of view that will be created if configuration requires it */

	@UiField
	protected TextBox titleTextBox;

	@UiField
	protected TextBox rasterDpiTextBox;

	@UiField
	protected TextBox fileNameTextBox;

	@UiField
	protected ListBox pageSizeListBox;

	@UiField
	protected RadioButton optionLandscapeOrientation;

	@UiField
	protected RadioButton optionPortraitOrientation;

	@UiField
	protected VerticalPanel postPrintActionRadioGroup;

	@UiField
	protected CheckBox arrowCheckBox;

	@UiField
	protected CheckBox scaleBarBox;

	@UiField
	protected VerticalPanel titleSection;
	@UiField
	protected VerticalPanel orientationSection;
	@UiField
	protected VerticalPanel pageSizeSection;
	@UiField
	protected HorizontalPanel withArrowSection;
	@UiField
	protected HorizontalPanel withScaleBarSection;
	@UiField
	protected VerticalPanel rasterDpiSection;
	@UiField
	protected VerticalPanel postPrintActionSection;
	@UiField
	protected VerticalPanel fileNameSection;

	private Map<PrintSettings.PostPrintAction, RadioButton> postPrintActionRadioButtonMap =
			new HashMap<PrintSettings.PostPrintAction, RadioButton>();

	private OptionsToShowConfiguration optionsToShowConfiguration = new OptionsToShowConfiguration();

	/** Default constructor. Create an instance using the default resource bundle and layout. */
	public OptionsPrintPanel() {
		this((PrintWidgetResource) GWT.create(PrintWidgetResource.class));
	}

	/**
	 * Create a new instance using a custom resource bundle.
	 *
	 * @param resource The custom resource bundle to use.
	 */
	public OptionsPrintPanel(PrintWidgetResource resource) {
		super(resource);

		UIBINDER.createAndBindUi(this);

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
		createViewBasedOnConfiguration();
	}

	public OptionsToShowConfiguration getOptionsToShowConfiguration() {
		return optionsToShowConfiguration;
	}

	/* override the configured options */
	@Override
	public String getTitle() {
		if (optionsToShowConfiguration.isShowTitleOption())  {
			String title = titleTextBox.getText().trim();
			if (title != null && !title.isEmpty()) {
				return title;
			}
		}
		return super.getTitle();
	}

	@Override
	public boolean isLandscape() {
		if (optionsToShowConfiguration.isShowLandscapeOption())  {
			return optionLandscapeOrientation.getValue();
		}
		return super.isLandscape();
	}

	@Override
	public PageSize getPageSize() {
		if (optionsToShowConfiguration.isShowPageSizeOption())  {
			return PageSize.getByName(pageSizeListBox.getValue(pageSizeListBox.getSelectedIndex()));
		}
		return super.getPageSize();
	}

	@Override
	public boolean isWithArrow() {
		if (optionsToShowConfiguration.isShowWithArrowOption())  {
			return arrowCheckBox.getValue();
		}
		return super.isWithArrow();
	}

	@Override
	public boolean isWithScaleBar() {
		if (optionsToShowConfiguration.isShowWithScaleBarOption())  {
			return scaleBarBox.getValue();
		}
		return super.isWithScaleBar();
	}

	@Override
	public Integer getRasterDpi() {
		if (optionsToShowConfiguration.isShowRasterDpiOption())  {
			// TODO turn into a slider?: get value from (Integer) rasterDpiSlider.getValue()
			try {
				return Integer.parseInt(rasterDpiTextBox.getText());
			} catch (NumberFormatException nfe) {
				return null;
			}
		}
		return super.getRasterDpi();
	}

	@Override
	public PrintSettings.PostPrintAction getPostPrintAction() {
		if (optionsToShowConfiguration.isShowPostPrintActionOption())  {
			for (Map.Entry<PrintSettings.PostPrintAction, RadioButton> entry :
					postPrintActionRadioButtonMap.entrySet()) {
				if (entry.getValue().getValue()) {
					return entry.getKey();
				}
			}
			return null;
		}
		return super.getPostPrintAction();
	}

	@Override
	public String getFileName() {
		if (optionsToShowConfiguration.isShowFileNameOption())  {
			String fileName = fileNameTextBox.getText();
			if (fileName != null && !fileName.isEmpty()) {
				return fileName;
			}
		}
		// return default value
		return super.getFileName();
	}

	@UiHandler("printButton")
	public void onClick(ClickEvent event) {
		if (validate()) {
			handler.print();
		}
	}

	protected boolean validate() {
		if (getRasterDpi() == null) {
			Window.alert("The dpi must be an integer value");
			return false;
		}
		if (getPostPrintAction() == null) {
			Window.alert("Incorrect Action Type");
			return false;
		}
		return true;
	}

	@Override
	public Widget asWidget() {
		return totalPanel;
	}

	/**
	 * Will set current default values and show/hide options.
	 */
	public void createViewBasedOnConfiguration() {
		// set default values, when appropriate
		arrowCheckBox.setValue(super.isWithArrow());
		scaleBarBox.setValue(super.isWithScaleBar());
		rasterDpiTextBox.setText(super.getRasterDpi().toString());
		if (super.isLandscape()) {
			optionLandscapeOrientation.setValue(true);
		} else {
			optionPortraitOrientation.setValue(true);
		}
		postPrintActionRadioButtonMap.get(super.getPostPrintAction()).setValue(true);

		// show/hide option sections
		titleSection.setVisible(getOptionsToShowConfiguration().isShowTitleOption());
		orientationSection.setVisible(getOptionsToShowConfiguration().isShowLandscapeOption());
		pageSizeSection.setVisible(getOptionsToShowConfiguration().isShowPageSizeOption());
		withArrowSection.setVisible(getOptionsToShowConfiguration().isShowWithArrowOption());
		withScaleBarSection.setVisible(getOptionsToShowConfiguration().isShowWithScaleBarOption());
		rasterDpiSection.setVisible(getOptionsToShowConfiguration().isShowRasterDpiOption());
		postPrintActionSection.setVisible(getOptionsToShowConfiguration().isShowPostPrintActionOption());
		fileNameSection.setVisible(getOptionsToShowConfiguration().isShowFileNameOption());
	}

	private String getDisplayOption(boolean visible) {
		return visible ? "inline" : "none";
	}

	private String getVisibilityOption(boolean visible) {
		return visible ? "visible" : "hidden";
	}

	/**
	 * Contains booleans for the configuration of the visibility of the panel elements.
	 */
	public class OptionsToShowConfiguration {
		private boolean showTitleOption;

		private boolean showLandscapeOption;

		private boolean showPageSizeOption;

		private boolean showWithArrowOption;

		private boolean showWithScaleBarOption;

		private boolean showRasterDpiOption;

		private boolean showPostPrintActionOption;

		private boolean showFileNameOption;

		public boolean isShowTitleOption() {
			return showTitleOption;
		}

		public void setShowTitleOption(boolean showTitleOption) {
			this.showTitleOption = showTitleOption;
		}

		public boolean isShowLandscapeOption() {
			return showLandscapeOption;
		}

		public void setShowLandscapeOption(boolean showLandscapeOption) {
			this.showLandscapeOption = showLandscapeOption;
		}

		public boolean isShowPageSizeOption() {
			return showPageSizeOption;
		}

		public void setShowPageSizeOption(boolean showPageSizeOption) {
			this.showPageSizeOption = showPageSizeOption;
		}

		public boolean isShowWithArrowOption() {
			return showWithArrowOption;
		}

		public void setShowWithArrowOption(boolean showWithArrowOption) {
			this.showWithArrowOption = showWithArrowOption;
		}

		public boolean isShowWithScaleBarOption() {
			return showWithScaleBarOption;
		}

		public void setShowWithScaleBarOption(boolean showWithScaleBarOption) {
			this.showWithScaleBarOption = showWithScaleBarOption;
		}

		public boolean isShowRasterDpiOption() {
			return showRasterDpiOption;
		}

		public void setShowRasterDpiOption(boolean showRasterDpiOption) {
			this.showRasterDpiOption = showRasterDpiOption;
		}

		public boolean isShowPostPrintActionOption() {
			return showPostPrintActionOption;
		}

		public void setShowPostPrintActionOption(boolean showPostPrintActionOption) {
			this.showPostPrintActionOption = showPostPrintActionOption;
		}

		public boolean isShowFileNameOption() {
			return showFileNameOption;
		}

		public void setShowFileNameOption(boolean showFileNameOption) {
			this.showFileNameOption = showFileNameOption;
		}
	}
}
