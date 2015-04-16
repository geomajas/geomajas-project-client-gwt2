package org.geomajas.gwt2.plugin.wfs.example.client.sample;

import org.geomajas.gwt2.client.gfx.VectorContainer;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.AbstractLayer;
import org.geomajas.gwt2.client.map.render.LayerRenderer;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeInfo;
import org.vaadin.gwtgraphics.client.VectorObjectContainer;

public class SimpleWfsLayer extends AbstractLayer {

	private WfsFeatureTypeInfo wfsFeatureTypeInfo;

	private String id;

	private SimpleWfsRenderer layerRenderer;

	private String baseUrl;

	public SimpleWfsLayer(String baseUrl, WfsFeatureTypeInfo wfsFeatureTypeInfo, String id, ViewPort viewPort,
			VectorObjectContainer container) {
		super(id);
		this.baseUrl = baseUrl;
		this.wfsFeatureTypeInfo = wfsFeatureTypeInfo;
		layerRenderer = new SimpleWfsRenderer(this, viewPort, container);
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getTitle() {
		return wfsFeatureTypeInfo.getTitle();
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public String getTypeName() {
		return wfsFeatureTypeInfo.getName();
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setWfsFeatureTypeInfo(WfsFeatureTypeInfo wfsFeatureTypeInfo) {
		this.wfsFeatureTypeInfo = wfsFeatureTypeInfo;
	}

	@Override
	public LayerRenderer getRenderer() {
		return layerRenderer;
	}

	@Override
	public void setOpacity(double opacity) {

	}

	@Override
	public double getOpacity() {
		return 1.0;
	}

}
