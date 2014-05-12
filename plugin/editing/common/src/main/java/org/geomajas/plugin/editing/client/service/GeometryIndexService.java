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

package org.geomajas.plugin.editing.client.service;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.geometry.service.GeometryValidationState;

/**
 * Service for managing sub-parts of geometries through special geometry indices.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @since 2.0.0
 */
@Api(allMethods = true)
public class GeometryIndexService {

	private org.geomajas.geometry.service.GeometryIndexService delegate = new org.geomajas.geometry.service.GeometryIndexService();

	// ------------------------------------------------------------------------
	// Methods concerning index construction:
	// ------------------------------------------------------------------------

	/**
	 * Create a new index given a type and a list of values. This index will be built recursively and will have a depth
	 * equal to the number of values given.
	 * 
	 * @param type The type of index applied on the deepest child index. Note that all parent index nodes will be of the
	 *        type <code>GeometryIndexType.TYPE_GEOMETRY</code>. Only the deepest child will use this given type.
	 * @param values A list of integer values that determine the indices on each level in the index.
	 * @return The recursive geometry index resulting from the given parameters.
	 */
	public GeometryIndex create(GeometryIndexType type, int... values) {
		return fromDelegate(delegate.create(toDelegate(type), values));
	}

	/**
	 * Given a certain geometry index, add more levels to it (This method will not change the underlying geometry !).
	 * 
	 * @param index The index to start out from.
	 * @param type Add more levels to it, where the deepest level should be of this type.
	 * @param values A list of integer values that determine the indices on each level in the index.
	 * @return The recursive geometry index resulting from adding the given parameters to the given parent index.
	 */
	public GeometryIndex addChildren(GeometryIndex index, GeometryIndexType type, int... values) {
		return fromDelegate(delegate.addChildren(toDelegate(index), toDelegate(type), values));
	}

	// ------------------------------------------------------------------------
	// Methods concerning index parsing/formatting:
	// ------------------------------------------------------------------------

	/**
	 * Format a given geometry index, creating something like "geometry2.vertex1".
	 * 
	 * @param index The geometry index to format.
	 * @return Returns the string value resulting from the index.
	 */
	public String format(GeometryIndex index) {
		return delegate.format(toDelegate(index));
	}

	/**
	 * Given a certain string identifier, parse it as a geometry index.
	 * 
	 * @param id The identifier to try and parse.
	 * @return Returns the associating geometry index (if no exception was thrown).
	 * @throws GeometryIndexNotFoundException In case the identifier could not be parsed.
	 */
	public GeometryIndex parse(String id) throws GeometryIndexNotFoundException {
		try {
			return fromDelegate(delegate.parse(id));
		} catch (org.geomajas.geometry.service.GeometryIndexNotFoundException e) {
			throw fromDelegate(e);
		}
	}

	// ------------------------------------------------------------------------
	// Methods for geometry retrieval:
	// ------------------------------------------------------------------------

	/**
	 * Given a certain geometry, get the sub-geometry the index points to. This only works if the index actually points
	 * to a sub-geometry.
	 * 
	 * @param geometry The geometry to search in.
	 * @param index The index that points to a sub-geometry within the given geometry.
	 * @return Returns the sub-geometry if it exists.
	 * @throws GeometryIndexNotFoundException Thrown in case the index is of the wrong type, or if the sub-geometry
	 *         could not be found within the given geometry.
	 */
	public Geometry getGeometry(Geometry geometry, GeometryIndex index) throws GeometryIndexNotFoundException {
		try {
			return delegate.getGeometry(geometry, toDelegate(index));
		} catch (org.geomajas.geometry.service.GeometryIndexNotFoundException e) {
			throw fromDelegate(e);
		}
	}

	/**
	 * Given a certain geometry, get the vertex the index points to. This only works if the index actually points to a
	 * vertex.
	 * 
	 * @param geometry The geometry to search in.
	 * @param index The index that points to a vertex within the given geometry.
	 * @return Returns the vertex if it exists.
	 * @throws GeometryIndexNotFoundException Thrown in case the index is of the wrong type, or if the vertex could not
	 *         be found within the given geometry.
	 */
	public Coordinate getVertex(Geometry geometry, GeometryIndex index) throws GeometryIndexNotFoundException {
		try {
			return delegate.getVertex(geometry, toDelegate(index));
		} catch (org.geomajas.geometry.service.GeometryIndexNotFoundException e) {
			throw fromDelegate(e);
		}
	}

	/**
	 * Given a certain geometry, get the edge the index points to. This only works if the index actually points to an
	 * edge.
	 * 
	 * @param geometry The geometry to search in.
	 * @param index The index that points to an edge within the given geometry.
	 * @return Returns the edge if it exists.
	 * @throws GeometryIndexNotFoundException Thrown in case the index is of the wrong type, or if the edge could not be
	 *         found within the given geometry.
	 */
	public Coordinate[] getEdge(Geometry geometry, GeometryIndex index) throws GeometryIndexNotFoundException {
		try {
			return delegate.getEdge(geometry, toDelegate(index));
		} catch (org.geomajas.geometry.service.GeometryIndexNotFoundException e) {
			throw fromDelegate(e);
		}
	}

	// ------------------------------------------------------------------------
	// Helper methods:
	// ------------------------------------------------------------------------

	/**
	 * Does the given index point to a vertex or not? We look at the deepest level to check this.
	 * 
	 * @param index The index to check.
	 * @return true or false.
	 */
	public boolean isVertex(GeometryIndex index) {
		return delegate.isVertex(toDelegate(index));
	}

	/**
	 * Does the given index point to an edge or not? We look at the deepest level to check this.
	 * 
	 * @param index The index to check.
	 * @return true or false.
	 */
	public boolean isEdge(GeometryIndex index) {
		return delegate.isEdge(toDelegate(index));
	}

	/**
	 * Does the given index point to a sub-geometry or not? We look at the deepest level to check this.
	 * 
	 * @param index The index to check.
	 * @return true or false.
	 */
	public boolean isGeometry(GeometryIndex index) {
		return delegate.isGeometry(toDelegate(index));
	}

	/**
	 * Get the type of sub-part the given index points to. We look at the deepest level to check this.
	 * 
	 * @param index The index to check.
	 * @return true or false.
	 */
	public GeometryIndexType getType(GeometryIndex index) {
		return fromDelegate(delegate.getType(toDelegate(index)));
	}

	/**
	 * What is the geometry type of the sub-geometry pointed to by the given index? If the index points to a vertex or
	 * edge, the geometry type at the parent level is returned.
	 * 
	 * @param geometry The geometry wherein to search.
	 * @param index The index pointing to a vertex/edge/sub-geometry. In the case of a vertex/edge, the parent geometry
	 *        type is returned. If index is null, the type of the given geometry is returned.
	 * @return The geometry type as defined in the {@link Geometry} class.
	 * @throws GeometryIndexNotFoundException Thrown in case the index points to a non-existing sub-geometry.
	 */
	public String getGeometryType(Geometry geometry, GeometryIndex index) throws GeometryIndexNotFoundException {
		try {
			return delegate.getGeometryType(geometry, toDelegate(index));
		} catch (org.geomajas.geometry.service.GeometryIndexNotFoundException e) {
			throw fromDelegate(e);
		}
	}

	/**
	 * Checks to see if a given index is the child of another index.
	 * 
	 * @param parentIndex The so-called parent index.
	 * @param childIndex The so-called child index.
	 * @return Is the second index really a child of the first index?
	 */
	public boolean isChildOf(GeometryIndex parentIndex, GeometryIndex childIndex) {
		return delegate.isChildOf(toDelegate(parentIndex), toDelegate(childIndex));
	}

	/**
	 * Returns the value of the innermost child index.
	 * 
	 * @param index The index to recursively search.
	 * @return The value of the deepest child.
	 */
	public int getValue(GeometryIndex index) {
		return delegate.getValue(toDelegate(index));
	}

	/**
	 * Returns a new index that is one level higher than this index. Useful to get the index of a ring for a vertex.
	 * 
	 * @param index
	 * @return the parent index
	 */
	public GeometryIndex getParent(GeometryIndex index) {
		return fromDelegate(delegate.getParent(toDelegate(index)));
	}

	// ------------------------------------------------------------------------
	// Methods concerning adjacency (finding ones neighbors):
	// ------------------------------------------------------------------------

	/**
	 * Given a certain geometry and index, find the neighboring vertices. It is important to understand that searching
	 * vertices within a closed ring will always return 2 vertices (unless the ring contains only 1 or 2 coordinates),
	 * while searching within a LineString can yield different results (the beginning or end only has 1 neighbor).
	 * 
	 * @param geometry The geometry wherein to search for neighboring vertices.
	 * @param index The index to start out from. Must point to either a vertex or and edge.
	 * @return The list of neighboring vertices.
	 * @throws GeometryIndexNotFoundException Thrown in case the given index does not match the given geometry.
	 */
	public List<GeometryIndex> getAdjacentVertices(Geometry geometry, GeometryIndex index)
			throws GeometryIndexNotFoundException {
		try {
			List<org.geomajas.geometry.service.GeometryIndex> delegateVertices = delegate.getAdjacentVertices(geometry,
					toDelegate(index));
			List<GeometryIndex> vertices = new ArrayList<GeometryIndex>();
			for (org.geomajas.geometry.service.GeometryIndex i : delegateVertices) {
				vertices.add(fromDelegate(i));
			}
			return vertices;
		} catch (org.geomajas.geometry.service.GeometryIndexNotFoundException e) {
			throw fromDelegate(e);
		}
	}

	/**
	 * Given a certain geometry and index, find the neighboring edges. It is important to understand that searching
	 * edges within a closed ring will always return 2 results (unless the ring contains only 1 or 2 coordinates), while
	 * searching within a LineString can yield different results (the beginning or end only has 1 neighbor).
	 * 
	 * @param geometry The geometry wherein to search for neighboring edges.
	 * @param index The index to start out from. Must point to either a vertex or and edge.
	 * @return The list of neighboring edges.
	 * @throws GeometryIndexNotFoundException Thrown in case the given index does not match the given geometry.
	 */
	public List<GeometryIndex> getAdjacentEdges(Geometry geometry, GeometryIndex index)
			throws GeometryIndexNotFoundException {
		try {
			List<org.geomajas.geometry.service.GeometryIndex> delegateEdges = delegate.getAdjacentEdges(geometry,
					toDelegate(index));
			List<GeometryIndex> edges = new ArrayList<GeometryIndex>();
			for (org.geomajas.geometry.service.GeometryIndex i : delegateEdges) {
				edges.add(fromDelegate(i));
			}
			return edges;
		} catch (org.geomajas.geometry.service.GeometryIndexNotFoundException e) {
			throw fromDelegate(e);
		}
	}

	/**
	 * Given a certain geometry and index (one), check if the the other index (two) is a neighbor.
	 * 
	 * @param geometry The geometry wherein to search if indices one and two are neighbors.
	 * @param one One of the indices. Must point to either a vertex or and edge.
	 * @param two Another one of the indices. Must point to either a vertex or and edge.
	 * @return true or false.
	 */
	public boolean isAdjacent(Geometry geometry, GeometryIndex one, GeometryIndex two) {
		return delegate.isAdjacent(geometry, toDelegate(one), toDelegate(two));
	}

	/**
	 * Given a certain index, find the next vertex in line.
	 * 
	 * @param index The index to start out from. Must point to either a vertex or and edge.
	 * @return Returns the next vertex index. Note that no geometry is given, and so no actual checking is done. It just
	 *         returns the theoretical answer.
	 */
	public GeometryIndex getNextVertex(GeometryIndex index) {
		return fromDelegate(delegate.getNextVertex(toDelegate(index)));
	}

	/**
	 * Given a certain index, find the previous vertex in line.
	 * 
	 * @param index The index to start out from. Must point to either a vertex or and edge.
	 * @return Returns the previous vertex index. Note that no geometry is given, and so no actual checking is done. It
	 *         just returns the theoretical answer.
	 */
	public GeometryIndex getPreviousVertex(GeometryIndex index) {
		return fromDelegate(delegate.getPreviousVertex(toDelegate(index)));
	}

	/**
	 * Given a certain index, how many indices of the same type can be found within the given geometry. This count
	 * includes the given index.<br>
	 * For example, if the index points to a vertex on a LinearRing within a polygon, then this will return the amount
	 * of vertices on that LinearRing.
	 * 
	 * @param geometry The geometry to look into.
	 * @param index The index to take as example (can be of any type).
	 * @return Returns the total amount of siblings.
	 */
	public int getSiblingCount(Geometry geometry, GeometryIndex index) {
		return delegate.getSiblingCount(geometry, toDelegate(index));
	}

	/**
	 * Get the full list of sibling vertices in the form of a coordinate array.
	 * 
	 * @param geometry The geometry wherein to search for a certain coordinate array.
	 * @param index An index pointing to a vertex or edge within the geometry. This index will then naturally be a part
	 *        of a coordinate array. It is this array we're looking for.
	 * @return Returns the array of coordinate from within the geometry where the given index is a part of.
	 * @throws GeometryIndexNotFoundException geometry index not found
	 */
	public Coordinate[] getSiblingVertices(Geometry geometry, GeometryIndex index)
			throws GeometryIndexNotFoundException {
		try {
			return delegate.getSiblingVertices(geometry, toDelegate(index));
		} catch (org.geomajas.geometry.service.GeometryIndexNotFoundException e) {
			throw fromDelegate(e);
		}
	}

	/**
	 * Get the index of the (smallest) linear ring of the geometry that contains this coordinate.
	 * 
	 * @param geometry
	 * @param c
	 * @return the index (empty array indicates no containment)
	 * @since 2.1.0
	 */
	public GeometryIndex getLinearRingIndex(Geometry geometry, Coordinate location) {
		return fromDelegate(GeometryService.getLinearRingIndex(geometry, location));
	}

	/**
	 * Validates a geometry, focusing on changes at a specific sub-level of the geometry. The sublevel is indicated by
	 * passing an index. The only checks are on intersection (for coordinates) and containment (for subgeometries), we
	 * don't check on too few coordinates as we want to support incremental creation of polygons.
	 * 
	 * @param geometry The geometry to check.
	 * @param index index that points to a sub-geometry, edge, vertex, etc...
	 * @return validation state.
	 * @since 2.1.0
	 */
	public GeometryValidationState validate(Geometry geometry, GeometryIndex index) {
		return GeometryService.validate(geometry, toDelegate(index));
	}

	// Conversion methods from/to the indexService of the geometry project
	private org.geomajas.geometry.service.GeometryIndex toDelegate(GeometryIndex index) {
		if (index == null) {
			return null;
		}
		org.geomajas.geometry.service.GeometryIndex result = delegate.create(toDelegate(index.getType()), index.getValue());
		GeometryIndex child = index.getChild();
		while(child != null) {
			result = delegate.addChildren(result, toDelegate(child.getType()), child.getValue());
			child = child.getChild();
		}
		return result;
	}

	private GeometryIndex fromDelegate(org.geomajas.geometry.service.GeometryIndex index) {
		if (index == null) {
			return null;
		}
		GeometryIndex result = new GeometryIndex(fromDelegate(index.getType()), index.getValue(),
				fromDelegate(index.getChild()));
		return result;
	}

	private org.geomajas.geometry.service.GeometryIndexType toDelegate(GeometryIndexType type) {
		return org.geomajas.geometry.service.GeometryIndexType.valueOf(type.name());
	}

	private GeometryIndexType fromDelegate(org.geomajas.geometry.service.GeometryIndexType type) {
		return GeometryIndexType.valueOf(type.name());
	}

	private GeometryIndexNotFoundException fromDelegate(org.geomajas.geometry.service.GeometryIndexNotFoundException e) {
		return new GeometryIndexNotFoundException(e.getMessage(), e.getCause());
	}

}