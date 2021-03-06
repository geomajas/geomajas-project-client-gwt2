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
package org.geomajas.gwt2.plugin.wfs.server.command.factory.impl;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import org.geomajas.gwt2.plugin.wfs.server.command.factory.WfsDataStoreFactory;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.data.wfs.internal.WFSConfig;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.ows.ServiceException;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequenceFactory;

/**
 * Replaces geotools {@link WfsDataStoreFactory} to inject custom {@link HTTPClient}.
 * 
 * @author Jan De Moerloose
 *
 */
public class DefaultWfsDataStoreFactory implements WfsDataStoreFactory {

	@Override
	public WFSDataStore createDataStore(Map<String, Serializable> params, HTTPClient http) throws IOException {
		final WFSConfig config = WFSConfig.fromParams(params);
		String user = config.getUser();
		String password = config.getPassword();
		if (((user == null) && (password != null)) || ((config.getPassword() == null) && (config.getUser() != null))) {
			throw new IOException("Cannot define only one of USERNAME or PASSWORD, must define both or neither");
		}
		final URL capabilitiesURL = (URL) WFSDataStoreFactory.URL.lookUp(params);
		http.setUser(config.getUser());
		http.setPassword(config.getPassword());
		int timeoutMillis = config.getTimeoutMillis();
		http.setConnectTimeout(timeoutMillis / 1000);

		// WFSClient performs version negotiation and selects the correct strategy
		WFSClient wfsClient;
		try {
			wfsClient = new WFSClient(capabilitiesURL, http, new WfsConfigAxisOrder(config));
		} catch (ServiceException e) {
			throw new IOException(e);
		}
		WFSDataStore dataStore = new WFSDataStore(wfsClient);
		// factories
		dataStore.setFilterFactory(CommonFactoryFinder.getFilterFactory(null));
		dataStore.setGeometryFactory(new GeometryFactory(PackedCoordinateSequenceFactory.DOUBLE_FACTORY));
		dataStore.setFeatureTypeFactory(new FeatureTypeFactoryImpl());
		dataStore.setFeatureFactory(CommonFactoryFinder.getFeatureFactory(null));
		dataStore.setNamespaceURI(config.getNamespaceOverride());

		return dataStore;
	}
	
	/**
	 * Trying to force x-y order...
	 * 
	 * @author Jan De Moerloose
	 *
	 */
	class WfsConfigAxisOrder extends WFSConfig {
		
		private WFSConfig wfsConfig;

		public WfsConfigAxisOrder(WFSConfig wfsConfig) {
			this.wfsConfig = wfsConfig;
		}

		public int hashCode() {
			return wfsConfig.hashCode();
		}

		public boolean equals(Object obj) {
			return wfsConfig.equals(obj);
		}

		public String getUser() {
			return wfsConfig.getUser();
		}

		public String getPassword() {
			return wfsConfig.getPassword();
		}

		public int getTimeoutMillis() {
			return wfsConfig.getTimeoutMillis();
		}

		public PreferredHttpMethod getPreferredMethod() {
			return wfsConfig.getPreferredMethod();
		}

		public int getBuffer() {
			return wfsConfig.getBuffer();
		}

		public boolean isTryGZIP() {
			return wfsConfig.isTryGZIP();
		}

		public boolean isLenient() {
			return wfsConfig.isLenient();
		}

		public Integer getMaxFeatures() {
			return wfsConfig.getMaxFeatures();
		}

		public Charset getDefaultEncoding() {
			return wfsConfig.getDefaultEncoding();
		}

		public String getWfsStrategy() {
			return wfsConfig.getWfsStrategy();
		}

		public Integer getFilterCompliance() {
			return wfsConfig.getFilterCompliance();
		}

		public String getNamespaceOverride() {
			return wfsConfig.getNamespaceOverride();
		}

		public String getOutputformatOverride() {
			return wfsConfig.getOutputformatOverride();
		}

		public boolean isUseDefaultSrs() {
			return wfsConfig.isUseDefaultSrs();
		}

		public String getAxisOrder() {
			return WFSDataStoreFactory.AXIS_ORDER_EAST_NORTH;
		}

		public String getAxisOrderFilter() {
			return wfsConfig.getAxisOrderFilter();
		}

		public String toString() {
			return wfsConfig.toString();
		}
		
	}

}
