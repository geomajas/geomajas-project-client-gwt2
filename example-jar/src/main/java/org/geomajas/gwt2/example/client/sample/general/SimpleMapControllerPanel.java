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

package org.geomajas.gwt2.example.client.sample.general;

import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.controller.SimpleMapController;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.widget.MapLayoutPanel;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.hammergwt.client.event.EventType;
import org.geomajas.hammergwt.client.event.NativeHammerEvent;
import org.geomajas.hammergwt.client.event.PointerType;
import org.geomajas.hammergwt.client.handler.HammerGWTCallback;
import org.geomajas.hammergwt.client.handler.HammerHandler;
import org.geomajas.hammergwt.client.impl.HammerGWT;
import org.geomajas.hammergwt.client.impl.HammerTime;
import org.geomajas.hammergwt.client.impl.option.GestureOptions;

/**
 * ContentPanel with full screen map that demonstrates the use of {@link SimpleMapController} interface.
 *
 * @author Dosi Bingov
 */
public class SimpleMapControllerPanel implements SamplePanel {

	private MapPresenter mapPresenter;

	public Widget asWidget() {
		// Define the layout:
		ResizeLayoutPanel resizeLayoutPanel = new ResizeLayoutPanel();
		final MapLayoutPanel layout = new MapLayoutPanel();
		resizeLayoutPanel.setWidget(layout);
		resizeLayoutPanel.setSize("100%", "100%");

		// Create the MapPresenter and add to the layout:
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter();
		mapPresenter.setMapController(new SimpleMapTouchController());
		layout.setPresenter(mapPresenter);

		// Initialize the map, and return the layout:
		GeomajasServerExtension.getInstance().initializeMap(mapPresenter, "gwt-app", "mapOsm");

		return resizeLayoutPanel;
	}

	/**
	 *
	 * Use of Third-party library hammer GWT in simple map controller.
	 *
	 * @author Dosi Bingov
	 *
	 */
	public class SimpleMapTouchController implements SimpleMapController, HammerHandler {
		private HammerGWTCallback hammerGWTCallback;

		private double beginResolution;

		private Coordinate dragOrigin;

		private int currentResIndex;

		private MapPresenter mapPresenter;

		@Override
		public void onActivate(MapPresenter presenter) {
			mapPresenter = presenter;

			//we pass the map widget to the creation
			HammerTime hammerTime = HammerGWT.createInstance(presenter.asWidget());

		 	hammerGWTCallback = HammerGWT.on(hammerTime, this,
		 			EventType.PINCH, EventType.TOUCH, EventType.DRAG, EventType.DRAGSTART);

			hammerTime.setOption(GestureOptions.PREVENT_DEFAULT, true);
		}

		@Override
		public void onDeactivate(MapPresenter presenter) {
			//TODO remove all hammer gwt events

		}

		@Override
		public void onHammerEvent(NativeHammerEvent event) {
			event.preventDefault();
			event.preventNativeDefault();
			//log(event);

			switch (event.getType()) {
				case TOUCH:
					currentResIndex =
							mapPresenter.getViewPort().getResolutionIndex(mapPresenter.getViewPort().getResolution());
					beginResolution = mapPresenter.getViewPort().getResolution();
					break ;

				case PINCH:
					double resolution = beginResolution / event.getScale();

					if (mapPresenter.getViewPort().getResolutionIndex(resolution) == 0) {
						//do nothing because max or min resolution is reached
						break;
					}

					int midX = 0;
					int midY = 0;

					if (event.getNativeEvent().getTouches().length() == 2) {
						int x1 = event.getNativeEvent().getTouches().get(0).getClientX();
						int x2 = event.getNativeEvent().getTouches().get(1).getClientX();

						int y1 = event.getNativeEvent().getTouches().get(0).getClientY();
						int y2 = event.getNativeEvent().getTouches().get(1).getClientY();

						midX = (x1 + x2) / 2;

						midY = (y1 + y2) / 2;
					}


					Coordinate worldCoordinate = mapPresenter.getViewPort().getTransformationService()
							.transform(new Coordinate(midX, midY), RenderSpace.SCREEN, RenderSpace.WORLD);

					View view = new View(calculatePosition(resolution, worldCoordinate), resolution);
					mapPresenter.getViewPort().applyView(view);

					break;

				case DRAGSTART:
					dragOrigin = getLocation(event, RenderSpace.SCREEN);
					break;

				case DRAG:
					updateView(event);
					break;

				default:
					break;

			}
		}

		private Coordinate getLocation(NativeHammerEvent event, RenderSpace renderSpace) {
			switch (renderSpace) {
				case WORLD:
					Coordinate screen = getLocation(event, RenderSpace.SCREEN);
					return mapPresenter.getViewPort().getTransformationService()
							.transform(screen, RenderSpace.SCREEN, RenderSpace.WORLD);
				case SCREEN:
				default:

					if (event.getPointerType() == PointerType.MOUSE) {
						double offsetX = event.getRelativeX();
						double offsetY = event.getRelativeY();

						return new Coordinate(offsetX, offsetY);
						//center between two fingers
					} else if (event.getPointerType() == PointerType.TOUCH) {
						return new Coordinate(event.getPageX(), event.getPageY());
					} else {
						return new Coordinate(event.getPageX(), event.getPageY());
					}
			}
		}

		private void updateView(NativeHammerEvent event) {
			Coordinate end = getLocation(event, RenderSpace.SCREEN);
			Coordinate beginWorld = mapPresenter.getViewPort().getTransformationService()
					.transform(dragOrigin, RenderSpace.SCREEN, RenderSpace.WORLD);
			Coordinate endWorld = mapPresenter.getViewPort().getTransformationService()
					.transform(end, RenderSpace.SCREEN, RenderSpace.WORLD);

			double x = mapPresenter.getViewPort().getPosition().getX() + beginWorld.getX() - endWorld.getX();
			double y = mapPresenter.getViewPort().getPosition().getY() + beginWorld.getY() - endWorld.getY();
			mapPresenter.getViewPort().applyPosition(new Coordinate(x, y));
			dragOrigin = end;
		}

		/**
		 * Calculate the target position should there be a rescale point. The idea is that after zooming in or out, the
		 * mouse cursor would still lie at the same position in world space.
		 */
		protected Coordinate calculatePosition(double resolution, Coordinate rescalePoint) {
			ViewPort viewPort = mapPresenter.getViewPort();
			Coordinate position = viewPort.getPosition();
			double factor = viewPort.getResolution() / resolution;
			double dX = (rescalePoint.getX() - position.getX()) * (1 - 1 / factor);
			double dY = (rescalePoint.getY() - position.getY()) * (1 - 1 / factor);
			return new Coordinate(position.getX() + dX, position.getY() + dY);
		}

	}
}