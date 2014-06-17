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
package org.geomajas.gwt2.client.map.render.canvas.container;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.ImageData;

/**
 * Check if canvas width/height are within range. Each browser has its own limits.
 * 
 * @author Jan De Moerloose
 * 
 */
public class CanvasSizeTest {

	private Canvas canvas;

	private ImageData imageData;

	private static CanvasSizeTest instance;

	private CanvasSizeTest() {
		restart();
	}

	public static CanvasSizeTest getInstance() {
		if (instance == null) {
			instance = new CanvasSizeTest();
		}
		return instance;
	}

	private void restart() {
		canvas = Canvas.createIfSupported();
		imageData = canvas.getContext2d().createImageData(1, 1);
		imageData.getData().set(0, 1);
		imageData.getData().set(1, 10);
		imageData.getData().set(2, 100);
		imageData.getData().set(3, 200);
	}

	/**
	 * Is this size too big ?
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	public boolean isTooBig(int width, int height) {
		try {
			if (width > canvas.getCoordinateSpaceWidth() || height > canvas.getCoordinateSpaceHeight()) {
				canvas.setCoordinateSpaceWidth(width);
				canvas.setCoordinateSpaceHeight(height);
				canvas.getContext2d().putImageData(imageData, width - 1, height - 1);
				ImageData result = canvas.getContext2d().getImageData(width - 1, height - 1, 1, 1);
				for (int i = 0; i < result.getData().getLength(); i++) {
					if (imageData.getData().get(i) != result.getData().get(i)) {
						restart();
						System.out.println(imageData.getData().get(i)+","+result.getData().get(i));
						return true;
					}
				}
			}
			return false;
		} catch (Exception e) {
			restart();
			return true;
		}
	}

}
