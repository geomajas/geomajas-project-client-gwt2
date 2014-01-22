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
import org.geomajas.plugin.editing.example.client.sample.EditLinePanel;
import org.geomajas.plugin.editing.example.client.sample.EditPointPanel;
import org.geomajas.plugin.editing.example.client.sample.EditPolygonPanel;
import org.geomajas.plugin.editing.example.client.sample.EditingPanel;
import org.geomajas.plugin.editing.example.client.sample.MergeCountriesPanel;
import org.geomajas.plugin.editing.example.client.sample.SnapToCountriesPanel;
import org.geomajas.plugin.editing.example.client.sample.SplitCountryPanel;
import org.geomajas.plugin.editing.example.client.sample.UndoRedoPanel;

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
		SamplePanelRegistry.registerFactory(CATEGORY_EDITING, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new EditPointPanel();
			}

			public String getTitle() {
				return MESSAGES.editPointTitle();
			}

			public String getShortDescription() {
				return MESSAGES.editPointShort();
			}

			public String getDescription() {
				return MESSAGES.editPointDescription();
			}

			public String getCategory() {
				return CATEGORY_EDITING;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_EDITING, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new EditLinePanel();
			}

			public String getTitle() {
				return MESSAGES.editLineTitle();
			}

			public String getShortDescription() {
				return MESSAGES.editLineShort();
			}

			public String getDescription() {
				return MESSAGES.editLineDescription();
			}

			public String getCategory() {
				return CATEGORY_EDITING;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_EDITING, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new EditPolygonPanel();
			}

			public String getTitle() {
				return MESSAGES.editPolygonTitle();
			}

			public String getShortDescription() {
				return MESSAGES.editPolygonShort();
			}

			public String getDescription() {
				return MESSAGES.editPolygonDescription();
			}

			public String getCategory() {
				return CATEGORY_EDITING;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_EDITING, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new UndoRedoPanel();
			}

			public String getTitle() {
				return MESSAGES.undoRedoTitle();
			}

			public String getShortDescription() {
				return MESSAGES.undoRedoShort();
			}

			public String getDescription() {
				return MESSAGES.undoRedoDescription();
			}

			public String getCategory() {
				return CATEGORY_EDITING;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_EDITING, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new SnapToCountriesPanel();
			}

			public String getTitle() {
				return MESSAGES.snapToCountriesTitle();
			}

			public String getShortDescription() {
				return MESSAGES.snapToCountriesShort();
			}

			public String getDescription() {
				return MESSAGES.snapToCountriesDescription();
			}

			public String getCategory() {
				return CATEGORY_EDITING;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_EDITING, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new SplitCountryPanel();
			}

			public String getTitle() {
				return MESSAGES.splitCountryTitle();
			}

			public String getShortDescription() {
				return MESSAGES.splitCountryShort();
			}

			public String getDescription() {
				return MESSAGES.splitCountryDescription();
			}

			public String getCategory() {
				return CATEGORY_EDITING;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_EDITING, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new MergeCountriesPanel();
			}

			public String getTitle() {
				return MESSAGES.mergeCountriesTitle();
			}

			public String getShortDescription() {
				return MESSAGES.mergeCountriesShort();
			}

			public String getDescription() {
				return MESSAGES.mergeCountriesDescription();
			}

			public String getCategory() {
				return CATEGORY_EDITING;
			}
		});
	}
}