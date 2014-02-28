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

package org.geomajas.gwt2.plugin.editing.client.gfx;

import org.geomajas.gwt2.client.gfx.ShapeStyle;

/**
 * Provides styles to be used during the editing process. By default the color scheme is quite orange.
 *
 * @author Pieter De Graef
 */
public class StyleProvider {

	private ShapeStyle vertexStyle;

	private ShapeStyle vertexHoverStyle;

	private ShapeStyle vertexSelectStyle;

	private ShapeStyle vertexDisabledStyle;

	private ShapeStyle vertexSelectHoverStyle;

	private ShapeStyle vertexMarkForDeletionStyle;

	private ShapeStyle edgeStyle;

	private ShapeStyle edgeHoverStyle;

	private ShapeStyle edgeSelectStyle;

	private ShapeStyle edgeDisabledStyle;

	private ShapeStyle edgeSelectHoverStyle;

	private ShapeStyle edgeMarkForDeletionStyle;

	private ShapeStyle edgeInsertMoveStyle;

	private ShapeStyle lineStringStyle;

	private ShapeStyle linearRingStyle;

	private ShapeStyle backgroundStyle;

	private ShapeStyle backgroundMarkedForDeletionStyle;

	private ShapeStyle backgroundDisabledStyle;

	private ShapeStyle snappedVertexStyle;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Initializes all default styles.
	 */
	public StyleProvider() {
		vertexStyle = new ShapeStyle();
		vertexStyle.setFillColor("#FFFFFF");
		vertexStyle.setFillOpacity(1);
		vertexStyle.setStrokeColor("#CC6600");
		vertexStyle.setStrokeOpacity(1);
		vertexStyle.setStrokeWidth(1);

		vertexHoverStyle = new ShapeStyle();
		vertexHoverStyle.setFillColor("#888888");
		vertexHoverStyle.setFillOpacity(1);
		vertexHoverStyle.setStrokeColor("#CC6600");
		vertexHoverStyle.setStrokeOpacity(1);
		vertexHoverStyle.setStrokeWidth(1);

		vertexSelectStyle = new ShapeStyle();
		vertexSelectStyle.setFillColor("#666666");
		vertexSelectStyle.setFillOpacity(1);
		vertexSelectStyle.setStrokeColor("#FFFF33");
		vertexSelectStyle.setStrokeOpacity(1);
		vertexSelectStyle.setStrokeWidth(1);

		vertexDisabledStyle = new ShapeStyle();
		vertexDisabledStyle.setFillColor("#999999");
		vertexDisabledStyle.setFillOpacity(1);
		vertexDisabledStyle.setStrokeColor("#666666");
		vertexDisabledStyle.setStrokeOpacity(1);
		vertexDisabledStyle.setStrokeWidth(1);

		vertexSelectHoverStyle = new ShapeStyle();
		vertexSelectHoverStyle.setFillColor("#888888");
		vertexSelectHoverStyle.setFillOpacity(1);
		vertexSelectHoverStyle.setStrokeColor("#FFFF33");
		vertexSelectHoverStyle.setStrokeOpacity(1);
		vertexSelectHoverStyle.setStrokeWidth(1);

		vertexMarkForDeletionStyle = new ShapeStyle();
		vertexMarkForDeletionStyle.setFillColor("#FF0000");
		vertexMarkForDeletionStyle.setFillOpacity(1);
		vertexMarkForDeletionStyle.setStrokeColor("#990000");
		vertexMarkForDeletionStyle.setStrokeOpacity(1);
		vertexMarkForDeletionStyle.setStrokeWidth(2);

		edgeStyle = new ShapeStyle();
		edgeStyle.setFillColor("#FFFFFF");
		edgeStyle.setFillOpacity(0);
		edgeStyle.setStrokeColor("#FFAA00");
		edgeStyle.setStrokeOpacity(1);
		edgeStyle.setStrokeWidth(4);

		edgeHoverStyle = new ShapeStyle();
		edgeHoverStyle.setFillColor("#FFFFFF");
		edgeHoverStyle.setFillOpacity(0);
		edgeHoverStyle.setStrokeColor("#FFCC33");
		edgeHoverStyle.setStrokeOpacity(1);
		edgeHoverStyle.setStrokeWidth(6);

		edgeSelectStyle = new ShapeStyle();
		edgeSelectStyle.setFillColor("#FFFFFF");
		edgeSelectStyle.setFillOpacity(0);
		edgeSelectStyle.setStrokeColor("#CC8800");
		edgeSelectStyle.setStrokeOpacity(1);
		edgeSelectStyle.setStrokeWidth(6);

		edgeDisabledStyle = new ShapeStyle();
		edgeDisabledStyle.setFillColor("#FFFFFF");
		edgeDisabledStyle.setFillOpacity(0);
		edgeDisabledStyle.setStrokeColor("#999999");
		edgeDisabledStyle.setStrokeOpacity(1);
		edgeDisabledStyle.setStrokeWidth(4);

		edgeSelectHoverStyle = new ShapeStyle();
		edgeSelectHoverStyle.setFillColor("#FFFFFF");
		edgeSelectHoverStyle.setFillOpacity(0);
		edgeSelectHoverStyle.setStrokeColor("#FFCC33");
		edgeSelectHoverStyle.setStrokeOpacity(1);
		edgeSelectHoverStyle.setStrokeWidth(6);

		edgeMarkForDeletionStyle = new ShapeStyle();
		edgeMarkForDeletionStyle.setFillColor("#FF0000");
		edgeMarkForDeletionStyle.setFillOpacity(0);
		edgeMarkForDeletionStyle.setStrokeColor("#990000");
		edgeMarkForDeletionStyle.setStrokeOpacity(1);
		edgeMarkForDeletionStyle.setStrokeWidth(6);

		edgeInsertMoveStyle = new ShapeStyle();
		edgeInsertMoveStyle.setFillColor("#FFFFFF");
		edgeInsertMoveStyle.setFillOpacity(0);
		edgeInsertMoveStyle.setStrokeColor("#666666");
		edgeInsertMoveStyle.setStrokeOpacity(1);
		edgeInsertMoveStyle.setStrokeWidth(2);

		lineStringStyle = new ShapeStyle();
		lineStringStyle.setFillColor("#FFFFFF");
		lineStringStyle.setFillOpacity(0);
		lineStringStyle.setStrokeColor("#FFFFFF");
		lineStringStyle.setStrokeOpacity(0);
		lineStringStyle.setStrokeWidth(0);

		linearRingStyle = new ShapeStyle();
		linearRingStyle.setFillColor("#FFEE00");
		linearRingStyle.setFillOpacity(0);
		linearRingStyle.setStrokeColor("#FFAA00");
		linearRingStyle.setStrokeOpacity(1);
		linearRingStyle.setStrokeWidth(3);

		backgroundStyle = new ShapeStyle();
		backgroundStyle.setFillColor("#FFCC00");
		backgroundStyle.setFillOpacity(0.35f);
		backgroundStyle.setStrokeColor("#FFAA00");
		backgroundStyle.setStrokeOpacity(0);
		backgroundStyle.setStrokeWidth(0);

		backgroundMarkedForDeletionStyle = new ShapeStyle();
		backgroundMarkedForDeletionStyle.setFillColor("#FF0000");
		backgroundMarkedForDeletionStyle.setFillOpacity(0.35f);
		backgroundMarkedForDeletionStyle.setStrokeColor("#CC0000");
		backgroundMarkedForDeletionStyle.setStrokeOpacity(0);
		backgroundMarkedForDeletionStyle.setStrokeWidth(0);

		backgroundDisabledStyle = new ShapeStyle();
		backgroundDisabledStyle.setFillColor("#999999");
		backgroundDisabledStyle.setFillOpacity(0.35f);
		backgroundDisabledStyle.setStrokeColor("#999999");
		backgroundDisabledStyle.setStrokeOpacity(0);
		backgroundDisabledStyle.setStrokeWidth(0);

		snappedVertexStyle = new ShapeStyle();
		snappedVertexStyle.setFillColor("#888888");
		snappedVertexStyle.setFillOpacity(1);
		snappedVertexStyle.setStrokeColor("#CC0000");
		snappedVertexStyle.setStrokeOpacity(1);
		snappedVertexStyle.setStrokeWidth(1);
	}

	// ------------------------------------------------------------------------
	// StyleService implementation:
	// ------------------------------------------------------------------------

	public ShapeStyle getEdgeMarkForDeletionStyle() {
		return edgeMarkForDeletionStyle;
	}

	public void setEdgeMarkForDeletionStyle(ShapeStyle edgeMarkForDeletionStyle) {
		this.edgeMarkForDeletionStyle = edgeMarkForDeletionStyle;
	}

	public ShapeStyle getVertexSelectHoverStyle() {
		return vertexSelectHoverStyle;
	}

	public void setVertexSelectHoverStyle(ShapeStyle vertexSelectHoverStyle) {
		this.vertexSelectHoverStyle = vertexSelectHoverStyle;
	}

	public ShapeStyle getEdgeSelectHoverStyle() {
		return edgeSelectHoverStyle;
	}

	public void setEdgeSelectHoverStyle(ShapeStyle edgeSelectHoverStyle) {
		this.edgeSelectHoverStyle = edgeSelectHoverStyle;
	}

	public ShapeStyle getVertexStyle() {
		return vertexStyle;
	}

	public void setVertexStyle(ShapeStyle vertexStyle) {
		this.vertexStyle = vertexStyle;
	}

	public ShapeStyle getVertexHoverStyle() {
		return vertexHoverStyle;
	}

	public void setVertexHoverStyle(ShapeStyle vertexHoverStyle) {
		this.vertexHoverStyle = vertexHoverStyle;
	}

	public ShapeStyle getVertexSelectStyle() {
		return vertexSelectStyle;
	}

	public void setVertexSelectStyle(ShapeStyle vertexSelectStyle) {
		this.vertexSelectStyle = vertexSelectStyle;
	}

	public ShapeStyle getVertexMarkForDeletionStyle() {
		return vertexMarkForDeletionStyle;
	}

	public void setVertexMarkForDeletionStyle(ShapeStyle vertexMarkForDeletionStyle) {
		this.vertexMarkForDeletionStyle = vertexMarkForDeletionStyle;
	}

	public ShapeStyle getEdgeStyle() {
		return edgeStyle;
	}

	public void setEdgeStyle(ShapeStyle edgeStyle) {
		this.edgeStyle = edgeStyle;
	}

	public ShapeStyle getEdgeHoverStyle() {
		return edgeHoverStyle;
	}

	public void setEdgeHoverStyle(ShapeStyle edgeHoverStyle) {
		this.edgeHoverStyle = edgeHoverStyle;
	}

	public ShapeStyle getEdgeSelectStyle() {
		return edgeSelectStyle;
	}

	public void setEdgeSelectStyle(ShapeStyle edgeSelectStyle) {
		this.edgeSelectStyle = edgeSelectStyle;
	}

	public ShapeStyle getLineStringStyle() {
		return lineStringStyle;
	}

	public void setLineStringStyle(ShapeStyle lineStringStyle) {
		this.lineStringStyle = lineStringStyle;
	}

	public ShapeStyle getLinearRingStyle() {
		return linearRingStyle;
	}

	public void setLinearRingStyle(ShapeStyle linearRingStyle) {
		this.linearRingStyle = linearRingStyle;
	}

	public ShapeStyle getBackgroundStyle() {
		return backgroundStyle;
	}

	public void setBackgroundStyle(ShapeStyle backgroundStyle) {
		this.backgroundStyle = backgroundStyle;
	}

	public ShapeStyle getEdgeTentativeMoveStyle() {
		return edgeInsertMoveStyle;
	}

	public void setEdgeInsertMoveStyle(ShapeStyle edgeInsertMoveStyle) {
		this.edgeInsertMoveStyle = edgeInsertMoveStyle;
	}

	public ShapeStyle getVertexDisabledStyle() {
		return vertexDisabledStyle;
	}

	public void setVertexDisabledStyle(ShapeStyle vertexDisabledStyle) {
		this.vertexDisabledStyle = vertexDisabledStyle;
	}

	public ShapeStyle getEdgeDisabledStyle() {
		return edgeDisabledStyle;
	}

	public void setEdgeDisabledStyle(ShapeStyle edgeDisabledStyle) {
		this.edgeDisabledStyle = edgeDisabledStyle;
	}

	public ShapeStyle getBackgroundDisabledStyle() {
		return backgroundDisabledStyle;
	}

	public void setBackgroundDisabledStyle(ShapeStyle backgroundDisabledStyle) {
		this.backgroundDisabledStyle = backgroundDisabledStyle;
	}

	public ShapeStyle getBackgroundMarkedForDeletionStyle() {
		return backgroundMarkedForDeletionStyle;
	}

	public void setBackgroundMarkedForDeletionStyle(ShapeStyle backgroundMarkedForDeletionStyle) {
		this.backgroundMarkedForDeletionStyle = backgroundMarkedForDeletionStyle;
	}

	public ShapeStyle getVertexSnappedStyle() {
		return snappedVertexStyle;
	}

	public void setSnappedVertexStyle(ShapeStyle snappedVertexStyle) {
		this.snappedVertexStyle = snappedVertexStyle;
	}
}