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

package org.geomajas.gwt2.tools.client.event;

import com.google.gwt.event.shared.GwtEvent;
import org.geomajas.annotation.Api;
import org.geomajas.gwt2.tools.client.BaseAction;

/**
 * <p>
 * Event that signals the disabling of a single {@link BaseAction}.
 * </p>
 * 
 * @author Pieter De Graef
 * @author
 * @since Oliver May
 */
@Api(allMethods = true)
public class ActionDisabledEvent extends GwtEvent<ActionHandler> {

	@Override
	public Type<ActionHandler> getAssociatedType() {
		return ActionHandler.TYPE;
	}

	@Override
	protected void dispatch(ActionHandler actionHandler) {
		actionHandler.onActionDisabled(this);
	}

	/**
	 * Get the {@link BaseAction} that is disabled.
	 *
	 * @return action which is disabled
	 */
	public BaseAction getAction() {
		return (BaseAction) getSource();
	}
}
