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

package org.geomajas.gwt2.client.map.render.dom.container;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.util.Dom;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * <p>
 * Container for {@link AbstractHtmlObject}s. This class is an extension of an {@link AbstractHtmlObject} that in turn
 * is used to store a list of child {@link AbstractHtmlObject}s. In other words instances of this class represent the
 * nodes in an HTML tree structure.
 * </p>
 * <p>
 * The main goal for this class is to provide a container for child {@link AbstractHtmlObject}s. Through it's methods
 * these children can be managed. Note though that this class is itself also an {@link AbstractHtmlObject}, which means
 * that this class too has a fixed size and position. Child positions are always relative to this class' position.
 * </p>
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public class HtmlGroup extends AbstractHtmlObject implements HtmlContainer {

	private Coordinate origin;

	private double scale = 1.0;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create an HTML container widget that represents a DIV element. When using this constructor, width and height are
	 * set to 100%. We always set the width and height, because XHTML does not like DIV's without sizing attributes.
	 */
	public HtmlGroup() {
		super(new DivPanel());
		asWidget().setSize("100%", "100%");
		asWidget().getElement().getStyle().setZIndex(0);
		String transition = "opacity .7s";
		asWidget().getElement().getStyle().setProperty("transition", transition);
		asWidget().getElement().getStyle().setProperty("MozTransition", transition);
		asWidget().getElement().getStyle().setProperty("WebkitTransition", transition);
	}

	/**
	 * Create an HTML container widget that represents a DIV element.
	 * 
	 * @param width
	 *            The width for this container, expressed in pixels.
	 * @param height
	 *            The height for this container, expressed in pixels.
	 */
	public HtmlGroup(int width, int height) {
		super(new DivPanel(), width, height);
	}

	/**
	 * Create an HTML container widget that represents a DIV element.
	 * 
	 * @param width
	 *            The width for this container, expressed in pixels.
	 * @param height
	 *            The height for this container, expressed in pixels.
	 * @param top
	 *            How many pixels should this container be placed from the top (relative to the parent origin).
	 * @param left
	 *            How many pixels should this container be placed from the left (relative to the parent origin).
	 */
	public HtmlGroup(int width, int height, int top, int left) {
		super(new DivPanel(), width, height, top, left);
	}

	// ------------------------------------------------------------------------
	// HtmlContainer implementation:
	// ------------------------------------------------------------------------

	/**
	 * Add a new child HtmlObject to the list. Note that using this method children are added to the back of the list,
	 * which means that they are added on top of the visibility stack and thus may obscure other children. Take this
	 * order into account when adding children.
	 * 
	 * @param child
	 *            The actual child to add.
	 */
	public void add(HtmlObject child) {
		asDivPanel().add(child);
		if (child instanceof AbstractHtmlObject) {
			((AbstractHtmlObject) child).setParent(this);
		}
		child.setLeft(child.getLeft());
		child.setTop(child.getTop());
	}

	/**
	 * Insert a new child HtmlObject in the list at a certain index. The position will determine it's place in the
	 * visibility stack, where the first element lies at the bottom and the last element on top.
	 * 
	 * @param child
	 *            The actual child to add.
	 * @param beforeIndex
	 *            The position in the list where this child should end up.
	 */
	public void insert(HtmlObject child, int beforeIndex) {
		asDivPanel().insertBefore(child, beforeIndex);
		if (child instanceof AbstractHtmlObject) {
			((AbstractHtmlObject) child).setParent(this);
		}
		child.setLeft(child.getLeft());
		child.setTop(child.getTop());
	}

	/**
	 * Remove a child element from the list.
	 * 
	 * @param child
	 *            The actual child to remove.
	 * @return Returns true or false indicating whether or not removal was successful.
	 */
	public boolean remove(HtmlObject child) {
		if (child instanceof AbstractHtmlObject) {
			((AbstractHtmlObject) child).setParent(null);
		}
		return asDivPanel().remove(child);
	}

	/**
	 * Bring a certain child to the front. In reality this child is moved to the back of the list.
	 * 
	 * @param child
	 *            The child to bring to the front.
	 */
	public void bringToFront(HtmlObject child) {
		asDivPanel().bringToFront(child);
	}

	/** Remove all children from this container. */
	public void clear() {
		asDivPanel().clear();
	}

	/**
	 * Get the total number of children in this container.
	 * 
	 * @return The total number of children in this container.
	 */
	public int getChildCount() {
		return asDivPanel().getChildCount();
	}

	/**
	 * Get the child at a certain index.
	 * 
	 * @param index
	 *            The index to look for a child.
	 * @return The actual child if it was found.
	 */
	public HtmlObject getChild(int index) {
		return (HtmlObject) (asDivPanel().getChild(index));
	}

	@Override
	public void applyScale(double scale, int x, int y) {
		this.scale = scale;

		if (!Dom.isTransformationSupported()) {
			// Best effort to make animations work in IE8...
			if (scale == 1.0) {
				asWidget().getElement().getStyle().clearProperty("filter");
				asWidget().setSize("100px", "100px");
			} else {
				asWidget().setSize("100%", "100%");
				String filter = "progid:DXImageTransform.Microsoft.Matrix(M11=" + scale + ", M12=0, M21=0, M22="
						+ scale + ", SizingMethod='auto expand')";
				Dom.setStyleAttribute(asWidget().getElement(), "filter", filter);
			}
		} else {
			// Normal browsers:
			Dom.setTransform(asWidget().getElement(), "scale(" + scale + ")");
			Dom.setTransformOrigin(asWidget().getElement(), x + "px " + y + "px");
		}
	}

	@Override
	public double getScale() {
		return scale;
	}

	public void setHeight(String height) {
		asWidget().setHeight(height);
	}

	public void setWidth(String width) {
		asWidget().setWidth(width);
	}

	public void setSize(String width, String height) {
		asWidget().setSize(width, height);
	}

	public void setPixelSize(int width, int height) {
		asWidget().setPixelSize(width, height);
	}

	@Override
	public Coordinate getOrigin() {
		return origin;
	}

	@Override
	public void setOrigin(Coordinate origin) {
		this.origin = origin;
	}

	protected DivPanel asDivPanel() {
		return (DivPanel) asWidget();
	}

	/**
	 * We need a special panel to keep track of the {@link IsWidget} children.
	 * 
	 * @author Jan De Moerloose
	 */
	public static class DivPanel extends Panel {

		private List<IsWidget> children = new ArrayList<IsWidget>();

		public DivPanel() {
			Element element = DOM.createElement("div");
			setElement(element);
		}

		@Override
		public void clear() {
			super.clear();
			while (children.size() > 0) {
				remove(children.get(children.size() - 1));
			}
		}

		public int getChildCount() {
			return children.size();
		}

		public IsWidget getChild(int index) {
			if (index < children.size()) {
				return children.get(index);
			}
			return null;
		}

		public void insertBefore(IsWidget child, int beforeIndex) {
			if (beforeIndex >= children.size()) {
				add(child);
				return;
			}
			getElement().appendChild(child.asWidget().getElement());

			List<IsWidget> newChildList = new ArrayList<IsWidget>();
			for (int i = 0; i < children.size(); i++) {
				if (i == beforeIndex) {
					newChildList.add(child);
				}
				newChildList.add(children.get(i));
			}
			children = newChildList;
			adopt(child.asWidget());
			setZIndeces();
		}

		@Override
		public Iterator<Widget> iterator() {
			List<Widget> widgets = new ArrayList<Widget>();
			for (IsWidget child : children) {
				widgets.add(child.asWidget());
			}
			return widgets.iterator();
		}

		public void add(IsWidget child) {
			children.add(child);
			getElement().appendChild(child.asWidget().getElement());
			adopt(child.asWidget());
			setZIndeces();
		}

		public void add(Widget child) {
			getElement().appendChild(child.getElement());
			children.add(child);
			adopt(child);
			setZIndeces();
		}

		@Override
		public boolean remove(IsWidget child) {
			int index = getIndex(child);
			if (index >= 0) {
				orphan(child.asWidget());
				getElement().removeChild(child.asWidget().getElement());
				children.remove(index);
				return true;
			}
			return false;
		}

		@Override
		public boolean remove(Widget child) {
			throw new UnsupportedOperationException("Use remove(IsWidget) instead");
		}

		public void bringToFront(IsWidget child) {
			if (children.remove(child)) {
				children.add(child);
			}
			setZIndeces();
		}

		private int getIndex(IsWidget child) {
			for (int i = 0; i < children.size(); i++) {
				IsWidget img = children.get(i);
				if (img.equals(child)) {
					return i;
				}
			}
			return -1;
		}

		private void setZIndeces() {
			for (int i = 0; i < children.size(); i++) {
				IsWidget object = children.get(i);
				object.asWidget().getElement().getStyle().setZIndex(i);
			}
		}
	}
}