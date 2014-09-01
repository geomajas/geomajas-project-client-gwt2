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
package org.geomajas.gwt2.example.client.sample.rendering;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.ImageElement;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Matrix;
import org.geomajas.gwt2.client.gfx.CanvasShape;

/**
 * {@link CanvasShape} for drawing an {@link com.google.gwt.dom.client.ImageElement} in a {@link Bbox}.
 *
 * @author Jan Venstermans
 *
 */
public class CanvasImageElement implements CanvasShape {

	/**
	 * Bbox of world coordinates the image needs to be drawn in.
	 */
	private Bbox box;

	/**
	 * Original image will be drawn on this hidden canvas.
	 */
	private Canvas hiddenImageCanvas;

	public CanvasImageElement(ImageElement imageElement, Bbox box) {
		this.box = new Bbox(box.getX(), box.getY(), box.getWidth(), box.getHeight());
		hiddenImageCanvas = Canvas.createIfSupported();
		hiddenImageCanvas.setVisible(false);
		hiddenImageCanvas.setCoordinateSpaceWidth(imageElement.getWidth());
		hiddenImageCanvas.setCoordinateSpaceHeight(imageElement.getHeight());
		hiddenImageCanvas.getContext2d().drawImage(imageElement, 0, 0);
	}

	@Override
	public void paint(Canvas canvas, Matrix matrix) {
		/*
		* paint image on canvas based on the original and with the transformations of the matrix.
		* */
		Context2d context2d = canvas.getContext2d();
		context2d.save();
		boolean xReversal = matrix.getXx() < 0;
		boolean yReversal = matrix.getYy() < 0;
		context2d.scale(xReversal ? -1 : 1, yReversal ? -1 : 1);
		double xValue = xReversal ? box.getMaxX() * -1 : box.getX();
		double yValue = yReversal ? box.getMaxY() * -1 : box.getY();
		context2d.drawImage(hiddenImageCanvas.getCanvasElement(), xValue, yValue, box.getWidth(), box.getHeight());
		context2d.restore();
	}

	@Override
	public Bbox getBounds() {
		return box;
	}
}
