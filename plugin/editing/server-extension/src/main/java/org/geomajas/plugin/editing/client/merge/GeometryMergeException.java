/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.editing.client.merge;

import org.geomajas.annotation.Api;

/**
 * Exception that is thrown when something went wrong while merging geometries.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public class GeometryMergeException extends Exception {

	private static final long serialVersionUID = 100L;

	/** Default constructor. */
	public GeometryMergeException() {
		super();
	}

	/**
	 * Create an exception using the given message.
	 * 
	 * @param message
	 *            The error message.
	 */
	public GeometryMergeException(String message) {
		super(message);
	}

	/**
	 * Create an exception.
	 * 
	 * @param message
	 *            The error message.
	 * @param cause
	 *            Original exception.
	 */
	public GeometryMergeException(String message, Throwable cause) {
		super(message, cause);
	}
}