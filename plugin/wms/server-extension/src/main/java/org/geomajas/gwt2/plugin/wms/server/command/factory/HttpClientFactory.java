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
package org.geomajas.gwt2.plugin.wms.server.command.factory;

import java.net.URL;

import org.apache.http.client.HttpClient;


/**
 * Factory for {@link HttpClient}.
 * 
 * @author Jan De Moerloose
 *
 */
public interface HttpClientFactory {

	/**
	 * Get a client for this url.
	 * 
	 * @param url
	 * @return
	 */
	HttpClient createClientForUrl(URL url);
}
