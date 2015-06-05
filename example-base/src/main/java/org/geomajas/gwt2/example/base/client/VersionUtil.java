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

package org.geomajas.gwt2.example.base.client;

import com.google.gwt.core.client.GWT;
import org.geomajas.gwt2.example.base.client.resource.VersionMessages;

/**
 * Get the version of current project.
 * Inspired by {@link org.geomajas.gwt.client.Geomajas#getVersion()}
 *
 * @author Jan Venstermans
 */
public final class VersionUtil {

	private static final VersionMessages MESSAGES = GWT.create(VersionMessages.class);

	private VersionUtil() {
	}

	/**
	 * Returns the current version of Geomajas as a string.
	 *
	 * @return Geomajas version
	 */
	public static String getVersion() {
		return MESSAGES.version();
	}

}