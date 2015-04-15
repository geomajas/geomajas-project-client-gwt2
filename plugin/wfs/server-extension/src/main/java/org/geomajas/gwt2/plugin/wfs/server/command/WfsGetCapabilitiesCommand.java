package org.geomajas.gwt2.plugin.wfs.server.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStoreFinder;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSDataStoreFactory;

public class WfsGetCapabilitiesCommand {

	public String[] getCapabilities(String baseUrl) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl);
		if (baseUrl.contains("?")) {
			sb.append("&").append("service=wfs&request=GetCapabilities&VERSION=1.0.0");
		} else {
			sb.append("?").append("service=wfs&request=GetCapabilities&VERSION=1.0.0");
		}

		Map<Object, Object> connectionParameters = new HashMap<Object, Object>();
		connectionParameters.put(WFSDataStoreFactory.URL.key, sb.toString());
		connectionParameters.put(WFSDataStoreFactory.PROTOCOL.key, Boolean.TRUE); // use POST
		WFSDataStore wfs = (WFSDataStore) DataStoreFinder.getDataStore(connectionParameters);
		return wfs.getTypeNames();
	}
}
