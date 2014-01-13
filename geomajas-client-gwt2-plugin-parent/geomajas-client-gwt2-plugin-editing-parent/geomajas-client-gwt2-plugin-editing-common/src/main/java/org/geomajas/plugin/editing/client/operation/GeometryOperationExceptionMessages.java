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

/**
 * Messages fed to Exception that are thrown in one of the GeometryOperations.
 * 
 * @author Jan Venstermans
 */
public final class GeometryOperationExceptionMessages {

	private static GeometryOperationExceptionMessages messages;

	private static String polygonLinesCannotIntersectMessage
			= "Inserted/edited vertex will result in intersecting edges.";

	private GeometryOperationExceptionMessages() {
		super();
	}

	public static GeometryOperationExceptionMessages getInstance() {
		 if (messages == null) {
			 messages = new GeometryOperationExceptionMessages();
		 }
		return messages;
	}

	public String getPolygonLinesCannotIntersectMessage() {
		return polygonLinesCannotIntersectMessage;
	}

	public void setPolygonLinesCannotIntersectMessage(String polygonLinesCannotIntersectMessage) {
		this.polygonLinesCannotIntersectMessage = polygonLinesCannotIntersectMessage;
	}
}