package org.geomajas.gwt2.plugin.wfs.server.command.factory;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import org.geotools.data.ows.HTTPClient;
import org.geotools.data.wfs.WFSDataStore;


public interface WfsDataStoreFactory {

	WFSDataStore createDataStore(Map<String, Serializable> params, HTTPClient http) throws IOException;

}
