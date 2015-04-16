/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.plugin.wfs.example.client;

import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.example.base.client.sample.SamplePanelRegistry;
import org.geomajas.gwt2.example.base.client.sample.ShowcaseSampleDefinition;
import org.geomajas.gwt2.plugin.wfs.example.client.sample.WfsCapabilitiesPanel;

import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point and main class for the GWT client example application.
 *
 * @author Jan De Moerloose
 */
public class ServerExampleJar implements EntryPoint {

	public static final String CATEGORY_WFS = "WFS Plugin";

	public void onModuleLoad() {
		// Register all samples:
		registerSamples();
	}

	/**
	 * Register samples.
	 */
	private void registerSamples() {

		SamplePanelRegistry.registerFactory(CATEGORY_WFS, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new WfsCapabilitiesPanel();
			}

			public String getTitle() {
				return "";
			}

			public String getShortDescription() {
				return "";
			}

			public String getDescription() {
				return "";
			}

			public String getCategory() {
				return CATEGORY_WFS;
			}

			@Override
			public String getKey() {
				return "wfscapabilities";
			}
		});
	}
}
