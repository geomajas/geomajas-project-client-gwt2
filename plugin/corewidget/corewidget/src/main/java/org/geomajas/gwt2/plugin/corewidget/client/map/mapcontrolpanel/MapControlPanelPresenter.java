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

package org.geomajas.gwt2.plugin.corewidget.client.map.mapcontrolpanel;

import org.geomajas.annotation.Api;

/**
 * MVP presenter for {@link org.geomajas.gwt2.plugin.corewidget.client.map.layercontrolpanel.LayerControlPanel}.
 *
 * @author Dosi Bingov
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface MapControlPanelPresenter {

	/**
	 * Should layer visibility toggle control stay enabled when its layer is out of range. Default when the layer is out
	 * of range visibility toggle control is disabled.
	 *
	 * @param disable when false visibility toggle control (e.g. chekcbox) stays enabled even if the layer is out
	 *                   of range.
	 */
	void setDisableToggleOutOfRange(boolean disable);
}
