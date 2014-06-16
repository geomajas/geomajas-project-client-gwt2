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
package org.geomajas.gwt2.widget.client.other.dialog;

import com.google.gwt.core.client.GWT;
import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

/**
 * CloseableDialogBox Resource Factory class.
 *
 * @author David Debuck.
 * @since 2.1.0
 */
@Api(allMethods = true)
@UserImplemented
public class CloseableDialogBoxClientBundleFactory {

	/**
	 * CloseableDialogBoxClientBundleFactory constructor.
	 *
	 * @return CloseableDialogBoxWidgetResource
	 */
	public CloseableDialogBoxWidgetResource createCloseableDialogBoxWidgetResource() {

		return GWT.create(CloseableDialogBoxWidgetResource.class);

	}

}
