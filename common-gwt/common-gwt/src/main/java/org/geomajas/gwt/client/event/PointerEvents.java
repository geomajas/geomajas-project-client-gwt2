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

import org.geomajas.annotation.Api;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Entrypoint class for pointer event support.
 * 
 * @author Jan De Moerloose
 *
 * @since 2.4.0
 */
@Api
public class PointerEvents implements EntryPoint {

	static final PointerEvents IMPL = GWT.create(PointerEvents.class);

	public PointerEvents() {
	}

	/**
	 * Initialize pointer support. Called automatically from entrypoint.
	 */
	public static void init() {
		IMPL.doInit();
	}

	/**
	 * Are pointer events supported (currently only for IE 10+) ?
	 * @return true if supported
	 * @since 2.4.0
	 */
	@Api
	public static boolean isSupported() {
		return IMPL.supports();
	}

	protected boolean supports() {
		return false;
	}

	protected void doInit() {
	}

	public String getNativeTypeName(PointerEventType pointerEventType) {
		return "";
	}

	@Override
	public void onModuleLoad() {
		init();
	}

}
