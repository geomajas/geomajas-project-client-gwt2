/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.client.map;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt.client.util.Dom;
import org.geomajas.gwt2.client.animation.NavigationAnimation;
import org.geomajas.gwt2.client.event.NavigationStopEvent;
import org.geomajas.gwt2.client.event.NavigationStopHandler;
import org.geomajas.gwt2.client.event.ViewPortChangedEvent;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;

/**
 * Implementation of the ViewPort interface.
 * 
 * @author Pieter De Graef
 */
public final class ViewPortImpl implements ViewPort {

	private final MapEventBus eventBus;

	private final MapConfiguration configuration;

	private final ViewPortTransformationService transformationService;

	/** The map's width in pixels. */
	private int mapWidth;

	/** The map's height in pixels. */
	private int mapHeight;

	/** The maximum bounding box available to this MapView. Never go outside it! */
	private Bbox maxBounds;

	private final List<Double> fixedScales = new ArrayList<Double>();

	private String crs;

	// Current viewing parameters:

	private double scale;

	private Coordinate position;

	private boolean initialized;

	private NavigationAnimation currentAnimation;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public ViewPortImpl(MapEventBus eventBus, MapConfiguration configuration) {
		this.eventBus = eventBus;
		this.configuration = configuration;
		this.transformationService = new ViewPortTransformationServiceImpl(this);
		this.position = new Coordinate();

		eventBus.addNavigationStopHandler(new NavigationStopHandler() {

			@Override
			public void onNavigationStopped(NavigationStopEvent event) {
				currentAnimation = null;
			}
		});
		if (configuration.getMapOptions() != null) {
			initialize(configuration.getMapOptions());
		}
	}

	// -------------------------------------------------------------------------
	// Configuration stuff:
	// -------------------------------------------------------------------------

	protected void initialize(MapOptions mapOptions) {
		crs = mapOptions.getCrs();

		// Calculate maximum bounds:
		maxBounds = new Bbox(mapOptions.getMaxBounds().getX(), mapOptions.getMaxBounds().getY(), mapOptions
				.getMaxBounds().getWidth(), mapOptions.getMaxBounds().getHeight());

		if (mapOptions.getResolutions() != null && mapOptions.getResolutions().size() > 0) {
			for (Double resolution : mapOptions.getResolutions()) {
				fixedScales.add(resolution);
			}
		} else if (mapOptions.getMaximumScale() != 0) {
			// If there are no fixed scale levels, we'll calculate them:
			double tempScale = getMaxBoundsScale();
			fixedScales.add(tempScale);
			while (tempScale < mapOptions.getMaximumScale()) {
				tempScale *= 2;
				fixedScales.add(tempScale);
			}
		} else {
			throw new IllegalStateException(
					"The map configuration must either contain fixed resolutions or a maximum scale");
		}

		initialized = true;
	}

	@Override
	public Bbox getMaximumBounds() {
		return maxBounds;
	}

	@Override
	public double getMinimumScale() {
		if (fixedScales.size() == 0) {
			return 0;
		}
		return fixedScales.get(0);
	}

	@Override
	public double getMaximumScale() {
		if (fixedScales.size() == 0) {
			return Double.MAX_VALUE;
		}
		return fixedScales.get(fixedScales.size() - 1);
	}

	@Override
	public int getFixedScaleCount() {
		return fixedScales.size();
	}

	@Override
	public double getFixedScale(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("Scale index cannot be found.");
		}
		if (index >= fixedScales.size()) {
			throw new IllegalArgumentException("Scale index cannot be found.");
		}
		return fixedScales.get(index);
	}

	@Override
	public int getFixedScaleIndex(double scale) {
		double minimumScale = getMinimumScale();
		if (scale <= minimumScale) {
			return 0;
		}
		double maximumScale = getMaximumScale();
		if (scale >= maximumScale) {
			return fixedScales.size() - 1;
		}

		for (int i = 0; i < fixedScales.size(); i++) {
			double lower = fixedScales.get(i);
			double upper = fixedScales.get(i + 1);
			if (scale <= upper && scale > lower) {
				if (Math.abs(upper - scale) >= Math.abs(lower - scale)) {
					return i;
				} else {
					return i + 1;
				}
			}
		}
		return 0;
	}

	@Override
	public void setMapSize(int width, int height) {
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
	public double getScale() {
		return scale;
	}

	@Override
	public View getView() {
		return new View(new Coordinate(position), scale);
	}

	/**
	 * Given the information in this ViewPort object, what is the currently visible area? This value is expressed in
	 * world coordinates.
	 * 
	 * @return Returns the bounding box that covers the currently visible area on the map.
	 */
	public Bbox getBounds() {
		double w = mapWidth / scale;
		double h = mapHeight / scale;
		double x = position.getX() - w / 2;
		double y = position.getY() - h / 2;
		return new Bbox(x, y, w, h);
	}

	// -------------------------------------------------------------------------
	// Methods that manipulate what is visible on the map:
	// -------------------------------------------------------------------------

	@Override
	public void registerAnimation(NavigationAnimation animation) {
		boolean cancelSupport = configuration.getMapHintValue(MapConfiguration.ANIMATION_CANCEL_SUPPORT);
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
		Coordinate tempPosition = checkPosition(coordinate, scale);
		if (tempPosition != position) {
			View oldView = getView();
			position = tempPosition;
			eventBus.fireEvent(new ViewPortChangedEvent(oldView, getView(), currentAnimation));
		}
	}

	@Override
	public void applyScale(double scale) {
		applyScale(scale, position, ZoomOption.FREE);
	}

	@Override
	public void applyScale(double scale, ZoomOption zoomOption) {
		applyScale(scale, position, zoomOption);
	}

	@Override
	public void applyView(View view) {
		applyView(view, ZoomOption.FREE);
	}

	@Override
	public void applyView(View view, ZoomOption zoomOption) {
		double tempScale = checkScale(view.getScale(), ZoomOption.FREE);
		Coordinate tempPosition = checkPosition(view.getPosition(), scale);
		applyViewNoChecks(tempPosition, tempScale);
	}

	@Override
	public void applyBounds(Bbox bounds) {
		applyBounds(bounds, ZoomOption.FREE);
	}

	@Override
	public void applyBounds(Bbox bounds, ZoomOption zoomOption) {
		double tempScale = getScaleForBounds(bounds, zoomOption);
		Coordinate tempPosition = checkPosition(BboxService.getCenterPoint(bounds), tempScale);
		applyViewNoChecks(tempPosition, tempScale);
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}

	@Override
	public double toScale(double scaleDenominator) {
		//return mapInfo.getUnitLength() / (mapInfo.getPixelLength() * scaleDenominator);
		return 1 / (configuration.getMapOptions().getPixelsPerUnit() * scaleDenominator);
	}

	@Override
	public Bbox asBounds(View view) {
		double w = mapWidth / view.getScale();
		double h = mapHeight / view.getScale();
		double x = view.getPosition().getX() - w / 2;
		double y = view.getPosition().getY() - h / 2;
		return new Bbox(x, y, w, h);
	}

	@Override
	public View asView(Bbox bounds, ZoomOption zoomOption) {
		double tempScale = getScaleForBounds(bounds, zoomOption);
		Coordinate tempPosition = checkPosition(BboxService.getCenterPoint(bounds), tempScale);
		return new View(tempPosition, tempScale);
	}

	@Override
	public ViewPortTransformationService getTransformationService() {
		return transformationService;
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	private void applyScale(double newScale, Coordinate rescalePoint, ZoomOption zoomOption) {
		double limitedScale = checkScale(newScale, zoomOption);
		if (limitedScale != scale) {
			// Calculate theoretical new bounds. First create a BBOX of correct size:
			Bbox newBbox = new Bbox(0, 0, getMapWidth() / limitedScale, getMapHeight() / limitedScale);

			// Calculate translate vector to assure rescalePoint is on the same position as before.
			double factor = limitedScale / scale;
			double dX = (rescalePoint.getX() - position.getX()) * (1 - 1 / factor);
			double dY = (rescalePoint.getY() - position.getY()) * (1 - 1 / factor);

			// Apply translation to set the BBOX on the correct location:
			newBbox = BboxService.setCenterPoint(newBbox, new Coordinate(position.getX(), position.getY()));
			newBbox = BboxService.translate(newBbox, dX, dY);

			// Now apply on this view port:
			Coordinate tempPosition = checkPosition(BboxService.getCenterPoint(newBbox), limitedScale);
			applyViewNoChecks(tempPosition, limitedScale);
		}
	}

	private double getScaleForBounds(Bbox bounds, ZoomOption zoomOption) {
		double wRatio;
		double boundsWidth = bounds.getWidth();
		if (boundsWidth <= 0) {
			wRatio = getMinimumScale();
		} else {
			wRatio = mapWidth / boundsWidth;
		}
		double hRatio;
		double boundsHeight = bounds.getHeight();
		if (boundsHeight <= 0) {
			hRatio = getMinimumScale();
		} else {
			hRatio = mapHeight / boundsHeight;
		}
		// Return the checked scale for the minimum to fit inside:
		return checkScale(wRatio < hRatio ? wRatio : hRatio, zoomOption);
	}

	private double getMaxBoundsScale() {
		if (maxBounds == null) {
			return 0;
		}
		double wRatio;
		double boundsWidth = maxBounds.getWidth();
		if (boundsWidth <= 0) {
			wRatio = getMinimumScale();
		} else {
			wRatio = mapWidth / boundsWidth;
		}
		double hRatio;
		double boundsHeight = maxBounds.getHeight();
		if (boundsHeight <= 0) {
			hRatio = getMinimumScale();
		} else {
			hRatio = mapHeight / boundsHeight;
		}
		// Return the checked scale for the minimum to fit inside:
		return wRatio < hRatio ? wRatio : hRatio;
	}

	// Returns a position that's within the maximum bounds:
	private Coordinate checkPosition(final Coordinate newPosition, final double newScale) {
		double xCenter = newPosition.getX();
		double yCenter = newPosition.getY();
		if (maxBounds != null) {
			double w = mapWidth / (newScale * 2);
			double h = mapHeight / (newScale * 2);
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

	// Returns a scale as requested by the zoom option:
	private double checkScale(double scale, ZoomOption zoomOption) {
		double allowedScale = scale;
		double minimumScale = getMinimumScale();
		if (allowedScale < minimumScale) {
			allowedScale = minimumScale;
		} else if (allowedScale > getMaximumScale()) {
			allowedScale = getMaximumScale();
		}

		if (zoomOption == ZoomOption.FREE) {
			return allowedScale;
		}

		for (int i = 0; i < fixedScales.size() - 1; i++) {
			double lower = fixedScales.get(i);
			double upper = fixedScales.get(i + 1);

			if (allowedScale == upper) {
				return upper;
			} else if (allowedScale == lower) {
				return lower;
			} else if (allowedScale < upper && allowedScale > lower) {
				switch (zoomOption) {
					case LEVEL_FIT:
						return lower;
					case LEVEL_CLOSEST:
						if (Math.abs(upper - allowedScale) < Math.abs(allowedScale - lower)) {
							return upper;
						} else {
							return lower;
						}
					default:
						return allowedScale;
				}
			}
		}

		return allowedScale;
	}

	private void applyViewNoChecks(Coordinate tempPosition, double tempScale) {
		if (tempScale != scale || !position.equals(tempPosition)) {
			View oldView = getView();
			scale = tempScale;
			position = tempPosition;
			eventBus.fireEvent(new ViewPortChangedEvent(oldView, getView(), currentAnimation));
		}
	}
}