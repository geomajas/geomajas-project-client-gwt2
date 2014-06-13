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


import com.google.gwt.core.client.Callback;
import org.geomajas.plugin.print.client.event.PrintFinishedInfo;
import org.geomajas.plugin.print.command.dto.PrintTemplateInfo;

/**
 * Presenter for print method.
 * 
 * @author Jan Venstermans
 * 
 */
public interface PrintService {

	/**
	 * Will communicate with server to turn a {@link PrintTemplateInfo} into printed form.
	 *
	 * @param printTemplateInfo
	 * @param callback
	 */
	void print(PrintTemplateInfo printTemplateInfo, final Callback<PrintFinishedInfo, Void> callback);

}
