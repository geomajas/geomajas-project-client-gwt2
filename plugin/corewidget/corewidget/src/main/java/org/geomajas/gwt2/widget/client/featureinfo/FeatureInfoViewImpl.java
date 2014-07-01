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

package org.geomajas.gwt2.widget.client.featureinfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.widget.client.featureinfo.resource.FeatureInfoResource;
import org.geomajas.gwt2.widget.client.i18n.WidgetCoreInternationalization;

/**
 * View implementation of the {@link org.geomajas.gwt2.widget.client.featureinfo.FeatureInfoWidget}.
 *
 * @author Youri Flement
 */
public class FeatureInfoViewImpl implements FeatureInfoView {

    private static final WidgetCoreInternationalization MSG = GWT.create(WidgetCoreInternationalization.class);

    private static final FeatureAttributeWidgetFactory ATTRIBUTE_FACTORY = GWT.create(FeatureAttributeWidgetFactory.class);

    private FeatureInfoPresenter presenter;

    private FeatureInfoResource resource;

    /**
     * The selected feature or <code>null</code> if there is none selected.
     */
    private Feature selectedFeature;

    @UiField
    protected VerticalPanel contentPanel;

    @UiField
    protected VerticalPanel optionsPanel;

    @UiField
    protected ScrollPanel infoPanel;

    @UiField
    protected Button zoomToObjectButton;

    private static final FeatureInfoUiBinder UI_BINDER = GWT.create(FeatureInfoUiBinder.class);

    interface FeatureInfoUiBinder extends UiBinder<Widget, FeatureInfoViewImpl> {

    }

    public FeatureInfoViewImpl(FeatureInfoResource resource) {
        this.resource = resource;
        this.resource.css().ensureInjected();
        UI_BINDER.createAndBindUi(this);
        zoomToObjectButton.setText(MSG.zoomToObjectButton());
    }

    @Override
    public void setFeature(Feature feature) {
        // Set the selected feature and clear the current panel:
        selectedFeature = feature;
        infoPanel.clear();

        // Layout the attributes of the feature in a grid:
        Grid grid = new Grid(feature.getAttributes().size(), 3);
        CellFormatter formatter = grid.getCellFormatter();
        int i = 0;
        for (AttributeDescriptor descriptor : feature.getLayer().getAttributeDescriptors()) {
			// Put the label in the first column:
			grid.setText(i, 0, descriptor.getLabel());
			formatter.getElement(i, 0).addClassName(resource.css().attributeLabel());

            // Put a delimiter in the second column:
            grid.setText(i, 1, ": ");

            // Create a widget for the attribute value and put it in the last column:
            grid.setWidget(i, 2, ATTRIBUTE_FACTORY.createFeatureAttributeWidget(feature, descriptor));

            i++;
        }

        infoPanel.add(grid);
    }

    @UiHandler("zoomToObjectButton")
    public void handleClick(ClickEvent event) {
        presenter.zoomToObject(selectedFeature);
    }

    @Override
    public void showOptions(boolean show) {
        optionsPanel.setVisible(show);
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
