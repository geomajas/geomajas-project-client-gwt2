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

package org.geomajas.gwt2.plugin.print.client;

import org.geomajas.gwt2.plugin.print.client.template.PageSize;
import org.geomajas.gwt2.plugin.print.client.template.TemplateBuilderDataProvider;

/**
 * Test class for {@link org.geomajas.gwt2.plugin.print.client.widget.PrintWidgetPresenterImpl}.
 *
 * @author Jan Venstermans
 */
public class ViewMockData implements TemplateBuilderDataProvider {
	private String applicationId;
	private String title;
	private PageSize pageSize;
	private int rasterDpi;
	private int dpi;
	private boolean landscape;
	private boolean withArrow;
	private boolean withScaleBar;

	public void resetData() {
		applicationId = "applicationId";
		title = "title";
		pageSize = PageSize.A2;
		rasterDpi = 123;
		landscape = true;
		withArrow = true;
		withScaleBar = true;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public PageSize getPageSize() {
		return pageSize;
	}

	public void setPageSize(PageSize pageSize) {
		this.pageSize = pageSize;
	}

	public int getDpi() {
		return dpi;
	}

	public void setDpi(int dpi) {
		this.dpi = dpi;
	}

	public int getRasterDpi() {
		return rasterDpi;
	}

	public void setRasterDpi(int rasterDpi) {
		this.rasterDpi = rasterDpi;
	}

	@Override
	public String getTitle() {
		return title;
	}

	public boolean isLandscape() {
		return landscape;
	}

	public void setLandscape(boolean landscape) {
		this.landscape = landscape;
	}

	public boolean isWithArrow() {
		return withArrow;
	}

	public void setWithArrow(boolean withArrow) {
		this.withArrow = withArrow;
	}

	public boolean isWithScaleBar() {
		return withScaleBar;
	}

	public void setWithScaleBar(boolean withScaleBar) {
		this.withScaleBar = withScaleBar;
	}
}