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
package org.geomajas.gwt2.client.map.render.canvas.container;

import org.geomajas.gwt.client.util.Dom;
import org.geomajas.gwt2.client.map.render.TileCode;

import com.google.gwt.core.client.Callback;
import com.google.gwt.dom.client.ImageElement;

/**
 * Image tile for a single {@link TileCode}. Needed because images must be pre-loaded for canvas !
 * 
 * @author Jan De Moerloose
 * 
 */
public class CanvasTile {

	private Callback<String, String> onLoadingDone;

	private ImageElement img;

	private boolean loaded;

	private boolean rendered;

	private TileCode tileCode;

	/**
	 * Preload image with the specified src, bounding box and callback.
	 * 
	 * @param src
	 * @param bbox
	 * @param onLoadingDone
	 * @param resolution
	 */
	public CanvasTile(String src, TileCode tileCode, Callback<String, String> onLoadingDone) {
		this.tileCode = tileCode;
		this.onLoadingDone = onLoadingDone;
		img = loadImage(Dom.makeUrlAbsolute(src));
	}

	public ImageElement getImageElement() {
		return img;
	}

	public TileCode getTileCode() {
		return tileCode;
	}

	public void onLoadingDone() {
		onLoadingDone.onSuccess("");
		loaded = true;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public boolean isRendered() {
		return rendered;
	}

	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}

	public Callback<String, String> getCallback() {
		return onLoadingDone;
	}

	/**
	 * Returns a handle to an img object. Ties back to the ImageLoader instance (see Google incubator)
	 */
	private native ImageElement loadImage(String url)/*-{	     
		var img = new Image();
		var __this = this;
		
		img.onload = function() {
		if(!img.__isLoaded) {
		
		// __isLoaded should be set for the first time here.
		// if for some reason img fires a second onload event
		// we do not want to execute the following again (hence the guard)
		img.__isLoaded = true;       
		img.onload = null;
		
		// we call this function when onload fires
		__this.@org.geomajas.gwt2.client.map.render.canvas.container.CanvasTile::onLoadingDone()();   
		} 
		}
		
		img.src = url;
		
		return img;
		}-*/;

}
