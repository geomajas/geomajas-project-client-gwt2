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

package org.geomajas.gwt2.plugin.tilebasedlayer.example.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.example.base.client.sample.SamplePanelRegistry;
import org.geomajas.gwt2.example.base.client.sample.ShowcaseSampleDefinition;
import org.geomajas.gwt2.plugin.tilebasedlayer.example.client.i18n.SampleMessages;
import org.geomajas.gwt2.plugin.tilebasedlayer.example.client.sample.ClientSideOsmLayerPanel;

/**
 * Entry point that adds related samples to the showcase application.
 *
 * @author Youri Flement
 */
public class ExampleJar implements EntryPoint {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String CATEGORY_TILEBASEDLAYER = "Tile Based Layer Plugin";

	public void onModuleLoad() {
		// Register all samples:
		registerSamples();
	}

	private void registerSamples() {
		SamplePanelRegistry.registerFactory(CATEGORY_TILEBASEDLAYER, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new ClientSideOsmLayerPanel();
			}

			public String getTitle() {
				return MESSAGES.clientSideOsmLayerTitle();
			}

			public String getShortDescription() {
				return MESSAGES.clientSideOsmLayerShort();
			}

			public String getDescription() {
				return MESSAGES.clientSideOsmLayerDescription();
			}

			public String getCategory() {
				return CATEGORY_TILEBASEDLAYER;
			}

			@Override
			public String getKey() {
				return "clientsideosmlayer";
			}
		});
	}

}
