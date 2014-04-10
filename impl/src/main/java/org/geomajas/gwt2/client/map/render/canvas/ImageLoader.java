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

package org.geomajas.gwt2.client.map.render.canvas;

import org.geomajas.geometry.Bbox;
import org.geomajas.gwt.client.util.Dom;

import com.google.gwt.core.client.Callback;
import com.google.gwt.dom.client.ImageElement;

/**
 * Preloads an image. The image is not added to the DOM (this does not work with regular GWT Image class !!!).
 * 
 * @author Jan De Moerloose
 * 
 */
public class ImageLoader {

	private Bbox bbox;

	private double resolution;

	private Callback<String, String> onLoadingDone;

	private ImageElement img;

	private boolean loaded;

	/**
	 * Preload image with the specified src, bounding box and callback.
	 * 
	 * @param src
	 * @param bbox
	 * @param onLoadingDone
	 * @param resolution
	 */
	public ImageLoader(String src, Bbox bbox, Callback<String, String> onLoadingDone, double resolution) {
		this.bbox = bbox;
		this.resolution = resolution;
		this.onLoadingDone = onLoadingDone;
		img = loadImage(Dom.makeUrlAbsolute(src));
	}

	public Bbox getBbox() {
		return bbox;
	}

	public double getResolution() {
		return resolution;
	}

	public ImageElement getImageElement() {
		return img;
	}

	public void onLoadingDone() {
		onLoadingDone.onSuccess("");
		loaded = true;
	}

	public boolean isLoaded() {
		return loaded;
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
		__this.@org.geomajas.gwt2.client.map.render.canvas.ImageLoader::onLoadingDone()();   
		} 
		}
		
		img.src = url;
		
		return img;
	}-*/;

}
