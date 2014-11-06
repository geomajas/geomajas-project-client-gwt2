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
package org.geomajas.gwt2.plugin.print.client.json;

import org.geomajas.plugin.printing.command.dto.PrintTemplateInfo;

import com.github.nmorel.gwtjackson.client.ObjectMapper;

/**
 * GWT jackson mapper for the print template.
 * 
 * @author Jan De Moerloose
 *
 */
public interface JsonPrintTemplateInfoMapper extends ObjectMapper<PrintTemplateInfo> {

}
