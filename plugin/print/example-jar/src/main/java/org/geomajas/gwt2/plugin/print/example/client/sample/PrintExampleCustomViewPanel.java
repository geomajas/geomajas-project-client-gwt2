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

package org.geomajas.gwt2.plugin.print.example.client.sample;

import org.geomajas.gwt2.plugin.print.client.template.PageSize;
import org.geomajas.gwt2.plugin.print.client.util.PrintConfiguration;
import org.geomajas.gwt2.plugin.print.client.widget.OptionsPrintPanel;
import org.geomajas.gwt2.plugin.print.client.widget.PrintWidget;
import org.geomajas.gwt2.plugin.print.example.client.i18n.SampleMessages;

import com.google.gwt.core.client.GWT;

/**
 * Extension of {@link PrintExamplePanel} for custom view.
 * 
 * @author Jan Venstermans
 */
public class PrintExampleCustomViewPanel extends PrintExamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	@Override
	protected PrintWidget createPrintWidget() {
		OptionsPrintPanel optionsPrintPanel = new OptionsPrintPanel();
		// set some default options
		optionsPrintPanel.getDefaultPrintRequestDataProvider().getDefaultTemplateBuilderDataProvider()
				.setWithArrow(false);
		optionsPrintPanel.getDefaultPrintRequestDataProvider().getDefaultTemplateBuilderDataProvider()
				.setLandscape(true);
		optionsPrintPanel.getDefaultPrintRequestDataProvider().getDefaultTemplateBuilderDataProvider()
				.setPageSize(PageSize.A1);
		optionsPrintPanel.getDefaultPrintRequestDataProvider().getDefaultTemplateBuilderDataProvider().setDpi(72);
		optionsPrintPanel.getDefaultPrintRequestDataProvider().getDefaultTemplateBuilderDataProvider().setRasterDpi(72);
		optionsPrintPanel.getDefaultPrintRequestDataProvider().setFileName(MESSAGES.printCustomViewFileName());
		optionsPrintPanel.getDefaultPrintRequestDataProvider().setPostPrintAction(
				PrintConfiguration.PostPrintAction.SAVE);
		optionsPrintPanel.getDefaultPrintRequestDataProvider().setSync(false);

		// activate all options
		optionsPrintPanel.getOptionsToShowConfiguration().setShowTitleOption(true);
		optionsPrintPanel.getOptionsToShowConfiguration().setShowLandscapeOption(true);
		optionsPrintPanel.getOptionsToShowConfiguration().setShowPageSizeOption(true);
		optionsPrintPanel.getOptionsToShowConfiguration().setShowWithArrowOption(true);
		optionsPrintPanel.getOptionsToShowConfiguration().setShowWithScaleBarOption(true);
		optionsPrintPanel.getOptionsToShowConfiguration().setShowDpiOption(true);
		optionsPrintPanel.getOptionsToShowConfiguration().setShowRasterDpiOption(true);
		optionsPrintPanel.getOptionsToShowConfiguration().setShowPostPrintActionOption(true);
		optionsPrintPanel.getOptionsToShowConfiguration().setShowFileNameOption(true);
		optionsPrintPanel.getOptionsToShowConfiguration().setShowSyncOption(true);
		optionsPrintPanel.createViewBasedOnConfiguration();

		return new PrintWidget(getMapPresenter(), APPLICATION_ID, optionsPrintPanel);
	}
}