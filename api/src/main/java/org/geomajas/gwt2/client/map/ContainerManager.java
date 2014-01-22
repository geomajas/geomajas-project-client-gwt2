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

package org.geomajas.gwt2.client.map;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.gfx.CanvasContainer;
import org.geomajas.gwt2.client.gfx.TransformableWidgetContainer;
import org.geomajas.gwt2.client.gfx.VectorContainer;

/**
 * Manages different types of containers that can be added to the map. These containers are used to overlay
 * drawings/widgets/etc on the map.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public interface ContainerManager {

	/**
	 * <p>
	 * Create a new container in world space wherein one can render new vector objects and return it. Note that all
	 * objects drawn into such a container should be expressed in world coordinates (the CRS of the map). These objects
	 * will also be automatically redrawn when the view port on the map changes.<br/>
	 * New containers are automatically drawn on top of all other containers - that includes both world and screen
	 * containers.
	 * </p>
	 * <p>
	 * WARNING: adding subgroups to the returned result does not work in IE !
	 * </p>
	 * 
	 * @return Returns the world vector container.
	 */
	VectorContainer addWorldContainer();

	/**
	 * <p>
	 * Create a new container in world space to which one can add transformable widgets and return it. Note that all
	 * objects drawn into such a container should be expressed in world coordinates (the CRS of the map). These objects
	 * will also be automatically redrawn when the view port on the map changes.<br/>
	 * New containers are automatically drawn on top of all other containers - that includes both world and screen
	 * containers and vector object containers.
	 * </p>
	 * 
	 * @return Returns the world widget container.
	 */
	TransformableWidgetContainer addWorldWidgetContainer();

	/**
	 * <p>
	 * Create a new container in world space wherein one can render new canvas objects and return it. Note that all
	 * objects drawn into such a container should be expressed in world coordinates (the CRS of the map). These objects
	 * will also be automatically redrawn when the view port on the map changes.<br/>
	 * New containers are automatically drawn on top of all other containers - that includes both world and screen
	 * containers.
	 * </p>
	 * 
	 * @return Returns the canvas container.
	 */
	CanvasContainer addWorldCanvasContainer();

	/**
	 * Create a new container in screen space wherein one can render new vector objects and return it. Note that all
	 * objects drawn into such a container should be expressed in pixel coordinates. No matter how much the map moves or
	 * zooms, these objects will always remain on the same fixed position.<br/>
	 * New containers are automatically drawn on top of all other containers - that includes both world and screen
	 * containers.
	 * 
	 * @return Returns the screen vector container.
	 */
	VectorContainer addScreenContainer();

	/**
	 * Remove an existing vector container from the map. This can be either a world or a screen container.
	 * 
	 * @param container
	 *            The identifier of the container. If no such container exists, false will be returned.
	 * @return Was the removal successful or not?
	 */
	boolean removeVectorContainer(VectorContainer container);

	/**
	 * Remove an existing widget container from the map.
	 * 
	 * @param container
	 *            The identifier of the container. If no such container exists, false will be returned.
	 * @return Was the removal successful or not?
	 */
	boolean removeWorldWidgetContainer(TransformableWidgetContainer container);

	/**
	 * Bring an existing vector container to the front. This container must be a registered world or screen container.
	 * 
	 * @param container
	 *            The vector container to bring to the front. This container must be acquired either through the
	 *            {@link #addScreenContainer()} or the {@link #addWorldContainer()} methods.
	 * @return Could the container be successfully brought to the front or not?
	 */
	boolean bringToFront(VectorContainer container);
}