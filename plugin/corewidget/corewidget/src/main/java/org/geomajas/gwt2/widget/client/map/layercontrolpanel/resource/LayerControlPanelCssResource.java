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

package org.geomajas.gwt2.widget.client.map.layercontrolpanel.resource;

import com.google.gwt.resources.client.CssResource;

/**
 * Client resource bundle interface for {@link org.geomajas.gwt2.widget.client.map.layercontrolpanel.LayerControlPanel}.
 *
 * @author Dosi Bingov
 */
public interface LayerControlPanelCssResource extends CssResource {

	/**
	 * Get a CSS style class.
	 *
	 * @return
	 */
	@ClassName("gm-layerControlPanel")
	String layerControlPanel();


	/**
	 * Get a CSS style class.
	 *
	 * @return
	 */
	@ClassName("gm-layerControlPanelTitle")
	String layerControlPanelTitle();


	/**
	 * Get a CSS style class.
	 *
	 * @return
	 */
	@ClassName("gm-layerControlPanelToggle")
	String layerControlPanelToggle();
}