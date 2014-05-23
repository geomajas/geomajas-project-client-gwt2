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

package org.geomajas.plugin.jsapi.gwt2.client.exporter.event;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.event.FeatureDeselectedEvent;
import org.geomajas.gwt2.client.event.FeatureSelectedEvent;
import org.geomajas.gwt2.client.event.FeatureSelectionHandler;
import org.geomajas.gwt2.client.event.LayerAddedEvent;
import org.geomajas.gwt2.client.event.LayerRemovedEvent;
import org.geomajas.gwt2.client.event.MapCompositionHandler;
import org.geomajas.plugin.jsapi.client.event.FeatureDeselectedHandler;
import org.geomajas.plugin.jsapi.client.event.FeatureSelectedHandler;
import org.geomajas.plugin.jsapi.client.event.JsEventBus;
import org.geomajas.plugin.jsapi.client.event.JsHandlerRegistration;
import org.geomajas.plugin.jsapi.client.event.LayersModelChangedEvent;
import org.geomajas.plugin.jsapi.client.event.LayersModelChangedHandler;
import org.geomajas.plugin.jsapi.client.map.Map;
import org.geomajas.plugin.jsapi.client.map.feature.Feature;
import org.geomajas.plugin.jsapi.client.map.layer.FeaturesSupported;
import org.geomajas.plugin.jsapi.gwt2.client.exporter.map.MapImpl;
import org.geomajas.plugin.jsapi.gwt2.client.exporter.map.feature.FeatureImpl;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Central event bus for handler registration and event firing.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export("JsEventBus")
@ExportPackage("org.geomajas.jsapi.event")
@Api(allMethods = true)
public class JsEventBusImpl implements Exportable, JsEventBus {

	private Map map;

	/** No-args constructor for GWT. */
	public JsEventBusImpl() {
	}

	/** Construct event bus for specific map. */
	public JsEventBusImpl(Map map) {
		this.map = map;
	}

	/**
	 * Add a handler to change events in the layer configuration from the map. This event is fired for example when the
	 * map gets it's configuration from the server. Only then can it know what layers it has available.
	 */
	public JsHandlerRegistration addLayersModelChangedHandler(final LayersModelChangedHandler handler) {
		HandlerRegistration registration = ((MapImpl) map).getMapPresenter().getEventBus()
				.addMapCompositionHandler(new MapCompositionHandler() {

					@Override
					public void onLayerRemoved(LayerRemovedEvent event) {
						handler.onLayersModelChanged(new LayersModelChangedEvent(map.getLayersModel()));
					}

					@Override
					public void onLayerAdded(LayerAddedEvent event) {
						handler.onLayersModelChanged(new LayersModelChangedEvent(map.getLayersModel()));
					}
				});
		return new JsHandlerRegistration(new HandlerRegistration[] { registration });
	}

	/**
	 * Add a handler for feature selection.
	 */
	public JsHandlerRegistration addFeatureSelectionHandler(final FeatureSelectedHandler selectedHandler,
			final FeatureDeselectedHandler deselectedHandler) {
		HandlerRegistration registration = ((MapImpl) map).getMapPresenter().getEventBus()
				.addFeatureSelectionHandler(new FeatureSelectionHandler() {

					@Override
					public void onFeatureSelected(FeatureSelectedEvent event) {
						Feature feature = new FeatureImpl(event.getFeature(), (FeaturesSupported) map.getLayersModel()
								.getLayer(event.getLayer().getId()));
						selectedHandler
								.onFeatureSelected(new org.geomajas.plugin.jsapi.client.event.FeatureSelectedEvent(
										feature));
					}

					@Override
					public void onFeatureDeselected(FeatureDeselectedEvent event) {
						Feature feature = new FeatureImpl(event.getFeature(), (FeaturesSupported) map.getLayersModel()
								.getLayer(event.getLayer().getId()));
						deselectedHandler
								.onFeatureDeselected(new org.geomajas.plugin.jsapi.client.event.FeatureDeselectedEvent(
										feature));
					}
				});
		return new JsHandlerRegistration(new HandlerRegistration[] { registration });
	}

}