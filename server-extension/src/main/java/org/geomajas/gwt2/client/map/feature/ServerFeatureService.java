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

package org.geomajas.gwt2.client.map.feature;

import org.geomajas.geometry.Geometry;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;
import org.geomajas.gwt2.client.map.layer.VectorServerLayer;
import org.geomajas.layer.feature.SearchCriterion;

/**
 * <p> Service for feature retrieval and manipulation. This service is map specific, and so the methods may assume the
 * features come from layers within the map's {@link org.geomajas.gwt2.client.map.layer.LayersModel}. </p>
 *
 * @author Pieter De Graef
 */
public interface ServerFeatureService {

	/**
	 * Logical operator used when searching for features through multiple attribute criteria.
	 *
	 * @author Pieter De Graef
	 */
	public enum LogicalOperator {
		/**
		 * Only retrieve features for which ALL criteria apply.
		 */
		AND("AND"),

		/**
		 * Retrieve features for which at least ONE criterion applies.
		 */
		OR("OR");

		private String value;

		private LogicalOperator(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * Definition of geometric query types.
	 *
	 * @author Pieter De Graef
	 */
	public enum QueryType {
		/**
		 * Apply the <code>intersects</code> calculation when searching features.
		 */
		INTERSECTS(1),

		/**
		 * Apply the <code>touches</code> calculation when searching features.
		 */
		TOUCHES(2),

		/**
		 * Apply the <code>within</code> calculation when searching features.
		 */
		WITHIN(3),

		/**
		 * Apply the <code>contains</code> calculation when searching features.
		 */
		CONTAINS(4);

		private int value;

		private QueryType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	/**
	 * Search types that determines which layers to search.
	 *
	 * @author Pieter De Graef
	 */
	public enum SearchLayerType {
		/**
		 * Go over all layers in the given order, but stop searching the moment a layer returns a result. If 4 layers
		 * are passed to the search, and the second layer produces a result, then the third and fourth layer will not be
		 * searched anymore. This option will therefore return results for maximum 1 layer.
		 */
		TOP_LAYER_ONLY(1),

		/**
		 * Always search in all given layers. Some layers may produce results while others will not. This option does
		 * not care, it will always loop over all layers.
		 */
		SEARCH_ALL_LAYERS(2),

		/**
		 * Searches only the selected layer in the {@link org.geomajas.gwt2.client.map.layer.LayersModel}, and only if
		 * this layer is of the type {@link VectorServerLayer}.
		 */
		SEARCH_SELECTED_LAYER(3);

		private int value;

		private SearchLayerType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	/**
	 * Transform a server-side feature object into a client-side feature object.
	 *
	 * @param feature The server-side feature.
	 * @param layer   The layer it belongs to.
	 * @return The client-side feature.
	 */
	Feature create(org.geomajas.layer.feature.Feature feature, FeaturesSupported layer);

	// ------------------------------------------------------------------------
	// Searching features by attributes:
	// ------------------------------------------------------------------------

	/**
	 * Search for features within a certain layer through a list of attribute criteria.
	 *
	 * @param crs           The map coordinate reference system.
	 * @param layer         The layer to search in.
	 * @param criteria      The list of attribute criteria.
	 * @param operator      The logical operator to use between the criteria (AND/OR).
	 * @param maxResultSize The maximum number of features to return. Use -1 if there should be no maximum.
	 * @param callback      Callback function to apply on the result.
	 */
	void search(String crs, VectorServerLayer layer, SearchCriterion[] criteria, LogicalOperator operator,
			int maxResultSize, FeatureMapFunction callback);

	// ------------------------------------------------------------------------
	// Searching features by location:
	// ------------------------------------------------------------------------

	/**
	 * Search for features within a certain layer by a given location.
	 *
	 * @param crs      The map coordinate reference system.
	 * @param layer    The layer to search in.
	 * @param location The location to search at. Should be expressed in the map CRS.
	 * @param buffer   A buffer that can be applied to extend the location. Keep this at 0 if no buffer is necessary.
	 * @param callback Callback function to apply on the result.
	 */
	void search(String crs, VectorServerLayer layer, Geometry location, double buffer, FeatureMapFunction callback);

	/**
	 * Search for features within the {@link org.geomajas.gwt2.client.map.layer.LayersModel} at the given location.
	 *
	 * @param mapPresenter The map to search for.
	 * @param location     The location to search at. Should be expressed in the map CRS.
	 * @param buffer       A buffer that can be applied to extend the location. Keep this at 0 if no buffer is
	 *                     necessary.
	 * @param queryType    The type of geometric calculation that should determine whether or not to include a certain
	 *                     feature.
	 * @param searchType   Determines which layers to search. Unless this type is "SELECTED_LAYER", all layers will be
	 *                     included in the search request.
	 * @param ratio        Used only in case the queryType equals INTERSECTS. This value (between 0 and 1) determines
	 *                     the ratio of intersection whereby to include features in the search. If for example a ratio
	 *                     of 0.5 is used, all features that intersect for at least 50% with the given location will be
	 *                     included.
	 * @param callback     Callback function to apply on the result.
	 */
	void search(MapPresenter mapPresenter, Geometry location, double buffer, QueryType queryType,
			SearchLayerType searchType, float ratio, FeatureMapFunction callback);
}