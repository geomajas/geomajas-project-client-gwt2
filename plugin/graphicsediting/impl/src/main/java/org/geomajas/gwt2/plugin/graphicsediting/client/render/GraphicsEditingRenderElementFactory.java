/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.gwt2.plugin.graphicsediting.client.render;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.graphics.client.render.AnchoredCircle;
import org.geomajas.graphics.client.render.AnchoredImage;
import org.geomajas.graphics.client.render.AnchoredRectangle;
import org.geomajas.graphics.client.render.AnchoredText;
import org.geomajas.graphics.client.render.BaseCircle;
import org.geomajas.graphics.client.render.BaseEllipse;
import org.geomajas.graphics.client.render.BaseImage;
import org.geomajas.graphics.client.render.BaseRectangle;
import org.geomajas.graphics.client.render.BaseText;
import org.geomajas.graphics.client.render.CoordinatePath;
import org.geomajas.graphics.client.render.Marker;
import org.geomajas.graphics.client.render.RenderArea;
import org.geomajas.graphics.client.render.RenderContainer;

/**
 * Interface for specific view elements.
 *
 * @author Jan Venstermans
 * @since 1.0.0
 *
 */
@Api(allMethods = true)
public interface GraphicsEditingRenderElementFactory {

	/**
	 * Create a fixed-size image with anchor point at a specific user location. The anchor point is defined in pixels
	 * w.r.t the lower-left corner.
	 * 
	 * @param userX
	 * @param userY
	 * @param width
	 * @param height
	 * @param href
	 * @param preserveRatio
	 * @param anchorX
	 * @param anchorY
	 * @return
	 */
	GraphicsEditingAnchoredImage createGraphicsEditingAnchoredImage(double userX, double userY, int width, int height, String href,
									  boolean preserveRatio, double anchorX, double anchorY);

}
