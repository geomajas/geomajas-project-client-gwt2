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

package org.geomajas.plugin.editing.client.operation;

import com.google.gwt.core.client.GWT;
import org.geomajas.plugin.editing.client.i18n.EditingCommonMessages;

/**
 * Messages fed to Exception that are thrown in one of the GeometryOperations.
 * 
 * @author Jan Venstermans
 */
public final class EditingCommonCustomMessages {

	private static EditingCommonCustomMessages customMessages;

	private static EditingCommonMessages messages = GWT.create(EditingCommonMessages.class);

	private static String polygonLinesCannotIntersectMessage = messages.intersectionException();

	private EditingCommonCustomMessages() {
		super();
	}

	public static EditingCommonCustomMessages getInstance() {
		 if (customMessages == null) {
			 customMessages = new EditingCommonCustomMessages();

		 }
		return customMessages;
	}

	public String getPolygonLinesCannotIntersectMessage() {
		return polygonLinesCannotIntersectMessage;
	}

	public void setPolygonLinesCannotIntersectMessage(String polygonLinesCannotIntersectMessage) {
		this.polygonLinesCannotIntersectMessage = polygonLinesCannotIntersectMessage;
	}
}