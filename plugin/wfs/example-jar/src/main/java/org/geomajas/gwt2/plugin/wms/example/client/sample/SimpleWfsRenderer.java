package org.geomajas.gwt2.plugin.wms.example.client.sample;

import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.render.LayerRenderer;
import org.geomajas.gwt2.client.map.render.RenderingInfo;
import org.geomajas.gwt2.plugin.wfs.client.WfsClient;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureCollectionInfo;
import org.geomajas.gwt2.plugin.wfs.client.query.dto.IncludeCriterionDto;
import org.geomajas.gwt2.plugin.wfs.client.query.dto.QueryDto;
import org.geomajas.gwt2.plugin.wfs.client.service.WfsService.WfsVersion;
import org.vaadin.gwtgraphics.client.VectorObjectContainer;

import com.google.gwt.core.client.Callback;

public class SimpleWfsRenderer implements LayerRenderer {

	private SimpleWfsLayer layer;

	private ViewPort viewPort;

	private VectorObjectContainer container;

	private boolean featuresLoaded;

	public SimpleWfsRenderer(SimpleWfsLayer layer, ViewPort viewPort, VectorObjectContainer container) {
		this.layer = layer;
		this.viewPort = viewPort;
		this.container = container;
	}

	@Override
	public void render(RenderingInfo renderingInfo) {
		if (!featuresLoaded) {
			featuresLoaded = true;
			QueryDto query = new QueryDto();
			query.setMaxFeatures(100);
			query.setMaxCoordsPerFeature(100);
			query.setCriterion(new IncludeCriterionDto());
			query.setCrs(viewPort.getCrs());
			WfsClient
					.getInstance()
					.getWfsService()
					.getFeatures(WfsVersion.V1_0_0, layer.getBaseUrl(), layer.getTypeName(), query,
							new Callback<WfsFeatureCollectionInfo, String>() {

								@Override
								public void onSuccess(WfsFeatureCollectionInfo result) {
									for (Feature feature : result.getFeatures()) {
										container.add(GeomajasImpl.getInstance().getGfxUtil().toShape(feature.getGeometry()));
									}

								}

								@Override
								public void onFailure(String reason) {
									// TODO Auto-generated method stub

								}
							});
		}
	}

	@Override
	public Layer getLayer() {
		return layer;
	}

	@Override
	public void setOpacity(double opacity) {
	}

	@Override
	public double getOpacity() {
		return 1.0;
	}

}
