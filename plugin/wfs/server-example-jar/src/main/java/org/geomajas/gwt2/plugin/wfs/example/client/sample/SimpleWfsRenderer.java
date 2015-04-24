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

import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.event.LayerAddedEvent;
import org.geomajas.gwt2.client.event.LayerRemovedEvent;
import org.geomajas.gwt2.client.event.MapCompositionHandler;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.feature.query.Criterion;
import org.geomajas.gwt2.client.map.feature.query.Query;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.render.LayerRenderer;
import org.geomajas.gwt2.client.map.render.RenderingInfo;
import org.geomajas.gwt2.plugin.wfs.client.WfsServerExtension;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureCollectionInfo;
import org.geomajas.gwt2.plugin.wfs.client.service.WfsService;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.VectorObjectContainer;

import com.google.gwt.core.client.Callback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

/**
 * Renderer for {@link SimpleWfsLayer}.
 * 
 * @author Jan De Moerloose
 *
 */
public class SimpleWfsRenderer implements LayerRenderer {

	private SimpleWfsLayer layer;

	private ViewPort viewPort;

	private VectorObjectContainer container;

	private boolean featuresLoaded;

	private int maxFeatures = 500;

	private int maxCoordinates = 500;

	private MapEventBus eventBus;

	public SimpleWfsRenderer(final SimpleWfsLayer layer, ViewPort viewPort, VectorObjectContainer container,
			MapEventBus eventBus) {
		this.layer = layer;
		this.viewPort = viewPort;
		this.container = container;
		this.eventBus = eventBus;
		eventBus.addMapCompositionHandler(new MapCompositionHandler() {

			@Override
			public void onLayerRemoved(LayerRemovedEvent event) {
				if (event.getLayer() == layer) {
					clear();
				}
			}

			@Override
			public void onLayerAdded(LayerAddedEvent event) {
			}
		});

	}

	@Override
	public void render(RenderingInfo renderingInfo) {
		if (!featuresLoaded) {
			featuresLoaded = true;
			WfsService wfsService = WfsServerExtension.getInstance().getWfsService();
			Criterion criterion = wfsService.buildCriterion().include().build();
			Query query = wfsService.buildQuery().criterion(criterion).maxFeatures(maxFeatures)
					.maxCoordinates(maxCoordinates).crs(viewPort.getCrs()).attributeDescriptors(layer.getSchema())
					.build();
			wfsService.getFeatures(layer.getVersion(), layer, layer.getBaseUrl(), layer.getTypeName(), query,
					new Callback<WfsFeatureCollectionInfo, String>() {

						@Override
						public void onSuccess(WfsFeatureCollectionInfo result) {
							for (final Feature feature : result.getFeatures()) {
								VectorObject shape = GeomajasImpl.getInstance().getGfxUtil()
										.toShape(feature.getGeometry());
								if (shape != null) {
									container.add(shape);
									shape.addClickHandler(new ClickHandler() {

										@Override
										public void onClick(ClickEvent event) {
											eventBus.fireEvent(new ShowFeatureEvent(feature));
										}
									});
									new Highlighter(shape);
								}
							}

						}

						@Override
						public void onFailure(String reason) {
							// TODO Auto-generated method stub

						}
					});
		}
	}

	private void clear() {
		container.clear();
	}

	public int getMaxFeatures() {
		return maxFeatures;
	}

	public void setMaxFeatures(int maxFeatures) {
		this.maxFeatures = maxFeatures;
	}

	public int getMaxCoordinates() {
		return maxCoordinates;
	}

	public void setMaxCoordinates(int maxCoordinates) {
		this.maxCoordinates = maxCoordinates;
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

	/**
	 * Highlights feature on mouse over.
	 * 
	 * @author Jan De Moerloose
	 *
	 */
	class Highlighter implements MouseOverHandler, MouseOutHandler {

		private VectorObject shape;

		public Highlighter(VectorObject shape) {
			this.shape = shape;
			shape.addMouseOverHandler(this);
			shape.addMouseOutHandler(this);
		}

		@Override
		public void onMouseOut(MouseOutEvent event) {
			if (shape instanceof Shape) {
				((Shape) shape).setFillColor("white");
				((Shape) shape).setStrokeColor("black");
				((Shape) shape).setStrokeWidth(1);
			}
		}

		@Override
		public void onMouseOver(MouseOverEvent event) {
			if (shape instanceof Shape) {
				((Shape) shape).setFillColor("yellow");
				((Shape) shape).setStrokeColor("yellow");
				((Shape) shape).setStrokeWidth(3);
			}
		}

	}

}
