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

import java.util.List;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.sld.RuleInfo;

/**
 * Default layer for {@link ClientVectorLayerInfo}.
 * 
 * @author Jan De Moerloose
 */
public interface VectorServerLayer extends ServerLayer<ClientVectorLayerInfo>, LabelsSupported, FeaturesSupported {

	/**
	 * Update the style for a layer. The layer will notify it's listeners.
	 * 
	 * @param styleInfo
	 *            the styleinfo
	 */
	void updateStyle(NamedStyleInfo styleInfo);

	/**
	 * Get the SLD rules for this layer.
	 * 
	 * @return the rules
	 */
	List<RuleInfo> getRules();

	/**
	 * Apply a filter on the layer. Such a filter specifies which features are to be shown on the map, and which aren't.
	 * This filter is actually used on the back-end and therefore follows the CQL standards.
	 *
	 * @param filter
	 *            The CQL filter, based upon the layer attribute definitions. Use null to disable filtering.
	 */
	void setFilter(String filter);

	/**
	 * Returns the filter that has been set onto this layer.
	 *
	 * @return Returns the CQL filter.
	 */
	String getFilter();
}