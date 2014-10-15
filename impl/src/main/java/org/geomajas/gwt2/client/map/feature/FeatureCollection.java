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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;

/**
 * Collection of features that belong to a single layer. Could be the result of a search operation, a selection or
 * whatever...
 * 
 * @author Jan De Moerloose
 * 
 */
public class FeatureCollection {

	private FeaturesSupported layer;

	private List<Feature> features = new ArrayList<Feature>();

	private Bbox bounds;

	/**
	 * Create an empty collection of features.
	 * 
	 * @param layer layer
	 */
	public FeatureCollection(FeaturesSupported layer) {
		this(layer, new ArrayList<Feature>(), null);
	}

	/**
	 * Create a collection of features.
	 * 
	 * @param layer layer
	 * @param features features
	 * @param bounds the bounds (optional, can be calculated by iteration as well)
	 */
	public FeatureCollection(FeaturesSupported layer, List<Feature> features, Bbox bounds) {
		this.layer = layer;
		this.features = features;
		this.bounds = bounds;
	}

	public FeaturesSupported getLayer() {
		return layer;
	}

	public List<Feature> getFeatures() {
		return features;
	}

	public boolean isEmpty() {
		return features.isEmpty();
	}

	public void setBounds(Bbox bounds) {
		this.bounds = bounds;
	}

	public Bbox getBounds() {
		if (bounds == null && !features.isEmpty()) {
			for (Feature feature : features) {
				org.geomajas.geometry.Geometry geom = feature.getGeometry();
				if (geom != null) {
					Bbox b = GeometryService.getBounds(geom);
					if (bounds == null) {
						bounds = b;
					} else {
						bounds = BboxService.union(bounds, b);
					}
				}
			}
		}
		return bounds;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((features == null) ? 0 : features.hashCode());
		result = prime * result + ((layer == null) ? 0 : layer.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FeatureCollection other = (FeatureCollection) obj;
		if (features == null) {
			if (other.features != null) {
				return false;
			}
		} else if (!features.equals(other.features)) {
			return false;
		}
		if (layer == null) {
			if (other.layer != null) {
				return false;
			}
		} else if (!layer.equals(other.layer)) {
			return false;
		}
		return true;
	}
	
	

}
