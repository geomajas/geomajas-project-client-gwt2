/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.plugin.rasterizing;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.geometry.Crs;
import org.geomajas.geometry.CrsTransform;
import org.geomajas.global.GeomajasException;
import org.geomajas.internal.layer.tile.InternalTileImpl;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.pipeline.GetTileContainer;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheManagerService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Step which does the actual rasterizing. This is done by invoking the get getTile() call on the vector layer.
 * The {@link RasterTileStep} there will actually fill the {@link RasterizingContainer} object.
 *
 * @author Joachim Van der Auwera
 */
public class RasterizeStep extends AbstractRasterizingStep {

	private final Logger log = LoggerFactory.getLogger(RasterizeStep.class);

	@Autowired
	private CacheManagerService cacheManager;

	@Autowired
	private PipelineService pipelineService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private DtoConverterService dtoConverterService;

	public void execute(PipelineContext rasterContext, RasterizingContainer rasterizingContainer) throws
			GeomajasException {

		String cacheKey = rasterContext.get(RasterizingPipelineCode.IMAGE_ID_KEY, String.class);
		String layerId = rasterContext.get(PipelineCode.LAYER_ID_KEY, String.class);
		VectorLayer layer = rasterContext.get(PipelineCode.LAYER_KEY, VectorLayer.class);

		RebuildCacheContainer cc = cacheManager.get(layer, CacheCategory.REBUILD, cacheKey,
				RebuildCacheContainer.class);
		if (null == cc) {
			log.error("Cannot find raster information in rebuild cache. This will result in zero-length image.");
			rasterizingContainer.setImage(new byte[0]);
		} else {
			TileMetadata tileMetadata = cc.getMetadata();
			// @todo need to switch to the correct security context

			PipelineContext context = pipelineService.createContext();
			context.put(RasterizingPipelineCode.CONTAINER_KEY, rasterizingContainer); // container to render image in!
			context.put(PipelineCode.LAYER_ID_KEY, layerId);
			context.put(PipelineCode.LAYER_KEY, layer);
			context.put(PipelineCode.TILE_METADATA_KEY, tileMetadata);
			Crs crs = geoService.getCrs2(tileMetadata.getCrs());
			context.put(PipelineCode.CRS_KEY, crs);
			CrsTransform layerToMap = geoService.getCrsTransform(layer.getCrs(), crs);
			context.put(PipelineCode.CRS_TRANSFORM_KEY, layerToMap);
			context.put(PipelineCode.FEATURE_INCLUDES_KEY, tileMetadata.getFeatureIncludes());
			Envelope layerExtent = dtoConverterService.toInternal(layer.getLayerInfo().getMaxExtent());
			Envelope tileExtent = geoService.transform(layerExtent, layerToMap);
			context.put(PipelineCode.TILE_MAX_EXTENT_KEY, tileExtent);
			InternalTile tile = new InternalTileImpl(tileMetadata.getCode(), tileExtent, tileMetadata.getScale());
			GetTileContainer response = new GetTileContainer();
			response.setTile(tile);
			pipelineService.execute(PipelineCode.PIPELINE_GET_VECTOR_TILE, layerId, context, response);
			log.debug("getTile response InternalTile {}", response);
		}
	}
}
