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
package org.geomajas.gwt2.widget.client.other.dialog;

import com.google.gwt.core.client.GWT;
import org.geomajas.gwt2.widget.client.other.dialog.i18n.CloseableDialogBoxWidgetMessages;

/**
 * Implementation of the CloseableDialogBoxWidgetPresenter interface.
 *
 * @author David Debuck.
 */
public class CloseableDialogBoxWidgetPresenterImpl implements CloseableDialogBoxWidgetPresenter {

	private CloseableDialogBoxWidgetMessages msg = GWT.create(CloseableDialogBoxWidgetMessages.class);

	private CloseableDialogBoxWidgetView view;

	/**
	 * CloseableDialogBoxWidgetPresenterImpl constructor.
	 *
	 * @param view CloseableDialogBoxWidgetView
	 */
	public CloseableDialogBoxWidgetPresenterImpl(CloseableDialogBoxWidgetView view) {

		this.view = view;
		this.view.setPresenter(this);

	}

}
