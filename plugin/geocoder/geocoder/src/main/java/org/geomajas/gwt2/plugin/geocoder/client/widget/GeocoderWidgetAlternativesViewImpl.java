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

package org.geomajas.gwt2.plugin.geocoder.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.plugin.geocoder.client.Geocoder;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringAlternative;

import java.util.List;

/**
 *  Default implementation of {@link GeocoderWidgetAlternativesView}.
 * @author Emiel Ackermann
 * @author Jan Venstermans
 */
public class GeocoderWidgetAlternativesViewImpl implements GeocoderWidgetAlternativesView {

	@UiField
	protected PopupPanel popup;

	@UiField
	protected VerticalPanel alternativesPanel;

	private GeocoderWidgetAlternativesPresenter alternativesPresenter;

	private GeocoderWidgetResource resource;

	private static final MyUiBinder UIBINDER = GWT.create(MyUiBinder.class);

	/**
	 * UI binder interface.
	 *
	 * @author Jan De Moerloose
	 *
	 */
	interface MyUiBinder extends UiBinder<Widget, GeocoderWidgetAlternativesViewImpl> {
	}

	/**
	 * Main constructor.
	 */
	public GeocoderWidgetAlternativesViewImpl() {
		this(Geocoder.getInstance().getBundleFactory().createGeocoderWidgetResource());
	}
	public GeocoderWidgetAlternativesViewImpl(GeocoderWidgetResource resource) {
		this.resource = resource;
		resource.css().ensureInjected();
		UIBINDER.createAndBindUi(this);
		popup.setAutoHideEnabled(true);
		popup.addCloseHandler(new CloseHandler<PopupPanel>() {

			public void onClose(CloseEvent<PopupPanel> event) {
				alternativesPresenter.onAlternativesViewClosed();
			}
		});
	}

	@Override
	public void setPosition(Bbox bbox) {
		int left = (int) bbox.getX();
		int top = (int) (bbox.getY() + bbox.getHeight());
		popup.setWidth(String.valueOf(bbox.getWidth()));
		popup.setPopupPosition(left, top);
	}

	@Override
	public void setAlternatives(List<GetLocationForStringAlternative> alternatives) {
		alternativesPanel.clear();
		for (GetLocationForStringAlternative alternative : alternatives) {
			final String altText = alternative.getCanonicalLocation();
			Label altLabel = new Label(altText);
			altLabel.setStyleName(resource.css().geocoderGadgetAltLabel());
			altLabel.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					alternativesPresenter.findLocation(altText);
				}
			});
			alternativesPanel.add(altLabel);
		}
	}

	@Override
	public void show() {
		popup.show();
	}

	@Override
	public void hide() {
		popup.hide();
	}

	@Override
	public void setPresenter(GeocoderWidgetAlternativesPresenter presenter) {
		this.alternativesPresenter = presenter;
	}

	@Override
	public Widget asWidget() {
		return popup;
	}
}