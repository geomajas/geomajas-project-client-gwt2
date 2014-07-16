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
package org.geomajas.gwt2.plugin.print.component;

import org.geomajas.gwt2.plugin.print.component.dto.PageComponentInfo;

/**
 * Component representing a page.
 * 
 * @author Jan De Moerloose
 *
 */
public interface PageComponent extends PrintComponent<PageComponentInfo> {

	void setSize(String pageSize, boolean b);
	
	/**
	 * Get the locale.
	 * 
	 * @return  locale
	 *            which should be used for error messages in the print
	 * @since 2.0.0
	 */

	String getLocale();

	/**
	 * Set the locale.
	 * 
	 * @param locale
	 *            which should be used for error messages in the print
	 * @since 2.0.0
	 */
	void setLocale(String locale);
	
}
