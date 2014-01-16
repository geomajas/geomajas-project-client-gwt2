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
package org.geomajas.gwt2.widget.client.itemselect.resources;

import com.google.gwt.resources.client.ClientBundle;

/**
 * @author Oliver May
 */
public interface ItemSelectResource extends ClientBundle {

	/**
	 * Get the css resource.
	 * @return the css resource
	 */
	@Source("itemSelectWidget.css")
	ItemSelectCssResource css();
}
