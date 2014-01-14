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

package org.geomajas.gwt2.tools.example.client;

import com.google.gwt.core.client.EntryPoint;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.example.base.client.sample.SamplePanelRegistry;
import org.geomajas.gwt2.example.base.client.sample.ShowcaseSampleDefinition;
import org.geomajas.gwt2.example.client.sample.general.NavigationOptionPanel;

/**
 * Example jar entry point.
 *
 * @author Oliver May
 */
public class ExampleJar implements EntryPoint {

	public static final String CATEGORY_TOOLBAR = "Toolbar samples";


	@Override
	public void onModuleLoad() {
		SamplePanelRegistry.registerCategory(CATEGORY_TOOLBAR, 100);
		SamplePanelRegistry.registerFactory(CATEGORY_TOOLBAR, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new ToolPanel();
			}

			public String getTitle() {
				//FIXME: i18n
				return "Toolbar sample";
			}

			public String getShortDescription() {
				//FIXME: i18n
				return "Toolbar sample";
			}

			public String getDescription() {
				//FIXME: i18n
				return "Toolbar sample";
			}

			public String getCategory() {
				return CATEGORY_TOOLBAR;
			}
		});

	}
}
