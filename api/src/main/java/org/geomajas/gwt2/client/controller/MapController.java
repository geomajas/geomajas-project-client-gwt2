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

package org.geomajas.gwt2.client.controller;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.gwt.client.controller.Controller;
import org.geomajas.gwt2.client.map.MapPresenter;

/**
 * <p>
 * General interface for an event controller that catches different types of mouse events on a map. Implementations of
 * this interface must than decide how to react on these events. Since a <code>MapController</code> receives the
 * original mouse events, it can influence their behavior. As such only one <code>MapController</code> can be active at
 * any one time on a {@link MapPresenter}.
 * </p>
 * <p>
 * Note that the {@link MapPresenter} also has a {@link MapPresenter#addMapListener(MapController)} method that allows
 * setting multiple <code>MapController</code>s as passive listeners to mouse events.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@UserImplemented
@Api(allMethods = true)
public interface MapController extends BaseMapController, Controller {

}