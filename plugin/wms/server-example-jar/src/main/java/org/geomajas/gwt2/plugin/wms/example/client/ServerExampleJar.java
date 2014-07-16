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

package org.geomajas.gwt2.plugin.wms.example.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.example.base.client.sample.SamplePanelRegistry;
import org.geomajas.gwt2.example.base.client.sample.ShowcaseSampleDefinition;
import org.geomajas.gwt2.plugin.wms.example.client.i18n.ServerSampleMessages;
import org.geomajas.gwt2.plugin.wms.example.client.sample.WmsSearchByLocationPanel;
import org.geomajas.gwt2.plugin.wms.example.client.sample.IsFeaturesSupportedPanel;
import org.geomajas.gwt2.plugin.wms.example.client.sample.WmsFeatureInfoPanel;

/**
 * Entry point and main class for the GWT client example application.
 *
 * @author Pieter De Graef
 */
public class ServerExampleJar implements EntryPoint {

	private static final ServerSampleMessages MESSAGES = GWT.create(ServerSampleMessages.class);

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
	}
}
