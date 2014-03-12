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

package org.geomajas.gwt2.client.map.layer;

import org.geomajas.command.dto.RegisterNamedStyleInfoRequest;
import org.geomajas.command.dto.RegisterNamedStyleInfoResponse;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt2.client.event.FeatureDeselectedEvent;
import org.geomajas.gwt2.client.event.FeatureSelectedEvent;
import org.geomajas.gwt2.client.event.LayerLabelHideEvent;
import org.geomajas.gwt2.client.event.LayerLabelShowEvent;
import org.geomajas.gwt2.client.event.LayerStyleChangedEvent;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptorImpl;
import org.geomajas.gwt2.client.map.attribute.AttributeType;
import org.geomajas.gwt2.client.map.attribute.PrimitiveAttributeTypeImpl;
import org.geomajas.gwt2.client.map.attribute.PrimitiveType;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.render.TileLevelRenderer;
import org.geomajas.gwt2.client.map.render.dom.DomFixedScaleLayerRenderer;
import org.geomajas.gwt2.client.map.render.LayerRenderer;
import org.geomajas.gwt2.client.map.render.dom.VectorServerLayerScaleRenderer;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlContainer;
import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.RuleInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Vector layer representation.
 *
 * @author Pieter De Graef
 */
public class VectorServerLayerImpl extends AbstractServerLayer<ClientVectorLayerInfo> implements VectorServerLayer {

	private final DomFixedScaleLayerRenderer renderer;

	private final Map<String, Feature> selection;

	private final List<AttributeDescriptor> descriptors;

	private String filter;

	private boolean labeled;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	@SuppressWarnings("deprecation")
	public VectorServerLayerImpl(ClientVectorLayerInfo layerInfo, final ViewPort viewPort, MapEventBus eventBus) {
		super(layerInfo, viewPort, eventBus);
		this.selection = new HashMap<String, Feature>();
		this.renderer = new DomFixedScaleLayerRenderer(viewPort, this, eventBus) {

			@Override
			public TileLevelRenderer createNewScaleRenderer(int tileLevel, View view, HtmlContainer scaleContainer) {
				return new VectorServerLayerScaleRenderer(VectorServerLayerImpl.this, tileLevel,
						viewPort.getResolution(tileLevel), viewPort, scaleContainer);
			}
		};

		this.descriptors = new ArrayList<AttributeDescriptor>();
		if (layerInfo.getFeatureInfo() != null && layerInfo.getFeatureInfo().getAttributes() != null) {
			for (AttributeInfo attributeInfo : layerInfo.getFeatureInfo().getAttributes()) {
				AttributeDescriptor descriptor = toDescriptor(attributeInfo);
				if (descriptor != null) {
					this.descriptors.add(descriptor);
				}
			}
		}
	}

	// ------------------------------------------------------------------------
	// Layer implementation:
	// ------------------------------------------------------------------------

	@Override
	public LayerRenderer getRenderer() {
		return renderer;
	}

	// ------------------------------------------------------------------------
	// FeaturesSupported implementation:
	// ------------------------------------------------------------------------

	@Override
	public void setFilter(String filter) {
		this.filter = filter;
		refresh();
	}

	@Override
	public String getFilter() {
		return filter;
	}

	@Override
	public List<AttributeDescriptor> getAttributeDescriptors() {
		return descriptors;
	}

	@Override
	public boolean isFeatureSelected(String featureId) {
		return selection.containsKey(featureId);
	}

	@Override
	public boolean selectFeature(Feature feature) {
		if (!selection.containsValue(feature) && feature.getLayer() == this) {
			selection.put(feature.getId(), feature);
			eventBus.fireEvent(new FeatureSelectedEvent(this, feature));
		}
		return false;
	}

	@Override
	public boolean deselectFeature(Feature feature) {
		if (selection.containsKey(feature.getId())) {
			selection.remove(feature.getId());
			eventBus.fireEvent(new FeatureDeselectedEvent(this, feature));
			return true;
		}
		return false;
	}

	@Override
	public void clearSelectedFeatures() {
		for (Feature feature : selection.values()) {
			eventBus.fireEvent(new FeatureDeselectedEvent(this, feature));
		}
		selection.clear();
	}

	@Override
	public Collection<Feature> getSelectedFeatures() {
		return selection.values();
	}

	// ------------------------------------------------------------------------
	// LabelsSupported implementation:
	// ------------------------------------------------------------------------

	@Override
	public void setLabeled(boolean labeled) {
		this.labeled = labeled;
		if (labeled) {
			eventBus.fireEvent(new LayerLabelShowEvent(this));
		} else {
			eventBus.fireEvent(new LayerLabelHideEvent(this));
		}
	}

	@Override
	public boolean isLabeled() {
		return labeled;
	}

	// ------------------------------------------------------------------------
	// VectorServerLayer implementation:
	// ------------------------------------------------------------------------

	@Override
	public void updateStyle(NamedStyleInfo nsi) {
		getLayerInfo().setNamedStyleInfo(nsi);
		GwtCommand commandRequest = new GwtCommand(RegisterNamedStyleInfoRequest.COMMAND);
		RegisterNamedStyleInfoRequest request = new RegisterNamedStyleInfoRequest();
		request.setLayerId(getServerLayerId());
		request.setNamedStyleInfo(getLayerInfo().getNamedStyleInfo());
		commandRequest.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(commandRequest,
				new AbstractCommandCallback<RegisterNamedStyleInfoResponse>() {

					@Override
					public void execute(RegisterNamedStyleInfoResponse response) {
						getLayerInfo().getNamedStyleInfo().setName(response.getStyleName());
						eventBus.fireEvent(new LayerStyleChangedEvent(VectorServerLayerImpl.this));
					}
				});
	}

	@Override
	public List<RuleInfo> getRules() {
		List<RuleInfo> rules = new ArrayList<RuleInfo>();
		for (FeatureTypeStyleInfo sfi : layerInfo.getNamedStyleInfo().getUserStyle().getFeatureTypeStyleList()) {
			rules.addAll(sfi.getRuleList());
		}
		return rules;
	}

	@Override
	public void setOpacity(double opacity) {
		renderer.setOpacity(opacity);
	}

	@Override
	public double getOpacity() {
		return renderer.getOpacity();
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	@SuppressWarnings("deprecation")
	private AttributeDescriptor toDescriptor(AttributeInfo attrInfo) throws IllegalArgumentException {
		if (attrInfo instanceof PrimitiveAttributeInfo) {
			PrimitiveAttributeInfo pai = (PrimitiveAttributeInfo) attrInfo;
			AttributeType type;
			switch (pai.getType()) {
				case BOOLEAN:
					type = new PrimitiveAttributeTypeImpl(PrimitiveType.BOOLEAN);
					break;
				case DATE:
					type = new PrimitiveAttributeTypeImpl(PrimitiveType.DATE);
					break;
				case CURRENCY:
				case DOUBLE:
					type = new PrimitiveAttributeTypeImpl(PrimitiveType.DOUBLE);
					break;
				case FLOAT:
					type = new PrimitiveAttributeTypeImpl(PrimitiveType.FLOAT);
					break;
				case INTEGER:
					type = new PrimitiveAttributeTypeImpl(PrimitiveType.INTEGER);
					break;
				case LONG:
					type = new PrimitiveAttributeTypeImpl(PrimitiveType.LONG);
					break;
				case SHORT:
					type = new PrimitiveAttributeTypeImpl(PrimitiveType.SHORT);
					break;
				case IMGURL:
				case URL:
				case STRING:
				default:
					type = new PrimitiveAttributeTypeImpl(PrimitiveType.STRING);
					break;
			}
			return new AttributeDescriptorImpl(type, pai.getName());
		}
		return null;
	}
}