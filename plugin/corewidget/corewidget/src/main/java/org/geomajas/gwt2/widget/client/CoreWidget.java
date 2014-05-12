package org.geomajas.gwt2.widget.client;

import org.geomajas.annotation.Api;

/**
 * Starting point for the core widget plugin.
 * 
 * @author Jan De Moerloose
 * @since 2.0.0
 */
@Api(allMethods = true)
public class CoreWidget {

	private static ViewFactory viewFactory;

	public static ViewFactory getViewFactory() {
		return viewFactory;
	}

	static void setViewFactory(ViewFactory newViewFactory) {
		viewFactory = newViewFactory;
	}
}
