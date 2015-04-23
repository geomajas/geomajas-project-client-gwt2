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

package org.geomajas.gwt2.plugin.corewidget.example.client.sample.feature.featureinfo.control;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * View implementation of the {@link FeatureInfoControl}.
 * The view consists of a simple togglebutton on a panel.
 *
 * @author Youri Flement
 */
public class FeatureInfoControlViewImpl implements FeatureInfoControlView {

	private FeatureInfoControlPresenter presenter;

	@UiField
	protected HTMLPanel panel;

	@UiField
	protected ToggleButton button;

	private static final FeatureInfoControlViewUiBinder UI_BINDER
			= GWT.create(FeatureInfoControlViewUiBinder.class);

	/**
	 * {@link UiBinder} for this class.
	 *
	 * @author Youri Flement
	 */
	interface FeatureInfoControlViewUiBinder extends UiBinder<HTMLPanel, FeatureInfoControlViewImpl> {

	}

	public FeatureInfoControlViewImpl() {
		UI_BINDER.createAndBindUi(this);

		button.getElement().getStyle().setWidth(25, Unit.PX);
		button.getElement().getStyle().setTextAlign(TextAlign.CENTER);
	}

	@UiHandler("button")
	public void handleClick(ClickEvent event) {
		if (button.isDown()) {
			presenter.enableFeatureInfo();
		} else {
			presenter.disableFeatureInfo();
		}
	}

	@Override
	public void setPresenter(FeatureInfoControlPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public Widget asWidget() {
		return panel;
	}
}
