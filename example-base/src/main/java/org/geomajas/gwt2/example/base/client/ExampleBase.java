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

package org.geomajas.gwt2.example.base.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.example.base.client.page.sample.SamplePage;
import org.geomajas.gwt2.example.base.client.resource.ShowcaseResource;
import org.geomajas.gwt2.example.base.client.sample.SamplePanelRegistry;
import org.geomajas.gwt2.example.base.client.sample.ShowcaseSampleDefinition;
import org.geomajas.gwt2.example.base.client.widget.ShowcaseDialogBox;

import java.util.List;
import java.util.Map;

/**
 * Entry point and main class for the GWT client example application.
 *
 * @author Pieter De Graef
 */
public class ExampleBase implements EntryPoint {

	private static final ShowcaseResource RESOURCE = GWT.create(ShowcaseResource.class);

	public static final Bbox BBOX_ITALY = new Bbox(41581, 4189061, 2348145, 2348145);

	public static final Bbox BBOX_AFRICA = new Bbox(-2915614, -4324501, 9392582, 9392582);

	public static final Bbox BBOX_LATLON_USA = new Bbox(-142.5, -16, 90.0, 90.0);

	private static boolean sampleInNewTab;

	public void onModuleLoad() {
		// Prepare styling:
		RESOURCE.css().ensureInjected();
	}

	public static Widget getLayout() {
		Widget layout;

		Map<String, List<String>> queryStringMap = Window.Location.getParameterMap();

		if (null != queryStringMap && queryStringMap.containsKey("sample")) {
			String exampleName = queryStringMap.get("sample").get(0);
			ShowcaseSampleDefinition foundFactory = findSampleDefinition(exampleName);

			if (null == foundFactory) {
				return new ShowcaseLayout();
			}

			layout = new SamplePage();
			layout.setTitle(foundFactory.getTitle());

			((SamplePage) layout).setSamplePanelFactory(foundFactory);

		} else {
			 layout = new ShowcaseLayout();
		}

		return layout;
	}

	public static ShowcaseSampleDefinition findSampleDefinition(String name) {
		if (null != name && !name.isEmpty()) {

			for (ShowcaseSampleDefinition factory : SamplePanelRegistry.getFactories()) {
				String key = factory.getKey();

				if (key.equalsIgnoreCase(name)) {
					return factory;
				}
			}
		}

		return null;

	}

	public static ShowcaseResource getShowcaseResource() {
		return RESOURCE;
	}

	public static void showSample(ShowcaseSampleDefinition sample) {
		if (ExampleBase.sampleInNewTab) {
			showSampleInNewTab(sample);
		} else {
			showSampleInDialog(sample);
		}
	}

	public static void showSampleInDialog(ShowcaseSampleDefinition sample) {
		ShowcaseDialogBox dialogBox = new ShowcaseDialogBox();
		dialogBox.setAnimationEnabled(true);
		dialogBox.setAutoHideEnabled(false);
		dialogBox.setModal(true);
		dialogBox.setGlassEnabled(true);
		dialogBox.setText(sample.getTitle());
		SamplePage page = new SamplePage();
		int width = Window.getClientWidth() - 200;
		int height = Window.getClientHeight() - 160;
		page.setSize(width + "px", height + "px");
		page.setSamplePanelFactory(sample);
		dialogBox.setWidget(page);
		dialogBox.center();
		dialogBox.show();
	}

	public static void showSampleInNewTab(ShowcaseSampleDefinition sample) {
		UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
		urlBuilder.setParameter("sample", sample.getKey());
		String url = urlBuilder.buildString();
		Window.open(url, sample.getTitle(), "");
	}

	public static void setSampleInNewTab(boolean sampleInNewTab) {
		ExampleBase.sampleInNewTab = sampleInNewTab;
	}
}