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
package org.geomajas.plugin.print.client.util;


/**
 * Url parameter keys for the Url buildup.
 * 
 * @author An Buyle
 * @author Jan Venstermans
 * 
 */
public interface PrintUrlParameterKey {

	String URL_PATH = "d/print";

	String URL_DOCUMENT_ID = "documentId";

	String URL_NAME = "name";

	String URL_TOKEN = "userToken";

	String URL_DOWNLOAD = "download";

}
