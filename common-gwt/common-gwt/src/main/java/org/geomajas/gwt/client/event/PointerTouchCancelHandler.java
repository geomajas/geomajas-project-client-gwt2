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
package org.geomajas.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler interface for {@link PointerTouchCancelEvent} events.
 * 
 * @author Jan De Moerloose
 */
public interface PointerTouchCancelHandler extends EventHandler {

  /**
   * Called when PointerTouchCancelEvent is fired.
   *
   * @param event the {@link PointerTouchCancelEvent} that was fired
   */
  void onTouchCancel(PointerTouchCancelEvent event);
}