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
package org.geomajas.gwt2.widget.client.other.dialog.resource;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import org.geomajas.annotation.Api;

/**
 * Interface for the CloseableDialogBox Resource.
 *
 * @author David Debuck.
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface CloseableDialogBoxWidgetResource extends ClientBundle {

	/**
	 * Css Resource for this widget.
	 *
	 * @return CloseableDialogBoxWidgetCssResource
	 */
	@Source("org/geomajas/gwt2/widget/client/resource/CoreWidget.css")
	CloseableDialogBoxWidgetCssResource css();

	/**
	 * Image sprite.
	 *
	 * @return ImageResource
	 */
	@Source("org/geomajas/gwt2/widget/client/resource/image/close.png")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource closeableDialogCloseIcon();

	/**
	 * Image sprite.
	 *
	 * @return ImageResource
	 */
	@Source("org/geomajas/gwt2/widget/client/resource/image/close_hover.png")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource closeableDialogCloseIconHover();

	/**
	 * Image sprite.
	 *
	 * @return ImageResource
	 */
	@Source("org/geomajas/gwt2/widget/client/resource/image/dialog-error.png")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None, width = 48, height = 48)
	ImageResource messageBoxErrorIcon();

	/**
	 * Image sprite.
	 *
	 * @return ImageResource
	 */
	@Source("org/geomajas/gwt2/widget/client/resource/image/dialog-help.png")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None, width = 48, height = 48)
	ImageResource messageBoxHelpIcon();

	/**
	 * Image sprite.
	 *
	 * @return ImageResource
	 */
	@Source("org/geomajas/gwt2/widget/client/resource/image/dialog-information.png")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None, width = 48, height = 48)
	ImageResource messageBoxInfoIcon();

	/**
	 * Image sprite.
	 *
	 * @return ImageResource
	 */
	@Source("org/geomajas/gwt2/widget/client/resource/image/dialog-warning.png")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None, width = 48, height = 48)
	ImageResource messageBoxWarnIcon();

}
