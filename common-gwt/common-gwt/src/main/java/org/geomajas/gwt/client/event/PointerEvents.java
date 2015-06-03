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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Entrypoint class for pointer event support.
 * 
 * @author Jan De Moerloose
 *
 */
public class PointerEvents implements EntryPoint {

	static final PointerEvents IMPL = GWT.create(PointerEvents.class);

	public PointerEvents() {
	}

	public static void init() {
		IMPL.doInit();
	}

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
