/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.editing.client.gfx;

import org.geomajas.gwt.client.gfx.style.ShapeStyle;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public interface StyleService {

	ShapeStyle getVertexStyle();
	
	ShapeStyle getVertexHoverStyle();

	ShapeStyle getVertexSelectStyle();
	
	ShapeStyle getVertexSelectHoverStyle();
	
	ShapeStyle getVertexMarkForDeletionStyle();

	ShapeStyle getEdgeStyle();

	ShapeStyle getEdgeHoverStyle();

	ShapeStyle getEdgeSelectStyle();
	
	ShapeStyle getEdgeSelectHoverStyle();
	
	ShapeStyle getEdgeMarkForDeletionStyle();

	ShapeStyle getLineStringStyle();

	ShapeStyle getLinearRingStyle();

	ShapeStyle getBackgroundStyle();
}
