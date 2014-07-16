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

package org.geomajas.gwt2.plugin.print.example.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * Specific messages for the print samples.
 * 
 * @author Jan De Moerloose
 */
public interface SampleMessages extends Messages {

	String printTitle();

	String printShort();

	String printDescription();

	/* custom view */

	String printCustomViewTitle();

	String printCustomViewShort();

	String printCustomViewDescription();

	String printCustomViewFileName();

	/* custom handler */

	String printCustomHandlerTitle();

	String printCustomHandlerShort();

	String printCustomHandlerDescription();

	String printCustomHandlerTitleInitial();
	String printCustomHandlerTitleRequestStart();
	String printCustomHandlerTitleRequestFinish();
	String printCustomHandlerTypeLabel(String type);
	String printCustomHandlerUrlLabel();

	/* svg layer */
	String printSvgLayerTitle();

	String printSvgLayerShort();

	String printSvgLayerDescription();

	/* without widget */

	String printServiceExampleTitle();

	String printServiceExampleShort();

	String printServiceExampleDescription();

	String printServiceExampleFixedFileName();

	String printServiceExampleFixedPrintTitle();
}