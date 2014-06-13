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
package org.geomajas.plugin.print.client;


/**
 * Presenter for the print widgets.
 * 
 * @author An Buyle
 * @author Jan Venstermans
 * 
 */
public interface PrintSettings {

	/**
	 * Enum choice of what to do when print has been created. E.g. open in new window, or save on machine.
	 */
	public enum ActionType {
		SAVE("save"),
		OPEN("open");
		private String typeName;

		private ActionType(String typeName) {
			this.typeName = typeName;
		}

		public String getTypeName() {
			return typeName;
		}
	}

	String URL_DOWNLOAD_YES = "1";

	String URL_DOWNLOAD_NO = "0";

	// String EXTENSION = ".pdf";

	// url parameter keys

	String URL_PATH = "d/print";

	String URL_DOCUMENT_ID = "documentId";

	String URL_NAME = "name";

	String URL_TOKEN = "userToken";

	String URL_DOWNLOAD = "download";

}
