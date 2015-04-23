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

package org.geomajas.gwt2.plugin.wms.client.layer;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.event.LayerStyleChangedEvent;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.layer.LegendConfig;
import org.geomajas.gwt2.plugin.wms.client.service.WmsService;

import java.io.Serializable;

/**
 * General WMS configuration object. The values herein will be translated into parameters for the WMS service. Note that
 * this configuration object has fields that are not directly supported through WMS. Some WMS vendors have added such
 * extra options though, so be sure to specify the {@link WmsServiceVendor} if possible.
 *
 * @author Pieter De Graef
 * @author An Buyle
 * @since 2.1.0
 */
@Api(allMethods = true)
public class WmsLayerConfiguration implements Serializable {

	private static final long serialVersionUID = 100L;

	private String baseUrl;

	private String format = "image/png";

	private String layers = "";

	private String styles = "";

	private String filter; // CQL in case the WMS server supports it.

	private boolean transparent = true;

	private WmsService.WmsVersion version = WmsService.WmsVersion.V1_3_0;

	private LegendConfig legendConfig = new LegendConfig();

	private WmsServiceVendor wmsServiceVendor = WmsServiceVendor.UNSPECIFIED;

	private double minimumResolution;

	private double maximumResolution;

	private Boolean useInvertedAxis;

	private MapEventBus eventBus;

	private WmsLayer parentLayer;

	private String crs;

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	/**
	 * Get the type of service that provides the WMS. This can be a specific brand, such as GeoServer.
	 *
	 * @return Type of service that provides the WMS service.
	 */
	public WmsServiceVendor getWmsServiceVendor() {
		return wmsServiceVendor;
	}

	/**
	 * Set the WMS service type to used. This may trigger vendor specific options to be used. E.g. If the WMS service is
	 * provided by a Geoserver, there is the possibility to configure the legend_options when performing a
	 * GetLegendGraphic request.
	 *
	 * @param wmsServiceVendor
	 */
	public void setWmsServiceVendor(WmsServiceVendor wmsServiceVendor) {
		this.wmsServiceVendor = wmsServiceVendor;
	}

	/**
	 * Get the GetMap image format. The default value is "image/png".
	 *
	 * @return The GetMap image format.
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Set the GetMap image format. The default value is "image/png".
	 *
	 * @param format The GetMap image format.
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * Get the layers parameter used in the GetMap requests.
	 *
	 * @return The GetMap layers parameter.
	 */
	public String getLayers() {
		return layers;
	}

	/**
	 * Set the layers parameter used in the GetMap requests.
	 *
	 * @param layers The GetMap layers parameter.
	 */
	public void setLayers(String layers) {
		this.layers = layers;
	}

	/**
	 * Get the styles parameter to be used in the GetMap requests.
	 *
	 * @return The styles parameter to be used in the GetMap requests.
	 */
	public String getStyles() {
		return styles;
	}

	/**
	 * Set the styles parameter to be used in the GetMap requests.
	 *
	 * @param styles The styles parameter to be used in the GetMap requests.
	 */
	public void setStyles(String styles) {
		this.styles = styles;
		if (eventBus != null && parentLayer != null) {
			eventBus.fireEvent(new LayerStyleChangedEvent(parentLayer));
		}
	}

	/**
	 * Get the filter parameter used in GetMap requests. Note this parameter is not a default WMS parameter, and not all
	 * WMS servers may support this.
	 *
	 * @return The GetMap filter parameter.
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * Set the filter parameter used in GetMap requests. Note this parameter is not a default WMS parameter, and not all
	 * WMS servers may support this.
	 *
	 * @param filter The GetMap filter parameter.
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * Get the GetMap transparent parameter. Default value is 'true'.
	 *
	 * @return The GetMap transparent parameter.
	 */
	public boolean isTransparent() {
		return transparent;
	}

	/**
	 * Set the transparent parameter used in the GetMap requests. Default value is 'true'.
	 *
	 * @param transparent The GetMap transparent parameter.
	 */
	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

	/**
	 * Get the WMS version used. Default value is '1.3.0'.
	 *
	 * @return The WMS version.
	 */
	public WmsService.WmsVersion getVersion() {
		return version;
	}

	/**
	 * Set the WMS version. Default value is '1.3.0'.
	 *
	 * @param version The WMS version.
	 */
	public void setVersion(WmsService.WmsVersion version) {
		this.version = version;
	}

	/**
	 * Get the base URL to the WMS service. This URL should not contain any WMS parameters.
	 *
	 * @return The base URL to the WMS service.
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Set the base URL to the WMS service. This URL should not contain any WMS parameters.
	 *
	 * @param baseUrl The base URL to the WMS service.
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * Get the default legend configuration for this layer. By default WMS does not support any of the options herein,
	 * but some WMS vendors have added extra options to allow for these.
	 *
	 * @return The default legend creation configuration for this layer.
	 */
	public LegendConfig getLegendConfig() {
		return legendConfig;
	}

	/**
	 * Set the default legend configuration for this layer. By default WMS does not support any of the options herein,
	 * but some WMS vendors have added extra options to allow for these.
	 *
	 * @param legendConfig The default legend creation configuration for this layer.
	 */
	public void setLegendConfig(LegendConfig legendConfig) {
		this.legendConfig = legendConfig;
	}

	/**
	 * Get the minimum resolution for which this layer should be visible (maximum zoom in).
	 *
	 * @return the minimum resolution
	 */
	public double getMinimumResolution() {
		return minimumResolution;
	}

	/**
	 * Set the minimum resolution for which this layer should be visible (maximum zoom in).
	 *
	 * @param minimumResolution the minimum resolution
	 */
	public void setMinimumResolution(double minimumResolution) {
		this.minimumResolution = minimumResolution;
	}

	/**
	 * Get the maximum resolution for which this layer should be visible (maximum zoom out).
	 *
	 * @return the maximum resolution
	 */
	public double getMaximumResolution() {
		return maximumResolution;
	}

	/**
	 * Set the maximum resolution for which this layer should be visible (maximum zoom out).
	 *
	 * @param maximumResolution the maximum resolution
	 */
	public void setMaximumResolution(double maximumResolution) {
		this.maximumResolution = maximumResolution;
	}

	/**
	 * Set the coordinate reference system for this layer. This should be the same as the map you want to add this laye
	 * to.
	 *
	 * @param crs The coordinate reference system for this layer.
	 */
	public void setCrs(String crs) {
		this.crs = crs;
	}

	/**
	 * Get the coordinate reference system for this layer.
	 *
	 * @return The coordinate reference system for this layer.
	 */
	public String getCrs() {
		return crs;
	}

	/**
	 * Should GetMap calls based upon this configuration object use inverted XY axis or not? If you're not sure, don't
	 * use this method. If you leave this untouched, Geomajas will attempt to figure it out for you, which should
	 * usually work just fine.
	 *
	 * @return True or false.
	 */
	public boolean isUseInvertedAxis() {
		if (useInvertedAxis == null) {
			useInvertedAxis = useInvertedAxis();
		}
		return useInvertedAxis;
	}

	/**
	 * Determine whether or not GetMap calls based upon this configuration object should use inverted XY axis or not. If
	 * you're not sure, don't use this method. If you leave this untouched, Geomajas will attempt to figure it out for
	 * you, which should usually work just fine.
	 *
	 * @param useInvertedAxis Yay or nay.
	 */
	public void setUseInvertedAxis(boolean useInvertedAxis) {
		this.useInvertedAxis = useInvertedAxis;
	}

	// ------------------------------------------------------------------------
	// Protected methods:
	// ------------------------------------------------------------------------

	protected void setParentLayer(MapEventBus eventBus, WmsLayer parentLayer) {
		this.eventBus = eventBus;
		this.parentLayer = parentLayer;
	}

	protected boolean useInvertedAxis() {
		if (crs != null && WmsService.WmsVersion.V1_3_0.equals(version) && ("EPSG:4326".equalsIgnoreCase(crs) ||
				"WGS:84".equalsIgnoreCase(crs))) {
			return true;
		}
		return false;
	}
}
