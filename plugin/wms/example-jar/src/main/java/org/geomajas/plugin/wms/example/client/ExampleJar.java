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

package org.geomajas.plugin.wms.example.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.example.base.client.sample.SamplePanelRegistry;
import org.geomajas.gwt2.example.base.client.sample.ShowcaseSampleDefinition;
import org.geomajas.plugin.wms.example.client.i18n.SampleMessages;
import org.geomajas.plugin.wms.example.client.sample.CapabilitiesPanel;
import org.geomajas.plugin.wms.example.client.sample.IsFeaturesSupportedPanel;
import org.geomajas.plugin.wms.example.client.sample.SelectStylePanel;
import org.geomajas.plugin.wms.example.client.sample.WmsFeatureInfoPanel;
import org.geomajas.plugin.wms.example.client.sample.WmsLayerLegendPanel;
import org.geomajas.plugin.wms.example.client.sample.WmsLayerPanel;
import org.geomajas.plugin.wms.example.client.sample.WmsSearchByLocationPanel;

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
		registerSamples();
	}

	/**
	 * Register samples.
	 */
	private void registerSamples() {
		SamplePanelRegistry.registerFactory(CATEGORY_WMS, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new WmsLayerPanel();
			}

			public String getTitle() {
				return MESSAGES.wmsLayerTitle();
			}

			public String getShortDescription() {
				return MESSAGES.wmsLayerShort();
			}

			public String getDescription() {
				return MESSAGES.wmsLayerDescription();
			}

			public String getCategory() {
				return CATEGORY_WMS;
			}

			@Override
			public String getKey() {
				return "basicwmslayer";
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_WMS, new ShowcaseSampleDefinition() {

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
				return CATEGORY_WMS;
			}

			@Override
			public String getKey() {
				return "wmsgetcapabilities";
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_WMS, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new WmsFeatureInfoPanel();
			}

			public String getTitle() {
				return MESSAGES.wmsGetFeatureInfoTitle();
			}

			public String getShortDescription() {
				return MESSAGES.wmsGetFeatureInfoShort();
			}

			public String getDescription() {
				return MESSAGES.wmsGetFeatureInfoDescription();
			}

			public String getCategory() {
				return CATEGORY_WMS;
			}

			@Override
			public String getKey() {
				return "wmsgetfeatureinfo";
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_WMS, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new IsFeaturesSupportedPanel();
			}

			public String getTitle() {
				return MESSAGES.isFeaturesSupportedTitle();
			}

			public String getShortDescription() {
				return MESSAGES.isFeaturesSupportedShort();
			}

			public String getDescription() {
				return MESSAGES.isFeaturesSupportedDescription();
			}

			public String getCategory() {
				return CATEGORY_WMS;
			}

			@Override
			public String getKey() {
				return "isfeaturessupported";
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_WMS, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new WmsSearchByLocationPanel();
			}

			public String getTitle() {
				return MESSAGES.wmsSearchByLocationTitle();
			}

			public String getShortDescription() {
				return MESSAGES.wmsSearchByLocationShort();
			}

			public String getDescription() {
				return MESSAGES.wmsSearchByLocationDescription();
			}

			public String getCategory() {
				return CATEGORY_WMS;
			}

			@Override
			public String getKey() {
				return "wmssearchbylocation";
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_WMS, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new SelectStylePanel();
			}

			public String getTitle() {
				return MESSAGES.selectStyleTitle();
			}

			public String getShortDescription() {
				return MESSAGES.selectStyleShort();
			}

			public String getDescription() {
				return MESSAGES.selectStyleDescription();
			}

			public String getCategory() {
				return CATEGORY_WMS;
			}

			@Override
			public String getKey() {
				return "selectstyle";
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_WMS, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new WmsLayerLegendPanel();
			}

			public String getTitle() {
				return MESSAGES.wmsLayerLegendTitle();
			}

			public String getShortDescription() {
				return MESSAGES.wmsLayerLegendShort();
			}

			public String getDescription() {
				return MESSAGES.wmsLayerLegendDescription();
			}

			public String getCategory() {
				return CATEGORY_WMS;
			}

			@Override
			public String getKey() {
				return "wmslayerlegend";
			}
		});
	}
}