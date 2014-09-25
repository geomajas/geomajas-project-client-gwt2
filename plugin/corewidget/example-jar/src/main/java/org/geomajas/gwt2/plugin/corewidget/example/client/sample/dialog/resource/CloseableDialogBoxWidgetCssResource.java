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
package org.geomajas.gwt2.plugin.corewidget.example.client.sample.dialog.resource;

import com.google.gwt.resources.client.CssResource;

/**
 * Interface for the CloseableDialogBox Widget Resource.
 *
 * @author David Debuck.
 */
public interface CloseableDialogBoxWidgetCssResource extends CssResource  {

	/**
	 * Css class.
	 * @return String
	 */
	@ClassName("gm-closeableDialogBox")
	String closeableDialogBox();

	/**
	 * Css class.
	 * @return String
	 */
	@ClassName("gm-closeableDialogBoxCaptionContainer")
	String closeableDialogBoxCaptionContainer();

	/**
	 * Css class.
	 * @return String
	 */
	@ClassName("gm-closeableDialogBoxCloseButton")
	String closeableDialogBoxCloseButton();

	/**
	 * Css class.
	 * @return String
	 */
	@ClassName("gm-closeableDialogBoxTitle")
	String closeableDialogBoxTitle();

	/////////////////////////////////////////////////////////////////////////////////////////////
	// MessageBox styling
	/////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Css class.
	 * @return String
	 */
	@ClassName("gm-closeableDialogBoxMessageBoxButton")
	String messageBoxButton();

	/**
	 * Css class.
	 * @return String
	 */
	@ClassName("gm-closeableDialogBoxMessageBoxIcon")
	String messageBoxIcon();

	/**
	 * Css class.
	 * @return String
	 */
	@ClassName("gm-closeableDialogBoxMessageBoxContent")
	String messageBoxContent();

	/**
	 * Css class.
	 * @return String
	 */
	@ClassName("gm-closeableDialogBoxMessageBoxIconHelp")
	String messageBoxIconHelp();

	/**
	 * Css class.
	 * @return String
	 */
	@ClassName("gm-closeableDialogBoxMessageBoxIconWarn")
	String messageBoxIconWarn();

	/**
	 * Css class.
	 * @return String
	 */
	@ClassName("gm-closeableDialogBoxMessageBoxIconError")
	String messageBoxIconError();

	/**
	 * Css class.
	 * @return String
	 */
	@ClassName("gm-closeableDialogBoxMessageBoxIconInfo")
	String messageBoxIconInfo();

}
