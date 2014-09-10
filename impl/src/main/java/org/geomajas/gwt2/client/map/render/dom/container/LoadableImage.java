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

import com.google.gwt.core.client.Callback;

import java.io.Serializable;

/**
 * Interface for an image that can be loaded.
 *
 * @param <K> The type returned on success of the callback.
 * @param <L> The type returned on failure of the callback.
 * @author Youri Flement
 */
public interface LoadableImage<K, L> extends HtmlObject, Serializable {

	/**
	 * Load the image with the given url and call {@code onLoadingDone} when the image is loaded.
	 *
	 * @param url           The url to the image.
	 * @param onLoadingDone Call-back to be executed when the image finished loading, or when an
	 *                      error occurs while loading.
	 * @param nrRetries     Total number of retries should loading fail.
	 */
	void load(String url, Callback<K, L> onLoadingDone, int nrRetries);

}
