/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.gwt2.plugin.wfs.example.client.sample;

import java.util.Collection;
import java.util.List;

import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.feature.query.Criterion;
import org.geomajas.gwt2.client.map.layer.AbstractLayer;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeDescriptionInfo;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeInfo;
import org.geomajas.gwt2.plugin.wfs.client.service.WfsService.WfsVersion;
import org.vaadin.gwtgraphics.client.VectorObjectContainer;

/**
 * Simple WFS layer implementation that shows features in {@link VectorObjectContainer}.
 * 
 * @author Jan De Moerloose
 *
 */
public class SimpleWfsLayer extends AbstractLayer implements FeaturesSupported {

	private WfsFeatureTypeInfo wfsFeatureTypeInfo;

	private String id;

	private SimpleWfsRenderer layerRenderer;

	private String baseUrl;

	private WfsVersion version;

	private WfsFeatureTypeDescriptionInfo wfsFeatureTypeDescriptionInfo;

	public SimpleWfsLayer(WfsVersion version, String baseUrl, WfsFeatureTypeInfo wfsFeatureTypeInfo,
			WfsFeatureTypeDescriptionInfo wfsFeatureTypeDescriptionInfo, String id, ViewPort viewPort,
			VectorObjectContainer container, MapEventBus eventBus) {
		super(id);
		this.version = version;
		this.baseUrl = baseUrl;
		this.wfsFeatureTypeInfo = wfsFeatureTypeInfo;
		this.wfsFeatureTypeDescriptionInfo = wfsFeatureTypeDescriptionInfo;
		layerRenderer = new SimpleWfsRenderer(this, viewPort, container, eventBus);
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

	public List<AttributeDescriptor> getSchema() {
		return wfsFeatureTypeDescriptionInfo.getAttributeDescriptors();
	}

	public WfsVersion getVersion() {
		return version;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setWfsFeatureTypeInfo(WfsFeatureTypeInfo wfsFeatureTypeInfo) {
		this.wfsFeatureTypeInfo = wfsFeatureTypeInfo;
	}

	@Override
	public SimpleWfsRenderer getRenderer() {
		return layerRenderer;
	}

	@Override
	public void setOpacity(double opacity) {

	}

	@Override
	public double getOpacity() {
		return 1.0;
	}

	@Override
	public double getMaxResolution() {
		return Double.MAX_VALUE;
	}

	@Override
	public double getMinResolution() {
		return Double.MIN_VALUE;
	}

	@Override
	public List<AttributeDescriptor> getAttributeDescriptors() {
		return wfsFeatureTypeDescriptionInfo.getAttributeDescriptors();
	}

	@Override
	public boolean isFeatureSelected(String featureId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean selectFeature(Feature feature) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deselectFeature(Feature feature) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clearSelectedFeatures() {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<Feature> getSelectedFeatures() {
		// TODO Auto-generated method stub
		return null;
	}

}
