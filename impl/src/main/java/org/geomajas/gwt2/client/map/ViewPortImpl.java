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

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt.client.util.Dom;
import org.geomajas.gwt2.client.animation.NavigationAnimation;
import org.geomajas.gwt2.client.event.NavigationStopEvent;
import org.geomajas.gwt2.client.event.NavigationStopHandler;
import org.geomajas.gwt2.client.event.ViewPortChangedEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Implementation of the ViewPort interface.
 *
 * @author Pieter De Graef
 */
public final class ViewPortImpl implements ViewPort {

	private static final double METER_PER_INCH = 0.0254;

	private final MapEventBus eventBus;

	private final ViewPortTransformationService transformationService;

	private MapConfiguration configuration;

	/**
	 * The map's width in pixels.
	 */
	private int mapWidth;

	/**
	 * The map's height in pixels.
	 */
	private int mapHeight;

	/**
	 * The maximum bounding box available to this MapView. Never go outside it!
	 */
	private Bbox maxBounds;

	private final List<Double> resolutions = new ArrayList<Double>();

	private String crs;

	private double resolution;

	private Coordinate position;

	private NavigationAnimation currentAnimation;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public ViewPortImpl(MapEventBus eventBus) {
		this.eventBus = eventBus;
		this.transformationService = new ViewPortTransformationServiceImpl(this);
		this.position = new Coordinate();

		eventBus.addNavigationStopHandler(new NavigationStopHandler() {

			@Override
			public void onNavigationStopped(NavigationStopEvent event) {
				currentAnimation = null;
			}
		});
	}

	// -------------------------------------------------------------------------
	// Configuration stuff:
	// -------------------------------------------------------------------------

	protected void initialize(MapConfiguration configuration) {
		this.configuration = configuration;
		this.crs = configuration.getCrs();

		// Calculate maximum bounds:
		this.maxBounds = new Bbox(configuration.getMaxBounds().getX(), configuration.getMaxBounds().getY(),
				configuration.getMaxBounds().getWidth(), configuration.getMaxBounds().getHeight());

		if (configuration.getResolutions() != null && configuration.getResolutions().size() > 0) {
			for (Double resolution : configuration.getResolutions()) {
				resolutions.add(resolution);
			}
		} else if (configuration.getMinimumResolution() != 0) {
			// If there are no resolutions, we'll calculate them:
			double tempResolution = getMaxBoundsResolution();
			if (tempResolution == 0.0) {
				throw new IllegalStateException("Could not initialize the map. Could it be it has no size?");
			}
			resolutions.add(tempResolution);
			while (tempResolution > configuration.getMinimumResolution()) {
				tempResolution /= 2;
				resolutions.add(tempResolution);
			}
		} else {
			throw new IllegalStateException(
					"The map configuration must either contain a fixed list resolutions or a minimum resolution");
		}
		Collections.sort(this.resolutions, new Comparator<Double>() {

			@Override
			public int compare(Double o1, Double o2) {
				return o2.compareTo(o1);
			}
		});
	}

	@Override
	public Bbox getMaximumBounds() {
		return maxBounds;
	}

	@Override
	public int getResolutionCount() {
		return resolutions.size();
	}

	@Override
	public double getResolution(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("Resolution cannot be found.");
		}
		if (index >= resolutions.size()) {
			throw new IllegalArgumentException("Resolution cannot be found.");
		}
		return resolutions.get(index);
	}

	@Override
	public int getResolutionIndex(double resolution) {
		double maximumResolution = getMaximumResolution();
		if (resolution >= maximumResolution) {
			return 0;
		}
		double minimumResolution = getMinimumResolution();
		if (resolution <= minimumResolution) {
			return resolutions.size() - 1;
		}

		for (int i = 0; i < resolutions.size(); i++) {
			double upper = resolutions.get(i);
			double lower = resolutions.get(i + 1);
			if (resolution < upper && resolution >= lower) {
				if (Math.abs(upper - resolution) >= Math.abs(lower - resolution)) {
					return i + 1;
				} else {
					return i;
				}
			}
		}
		return 0;
	}

	@Override
	public double getMaximumResolution() {
		if (resolutions.size() == 0) {
			return Double.MAX_VALUE;
		}
		return resolutions.get(0);
	}

	@Override
	public double getMinimumResolution() {
		if (resolutions.size() == 0) {
			return 0;
		}
		return resolutions.get(resolutions.size() - 1);
	}

	protected void setMapSize(int width, int height) {
		if (this.mapWidth != width || this.mapHeight != height) {
			View oldView = getView();
			Coordinate screen = new Coordinate((double) width / 2.0, (double) height / 2.0);
			position = getTransformationService().transform(screen, RenderSpace.SCREEN, RenderSpace.WORLD);
			this.mapWidth = width;
			this.mapHeight = height;
			if (eventBus != null) {
				eventBus.fireEvent(new ViewPortChangedEvent(oldView, getView(), currentAnimation));
			}
		}
	}

	@Override
	public String getCrs() {
		return crs;
	}

	@Override
	public int getMapWidth() {
		return mapWidth;
	}

	@Override
	public int getMapHeight() {
		return mapHeight;
	}

	// -------------------------------------------------------------------------
	// Methods that retrieve what is visible on the map:
	// -------------------------------------------------------------------------

	@Override
	public Coordinate getPosition() {
		return new Coordinate(position);
	}

	@Override
	public double getResolution() {
		return resolution;
	}

	@Override
	public View getView() {
		return new View(new Coordinate(position), resolution);
	}

	/**
	 * Given the information in this ViewPort object, what is the currently visible area? This value is expressed in
	 * world coordinates.
	 *
	 * @return Returns the bounding box that covers the currently visible area on the map.
	 */
	public Bbox getBounds() {
		double w = mapWidth * resolution;
		double h = mapHeight * resolution;
		double x = position.getX() - w / 2;
		double y = position.getY() - h / 2;
		return new Bbox(x, y, w, h);
	}

	// -------------------------------------------------------------------------
	// Methods that manipulate what is visible on the map:
	// -------------------------------------------------------------------------

	@Override
	public void registerAnimation(NavigationAnimation animation) {
		boolean cancelSupport = configuration.getHintValue(MapConfiguration.ANIMATION_CANCEL_SUPPORT);
		if (!cancelSupport && currentAnimation != null) {
			return;
		}

		if (currentAnimation != null) {
			currentAnimation.cancel();
		}
		this.currentAnimation = animation;

		// Schedule the animation from the moment the browser event loop returns:
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				if (currentAnimation != null) {
					if (Dom.isTransformationSupported()) {
						currentAnimation.run();
					} else {
						applyView(currentAnimation.getEndView());
						currentAnimation = null;
					}
				}
			}
		});
	}

	@Override
	public void applyPosition(Coordinate coordinate) {
		Coordinate tempPosition = checkPosition(coordinate, resolution);
		if (tempPosition != position) {
			View oldView = getView();
			position = tempPosition;
			eventBus.fireEvent(new ViewPortChangedEvent(oldView, getView(), currentAnimation));
		}
	}

	@Override
	public void applyResolution(double resolution) {
		applyResolution(resolution, position, ZoomOption.FREE);
	}

	@Override
	public void applyResolution(double resolution, ZoomOption zoomOption) {
		applyResolution(resolution, position, zoomOption);
	}

	@Override
	public void applyView(View view) {
		applyView(view, ZoomOption.FREE);
	}

	@Override
	public void applyView(View view, ZoomOption zoomOption) {
		double tempResolution = checkResolution(view.getResolution(), ZoomOption.FREE);
		Coordinate tempPosition = checkPosition(view.getPosition(), tempResolution);
		applyViewNoChecks(tempPosition, tempResolution);
	}

	@Override
	public void applyBounds(Bbox bounds) {
		applyBounds(bounds, ZoomOption.FREE);
	}

	@Override
	public void applyBounds(Bbox bounds, ZoomOption zoomOption) {
		double tempResolution = getResolutionForBounds(bounds, zoomOption);
		Coordinate tempPosition = checkPosition(BboxService.getCenterPoint(bounds), tempResolution);
		applyViewNoChecks(tempPosition, tempResolution);
	}

	@Override
	public double toResolution(double scaleDenominator) {
		double pixelsPerUnit = getPixelLength() / configuration.getUnitLength();
		return pixelsPerUnit * scaleDenominator;
	}

	@Override
	public Bbox asBounds(View view) {
		double w = mapWidth * view.getResolution();
		double h = mapHeight * view.getResolution();
		double x = view.getPosition().getX() - w / 2;
		double y = view.getPosition().getY() - h / 2;
		return new Bbox(x, y, w, h);
	}

	@Override
	public View asView(Bbox bounds, ZoomOption zoomOption) {
		double tempResolution = getResolutionForBounds(bounds, zoomOption);
		Coordinate tempPosition = checkPosition(BboxService.getCenterPoint(bounds), tempResolution);
		return new View(tempPosition, tempResolution);
	}

	@Override
	public ViewPortTransformationService getTransformationService() {
		return transformationService;
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	protected double getPixelLength() {
		return METER_PER_INCH / configuration.getHintValue(MapConfiguration.DPI);
	}

	private void applyResolution(double newResolution, Coordinate rescalePoint, ZoomOption zoomOption) {
		double validResolution = checkResolution(newResolution, zoomOption);
		if (validResolution != resolution) {
			// Calculate theoretical new bounds. First create a BBOX of correct size:
			Bbox newBbox = new Bbox(0, 0, getMapWidth() * validResolution, getMapHeight() * validResolution);

			// Calculate translate vector to assure rescalePoint is on the same position as before.
			double factor = resolution / validResolution;
			double dX = (rescalePoint.getX() - position.getX()) * (1 - 1 / factor);
			double dY = (rescalePoint.getY() - position.getY()) * (1 - 1 / factor);

			// Apply translation to set the BBOX on the correct location:
			newBbox = BboxService.setCenterPoint(newBbox, new Coordinate(position.getX(), position.getY()));
			newBbox = BboxService.translate(newBbox, dX, dY);

			// Now apply on this view port:
			Coordinate tempPosition = checkPosition(BboxService.getCenterPoint(newBbox), validResolution);
			applyViewNoChecks(tempPosition, validResolution);
		}
	}

	private double getResolutionForBounds(Bbox bounds, ZoomOption zoomOption) {
		double wRatio;
		double boundsWidth = bounds.getWidth();
		if (boundsWidth <= 0) {
			wRatio = getMinimumResolution();
		} else {
			wRatio = boundsWidth / mapWidth;
		}
		double hRatio;
		double boundsHeight = bounds.getHeight();
		if (boundsHeight <= 0) {
			hRatio = getMinimumResolution();
		} else {
			hRatio = boundsHeight / mapHeight;
		}
		// Return the checked resolution for the minimum to fit inside:
		return checkResolution(wRatio > hRatio ? wRatio : hRatio, zoomOption);
	}

	private double getMaxBoundsResolution() {
		if (maxBounds == null) {
			return 0;
		}
		double wRatio;
		double boundsWidth = maxBounds.getWidth();
		if (boundsWidth <= 0) {
			wRatio = getMaximumResolution();
		} else {
			wRatio = boundsWidth / mapWidth;
		}
		double hRatio;
		double boundsHeight = maxBounds.getHeight();
		if (boundsHeight <= 0) {
			hRatio = getMaximumResolution();
		} else {
			hRatio = boundsHeight / mapHeight;
		}
		// Return the checked resolution for the minimum to fit inside:
		return wRatio > hRatio ? wRatio : hRatio;
	}

	// Returns a position that's within the maximum bounds:
	private Coordinate checkPosition(final Coordinate newPosition, final double newResolution) {
		double xCenter = newPosition.getX();
		double yCenter = newPosition.getY();
		if (maxBounds != null) {
			double w = mapWidth * newResolution / 2;
			double h = mapHeight * newResolution / 2;
			Coordinate minCoordinate = BboxService.getOrigin(maxBounds);
			Coordinate maxCoordinate = BboxService.getEndPoint(maxBounds);

			if ((w * 2) > maxBounds.getWidth()) {
				xCenter = BboxService.getCenterPoint(maxBounds).getX();
			} else {
				if ((xCenter - w) < minCoordinate.getX()) {
					xCenter = minCoordinate.getX() + w;
				}
				if ((xCenter + w) > maxCoordinate.getX()) {
					xCenter = maxCoordinate.getX() - w;
				}
			}
			if ((h * 2) > maxBounds.getHeight()) {
				yCenter = BboxService.getCenterPoint(maxBounds).getY();
			} else {
				if ((yCenter - h) < minCoordinate.getY()) {
					yCenter = minCoordinate.getY() + h;
				}
				if ((yCenter + h) > maxCoordinate.getY()) {
					yCenter = maxCoordinate.getY() - h;
				}
			}
		}
		return new Coordinate(xCenter, yCenter);
	}

	// Returns a resolution as requested by the zoom option:
	private double checkResolution(double resolution, ZoomOption zoomOption) {
		double allowedResolution = resolution;
		double maximumResolution = getMaximumResolution();
		double minimumResolution = getMinimumResolution();
		if (allowedResolution > maximumResolution) {
			allowedResolution = maximumResolution;
		} else if (allowedResolution < minimumResolution) {
			allowedResolution = minimumResolution;
		}

		if (zoomOption == ZoomOption.FREE) {
			return allowedResolution;
		}

		for (int i = 0; i < getResolutionCount() - 1; i++) {
			double upper = getResolution(i);
			double lower = getResolution(i + 1);

			if (allowedResolution == upper) {
				return upper;
			} else if (allowedResolution == lower) {
				return lower;
			} else if (allowedResolution < upper && allowedResolution > lower) {
				switch (zoomOption) {
					case LEVEL_FIT:
						return upper;
					case LEVEL_CLOSEST:
						if (Math.abs(upper - allowedResolution) < Math.abs(allowedResolution - lower)) {
							return upper;
						} else {
							return lower;
						}
					default:
						return allowedResolution;
				}
			}
		}
		return allowedResolution;
	}

	private void applyViewNoChecks(Coordinate tempPosition, double tempResolution) {
		if (tempResolution != resolution || !position.equals(tempPosition)) {
			View oldView = getView();
			resolution = tempResolution;
			position = tempPosition;
			eventBus.fireEvent(new ViewPortChangedEvent(oldView, getView(), currentAnimation));
		}
	}
}
