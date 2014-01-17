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

package org.geomajas.plugin.wms.example.client;

import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.example.base.client.sample.SamplePanelRegistry;
import org.geomajas.gwt2.example.base.client.sample.ShowcaseSampleDefinition;
import org.geomajas.plugin.wms.example.client.i18n.SampleMessages;
import org.geomajas.plugin.wms.example.client.sample.v1_1_1.CapabilitiesV111Panel;
import org.geomajas.plugin.wms.example.client.sample.v1_1_1.WmsFeatureInfoV111Panel;
import org.geomajas.plugin.wms.example.client.sample.v1_1_1.WmsLayerV111Panel;
import org.geomajas.plugin.wms.example.client.sample.v1_3_0.CapabilitiesV130Panel;
import org.geomajas.plugin.wms.example.client.sample.v1_3_0.WmsFeatureInfoV130Panel;
import org.geomajas.plugin.wms.example.client.sample.v1_3_0.WmsLayerV130Panel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Entry point and main class for the GWT client example application.
 * 
 * @author Pieter De Graef
 */
public class ExampleJar implements EntryPoint {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String CATEGORY_WMS = "WMS Plugin";

	public void onModuleLoad() {
		// Register all samples:
		registerGeneralSamples();
	}

	public static SampleMessages getMessages() {
		return MESSAGES;
	}

	private void registerGeneralSamples() {
		SamplePanelRegistry.registerFactory(CATEGORY_WMS, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new CapabilitiesV111Panel();
			}

			public String getTitle() {
				return MESSAGES.capabilitiesV111Title();
			}

			public String getShortDescription() {
				return MESSAGES.capabilitiesV111Short();
			}

			public String getDescription() {
				return MESSAGES.capabilitiesV111Description();
			}

			public String getCategory() {
				return CATEGORY_WMS;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_WMS, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new WmsLayerV111Panel();
			}

			public String getTitle() {
				return MESSAGES.wmsLayerV111Title();
			}

			public String getShortDescription() {
				return MESSAGES.wmsLayerV111Short();
			}

			public String getDescription() {
				return MESSAGES.wmsLayerV111Description();
			}

			public String getCategory() {
				return CATEGORY_WMS;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_WMS, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new WmsFeatureInfoV111Panel();
			}

			public String getTitle() {
				return MESSAGES.wmsGetFeatureInfoV111Title();
			}

			public String getShortDescription() {
				return MESSAGES.wmsGetFeatureInfoV111Short();
			}

			public String getDescription() {
				return MESSAGES.wmsGetFeatureInfoV111Description();
			}

			public String getCategory() {
				return CATEGORY_WMS;
			}
		});

		SamplePanelRegistry.registerFactory(CATEGORY_WMS, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new CapabilitiesV130Panel();
			}

			public String getTitle() {
				return MESSAGES.capabilitiesV130Title();
			}

			public String getShortDescription() {
				return MESSAGES.capabilitiesV130Short();
			}

			public String getDescription() {
				return MESSAGES.capabilitiesV130Description();
			}

			public String getCategory() {
				return CATEGORY_WMS;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_WMS, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new WmsLayerV130Panel();
			}

			public String getTitle() {
				return MESSAGES.wmsLayerV130Title();
			}

			public String getShortDescription() {
				return MESSAGES.wmsLayerV130Short();
			}

			public String getDescription() {
				return MESSAGES.wmsLayerV130Description();
			}

			public String getCategory() {
				return CATEGORY_WMS;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_WMS, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new WmsFeatureInfoV130Panel();
			}

			public String getTitle() {
				return MESSAGES.wmsGetFeatureInfoV130Title();
			}

			public String getShortDescription() {
				return MESSAGES.wmsGetFeatureInfoV130Short();
			}

			public String getDescription() {
				return MESSAGES.wmsGetFeatureInfoV130Description();
			}

			public String getCategory() {
				return CATEGORY_WMS;
			}
		});
	}
}