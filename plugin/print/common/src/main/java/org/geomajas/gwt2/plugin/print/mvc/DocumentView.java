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
package org.geomajas.gwt2.plugin.print.mvc;

import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geomajas.gwt2.plugin.print.document.Document;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

/**
 * View to produce printed documents (PDF, PNG, ...).
 *
 * @author Jan De Moerloose
 * @author An Buyle
 */
@Component(PrintController.DOCUMENT_VIEW_NAME)
public class DocumentView extends AbstractView {

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Document doc = (Document) model.get(PrintController.DOCUMENT_KEY);
		String download = (String) model.get(PrintController.DOWNLOAD_KEY);
		String fileName = (String) model.get(PrintController.FILENAME_KEY);
		Document.Format format = (Document.Format) model.get(PrintController.FORMAT_KEY);
				
		// Write content type and also length (determined via byte array).
		response.setContentType(format.getMimetype());
		
		
		// check download method
		if (download.equals(PrintController.DOWNLOAD_METHOD_SAVE)) {
			response.setHeader("Content-Disposition", " attachment; filename=\"" + fileName + "\"");
		} else if (download.equals(PrintController.DOWNLOAD_METHOD_BROWSER)) {
			response.setHeader("Content-Disposition", " inline; filename=\"" + fileName + "\"");
		} else {
			throw new IllegalArgumentException("invalid download method " + download);
		}

		// Write the docmuent
		ServletOutputStream out = response.getOutputStream();
		doc.render(out, format);
		response.setContentLength(doc.getContentLength());
		out.flush();
	}

}
