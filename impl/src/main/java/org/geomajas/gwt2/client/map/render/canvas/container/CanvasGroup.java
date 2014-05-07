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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.map.ViewPort;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Jan De Moerloose
 *
 */
public class CanvasGroup implements IsWidget {
	private Logger remoteLogger = Logger.getLogger("");

	private Canvas canvas;

	private boolean visible = true;

	private double opacity = 1.0;

	private List<CanvasTileGrid> imageContainers = new ArrayList<CanvasTileGrid>();

	public CanvasGroup(Canvas canvas) {
		this.canvas = canvas;
	}

	public void render(ViewPort viewPort) {
		if (isVisible()) {
			for (CanvasTileGrid ic : imageContainers) {
				if (ic.isVisible()) {
					Coordinate origin = viewPort.getTransformationService().transform(ic.getUpperLeftCorner(),
							RenderSpace.WORLD, RenderSpace.SCREEN);
					double scale = ic.getResolution() / viewPort.getResolution();
					if (opacity < 1) {
						canvas.getContext2d().save();
						canvas.getContext2d().setGlobalAlpha(opacity);
					}
					remoteLogger.log(Level.SEVERE, "Real canvas height:" +  canvas.getCoordinateSpaceHeight() + " width " + canvas.getCoordinateSpaceWidth());


					remoteLogger.log(Level.SEVERE, "IN memory canvas id="+ ic.getCanvas().getCanvasElement().getId() + " height:" +  ic.getCanvas().getCoordinateSpaceHeight() + " width "+ ic.getCanvas().getCoordinateSpaceWidth());


					canvas.getContext2d().clearRect(0, 0, canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceHeight());

					//TODO: get rid of this code because it is time consuming (native android browser issue canvas is not cleared)
					int w = canvas.getElement().getPropertyInt("width");
					canvas.getElement().setPropertyInt("width", 1);
					canvas.getElement().setPropertyInt("width", w);

					canvas.getContext2d().drawImage(ic.getCanvasElement(), 0, 0, ic.getGridBounds().getWidth(),
							ic.getGridBounds().getHeight(), origin.getX(), origin.getY(),
							scale * ic.getGridBounds().getWidth(), scale * ic.getGridBounds().getHeight());

					remoteLogger.log(Level.SEVERE, "Real canvas after height:" +  canvas.getCoordinateSpaceHeight() + " width " + canvas.getCoordinateSpaceWidth());

					if (opacity < 1) {
						canvas.getContext2d().restore();
					}
				}
			}
		}
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setOpacity(double opacity) {
		this.opacity = opacity;
	}

	public boolean isVisible() {
		return visible;
	}

	public double getOpacity() {
		return opacity;
	}

	@Override
	public Widget asWidget() {
		return canvas;
	}

	public void add(CanvasTileGrid grid) {
		imageContainers.add(grid);
	}

	public void remove(CanvasTileGrid grid) {
		imageContainers.remove(grid);
	}

	public void clear() {
		imageContainers.clear();
	}

}
