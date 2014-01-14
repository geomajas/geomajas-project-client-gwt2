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
package org.geomajas.gwt2.tools.client;

import com.google.gwt.core.client.Callback;
import org.geomajas.annotation.Api;

/**
 * Abstract modal action class.
 *
 * @author Oliver May
 * @param <ST> The type returned on selection success
 * @param <SF> The type returned on selection failure
 * @param <DT> The type returned on deselection success
 * @param <DF> The type returned on deselection failure
 * @since 2.0.0
 */
@Api(allMethods = true)
public abstract class ModalAction<ST, SF, DT, DF> extends BaseAction {

	/**
	 * Perform the selection action.
	 *
	 * @param callback failure and success callback.
	 */
	public abstract void actionSelected(Callback<ST, SF> callback);

	/**
	 * Perform the deselection action.
	 *
	 * @param callback failure and success callback.
	 */
	public abstract void actionDeselected(Callback<DT, DF> callback);

}
