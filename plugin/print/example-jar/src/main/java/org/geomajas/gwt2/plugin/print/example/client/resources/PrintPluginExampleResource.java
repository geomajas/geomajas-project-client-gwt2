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
package org.geomajas.gwt2.plugin.print.example.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Client resource bundle interface for pure GWT2 print plugin.
 *
 * @author Jan Venstermans
 */
public interface PrintPluginExampleResource extends ClientBundle {

	/**
	 * Instance for use outside UIBinder.
	 */
	PrintPluginExampleResource INSTANCE = GWT.create(PrintPluginExampleResource.class);

	/**
	 * Get the css resource.
	 *
	 * @return the css resource
	 */
	@Source("print-widget-example.css")
	PrintPluginExampleCssResource css();

	/**
	 * Get the css resource.
	 *
	 * @return Image resource og geomajas logo.
	 */
	@Source("images/print.png")
	ImageResource print();
}