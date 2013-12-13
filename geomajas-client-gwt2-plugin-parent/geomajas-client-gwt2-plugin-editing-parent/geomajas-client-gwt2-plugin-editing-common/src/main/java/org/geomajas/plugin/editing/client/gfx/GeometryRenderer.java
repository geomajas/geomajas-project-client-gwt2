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
package org.geomajas.plugin.editing.client.gfx;

import org.geomajas.plugin.editing.client.handler.EdgeMapHandlerFactory;
import org.geomajas.plugin.editing.client.handler.VertexMapHandlerFactory;

/**
 * The actual renderer for the geometry. TODO: looks empty, is this interface necessary ?
 * 
 * @author Jan De Moerloose
 * 
 */
public interface GeometryRenderer {

	/**
	 * Redraw the geometry to apply changes in the editor internal state.
	 */
	void redraw();
	
	/**
	 * Make the edited geometry.visible/invisible.
	 * 
	 * @param visible true to make the geometry visible, false to make the geometry invisible 
	 */
	void setVisible(boolean visible);

	/**
	 * Add a custom factory that will create handler for every vertex.
	 * This factory will be added on top of the existing default factories.
	 *
	 * @param factory the factory to be added to every vertex
	 */
	void addVertexHandlerFactory(VertexMapHandlerFactory factory);

	/**
	 * Add a custom factory that will create handler for every edge.
	 * This factory will be added on top of the existing default factories.
	 *
	 * @param factory the factory to be added to every edge
	 */
	void addEdgeHandlerFactory(EdgeMapHandlerFactory factory);

}
