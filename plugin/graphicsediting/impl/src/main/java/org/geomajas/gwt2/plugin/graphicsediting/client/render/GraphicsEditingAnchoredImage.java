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

import com.google.gwt.event.dom.client.MouseUpHandler;
import org.geomajas.annotation.Api;
import org.geomajas.graphics.client.render.AnchoredImage;

/**
 * Interface for specific view elements.
 *
 * @author Jan Venstermans
 * @since 1.0.0
 *
 */
@Api(allMethods = true)
public interface GraphicsEditingAnchoredImage extends AnchoredImage {

	//TODO: maybe remove this, as AnchoredImage should always be fixed in size?
	void setFixedSize(boolean fixedSize);

	void addMouseUpHandler(MouseUpHandler editHandler);

	void setVisible(boolean visible);
}
