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

package org.geomajas.gwt2.client.map.render;

import org.geomajas.annotation.Api;

/**
 * Basic renderer definition. It is used to render the state of the ViewPort on the map. Typical implementation will
 * render a layer or the entire LayersModel.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public interface BasicRenderer {

	/**
	 * Start a rendering cycle, given the provided rendering info.
	 * 
	 * @param renderingInfo
	 *            All the information needed for a renderer to render what it's supposed to render (a layer, the
	 *            LayersModel, ...).
	 */
	void render(RenderingInfo renderingInfo);
}