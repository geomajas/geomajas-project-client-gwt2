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

import org.geomajas.gwt2.plugin.print.component.dto.ViewPortComponentInfo;

/**
 * Component representing a view port (small embedded map).
 * 
 * @author Jan De Moerloose
 *
 */
public interface ViewPortComponent extends MapComponent<ViewPortComponentInfo> {

	float getZoomScale();

	float getUserX();

	float getUserY();

}