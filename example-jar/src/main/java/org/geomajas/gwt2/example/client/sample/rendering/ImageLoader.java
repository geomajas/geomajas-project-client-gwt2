/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.gwt2.example.client.sample.rendering;

import com.google.gwt.core.client.Callback;
import com.google.gwt.dom.client.ImageElement;
import org.geomajas.gwt.client.util.Dom;

/**
 * Class to enable downloading an external image from a url. A callback can be used to
 * execute code when image has been (down)loaded.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 *
 */
public class ImageLoader {

	private Callback<ImageElement, String> onLoadingDone;

	private ImageElement img;

	private boolean loaded;

	/**
	 * Load image with the specified url and callback.
	 *
	 * @param url
	 * @param onLoadingDone
	 */
	public ImageLoader(String url, Callback<ImageElement, String> onLoadingDone) {
		this.onLoadingDone = onLoadingDone;
		img = loadImage(Dom.makeUrlAbsolute(url));
	}

	public ImageElement getImageElement() {
		return img;
	}

	public void onLoadingDone() {
		onLoadingDone.onSuccess(img);
		loaded = true;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public Callback<ImageElement, String> getCallback() {
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
				__this.@org.geomajas.gwt2.example.client.sample.rendering.ImageLoader::onLoadingDone()();
			}
		}

		img.src = url;

		return img;
	}-*/;

}
