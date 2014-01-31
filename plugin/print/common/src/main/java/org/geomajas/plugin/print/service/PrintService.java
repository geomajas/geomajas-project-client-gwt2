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
package org.geomajas.plugin.print.service;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.print.PrintException;
import org.geomajas.plugin.print.configuration.PrintTemplate;
import org.geomajas.plugin.print.document.Document;

/**
 * Service for printing and template handling.
 * 
 * @author Jan De Moerloose
 * @since 2.0.0
 */
@Api(allMethods = true)
public interface PrintService {

	/**
	 * Retrieves all available templates.
	 * 
	 * @return a list of templates ordered by name
	 * @throws PrintException
	 *             if the templates could not be fetched from db
	 */
	List<PrintTemplate> getAllTemplates() throws PrintException;

	/**
	 * Saves a new template or updates an existing one.
	 * 
	 * @param template
	 *            the template
	 * @throws PrintException
	 *             if persistence failed
	 */
	void saveOrUpdateTemplate(PrintTemplate template) throws PrintException;

	/**
	 * Creates a new default template.
	 * 
	 * @param pageSize
	 *            the size of the page (A1,A2,A3,A4,..)
	 * @param landscape
	 *            true for landscape orientation
	 * @return the template
	 * @throws PrintException no template found
	 */
	PrintTemplate createDefaultTemplate(String pageSize, boolean landscape) throws PrintException;

	/**
	 * Puts a new document in the service.
	 * 
	 * @param document document
	 * @return key unique key to reference the document
	 */
	String putDocument(Document document);

	/**
	 * Removes a document from the service.
	 * 
	 * @param key
	 *            unique key to reference the document
	 * @return the document or null if no such document
	 * 
	 * @throws PrintException
	 *             if document does not exist
	 */
	Document removeDocument(String key) throws PrintException;
}
