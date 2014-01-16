/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.gwt2.widget.example.client.sample.itemselect;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.widget.client.itemselect.ItemSelectView;
import org.geomajas.gwt2.widget.client.itemselect.ItemSelectWidget;

import java.util.Arrays;

/**
 * Class description.
 *
 * @author Dosi Bingov
 */
public class ItemSelectExample implements SamplePanel {

	protected DockLayoutPanel rootElement;

	@UiField
	protected Button selectButton;

	@Override
	public Widget asWidget() {
		if (rootElement == null) {
			rootElement = uiBinder.createAndBindUi(this);
			selectButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					ItemSelectWidget<String> itemSelectWidget = new ItemSelectWidget<String>();
					itemSelectWidget.addItemSelectHandler(new ItemSelectView.ItemSelectHandler<String>() {
						@Override
						public void itemSelected(String item) {
							Window.alert(item);
						}
					});
					itemSelectWidget.setItems(Arrays.asList("One", "Two", "Three"));
					itemSelectWidget.popup(event.getClientX(), event.getClientY(), true);
				}
			});
		}
		// return root layout element
		return rootElement;
	}

	/**
	 * UI binder interface.
	 *
	 * @author Dosi Bingov
	 */
	interface ItemSelectedExampleUiBinder extends UiBinder<DockLayoutPanel, ItemSelectExample> {

	}

	private final ItemSelectedExampleUiBinder uiBinder;

	public ItemSelectExample() {
		uiBinder = GWT.create(ItemSelectedExampleUiBinder.class);
	}

}