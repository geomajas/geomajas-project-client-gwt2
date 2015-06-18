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
package org.geomajas.gwt2.plugin.graphicsediting.example.client.annotation;

import com.google.web.bindery.event.shared.EventBus;
import org.geomajas.graphics.client.action.DeleteAction;
import org.geomajas.graphics.client.controller.GraphicsController;
import org.geomajas.graphics.client.controller.MetaController;
import org.geomajas.graphics.client.controller.create.base.CreateBaseIconController;
import org.geomajas.graphics.client.controller.create.base.CreateBaseRectangleController;
import org.geomajas.graphics.client.controller.create.base.CreateBaseTextController;
import org.geomajas.graphics.client.controller.create.updateable.CreateAnchoredIconController;
import org.geomajas.graphics.client.controller.delete.DeleteControllerFactory;
import org.geomajas.graphics.client.controller.drag.DragControllerFactory;
import org.geomajas.graphics.client.controller.popupmenu.PopupMenuControllerFactory;
import org.geomajas.graphics.client.controller.resize.ResizeControllerFactory;
import org.geomajas.graphics.client.editor.StrokeFillEditor;
import org.geomajas.graphics.client.editor.TextableEditor;
import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent;
import org.geomajas.graphics.client.service.GraphicsService;
import org.geomajas.graphics.client.service.GraphicsServiceImpl;
import org.geomajas.graphics.client.service.MetaControllerFactory;
import org.geomajas.gwt2.client.event.MapInitializationEvent;
import org.geomajas.gwt2.client.event.MapInitializationHandler;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.plugin.graphicsediting.client.GraphicsEditing;
import org.geomajas.gwt2.plugin.graphicsediting.client.StrokeFillCreationValues;
import org.geomajas.gwt2.plugin.graphicsediting.client.action.EditAction;
import org.geomajas.gwt2.plugin.graphicsediting.client.controller.CreateGeometryLineController;
import org.geomajas.gwt2.plugin.graphicsediting.client.controller.CreateGeometryPolygonController;
import org.geomajas.gwt2.plugin.graphicsediting.client.controller.GeometryEditControllerFactory;
import org.geomajas.gwt2.plugin.graphicsediting.example.client.sample.GraphicsEditingExample;

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
		graphicsService = new GraphicsServiceImpl(eventBus);
		bind(eventBus);
	}

	private void bind(final EventBus eventBus) {
		mapPresenter.getEventBus().addMapInitializationHandler(this);
		view.setHandler(this);
	}

	@Override
	public void onMapInitialized(MapInitializationEvent event) {		
		annotationContainer = new AnnotationContainer(mapPresenter, eventBus);
		//TODO: find out
//		annotationContainer.setWidgetContainer(mapPresenter.getContainerManager().addWorldContainer());
		annotationContainer.addGraphicsObjectContainerHandler(this);
		graphicsService.setMetaControllerFactory(new MetaControllerFactory() {

			@Override
			public MetaController createController(GraphicsService graphicsService) {
				return new NavigationMetaController(graphicsService, mapPresenter);
			}
		});
		graphicsService.setObjectContainer(annotationContainer);
		graphicsService.registerControllerFactory(new ResizeControllerFactory());
		graphicsService.registerControllerFactory(new DragControllerFactory());
//		graphicsService.registerControllerFactory(new AnchorControllerFactory());
		graphicsService.registerControllerFactory(new DeleteControllerFactory());
//		graphicsService.registerControllerFactory(new PropertyEditControllerFactory());
		switch (exampleType) {
			case CONTROLLER:
				graphicsService.registerControllerFactory(new GeometryEditControllerFactory(mapPresenter));
				break;
			case ACTION:
				PopupMenuControllerFactory popupFactory = new PopupMenuControllerFactory();
				popupFactory.registerAction(new EditAction(mapPresenter));
				popupFactory.registerAction(new DeleteAction());
//				popupFactory.registerEditor(new LabelEditor());
				popupFactory.registerEditor(new TextableEditor());
				popupFactory.registerEditor(new StrokeFillEditor());
//				popupFactory.registerEditor(new AnchorStyleEditor());
//				popupFactory.registerAction(new ToggleLabelAction());
//				popupFactory.registerAction(new AddTextAsAnchorAction());
				graphicsService.registerControllerFactory(popupFactory);
				break;
		}
		StrokeFillCreationValues creationValues = GraphicsEditing.getInstance().getStrokeFillCreationValues();
		creationValues.setPolygonCreateFillColor("red");
		creationValues.setPolygonCreateStrokeColor("yellow");
		creationValues.setLineCreateStrokeOpacity(0.5);
		creationValues.setLineCreateStrokeWidth(5);
		controllerMap.put(Action.CREATE_LINE, new CreateGeometryLineController(graphicsService, mapPresenter));
		controllerMap.put(Action.CREATE_POLYGON, new CreateGeometryPolygonController(graphicsService, mapPresenter));
		controllerMap.put(Action.CREATE_RECTANGLE, new CreateBaseRectangleController(graphicsService));
		controllerMap.put(Action.CREATE_TEXT, new CreateBaseTextController(graphicsService));
//		controllerMap.put(Action.CREATE_ANCHORED_TEXT, new CreateAnchoredTextController(graphicsService));
		controllerMap.put(Action.CREATE_ICON, new CreateBaseIconController(graphicsService,
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
