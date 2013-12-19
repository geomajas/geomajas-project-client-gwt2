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

package org.geomajas.plugin.editing.example.client;

import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.example.base.client.sample.SamplePanelRegistry;
import org.geomajas.gwt2.example.base.client.sample.ShowcaseSampleDefinition;
import org.geomajas.plugin.editing.example.client.i18n.SampleMessages;
import org.geomajas.plugin.editing.example.client.sample.EditingPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Entry point and main class for the GWT client example application.
 * 
 * @author Pieter De Graef
 */
public class ExampleJar implements EntryPoint {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String CATEGORY_EDITING = "Editing Plugin";

	public void onModuleLoad() {
		// Register all samples:
		registerGeneralSamples();
	}

	public static SampleMessages getMessages() {
		return MESSAGES;
	}

	private void registerGeneralSamples() {
		SamplePanelRegistry.registerCategory(CATEGORY_EDITING, 120);
		SamplePanelRegistry.registerFactory(CATEGORY_EDITING, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new EditingPanel();
			}

			public String getTitle() {
				return MESSAGES.editingTitle();
			}

			public String getShortDescription() {
				return MESSAGES.editingShort();
			}

			public String getDescription() {
				return MESSAGES.editingDescription();
			}

			public String getCategory() {
				return CATEGORY_EDITING;
			}
		});
	}
}