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
package org.geomajas.gwt.client.util.impl;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Extends {@link DomImplIE9} for IE10 browser.
 * Difference is in the transform functions.
 * 
 * @author Jan Venstermans
 * 
 */
public class DomImplIE10 extends DomImplIE9 {

	/**
	 * {@inheritDoc}
	 */
	public void setTransform(Element element, String transform) {
		DOM.setStyleAttribute(element, "transform", transform);
	}	

	/**
	 * {@inheritDoc}
	 */
	public void setTransformOrigin(Element element, String origin) {
		DOM.setStyleAttribute(element, "transformOrigin", origin);
	}

}
