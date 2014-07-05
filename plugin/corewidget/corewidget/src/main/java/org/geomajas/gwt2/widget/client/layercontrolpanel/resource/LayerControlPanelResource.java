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
package org.geomajas.gwt2.widget.client.layercontrolpanel.resource;

import com.google.gwt.resources.client.ClientBundle;
import org.geomajas.gwt2.widget.client.CoreWidget;

/**
 * Client resource bundle interface for {@link org.geomajas.gwt2.widget.client.layercontrolpanel.LayerControlPanel}.
 * 
 * @author Dosi Bingov
 * 
 */
public interface LayerControlPanelResource extends ClientBundle {
	
	/**
	 * Instance for use outside UIBinder.
	 */
	LayerControlPanelResource INSTANCE = CoreWidget.getInstance().getClientBundleFactory().
			createLayerControlPanelResource();

	/**
	 * Get the css resource.
	 * @return the css resource
	 */
	@Source("org/geomajas/gwt2/widget/client/resource/CoreWidget.css")
	LayerControlPanelCssResource css();
	
}