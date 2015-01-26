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

package org.geomajas.gwt2.plugin.print.client.template;

import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.plugin.printing.command.dto.PrintTemplateInfo;

/**
 * Interface for the build pattern of building a {@link PrintTemplateInfo}. Contains setters for the necessary elements
 * and a build method.
 * 
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public interface TemplateBuilder {

	/**
	 * Build the template.
	 * 
	 * @return
	 */
	PrintTemplateInfo buildTemplate();

	/**
	 * Set the page width (unit is dots, dpi is 72).
	 * 
	 * @param pageWidth
	 */
	void setPageWidth(double pageWidth);

	/**
	 * Set the page height (unit is dots, dpi is 72).
	 * 
	 * @param pageWidth
	 */
	void setPageHeight(double pageHeight);

	/**
	 * Set the title.
	 * 
	 * @param titleText
	 */
	void setTitleText(String titleText);

	/**
	 * Set the overall dpi of the image when converting print to image format.
	 * 
	 * @param rasterDpi
	 */
	void setDpi(int rasterDpi);

	/**
	 * Set the dpi of the embedded tiles in the pdf.
	 * 
	 * @param rasterDpi
	 */
	void setRasterDpi(int rasterDpi);

	/**
	 * Add a scalebar to the print.
	 * 
	 * @param withScaleBar
	 */
	void setWithScaleBar(boolean withScaleBar);

	/**
	 * Add an arrow to the print.
	 * 
	 * @param withScaleBar
	 */
	void setWithArrow(boolean withArrow);

	/**
	 * Set the page margin in the x-direction (left,right).
	 * 
	 * @param marginX
	 */
	void setMarginX(int marginX);

	/**
	 * Set the page margin in the y-direction (top,bottom).
	 * 
	 * @param marginX
	 */
	void setMarginY(int marginY);

	/**
	 * Set the map presenter.
	 * 
	 * @param mapPresenter
	 */
	void setMapPresenter(MapPresenter mapPresenter);

	/**
	 * Set the application id.
	 * 
	 * @param applicationId
	 */
	void setApplicationId(String applicationId);
}