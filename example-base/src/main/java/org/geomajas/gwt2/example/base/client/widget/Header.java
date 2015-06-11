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

package org.geomajas.gwt2.example.base.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.example.base.client.VersionUtil;

/**
 * Page header for this showcase.
 * 
 * @author Pieter De Graef
 */
public class Header extends Composite {

	/**
	 * UI binder interface this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, Header> {
	}

	@UiField
	protected DivElement headerLabel;

	private static final MyUiBinder UIBINDER = GWT.create(MyUiBinder.class);

	public Header() {
		initWidget(UIBINDER.createAndBindUi(this));
		headerLabel.setInnerText("Showcase Client Gwt " + VersionUtil.getVersion());
	}
}