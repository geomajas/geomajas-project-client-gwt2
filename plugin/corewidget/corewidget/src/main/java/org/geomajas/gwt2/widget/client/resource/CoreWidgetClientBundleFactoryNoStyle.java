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

package org.geomajas.gwt2.widget.client.resource;

import com.google.gwt.core.client.GWT;
import org.geomajas.gwt2.widget.client.feature.featureselectbox.resource.FeatureSelectBoxResource;
import org.geomajas.gwt2.widget.client.feature.featureselectbox.resource.FeatureSelectBoxResourceNoStyle;
import org.geomajas.gwt2.widget.client.other.dialog.resource.CloseableDialogBoxWidgetResource;
import org.geomajas.gwt2.widget.client.other.dialog.resource.CloseableDialogBoxWidgetResourceNoStyle;

/**
 * No style factory for client bundles defined within this artifact. This factory wipes out all css.
 *
 * @author Jan De Moerloose
 */
public class CoreWidgetClientBundleFactoryNoStyle extends CoreWidgetClientBundleFactory {

	/**
	 * Create an empty resource bundle for the
	 * {@link org.geomajas.gwt2.widget.client.FeatureSelectBox.FeatureSelectListener} widget.
	 *
	 * @return A new resource bundle.
	 */
	public FeatureSelectBoxResource createFeatureSelectBoxResource() {
		return GWT.create(FeatureSelectBoxResourceNoStyle.class);
	}

	/**
	 * Create an empty resource bundle for the
	 * {@link org.geomajas.gwt2.widget.client.other.dialog.CloseableDialogBoxWidget}.
	 *
	 * @return CloseableDialogBoxWidgetResource
	 */
	public CloseableDialogBoxWidgetResource createCloseableDialogBoxWidgetResource() {
		return GWT.create(CloseableDialogBoxWidgetResourceNoStyle.class);
	}

}
