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
package org.geomajas.gwt2.tools.client.tool.zoomin;

import com.google.gwt.core.client.Callback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.tools.client.event.ActionDisabledEvent;
import org.geomajas.gwt2.tools.client.event.ActionEnabledEvent;
import org.geomajas.gwt2.tools.client.event.ActionHandler;

/**
 * Presenter class for the zoom in tool.
 *
 * @author Oliver May
 */
public interface ZoomInToolPresenter {

	/**
	 * Attach the view to this presenter.
	 *
	 * @param view the view
	 */
	void setView(View view);

	public interface View {

		/**
		 * Disable the zoom in tool.
		 *
		 * @param disabled if disabled.
		 */
		void setDisabled(boolean disabled);

		/**
		 * Add a click handler to the tool.
		 *
		 * @param handler the handler.
		 */
		void addClickedHandler(ClickHandler handler);
	}

	/**
	 * Default implementation of the zoom in tool presenter.
	 */
	public class ZoomInToolPresenterImpl implements ZoomInToolPresenter {

		private MapPresenter mapPresenter;

		private View view;

		private ZoomInAction action;

		/**
		 * Constructor of the zoom in tool presenter.
		 *
		 * @param mapPresenter the map presenter to zoom in on.
		 */
		public ZoomInToolPresenterImpl(MapPresenter mapPresenter) {
			this.mapPresenter = mapPresenter;
			initializeAction();
		}

		@Override
		public void setView(View view) {
			this.view = view;
			attachActionHandler();
			attachClickHandler();
		}

		private void attachActionHandler() {
			action.addActionHandler(new ActionHandler() {
				@Override
				public void onActionEnabled(ActionEnabledEvent event) {
					ZoomInToolPresenterImpl.this.view.setDisabled(false);
				}

				@Override
				public void onActionDisabled(ActionDisabledEvent event) {
					ZoomInToolPresenterImpl.this.view.setDisabled(true);
				}
			});
		}

		private void attachClickHandler() {
			view.addClickedHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					zoomIn();
				}
			});
		}

		private void zoomIn() {
			action.actionPerformed(new Callback<Boolean, Boolean>() {
				@Override
				public void onFailure(Boolean reason) { }

				@Override
				public void onSuccess(Boolean result) { }
			});
		}

		private void initializeAction() {
			action = new ZoomInAction(mapPresenter);
		}


	}

}
