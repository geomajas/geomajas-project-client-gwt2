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
package org.geomajas.gwt2.widget.client.itemselect;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.annotation.Api;
import org.geomajas.gwt2.widget.client.itemselect.resources.ItemSelectResource;

import java.util.List;

/**
 * Widget that shows a list of items from where one can be selected. The widget will display the item's toString() as
 * label.
 *
 * @param <T> The type of the item.
 * @author Dosi Bingov
 * @author Oliver May
 * @since 2.0.0
 */
@Api(allMethods = true)
public class ItemSelectWidget<T> implements ItemSelectView<T>, IsWidget {

	private PopupPanel layout;

	/**
	 * UI binder interface.
	 *
	 * @author Dosi Bingov
	 */
	interface ItemSelectWidgetUiBinder extends UiBinder<Widget, ItemSelectWidget> {

	}

	private final ItemSelectWidgetUiBinder uiBinder;

	private final ItemSelectResource resource;

	private ItemSelectHandler<T> handler;

	private int xPos;

	private int yPos;

	@UiField
	protected VerticalPanel contentPanel;

	public ItemSelectWidget() {
		this((ItemSelectResource) GWT.create(ItemSelectResource.class));
	}

	public ItemSelectWidget(ItemSelectResource resource) {
		this(resource, (ItemSelectWidgetUiBinder) GWT.create(ItemSelectWidgetUiBinder.class));
	}

	public ItemSelectWidget(ItemSelectResource resource, ItemSelectWidgetUiBinder uiBinder) {
		this.resource = resource;
		this.uiBinder = uiBinder;
	}

	@Override
	public void setItems(List<T> data) {
		asWidget();
		contentPanel.clear();

		if (data != null) {
			for (final T s : data) {
				Label label = new Label();
				label.addStyleName(resource.css().itemSelectWidgetCell());
				label.setText(s.toString());
				label.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						hide();
						if (handler != null) {
							handler.itemSelected(s);
						}
					}
				});
				contentPanel.add(label);
			}
		}
	}

	@Override
	public void addItemSelectHandler(ItemSelectHandler<T> handler) {
		this.handler = handler;
	}

	@Override
	public void hide() {
		layout.hide();
	}

	/**
	 * Display this widget in a popup.
	 * This does not show anything when there are no items set.
	 *
	 * @param xPos the x position
	 * @param yPos the y position
	 * @param animate true if animation is wanted
	 */
	public void popup(int xPos, int yPos, boolean animate) {
		asWidget();
		if (contentPanel.getWidgetCount() > 0) {
			layout.setPopupPosition(xPos, yPos);
			layout.setAnimationEnabled(animate);
			layout.show();
		}
	}

	@Override
	public Widget asWidget() {
		this.resource.css().ensureInjected();
		if (layout == null) {
			layout = (PopupPanel) uiBinder.createAndBindUi(this);
			layout.addStyleName(resource.css().itemSelectWidget());
		}
		return layout;
	}
}
