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

package org.geomajas.gwt2.plugin.print.example.client;

import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.example.base.client.sample.SamplePanelRegistry;
import org.geomajas.gwt2.example.base.client.sample.ShowcaseSampleDefinition;
import org.geomajas.gwt2.plugin.print.example.client.i18n.SampleMessages;
import org.geomajas.gwt2.plugin.print.example.client.sample.PrintExampleCustomHandlerPanel;
import org.geomajas.gwt2.plugin.print.example.client.sample.PrintExampleCustomViewPanel;
import org.geomajas.gwt2.plugin.print.example.client.sample.PrintExamplePanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import org.geomajas.gwt2.plugin.print.example.client.sample.PrintExamplePrintServicePanel;
import org.geomajas.gwt2.plugin.print.example.client.sample.PrintExampleSvgLayerPanel;

/**
 * Entry point and main class for the GWT client example application.
 * 
 * @author Jan De Moerloose
 */
public class ExampleJar implements EntryPoint {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String CATEGORY_PRINT = "Print Plugin";

	public void onModuleLoad() {
		// Register all samples:
		registerGeneralSamples();
	}

	public static SampleMessages getMessages() {
		return MESSAGES;
	}

	private void registerGeneralSamples() {
		SamplePanelRegistry.registerFactory(CATEGORY_PRINT, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new PrintExamplePanel();
			}

			public String getTitle() {
				return MESSAGES.printTitle();
			}

			public String getShortDescription() {
				return MESSAGES.printShort();
			}

			public String getDescription() {
				return MESSAGES.printDescription();
			}

			public String getCategory() {
				return CATEGORY_PRINT;
			}

			@Override
			public String getKey() {
				return "printplugin";
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_PRINT, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new PrintExampleCustomViewPanel();
			}

			public String getTitle() {
				return MESSAGES.printCustomViewTitle();
			}

			public String getShortDescription() {
				return MESSAGES.printCustomViewShort();
			}

			public String getDescription() {
				return MESSAGES.printCustomViewDescription();
			}

			public String getCategory() {
				return CATEGORY_PRINT;
			}

			@Override
			public String getKey() {
				return "printplugin-customview";
			}

		});
		SamplePanelRegistry.registerFactory(CATEGORY_PRINT, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new PrintExampleCustomHandlerPanel();
			}

			public String getTitle() {
				return MESSAGES.printCustomHandlerTitle();
			}

			public String getShortDescription() {
				return MESSAGES.printCustomHandlerShort();
			}

			public String getDescription() {
				return MESSAGES.printCustomHandlerDescription();
			}

			public String getCategory() {
				return CATEGORY_PRINT;
			}

			@Override
			public String getKey() {
				return "printplugin-customhandler";
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_PRINT, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new PrintExampleSvgLayerPanel();
			}

			public String getTitle() {
				return MESSAGES.printSvgLayerTitle();
			}

			public String getShortDescription() {
				return MESSAGES.printSvgLayerShort();
			}

			public String getDescription() {
				return MESSAGES.printSvgLayerDescription();
			}

			public String getCategory() {
				return CATEGORY_PRINT;
			}

			@Override
			public String getKey() {
				return "printplugin-svglayer";
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_PRINT, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new PrintExamplePrintServicePanel();
			}

			public String getTitle() {
				return MESSAGES.printServiceExampleTitle();
			}

			public String getShortDescription() {
				return MESSAGES.printServiceExampleShort();
			}

			public String getDescription() {
				return MESSAGES.printServiceExampleDescription();
			}

			public String getCategory() {
				return CATEGORY_PRINT;
			}

			@Override
			public String getKey() {
				return "printplugin-printservice";
			}
		});
	}
}