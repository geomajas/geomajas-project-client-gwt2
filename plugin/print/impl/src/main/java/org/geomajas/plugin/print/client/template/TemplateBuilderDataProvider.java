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
package org.geomajas.plugin.print.client.template;

import com.google.gwt.user.client.ui.IsWidget;
import org.geomajas.plugin.print.client.util.PrintSettings;
import org.geomajas.plugin.print.client.template.PageSize;

/**
 * Interface for data provider of a Builder.
 * This is the default data provider.
 * 
 * @author Jan Venstermans
 */
public interface TemplateBuilderDataProvider {

	String getTitle();

	/**
	 *
	 * @return true landscape
	 * 			false portrait
	 */
	boolean isLandscape();

	PageSize getPageSize();

	boolean isWithArrow();

	boolean isWithScaleBar();

	int getRasterDpi();
}
