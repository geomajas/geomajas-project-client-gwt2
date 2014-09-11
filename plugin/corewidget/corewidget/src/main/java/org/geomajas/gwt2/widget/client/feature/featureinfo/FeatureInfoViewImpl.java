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

package org.geomajas.gwt2.widget.client.feature.featureinfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.widget.client.feature.featureinfo.builder.AttributeWidgetFactory;
import org.geomajas.gwt2.widget.client.feature.featureinfo.resource.FeatureInfoResource;

/**
 * View implementation of the {@link org.geomajas.gwt2.widget.client.feature.featureinfo.FeatureInfoWidget}.
 *
 * @author Youri Flement
 */
public class FeatureInfoViewImpl implements FeatureInfoView {

	private static final AttributeWidgetFactory ATTRIBUTE_FACTORY =
			GWT.create(AttributeWidgetFactory.class);

	private FeatureInfoPresenter presenter;

	private FeatureInfoResource resource;

	@UiField
	protected ScrollPanel contentPanel;

	private static final FeatureInfoViewImplUiBinder UI_BINDER = GWT.create(FeatureInfoViewImplUiBinder.class);

	/**
	 * {@link UiBinder} for this class.
	 *
	 * @author Youri Flement
	 */
	interface FeatureInfoViewImplUiBinder extends UiBinder<ScrollPanel, FeatureInfoViewImpl> {

	}

	public FeatureInfoViewImpl(FeatureInfoResource resource) {
		this.resource = resource;
		this.resource.css().ensureInjected();
		UI_BINDER.createAndBindUi(this);
	}

	@Override
	public void setFeature(Feature feature) {
		// Clear the current panel:
		contentPanel.clear();

		// Layout the attributes of the feature in a grid:
		Grid grid = new Grid(feature.getAttributes().size(), 3);
		CellFormatter formatter = grid.getCellFormatter();
		int i = 0;
		for (AttributeDescriptor descriptor : feature.getLayer().getAttributeDescriptors()) {
			// Put the attribute label in the first column:
			grid.setText(i, 0, getAttributeLabel(feature, descriptor));
			formatter.getElement(i, 0).addClassName(resource.css().attributeLabel());

			// Put a delimiter in the second column:
			grid.setText(i, 1, getDelimiter());

			// Create a widget for the attribute value and put it in the last column:
			Widget attributeWidget = ATTRIBUTE_FACTORY.createAttributeWidget(feature, descriptor);
			attributeWidget.getElement().addClassName(resource.css().attributeValue());
			grid.setWidget(i, 2, attributeWidget);

			i++;
		}

		contentPanel.add(grid);
	}

	/**
	 * Give developers some way to easily overwrite the default label of an attribute
	 * and still keep the general layout of the widget.
	 *
	 * @param feature    The feature.
	 * @param descriptor The attribute descriptor.
	 * @return A label for the attribute.
	 */
	protected String getAttributeLabel(Feature feature, AttributeDescriptor descriptor) {
		return descriptor.getName();
	}

	/**
	 * Give developers an easy way to overwrite the default delimiter and still keep the general
	 * layout of the widget.
	 *
	 * @return The delimiter.
	 */
	protected String getDelimiter() {
		return ": ";
	}

	@Override
	public void setPresenter(FeatureInfoPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public Widget asWidget() {
		return contentPanel;
	}

}
