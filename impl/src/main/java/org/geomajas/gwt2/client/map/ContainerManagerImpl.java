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

package org.geomajas.gwt2.client.map;

import org.geomajas.geometry.Matrix;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.gfx.CanvasContainer;
import org.geomajas.gwt2.client.gfx.TransformableWidgetContainer;
import org.geomajas.gwt2.client.gfx.VectorContainer;
import org.geomajas.gwt2.client.map.MapPresenterImpl.MapWidget;

/**
 * Default implementation of the {@link ContainerManager}.
 * 
 * @author Pieter De Graef
 */
public class ContainerManagerImpl implements ContainerManager {

	private final MapWidget display;

	private final ViewPort viewPort;

	protected ContainerManagerImpl(MapWidget display, ViewPort viewPort) {
		this.display = display;
		this.viewPort = viewPort;
	}

	@Override
	public VectorContainer addWorldContainer() {
		VectorContainer container = display.getNewWorldContainer();
		// set transform parameters once, after that all is handled by WorldContainerRenderer
		Matrix matrix = viewPort.getTransformationService().getTransformationMatrix(RenderSpace.WORLD,
				RenderSpace.SCREEN);
		container.setScale(matrix.getXx(), matrix.getYy());
		container.setTranslation(matrix.getDx(), matrix.getDy());
		return container;
	}

	@Override
	public TransformableWidgetContainer addWorldWidgetContainer() {
		TransformableWidgetContainer container = display.getNewWorldWidgetContainer();
		// set transform parameters once, after that all is handled by WorldContainerRenderer
		Matrix matrix = viewPort.getTransformationService().getTransformationMatrix(RenderSpace.WORLD,
				RenderSpace.SCREEN);
		container.setScale(matrix.getXx(), matrix.getYy());
		container.setTranslation(matrix.getDx(), matrix.getDy());
		return container;
	}

	@Override
	public CanvasContainer addWorldCanvasContainer() {
		CanvasContainer container = display.getNewWorldCanvas();
		// set transform parameters once, after that all is handled by WorldContainerRenderer
		Matrix matrix = viewPort.getTransformationService().getTransformationMatrix(RenderSpace.WORLD,
				RenderSpace.SCREEN);
		container.setScale(matrix.getXx(), matrix.getYy());
		container.setTranslation(matrix.getDx(), matrix.getDy());
		return container;
	}

	@Override
	public VectorContainer addScreenContainer() {
		return display.getNewScreenContainer();
	}

	@Override
	public boolean removeWorldWidgetContainer(TransformableWidgetContainer container) {
		return display.removeWorldWidgetContainer(container);
	}

	@Override
	public boolean removeVectorContainer(VectorContainer container) {
		return display.removeVectorContainer(container);
	}

	@Override
	public boolean bringToFront(VectorContainer container) {
		return display.bringToFront(container);
	}
}