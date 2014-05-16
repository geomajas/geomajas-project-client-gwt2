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
package org.geomajas.gwt2.widget.client.feature.featureselectbox;

import java.util.List;
import java.util.logging.Logger;

import org.geomajas.gwt2.widget.client.feature.featureselectbox.resource.FeatureSelectBoxResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Class.
 *
 * @author David Debuck.
 */
public class FeatureSelectBoxViewImpl implements FeatureSelectBoxView {

	private Logger log = Logger.getLogger(FeatureSelectBoxViewImpl.class.getName());

	private FeatureClickHandler featureClickHandler = new FeatureClickHandler();

	private FeatureSelectBoxPresenter presenter;

	private PopupPanel widget;

	private int xPos;

	private int yPos;

	@UiField
	protected VerticalPanel contentPanel;

	private FeatureSelectBoxResource resource;

	private static final FeatureSelectBoxUiBinder UIBINDER = GWT.create(FeatureSelectBoxUiBinder.class);

	/**
	 * UI binder interface.
	 *
	 * @author Dosi Bingov
	 *
	 */
	interface FeatureSelectBoxUiBinder extends UiBinder<Widget, FeatureSelectBoxViewImpl> {
	}

	public FeatureSelectBoxViewImpl(FeatureSelectBoxResource featureSelectBoxResource) {
		resource = featureSelectBoxResource;
		widget = (PopupPanel) UIBINDER.createAndBindUi(this);
		widget.addStyleName(featureSelectBoxResource.css().featureSelectBox());
		resource.css().ensureInjected();
	}

	@Override
	public void setLabels(List<String> labels) {
		contentPanel.clear();

		for (String s : labels) {
			Label label = new Label();
			label.setStyleName(resource.css().featureSelectBoxCell());
			label.setText(s);
			label.addClickHandler(featureClickHandler);
			contentPanel.add(label);
		}
	}

	@Override
	public void show(boolean animated) {
		// show widget only if there is content to show
		if (contentPanel.getWidgetCount() > 0) {
			widget.setPopupPosition(xPos, yPos);
			widget.setAnimationEnabled(animated);
			widget.show();
		}
	}

	@Override
	public void addLabel(String label) {
		Label l = new Label();
		l.setText(label);
		l.setStyleName(resource.css().featureSelectBoxCell());
		l.addClickHandler(featureClickHandler);
		contentPanel.add(l);
	}

	@Override
	public void setShowPosition(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
	}

	@Override
	public void hide() {
		widget.hide();
	}

	@Override
	public void clearLabels() {
		contentPanel.clear();
	}

	@Override
	public boolean isVisible() {
		return widget.isVisible();
	}

	@Override
	public void setPresenter(FeatureSelectBoxPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	/**
	 * FeatureClickHandler that handles click on feature label.
	 *
	 * @author Dosi Bingov
	 *
	 */
	private class FeatureClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			Label label = (Label) event.getSource();
			presenter.onFeatureSelected(label.getText());
		}
	}
}
