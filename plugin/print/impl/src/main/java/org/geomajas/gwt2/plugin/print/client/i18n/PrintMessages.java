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
package org.geomajas.gwt2.plugin.print.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * Message bundle for print plugin.
 *
 * @author An Buyle
 * @author Jan De Moerloose
 *
 */
public interface PrintMessages extends Messages {

	String printPrefsTitleLabel();
	String printPrefsTitleTooltip();
	String printPrefsTitlePlaceholder();
	String defaultPrintTitle();
	String printButtonTitle();

	String printPrefsChoose();
	String printPrefsHeader();
	String printPrefsPrint();

	String printPrefsSize();
	String printPrefsPageSizeLabel();
	String printPrefsPageSizeTooltip();

	String printPrefsOrientation();
	String printPrefsPortrait();
	String printPrefsLandscape();

	String printPrefsWithArrow();
	String printPrefsWithScaleBar();

	String printPrefsDpi();
	String printPrefsDpiTooltip();

	String printPrefsRasterDpi();
	String printPrefsRasterDpiTooltip();

	String printPrefsStatus();

	String printPrefsDownloadType();
	String printPrefsPostPrintActionLabel();
	String printPrefsSaveAsFile();
	String printPrefsOpenInBrowserWindow();

	String printPrefsFileName();
	String printPrefsFileNameTooltip();
	String defaultPrintFileName();
	
	String printPrefsSync();	
	String printPrefsSyncTooltip();
}
