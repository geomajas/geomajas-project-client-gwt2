/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client;

import org.geomajas.command.CommandResponse;
import org.geomajas.global.Api;
import org.geomajas.puregwt.client.command.Command;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * <p>
 * Interface for the service object that is responsible for the communication between the Geomajas client and server.
 * </p>
 *
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface GeomajasService extends RemoteService {

	/**
	 * Execute a <code>GwtCommandRequest</code>, and return the answer as a <code>CommandResponse</code>.
	 *
	 * @param gwtCommand
	 *            The gwtCommand to be executed.
	 * @return The result.
	 */
	CommandResponse execute(Command gwtCommand);
}