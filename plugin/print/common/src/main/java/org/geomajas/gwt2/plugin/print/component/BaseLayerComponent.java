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
package org.geomajas.gwt2.plugin.print.component;

import org.geomajas.gwt2.plugin.print.component.dto.PrintComponentInfo;

/**
 * Base layer component for printing.
 * 
 * @param <T> DTO object class
 *
 * @author Jan De Moerloose
 */
public interface BaseLayerComponent<T extends PrintComponentInfo> extends PrintComponent<T> {

	/** @todo javadoc incomplete. */
	boolean isVisible();

	/** @todo javadoc incomplete. */
	boolean isSelected();

	/** @todo javadoc incomplete. */
	String getLayerId();

}