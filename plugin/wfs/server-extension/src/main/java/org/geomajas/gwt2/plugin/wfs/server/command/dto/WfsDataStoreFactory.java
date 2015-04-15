package org.geomajas.gwt2.plugin.wfs.server.command.dto;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Map;

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

public class WfsDataStoreFactory {

	public WFSDataStore createDataStore(Map<String, Serializable> params, HTTPClient http) throws IOException {

		final WFSConfig config = WFSConfig.fromParams(params);

		{
			String user = config.getUser();
			String password = config.getPassword();
			if (((user == null) && (password != null))
					|| ((config.getPassword() == null) && (config.getUser() != null))) {
				throw new IOException("Cannot define only one of USERNAME or PASSWORD, must define both or neither");
			}
		}
		final URL capabilitiesURL = (URL) WFSDataStoreFactory.URL.lookUp(params);
		http.setUser(config.getUser());
		http.setPassword(config.getPassword());
		int timeoutMillis = config.getTimeoutMillis();
		http.setConnectTimeout(timeoutMillis / 1000);

		// WFSClient performs version negotiation and selects the correct strategy
		WFSClient wfsClient;
		try {
			wfsClient = new WFSClient(capabilitiesURL, http, config);
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

}
