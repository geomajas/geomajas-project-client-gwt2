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
package org.geomajas.gwt2.plugin.corewidget.client.map.layercontrolpanel;

import com.google.gwt.user.client.ui.IsWidget;
import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.layer.Layer;

/**
 * MVP view for {@link LayerControlPanel}.
 *
 * @author Dosi Bingov
 *
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface LayerControlPanelView extends IsWidget {

	/**
	 * Set layer that belongs to this view.
	 *
	 * @return The layer that belongs to this view.
	 */
	Layer getLayer();

	/**
	 * Get the layer  that belongs to this view.
	 *
	 * @param layer {@link Layer}
	 */
	void setLayer(Layer layer);

	/**
	 * Set the presenter for callback.
	 *
	 * @param presenter {@link LayerControlPanelPresenter}.
	 */
	void setPresenter(LayerControlPanelPresenter presenter);

	/**
	 * Set layer visible.
	 *
	 * @param visible when true the layer is set visible when false invisible.
	 */
	void setLayerVisible(boolean visible);

	/**
	 * Make possible to toggle visibility of the layer.
	 *
	 * @param enable
	 */
	void enableVisibilityToggle(boolean enable);

	/**
	 * Set the title of the layer.
	 *
	 * @param title layer title
	 */
	void setLayerTitle(String title);
}
