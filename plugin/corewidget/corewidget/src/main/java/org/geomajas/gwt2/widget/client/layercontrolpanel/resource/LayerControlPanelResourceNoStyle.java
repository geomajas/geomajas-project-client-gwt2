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

package org.geomajas.gwt2.widget.client.layercontrolpanel.resource;

import com.google.gwt.resources.client.ClientBundle;

/**
 * Client resource bundle interface with empty css classes.
 * 
 * @author Dosi Bingov
 */
public interface LayerControlPanelResourceNoStyle extends LayerControlPanelCssResource {

	/**
	 * Get the css resource.
	 * @return the css resource
	 */
	@ClientBundle.Source("org/geomajas/gwt2/widget/client/resource/CoreWidgetNoStyle.css")
	LayerControlPanelCssResource css();
}