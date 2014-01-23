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

package org.geomajas.plugin.graphicsediting.gwt2.example.client;

import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.example.base.client.sample.SamplePanelRegistry;
import org.geomajas.gwt2.example.base.client.sample.ShowcaseSampleDefinition;
import org.geomajas.plugin.graphicsediting.gwt2.example.client.i18n.GraphicsEditingSampleMessages;
import org.geomajas.plugin.graphicsediting.gwt2.example.client.sample.GraphicsEditingExample;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Class description.
 *
 * @author Jan De Moerloose
 */
public class GraphicsEditingExampleJar implements EntryPoint {
	private static final GraphicsEditingSampleMessages MESSAGES = GWT.create(GraphicsEditingSampleMessages.class);

	public static final String CATEGORY_GRAPHICS_EDITING = "Graphics Editing Plugin";

	public void onModuleLoad() {
		// Register all samples:
		registerSamples();
	}
	
	public static GraphicsEditingSampleMessages getMessages() {
		return MESSAGES;
	}

	private void registerSamples() {
		SamplePanelRegistry.registerFactory(CATEGORY_GRAPHICS_EDITING, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new GraphicsEditingExample(GraphicsEditingExample.EXAMPLE.CONTROLLER);
			}

			@Override
			public String getTitle() {
				return MESSAGES.graphicsEditingGeometryEditControllerTitle();
			}

			@Override
			public String getDescription() {
				return MESSAGES.graphicsEditingGeometryEditControllerDescription();
			}

			public String getShortDescription() {
				return MESSAGES.graphicsEditingGeometryEditControllerShortDescr();
			}

			public String getCategory() {
				return CATEGORY_GRAPHICS_EDITING;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_GRAPHICS_EDITING, new ShowcaseSampleDefinition() {

			@Override
			public SamplePanel create() {
				return new GraphicsEditingExample(GraphicsEditingExample.EXAMPLE.ACTION);
			}

			@Override
			public String getTitle() {
				return MESSAGES.graphicsEditingEditActionTitle();
			}

			@Override
			public String getShortDescription() {
				return MESSAGES.graphicsEditingEditActionShortDescr();
			}

			@Override
			public String getDescription() {
				return MESSAGES.graphicsEditingEditActionDescription();
			}

			@Override
			public String getCategory() {
				return CATEGORY_GRAPHICS_EDITING;
			}
		});
	}
}
