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

package org.geomajas.plugin.print.client.template;

import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.plugin.print.command.dto.PrintTemplateInfo;

/**
 * Interface for the build pattern of  building a {@link PrintTemplateInfo}.
 * Contains setters for the necessary elements and a build method.
 * 
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public interface TemplateBuilder {

	PrintTemplateInfo buildTemplate();

	void setPageWidth(double pageWidth);

	void setPageHeight(double pageHeight);

	void setTitleText(String titleText);

	void setRasterDpi(int rasterDpi);

	void setWithScaleBar(boolean withScaleBar);

	void setWithArrow(boolean withArrow);

	void setMarginX(int marginX);

	void setMarginY(int marginY);

	void setMapPresenter(MapPresenter mapPresenter);

	void setApplicationId(String applicationId);
}