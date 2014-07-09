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

package org.geomajas.plugin.tms.example.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.example.base.client.sample.SamplePanelRegistry;
import org.geomajas.gwt2.example.base.client.sample.ShowcaseSampleDefinition;
import org.geomajas.plugin.tms.example.client.i18n.SampleMessages;
import org.geomajas.plugin.tms.example.client.sample.CapabilitiesPanel;
import org.geomajas.plugin.tms.example.client.sample.TmsLayerPanel;

/**
 * Entry point that adds TMS related samples to the showcase application.
 *
 * @author Pieter De Graef
 */
public class ExampleJar implements EntryPoint {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String CATEGORY_TMS = "TMS Plugin";

	public void onModuleLoad() {
		// Register all samples:
		registerSamples();
	}

	/**
	 * Register samples.
	 */
	private void registerSamples() {
		SamplePanelRegistry.registerFactory(CATEGORY_TMS, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new TmsLayerPanel();
			}

			public String getTitle() {
				return MESSAGES.tmsLayerTitle();
			}

			public String getShortDescription() {
				return MESSAGES.tmsLayerShort();
			}

			public String getDescription() {
				return MESSAGES.tmsLayerDescription();
			}

			public String getCategory() {
				return CATEGORY_TMS;
			}

			@Override
			public String getKey() {
				return "tmslayer";
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_TMS, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new CapabilitiesPanel();
			}

			public String getTitle() {
				return MESSAGES.capabilitiesTitle();
			}

			public String getShortDescription() {
				return MESSAGES.capabilitiesShort();
			}

			public String getDescription() {
				return MESSAGES.capabilitiesDescription();
			}

			public String getCategory() {
				return CATEGORY_TMS;
			}

			@Override
			public String getKey() {
				return "tmscapa";
			}
		});
	}
}
