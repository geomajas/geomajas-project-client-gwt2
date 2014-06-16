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

import com.google.gwt.core.client.GWT;
import org.geomajas.plugin.print.client.i18n.PrintMessages;
import org.geomajas.plugin.print.client.util.PrintSettings;

/**
 * Default implementation of {@link PrintRequestDataProvider}.
 * It is a container for the default values.
 *
 * @author Jan Venstermans
 */
public class DefaultPrintRequestDataProvider implements PrintRequestDataProvider {

	private static final PrintMessages MESSAGES = GWT.create(PrintMessages.class);

	private DefaultTemplateBuilderDataProvider defaultTemplateBuilderDataProvider
			= new DefaultTemplateBuilderDataProvider();
	private PrintSettings.PostPrintAction defaultPostPrintAction = PrintSettings.PostPrintAction.OPEN;
	private String defaultFileName = MESSAGES.defaultPrintFileName();

	/* getters */
	@Override
	public TemplateBuilderDataProvider getTemplateBuilderDataProvider() {
		return defaultTemplateBuilderDataProvider;
	}

	public DefaultTemplateBuilderDataProvider getDefaultTemplateBuilderDataProvider() {
		return defaultTemplateBuilderDataProvider;
	}

	@Override
	public PrintSettings.PostPrintAction getPostPrintAction() {
		return defaultPostPrintAction;
	}

	@Override
	public String getFileName() {
		return defaultFileName;
	}

	/* setters */

	public void setFileName(String fileName) {
		this.defaultFileName = fileName;
	}

	public void setPostPrintAction(PrintSettings.PostPrintAction postPrintAction) {
		this.defaultPostPrintAction = postPrintAction;
	}

	/**
	 * Private default implementation of {@link TemplateBuilderDataProvider}.
	 */
	public class DefaultTemplateBuilderDataProvider implements TemplateBuilderDataProvider {

		private String defaultTitle = MESSAGES.defaultPrintTitle();
		private PageSize defaultPageSize = PageSize.A4;
		private int defaultRasterDpi = 200;
		private boolean defaultLandscape = true;
		private boolean defaultWithArrow = true;
		private boolean defaultWithScaleBar = true;

		@Override
		public String getTitle() {
			return defaultTitle;
		}

		public void setTitle(String title) {
			this.defaultTitle = title;
		}

		@Override
		public PageSize getPageSize() {
			return defaultPageSize;
		}

		public void setPageSize(PageSize pageSize) {
			this.defaultPageSize = pageSize;
		}

		@Override
		public Integer getRasterDpi() {
			return defaultRasterDpi;
		}

		public void setRasterDpi(int rasterDpi) {
			this.defaultRasterDpi = rasterDpi;
		}

		@Override
		public boolean isLandscape() {
			return defaultLandscape;
		}

		public void setLandscape(boolean landscape) {
			this.defaultLandscape = landscape;
		}

		@Override
		public boolean isWithArrow() {
			return defaultWithArrow;
		}

		public void setWithArrow(boolean withArrow) {
			this.defaultWithArrow = withArrow;
		}

		@Override
		public boolean isWithScaleBar() {
			return defaultWithScaleBar;
		}

		public void setWithScaleBar(boolean withScaleBar) {
			this.defaultWithScaleBar = withScaleBar;
		}
	}
}
