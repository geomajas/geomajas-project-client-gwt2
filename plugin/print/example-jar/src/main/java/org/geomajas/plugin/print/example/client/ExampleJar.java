/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.print.example.client;

import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.example.base.client.sample.SamplePanelRegistry;
import org.geomajas.gwt2.example.base.client.sample.ShowcaseSampleDefinition;
import org.geomajas.plugin.print.example.client.i18n.SampleMessages;
import org.geomajas.plugin.print.example.client.sample.PrintExamplePanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

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
		SamplePanelRegistry.registerCategory(CATEGORY_PRINT, 120);
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
		});
	}
}