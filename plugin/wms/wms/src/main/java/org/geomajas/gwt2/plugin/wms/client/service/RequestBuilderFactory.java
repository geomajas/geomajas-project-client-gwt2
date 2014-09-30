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

package org.geomajas.gwt2.plugin.wms.client.service;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;

/**
 * Factory for {@link RequestBuilder}, to allow testing.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface RequestBuilderFactory {

	/**
	 * Create a {@link RequestBuilder} for this method and URL.
	 * 
	 * @param method
	 * @param url
	 * @return
	 */
	RequestBuilder create(Method method, String url);
}
