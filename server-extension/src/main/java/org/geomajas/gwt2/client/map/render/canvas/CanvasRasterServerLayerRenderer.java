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

package org.geomajas.gwt2.client.map.render.canvas;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.dto.GetRasterTilesRequest;
import org.geomajas.command.dto.GetRasterTilesResponse;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Matrix;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.animation.Trajectory;
import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.layer.RasterServerLayer;
import org.geomajas.gwt2.client.map.render.LayerRenderer;
import org.geomajas.gwt2.client.map.render.RenderingInfo;
import org.geomajas.layer.tile.RasterTile;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.Callback;

/**
 * Canvas renderer for {@link RasterServerLayer}.
 * 
 * @author Jan De Moerloose
 *
 */
public class CanvasRasterServerLayerRenderer implements LayerRenderer {

	private RasterServerLayer layer;

	private ViewPort viewPort;

	private List<ImageLoader> images;

	private double mapExtentScaleAtFetch = 1.1;

	private Bbox fetchingTileBounds;

	private Canvas canvas;

	public CanvasRasterServerLayerRenderer(RasterServerLayer layer, ViewPort viewPort) {
		this.layer = layer;
		this.viewPort = viewPort;
		this.images = new ArrayList<ImageLoader>();
	}

	@Override
	public void render(RenderingInfo renderingInfo) {
		if (layer.isShowing()) {
			canvas = (Canvas) renderingInfo.getWidget();
			final View view = renderingInfo.getView();
			Bbox bounds = asBounds(view);
			int level = viewPort.getResolutionIndex(view.getResolution());
			final double resolution = viewPort.getResolution(level);
			Trajectory t = renderingInfo.getTrajectory();
			// in a trajectory we just don't fetch (better approach upcoming :-) !
			if (t == null) {
				// Scale the bounds to fetch tiles for (we want a bigger area than the map bounds):
				fetchingTileBounds = BboxService.scale(bounds, mapExtentScaleAtFetch);

				// Create the command:
				GetRasterTilesRequest request = new GetRasterTilesRequest();
				request.setBbox(new org.geomajas.geometry.Bbox(fetchingTileBounds.getX(), fetchingTileBounds.getY(),
						fetchingTileBounds.getWidth(), fetchingTileBounds.getHeight()));
				request.setCrs(viewPort.getCrs());
				request.setLayerId(layer.getServerLayerId());
				request.setScale(1 / resolution);
				GwtCommand command = new GwtCommand(GetRasterTilesRequest.COMMAND);
				command.setCommandRequest(request);

				// Execute the fetch, and render on success:
				GeomajasServerExtension.getInstance().getCommandService()
						.execute(command, new AbstractCommandCallback<GetRasterTilesResponse>() {

							public void execute(GetRasterTilesResponse response) {
								addTiles(response.getRasterData(), resolution);
							}
						});
			}
			// always render what we have !
			renderCanvas();
		}
	}

	@Override
	public Layer getLayer() {
		return layer;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void renderCanvas() {
		for (ImageLoader image : images) {
			// canvas can only render loaded images !
			if(image.isLoaded()) {
				renderImage(image);
			}
		}
	}

	protected void renderImage(ImageLoader image) {
		// save-restore cycle (does it affect performance ?)
		canvas.getContext2d().save();
		// we have to set the transform to the extra bit needed to go to screen space (scale delta + translation)
		double scaleDelta = image.getResolution() / viewPort.getResolution();
		Matrix t = viewPort.getTransformationService().getTranslationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN);
		canvas.getContext2d().setTransform(scaleDelta, 0, 0, scaleDelta, t.getDx(), t.getDy());
		// now draw
		canvas.getContext2d().drawImage(image.getImageElement(), image.getBbox().getX(), image.getBbox().getY(),
				image.getBbox().getWidth(), image.getBbox().getHeight());
		// and restore
		canvas.getContext2d().restore();
	}

	protected void addTiles(List<org.geomajas.layer.tile.RasterTile> rasterTiles, double resolution) {
		for (RasterTile tile : rasterTiles) {
			// Add the tile to the list and load it:
			loadTile(tile, resolution);
		}
	}

	protected void loadTile(RasterTile tile, double resolution) {
		RenderTileOnCallback callback = new RenderTileOnCallback();
		ImageLoader htmlImage = new ImageLoader(tile.getUrl(), tile.getBounds(), callback, resolution);
		callback.setImage(htmlImage);
		images.add(htmlImage);
	}

	protected Bbox asBounds(View view) {
		Bbox bounds = viewPort.asBounds(view);
		return bounds;
	}

	public class RenderTileOnCallback implements Callback<String, String> {

		private ImageLoader image;

		public void setImage(ImageLoader htmlImage) {
			this.image = htmlImage;
		}

		@Override
		public void onFailure(String reason) {
		}

		@Override
		public void onSuccess(String result) {
			renderImage(image);
		}

	}

}
