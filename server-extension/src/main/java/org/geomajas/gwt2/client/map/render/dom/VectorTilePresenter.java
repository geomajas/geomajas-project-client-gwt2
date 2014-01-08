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

package org.geomajas.gwt2.client.map.render.dom;

import org.geomajas.command.dto.GetVectorTileRequest;
import org.geomajas.command.dto.GetVectorTileResponse;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.Deferred;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.util.Dom;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.layer.VectorServerLayer;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlContainer;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlImage;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlImageImpl;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.tile.VectorTile;
import org.geomajas.layer.tile.VectorTile.VectorTileContentType;

import com.google.gwt.core.client.Callback;

/**
 * Presenter for a single tile within a vector layer.
 * 
 * @author Pieter De Graef
 */
public class VectorTilePresenter {

	/**
	 * Enumeration the determines tile loading status.
	 * 
	 * @author Pieter De Graef
	 */
	static enum STATUS {

		EMPTY, LOADING, LOADED
	}

	private final VectorServerLayer layer;

	private final HtmlContainer container;

	private final TileCode tileCode;

	private final double scale;

	private final String crs;

	private final Callback<String, String> onRendered;

	private HtmlImage display;

	private Deferred deferred;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	public VectorTilePresenter(VectorServerLayer layer, HtmlContainer container, TileCode tileCode, double scale,
			String crs, Callback<String, String> onRendered) {
		this.layer = layer;
		this.container = container;
		this.tileCode = tileCode;
		this.scale = scale;
		this.crs = crs;
		this.onRendered = onRendered;
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/** Render this tile. */
	public void render() {
		GwtCommand command = createCommand();
		deferred = GeomajasServerExtension.getCommandService().execute(command,
				new AbstractCommandCallback<GetVectorTileResponse>() {

					public void execute(GetVectorTileResponse response) {
						if (!(deferred != null && deferred.isCancelled())) {
							VectorTile tile = response.getTile();
							if (tile.getContentType() == VectorTileContentType.STRING_CONTENT) {
								throw new IllegalStateException(
										"Backend VectorLayers need to use the rasterizing plugin.");
							} else {
								Coordinate position = getTilePosition();
								display = new HtmlImageImpl(tile.getFeatureContent(), tile.getScreenWidth(), tile
										.getScreenHeight(), (int) Math.round(position.getY()), (int) Math
										.round(position.getX()), onRendered);
								// We don't want to fetch the images twice...
								// display.setContent(tile.getFeatureContent());
								container.add(display);
							}
						}
					}
				});
	}

	/** Cancel the fetching of this tile. No call-back will be executed anymore. */
	public void cancel() {
		if (deferred != null) {
			deferred.cancel();
		}
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/**
	 * Get tile code.
	 * 
	 * @return tile code
	 */
	public TileCode getTileCode() {
		return tileCode;
	}

	/**
	 * Return the current status of this VectorTile. Can be one of the following:
	 * <ul>
	 * <li>STATUS.EMPTY</li>
	 * <li>STATUS.LOADING</li>
	 * <li>STATUS.LOADED</li>
	 * </ul>
	 * 
	 * @return tile status
	 */
	public STATUS getStatus() {
		if (display != null) {
			return STATUS.LOADED;
		}
		if (deferred == null) {
			return STATUS.EMPTY;
		}
		return STATUS.LOADING;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private GwtCommand createCommand() {
		GetVectorTileRequest request = new GetVectorTileRequest();
		request.setCode(tileCode);
		request.setCrs(crs);
		request.setFilter(layer.getFilter());
		request.setLayerId(layer.getServerLayerId());

		// TODO Add support for labels
		request.setPaintGeometries(true);
		request.setPaintLabels(false);
		// request.setPaintLabels(renderer.getLayer().isLabeled());
		request.setPanOrigin(new Coordinate());
		request.setRenderer(Dom.isSvg() ? "SVG" : "VML");
		request.setScale(scale);
		request.setStyleInfo(layer.getLayerInfo().getNamedStyleInfo());
		GwtCommand command = new GwtCommand(GetVectorTileRequest.COMMAND);
		command.setCommandRequest(request);
		return command;
	}

	private Coordinate getTilePosition() {
		org.geomajas.geometry.Bbox layerBounds = layer.getLayerInfo().getMaxExtent();

		// Calculate tile width and height for tileLevel=tileCode.getTileLevel(); This is in world space.
		double div = Math.pow(2, tileCode.getTileLevel());
		double tileWidth = Math.ceil((scale * layerBounds.getWidth()) / div) / scale;
		double tileHeight = Math.ceil((scale * layerBounds.getHeight()) / div) / scale;

		// Now get the top-left corner for the tile in world space:
		double x = layerBounds.getX() + tileCode.getX() * tileWidth;
		double y = layerBounds.getY() + tileCode.getY() * tileHeight;

		// Convert to screen space. Note that the Y-axis is inverted, and so the top corner from the tile BBOX (world)
		// becomes the bottom corner (screen). That is why the tileHeight is added before compensating with the scale.
		x *= scale;
		y = -Math.round(scale * (y + tileHeight));
		return new Coordinate(x, y);
	}
}