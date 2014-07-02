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
package org.geomajas.gwt2.widget.client.layercontrolpanel;

import com.google.gwt.user.client.ui.IsWidget;
import org.geomajas.annotation.Api;

/**
 * MVP view for {@link org.geomajas.gwt2.widget.client.layercontrolpanel.LayerControlPanel}.
 *
 * @author Dosi Bingov
 *
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface LayerControlPanelView extends IsWidget {


	/**
	 * Set the presenter for callback.
	 *
	 * @param presenter
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

	/**
	 * Set url of legend URL.
	 * @param url of legend image
	 */
	void setLegendUrl(String url);

}
