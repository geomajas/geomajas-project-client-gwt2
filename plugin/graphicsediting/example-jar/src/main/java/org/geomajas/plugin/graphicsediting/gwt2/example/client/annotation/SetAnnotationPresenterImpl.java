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
package org.geomajas.plugin.graphicsediting.gwt2.example.client.annotation;

import com.google.web.bindery.event.shared.EventBus;
import org.geomajas.graphics.client.action.AddTextAsAnchorAction;
import org.geomajas.graphics.client.action.DeleteAction;
import org.geomajas.graphics.client.action.ToggleLabelAction;
import org.geomajas.graphics.client.controller.AnchorControllerFactory;
import org.geomajas.graphics.client.controller.CreateAnchoredIconController;
import org.geomajas.graphics.client.controller.CreateAnchoredTextController;
import org.geomajas.graphics.client.controller.CreateIconController;
import org.geomajas.graphics.client.controller.CreateRectangleController;
import org.geomajas.graphics.client.controller.CreateTextController;
import org.geomajas.graphics.client.controller.DeleteControllerFactory;
import org.geomajas.graphics.client.controller.DragControllerFactory;
import org.geomajas.graphics.client.controller.PopupMenuControllerFactory;
import org.geomajas.graphics.client.controller.PopupMenuFactory;
import org.geomajas.graphics.client.controller.ResizeControllerFactory;
import org.geomajas.graphics.client.editor.AnchorStyleEditor;
import org.geomajas.graphics.client.editor.LabelEditor;
import org.geomajas.graphics.client.editor.StrokeFillEditor;
import org.geomajas.graphics.client.editor.TextableEditor;
import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent;
import org.geomajas.graphics.client.service.GraphicsController;
import org.geomajas.graphics.client.service.GraphicsService;
import org.geomajas.graphics.client.service.GraphicsServiceImpl;
import org.geomajas.graphics.client.service.MetaControllerFactory;
import org.geomajas.gwt2.client.event.MapInitializationEvent;
import org.geomajas.gwt2.client.event.MapInitializationHandler;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.plugin.graphicsediting.gwt2.client.GraphicsEditing;
import org.geomajas.plugin.graphicsediting.gwt2.client.StrokeFillCreationValues;
import org.geomajas.plugin.graphicsediting.gwt2.client.action.EditAction;
import org.geomajas.plugin.graphicsediting.gwt2.client.controller.CreateLineController;
import org.geomajas.plugin.graphicsediting.gwt2.client.controller.CreatePolygonController;
import org.geomajas.plugin.graphicsediting.gwt2.client.controller.GeometryEditControllerFactory;
import org.geomajas.plugin.graphicsediting.gwt2.example.client.sample.GraphicsEditingExample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * SetAnnotationPresenterImpl.
 * 
 * @author Jan De Moerloose
 */
public class SetAnnotationPresenterImpl implements SetAnnotationPresenter, SetAnnotationPresenter.Handler,
		MapInitializationHandler, GraphicsObjectContainerEvent.Handler {

	private GraphicsService graphicsService;

	private EventBus eventBus;

	private View view;

	private MapPresenter mapPresenter;

	private AnnotationContainer annotationContainer;

	private Map<Action, GraphicsController> controllerMap = new HashMap<Action, GraphicsController>();

	private GraphicsEditingExample.EXAMPLE exampleType;

	public SetAnnotationPresenterImpl(final View view, final EventBus eventBus, final MapPresenter mapPresenter,
									  GraphicsEditingExample.EXAMPLE exampleType) {
		this.view = view;
		this.eventBus = eventBus;
		this.mapPresenter = mapPresenter;
		this.exampleType = exampleType;
		graphicsService = new GraphicsServiceImpl(eventBus, true, false);
		bind(eventBus);
	}

	private void bind(final EventBus eventBus) {
		mapPresenter.getEventBus().addMapInitializationHandler(this);
		view.setHandler(this);
	}

	@Override
	public void onMapInitialized(MapInitializationEvent event) {		
		annotationContainer = new AnnotationContainer(mapPresenter, eventBus);
		annotationContainer.setRootContainer(mapPresenter.getContainerManager().addWorldContainer());
		annotationContainer.addGraphicsObjectContainerHandler(this);
		graphicsService.setMetaControllerFactory(new MetaControllerFactory() {

			@Override
			public GraphicsController createController(GraphicsService graphicsService) {
				return new NavigationMetaController(graphicsService, mapPresenter);
			}
		});
		graphicsService.setObjectContainer(annotationContainer);
		graphicsService.registerControllerFactory(new ResizeControllerFactory());
		graphicsService.registerControllerFactory(new DragControllerFactory());
		graphicsService.registerControllerFactory(new AnchorControllerFactory());
		graphicsService.registerControllerFactory(new DeleteControllerFactory());
//		graphicsService.registerControllerFactory(new PropertyEditControllerFactory());
		switch (exampleType) {
			case CONTROLLER:
				graphicsService.registerControllerFactory(new GeometryEditControllerFactory(mapPresenter));
				break;
			case ACTION:
				PopupMenuControllerFactory popupFactory = new PopupMenuControllerFactory(new PopupMenuFactory(),
						1.3, 1.3);
				popupFactory.registerAction(new EditAction(mapPresenter));
				popupFactory.registerAction(new DeleteAction());
				popupFactory.registerEditor(new LabelEditor());
				popupFactory.registerEditor(new TextableEditor());
				popupFactory.registerEditor(new StrokeFillEditor());
				popupFactory.registerEditor(new AnchorStyleEditor());
				popupFactory.registerAction(new ToggleLabelAction());
				popupFactory.registerAction(new AddTextAsAnchorAction());
				graphicsService.registerControllerFactory(popupFactory);
				break;
		}
		StrokeFillCreationValues creationValues = GraphicsEditing.getInstance().getStrokeFillCreationValues();
		creationValues.setPolygonCreateFillColor("red");
		creationValues.setPolygonCreateStrokeColor("yellow");
		creationValues.setLineCreateStrokeOpacity(0.5);
		creationValues.setLineCreateStrokeWidth(5);
		controllerMap.put(Action.CREATE_LINE, new CreateLineController(graphicsService, mapPresenter));
		controllerMap.put(Action.CREATE_POLYGON, new CreatePolygonController(graphicsService, mapPresenter));
		controllerMap.put(Action.CREATE_RECTANGLE, new CreateRectangleController(graphicsService));
		controllerMap.put(Action.CREATE_TEXT, new CreateTextController(graphicsService));
		controllerMap.put(Action.CREATE_ANCHORED_TEXT, new CreateAnchoredTextController(graphicsService));
		controllerMap.put(Action.CREATE_ICON, new CreateIconController(graphicsService,
				34, 34, new ArrayList<String>()));
		controllerMap.put(Action.CREATE_ANCHORED_ICON,
				new CreateAnchoredIconController(graphicsService, 34, 34, new ArrayList<String>() ));
	}

	@Override
	public void onAction(GraphicsObjectContainerEvent event) {
		for (GraphicsController c : controllerMap.values()) {
			c.setActive(false);
		}
		// deactivate all
		for (Action action : Action.values()) {
			view.setActive(action, false);
		}
		graphicsService.getMetaController().setActive(true);
	}

	@Override
	public void onAction(Action action) {
		graphicsService.start();
		for (Action a : controllerMap.keySet()) {
			if (a != action) {
				controllerMap.get(a).setActive(false);
				view.setActive(a, false);
			}
		}
		GraphicsController c = controllerMap.get(action);
		graphicsService.getMetaController().setActive(c.isActive());
		c.setActive(!c.isActive());
		view.setActive(action, c.isActive());
	}

	@Override
	public View getView() {
		return view;
	}

	@Override
	public void onDuplicateWhenDragging(boolean duplicate) {
		graphicsService.setShowOriginalObjectWhileDragging(duplicate);
	}
}
