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

import javax.servlet.http.HttpServletResponse;

import org.geomajas.gwt2.plugin.print.PrintException;
import org.geomajas.gwt2.plugin.print.component.service.PrintConfigurationService;
import org.geomajas.gwt2.plugin.print.document.Document.Format;
import org.geomajas.gwt2.plugin.print.service.PrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Spring MVC controller that maps the pdf request to a document. The document should be available in the print service.
 * The view is referenced by name.
 * 
 * @author Jan De Moerloose
 * 
 */
@Controller("/print/**")
public class PrintController {

	public static final String DOCUMENT_VIEW_NAME = "plugin.print.mvc.DocumentView";

	//public static final String IMAGE_VIEW_NAME = "plugin.print.mvc.ImageView";

	public static final String DOCUMENT_KEY = "document";

	public static final String DOWNLOAD_KEY = "download";

	public static final String FILENAME_KEY = "name";

	public static final String FORMAT_KEY = "format";
	
	//public static final String IMAGE_KEY = "image";

	public static final String DOWNLOAD_METHOD_BROWSER = "0";

	public static final String DOWNLOAD_METHOD_SAVE = "1";

	@Autowired
	protected PrintService printService;

	@Autowired
	protected PrintConfigurationService configurationService;

	@RequestMapping(value = "/print", method = RequestMethod.GET)
	public ModelAndView printDocument(@RequestParam("documentId") String documentId,
			@RequestParam(value = "download", defaultValue = DOWNLOAD_METHOD_SAVE) String download,
			@RequestParam(value = "name", defaultValue = "geomajas.pdf") String fileName)
			throws PrintException {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(DOCUMENT_VIEW_NAME);
		mav.addObject(DOCUMENT_KEY, printService.removeDocument(documentId));
		mav.addObject(DOWNLOAD_KEY, download);
		mav.addObject(FILENAME_KEY, fileName);
		mav.addObject(FORMAT_KEY, Format.decode(fileName));
		return mav;
	}
	
	@ExceptionHandler
	public ModelAndView exception(PrintException exception, HttpServletResponse response) throws Exception {
		response.setContentType("text/html");
		switch (exception.getExceptionCode()) {
			case PrintException.DOCUMENT_NOT_FOUND:
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				break;
			default:
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		response.getWriter().println(exception.getLocalizedMessage());
		return new ModelAndView();
	}


}
