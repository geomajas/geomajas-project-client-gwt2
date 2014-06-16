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

package org.geomajas.plugin.print.example.client.sample;

import org.geomajas.plugin.print.client.template.PageSize;
import org.geomajas.plugin.print.client.util.PrintSettings;
import org.geomajas.plugin.print.client.widget.OptionsPrintPanel;
import org.geomajas.plugin.print.client.widget.PrintWidget;

/**
 * Extension of {@link PrintExamplePanel} for custom view.
 * 
 * @author Jan Venstermans
 */
public class PrintExampleCustomViewPanel extends PrintExamplePanel {

	@Override
	protected PrintWidget createPrintWidget() {
		OptionsPrintPanel optionsPrintPanel = new OptionsPrintPanel();
		// set some default options
		optionsPrintPanel.getDefaultPrintRequestDataProvider().getDefaultTemplateBuilderDataProvider()
				.setWithArrow(false);
		optionsPrintPanel.getDefaultPrintRequestDataProvider().getDefaultTemplateBuilderDataProvider()
				.setLandscape(false);
		optionsPrintPanel.getDefaultPrintRequestDataProvider().getDefaultTemplateBuilderDataProvider()
				.setPageSize(PageSize.A1);
		optionsPrintPanel.getDefaultPrintRequestDataProvider().setFileName("customFileName");
		optionsPrintPanel.getDefaultPrintRequestDataProvider().setPostPrintAction(PrintSettings.PostPrintAction.SAVE);

		// activate all options
		optionsPrintPanel.getOptionsToShowConfiguration().setShowTitleOption(true);
		optionsPrintPanel.getOptionsToShowConfiguration().setShowLandscapeOption(true);
		optionsPrintPanel.getOptionsToShowConfiguration().setShowPageSizeOption(true);
		optionsPrintPanel.getOptionsToShowConfiguration().setShowWithArrowOption(true);
		optionsPrintPanel.getOptionsToShowConfiguration().setShowWithScaleBarOption(true);
		optionsPrintPanel.getOptionsToShowConfiguration().setShowRasterDpiOption(true);
		optionsPrintPanel.getOptionsToShowConfiguration().setShowPostPrintActionOption(true);
		optionsPrintPanel.getOptionsToShowConfiguration().setShowFileNameOption(true);
		optionsPrintPanel.createViewBasedOnConfiguration();

		return new PrintWidget(getMapPresenter(), APPLICATION_ID, optionsPrintPanel);
	}
}