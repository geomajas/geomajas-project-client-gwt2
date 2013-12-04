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

package org.geomajas.plugin.editing.gwt.example.client.handler;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import org.geomajas.plugin.editing.client.handler.AbstractGeometryIndexMapHandler;

/**
 * Example of handler that can be created outside of default.
 * The mousehandlers and other interfaces can be customized.
 * 
 * @author Jan Venstermans
 */
public class GeometryIndexDummyHandler extends AbstractGeometryIndexMapHandler implements MouseOverHandler,
		MouseOutHandler {

	public void onMouseOver(MouseOverEvent event) {
		// custom code can be added
	}

	public void onMouseOut(MouseOutEvent event) {
		// custom code can be added
	}
}