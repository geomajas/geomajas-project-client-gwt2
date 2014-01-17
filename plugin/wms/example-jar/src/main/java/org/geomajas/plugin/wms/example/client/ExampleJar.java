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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.example.base.client.sample.SamplePanelRegistry;
import org.geomajas.gwt2.example.base.client.sample.ShowcaseSampleDefinition;
import org.geomajas.plugin.wms.example.client.i18n.SampleMessages;
import org.geomajas.plugin.wms.example.client.sample.v1_1_1.CapabilitiesV111Panel;
import org.geomajas.plugin.wms.example.client.sample.v1_1_1.SelectStyleV111Panel;
import org.geomajas.plugin.wms.example.client.sample.v1_1_1.WmsFeatureInfoV111Panel;
import org.geomajas.plugin.wms.example.client.sample.v1_1_1.WmsLayerLegendV111Panel;
import org.geomajas.plugin.wms.example.client.sample.v1_1_1.WmsLayerV111Panel;
import org.geomajas.plugin.wms.example.client.sample.v1_3_0.CapabilitiesV130Panel;
import org.geomajas.plugin.wms.example.client.sample.v1_3_0.SelectStyleV130Panel;
import org.geomajas.plugin.wms.example.client.sample.v1_3_0.WmsFeatureInfoV130Panel;
import org.geomajas.plugin.wms.example.client.sample.v1_3_0.WmsLayerLegendV130Panel;
import org.geomajas.plugin.wms.example.client.sample.v1_3_0.WmsLayerV130Panel;

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
		registerV111Samples();
		registerV130Samples();
	}

	/** Register samples using WMS 1.1.1. */
	private void registerV111Samples() {
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
				return new SelectStyleV111Panel();
			}

			public String getTitle() {
				return MESSAGES.selectStyleV111Title();
			}

			public String getShortDescription() {
				return MESSAGES.selectStyleV111Short();
			}

			public String getDescription() {
				return MESSAGES.selectStyleV111Description();
			}

			public String getCategory() {
				return CATEGORY_WMS;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_WMS, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new WmsLayerLegendV111Panel();
			}

			public String getTitle() {
				return MESSAGES.wmsLayerLegendV111Title();
			}

			public String getShortDescription() {
				return MESSAGES.wmsLayerLegendV111Short();
			}

			public String getDescription() {
				return MESSAGES.wmsLayerLegendV111Description();
			}

			public String getCategory() {
				return CATEGORY_WMS;
			}
		});
	}

	/** Register samples using WMS 1.3.0. */
	private void registerV130Samples() {
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
		SamplePanelRegistry.registerFactory(CATEGORY_WMS, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new SelectStyleV130Panel();
			}

			public String getTitle() {
				return MESSAGES.selectStyleV130Title();
			}

			public String getShortDescription() {
				return MESSAGES.selectStyleV130Short();
			}

			public String getDescription() {
				return MESSAGES.selectStyleV130Description();
			}

			public String getCategory() {
				return CATEGORY_WMS;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_WMS, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new WmsLayerLegendV130Panel();
			}

			public String getTitle() {
				return MESSAGES.wmsLayerLegendV130Title();
			}

			public String getShortDescription() {
				return MESSAGES.wmsLayerLegendV130Short();
			}

			public String getDescription() {
				return MESSAGES.wmsLayerLegendV130Description();
			}

			public String getCategory() {
				return CATEGORY_WMS;
			}
		});
	}
}