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

package org.geomajas.plugin.wms.example.client.sample.v1_3_0;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.*;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.gfx.VectorContainer;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapConfiguration.CrsType;
import org.geomajas.gwt2.client.map.MapConfigurationImpl;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.widget.MapLayoutPanel;
import org.geomajas.gwt2.example.base.client.ExampleBase;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.plugin.wms.client.controller.WmsGetFeatureInfoController;
import org.geomajas.plugin.wms.client.layer.FeaturesSupportedWmsLayer;
import org.geomajas.plugin.wms.client.layer.FeaturesSupportedWmsLayerImpl;
import org.geomajas.plugin.wms.client.layer.config.WmsLayerConfiguration;
import org.geomajas.plugin.wms.client.layer.config.WmsTileConfiguration;
import org.geomajas.plugin.wms.client.service.FeatureCollection;
import org.geomajas.plugin.wms.client.service.WmsService.GetFeatureInfoFormat;
import org.geomajas.plugin.wms.client.service.WmsService.WmsVersion;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * ContentPanel that demonstrates rendering abilities in world space with a map that supports resizing.
 *
 * @author Pieter De Graef
 */
public class WmsFeatureInfoV130Panel implements SamplePanel {

    /**
     * UI binder for this widget.
     *
     * @author Pieter De Graef
     */
    interface MyUiBinder extends UiBinder<Widget, WmsFeatureInfoV130Panel> {
    }

    private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

    private static final String WMS_BASE_URL = "http://apps.geomajas.org/geoserver/demo_world/ows";

    private static final String EPSG = "EPSG:4326";

    @UiField
    protected ListBox formatBox;

    @UiField
    protected ResizeLayoutPanel mapPanel;

    @UiField
    protected SimplePanel featureInfoParent;

    private VectorContainer featureContainer;

    public Widget asWidget() {
        Widget layout = UI_BINDER.createAndBindUi(this);

        // Create the mapPresenter and add an InitializationHandler:
        MapConfiguration configuration = new MapConfigurationImpl();
        configuration.setCrs(EPSG, CrsType.DEGREES);
        configuration.setMaxBounds(new Bbox(-180, -90, 360, 180));
        configuration.setMapHintValue(MapConfiguration.INITIAL_BOUNDS, ExampleBase.BBOX_LATLON_USA);
        configuration.setMaximumScale(8192);
        MapPresenter mapPresenter = GeomajasImpl.getInstance().createMapPresenter(configuration, 480, 480);

        // Now create a WMS layer and add it to the map:
        WmsTileConfiguration tileConfig = new WmsTileConfiguration(256, 256, new Coordinate(-180, -90));
        WmsLayerConfiguration layerConfig = new WmsLayerConfiguration();
        layerConfig.setBaseUrl(WMS_BASE_URL);
        layerConfig.setFormat("image/jpeg");
        layerConfig.setVersion(WmsVersion.V1_3_0);
        layerConfig.setLayers("simplified_country_borders");
        layerConfig.setMaximumScale(8192);
        layerConfig.setMinimumScale(0);
        FeaturesSupportedWmsLayer wmsLayer = new FeaturesSupportedWmsLayerImpl("Countries", layerConfig, tileConfig);
        mapPresenter.getLayersModel().addLayer(wmsLayer);

        // Define the whole layout:
        MapLayoutPanel mapLayoutPanel = new MapLayoutPanel();
        mapLayoutPanel.setPresenter(mapPresenter);
        mapPanel.setWidget(mapLayoutPanel);

        final WmsGetFeatureInfoController controller = new WmsGetFeatureInfoController();
        controller.addLayer(wmsLayer);
        controller.setFormat(getRequestFormat());
        controller.setHtmlCallback(new Callback<Object, String>() {

            @Override
            public void onSuccess(Object result) {
                featureContainer.clear();
                HTML html = new HTML((String) result);
                featureInfoParent.setWidget(html);
            }

            @Override
            public void onFailure(String reason) {
                featureContainer.clear();
                Window.alert("Something went wrong executing the WMS GetFeatureInfo request: " + reason);
            }
        });
        controller.setGmlCallback(new Callback<FeatureCollection, String>() {

            @Override
            public void onFailure(String reason) {
                featureContainer.clear();
                Window.alert("Something went wrong executing the WMS GetFeatureInfo request: " + reason);
            }

            @Override
            public void onSuccess(FeatureCollection result) {
                featureContainer.clear();
                VerticalPanel panel = new VerticalPanel();
                for (Feature feature : result.getFeatures()) {
                    if (feature.getGeometry() != null) {
                        VectorObject shape = GeomajasImpl.getInstance().getGfxUtil().toShape(feature.getGeometry());
                        GeomajasImpl.getInstance().getGfxUtil().applyFill(shape, "#CC0000", 0.7);
                        featureContainer.add(shape);
                    }
                }
                HTML html = new HTML("The features are drawn onto the map...");
                featureInfoParent.setWidget(html);
            }
        });
        mapPresenter.addMapListener(controller);

        featureContainer = mapPresenter.getContainerManager().addWorldContainer();
        formatBox.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                controller.setFormat(getRequestFormat());
            }
        });

        return layout;
    }

    private GetFeatureInfoFormat getRequestFormat() {
        if (formatBox.getSelectedIndex() == 0) {
            return GetFeatureInfoFormat.HTML;
        } else if (formatBox.getSelectedIndex() == 1) {
            return GetFeatureInfoFormat.TEXT;
        } else if (formatBox.getSelectedIndex() == 2) {
            return GetFeatureInfoFormat.JSON;
        } else if (formatBox.getSelectedIndex() == 3) {
            return GetFeatureInfoFormat.GML2;
        } else if (formatBox.getSelectedIndex() == 4) {
            return GetFeatureInfoFormat.GML3;
        }
        return GetFeatureInfoFormat.HTML;
    }
}