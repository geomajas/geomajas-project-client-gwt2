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
package org.geomajas.gwt2.widget.client.itemselect;

import com.google.gwt.resources.client.CssResource;

/**
 * CSS class for an item select widget.
 *
 * @author Oliver May
 */
public interface ItemSelectCssResource extends CssResource {

	/**
	 * Get a CSS style class.
	 *
	 * @return the css
	 */
	@ClassName("gm-itemSelectWidget")
	String itemSelectWidget();


	/**
	 * Get a CSS style class.
	 *
	 * @return the css
	 */
	@ClassName("gm-itemSelectWidgetCell")
	String itemSelectWidgetCell();
}
