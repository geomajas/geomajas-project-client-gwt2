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

package org.geomajas.gwt2.plugin.editing.example.client.i18n;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * Used for injecting HTML constructs as i18n messages into the samples.
 * 
 * @author Pieter De Graef
 */
public class SafeHtmlMessages {

	private static SampleMessages msg = GWT.create(SampleMessages.class);

	public SafeHtml explanation() {
		return SafeHtmlUtils.fromSafeConstant(msg.generalExplanation());
	}
	
	public SafeHtml mergeExplanation() {
		return SafeHtmlUtils.fromSafeConstant(msg.mergeCountriesSelect());
	}
}