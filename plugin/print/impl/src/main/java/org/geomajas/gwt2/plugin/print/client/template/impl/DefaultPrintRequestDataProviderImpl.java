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
package org.geomajas.gwt2.plugin.print.client.template.impl;

import com.google.gwt.core.client.GWT;
import org.geomajas.gwt2.plugin.print.client.i18n.PrintMessages;
import org.geomajas.gwt2.plugin.print.client.template.DefaultPrintRequestDataProvider;
import org.geomajas.gwt2.plugin.print.client.template.PageSize;
import org.geomajas.gwt2.plugin.print.client.template.TemplateBuilderDataProvider;
import org.geomajas.gwt2.plugin.print.client.util.PrintConfiguration;

/**
 * Default implementation of {@link DefaultPrintRequestDataProvider}.
 * It is a DTO-like container for the default values.
 *
 * @author Jan Venstermans
 */
public final class DefaultPrintRequestDataProviderImpl implements DefaultPrintRequestDataProvider {

	private static final PrintMessages MESSAGES = GWT.create(PrintMessages.class);

	private DefaultTemplateBuilderDataProvider defaultTemplateBuilderDataProvider
			= new DefaultTemplateBuilderDataProviderImpl();
	private PrintConfiguration.PostPrintAction defaultPostPrintAction = PrintConfiguration.PostPrintAction.OPEN;
	private String defaultFileName = MESSAGES.defaultPrintFileName();
	private boolean defaultSync;
	private int defaultDpi;

	/* getters */
	@Override
	public TemplateBuilderDataProvider getTemplateBuilderDataProvider() {
		return defaultTemplateBuilderDataProvider;
	}

	@Override
	public DefaultTemplateBuilderDataProvider getDefaultTemplateBuilderDataProvider() {
		return defaultTemplateBuilderDataProvider;
	}

	@Override
	public PrintConfiguration.PostPrintAction getPostPrintAction() {
		return defaultPostPrintAction;
	}

	@Override
	public String getFileName() {
		return defaultFileName;
	}
	
	@Override
	public boolean isSync() {
		return defaultSync;
	}

	@Override
	public int getDpi() {
		return defaultDpi;
	}
	
	

	/* setters */


	@Override
	public void setFileName(String fileName) {
		this.defaultFileName = fileName;
	}

	@Override
	public void setPostPrintAction(PrintConfiguration.PostPrintAction postPrintAction) {
		this.defaultPostPrintAction = postPrintAction;
	}
	
	@Override
	public void setSync(boolean sync) {
		this.defaultSync = sync;
	}

	@Override
	public void setDpi(int dpi) {
		this.defaultDpi = dpi;
	}

	/**
	 * Private default implementation of {@link TemplateBuilderDataProvider}.
	 */
	public class DefaultTemplateBuilderDataProviderImpl implements DefaultTemplateBuilderDataProvider {

		private String defaultTitle = MESSAGES.defaultPrintTitle();
		private PageSize defaultPageSize = PageSize.A4;
		private int defaultDpi = 144;
		private int defaultRasterDpi = 72;
		private boolean defaultLandscape = true;
		private boolean defaultWithArrow = true;
		private boolean defaultWithScaleBar = true;

		@Override
		public String getTitle() {
			return defaultTitle;
		}

		@Override
		public void setTitle(String title) {
			this.defaultTitle = title;
		}

		@Override
		public PageSize getPageSize() {
			return defaultPageSize;
		}

		@Override
		public void setPageSize(PageSize pageSize) {
			this.defaultPageSize = pageSize;
		}
		
		@Override
		public void setDpi(int dpi) {
			this.defaultDpi = dpi;
		}		

		@Override
		public int getDpi() {
			return defaultDpi;
		}

		@Override
		public int getRasterDpi() {
			return defaultRasterDpi;
		}

		@Override
		public void setRasterDpi(int rasterDpi) {
			this.defaultRasterDpi = rasterDpi;
		}

		@Override
		public boolean isLandscape() {
			return defaultLandscape;
		}

		@Override
		public void setLandscape(boolean landscape) {
			this.defaultLandscape = landscape;
		}

		@Override
		public boolean isWithArrow() {
			return defaultWithArrow;
		}

		@Override
		public void setWithArrow(boolean withArrow) {
			this.defaultWithArrow = withArrow;
		}

		@Override
		public boolean isWithScaleBar() {
			return defaultWithScaleBar;
		}

		@Override
		public void setWithScaleBar(boolean withScaleBar) {
			this.defaultWithScaleBar = withScaleBar;
		}		
		
	}
}
