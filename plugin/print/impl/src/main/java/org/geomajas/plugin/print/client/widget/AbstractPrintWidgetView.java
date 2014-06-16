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
package org.geomajas.plugin.print.client.widget;

import com.google.gwt.core.client.GWT;
import org.geomajas.plugin.print.client.i18n.PrintMessages;
import org.geomajas.plugin.print.client.template.DefaultPrintRequestDataProvider;
import org.geomajas.plugin.print.client.template.PageSize;
import org.geomajas.plugin.print.client.template.TemplateBuilderDataProvider;
import org.geomajas.plugin.print.client.util.PrintSettings;

/**
 * Default abstract implementation of {@link org.geomajas.plugin.print.client.widget.PrintWidgetView}.
 *
 * @author Jan Venstermans
 */
public abstract class AbstractPrintWidgetView implements PrintWidgetView, TemplateBuilderDataProvider {

	protected final PrintWidgetResource resource;

	protected static final PrintMessages MESSAGES = GWT.create(PrintMessages.class);

	/**
	 * contains the default values.
	 */
	private DefaultPrintRequestDataProvider defaultPrintRequestDataProvider = new DefaultPrintRequestDataProvider();

	protected PrintWidgetPresenter handler;

	/**
	 * Create a new instance using a custom resource bundle.
	 *
	 * @param resource The custom resource bundle to use.
	 */
	public AbstractPrintWidgetView(PrintWidgetResource resource) {
		this.resource = resource;

		// Inject the CSS and create the GUI:
		this.resource.css().ensureInjected();
	}

	@Override
	public void setHandler(PrintWidgetPresenter handler) {
		this.handler = handler;
	}

	@Override
	public TemplateBuilderDataProvider getTemplateBuilderDataProvider() {
		return this;
	}

	@Override
	public DefaultPrintRequestDataProvider getDefaultPrintRequestDataProvider() {
		return defaultPrintRequestDataProvider;
	}

	@Override
	public PrintSettings.PostPrintAction getPostPrintAction() {
		return defaultPrintRequestDataProvider.getPostPrintAction();
	}

	@Override
	public String getFileName() {
		return defaultPrintRequestDataProvider.getFileName();
	}

	@Override
	public boolean isLandscape() {
		return defaultPrintRequestDataProvider.getTemplateBuilderDataProvider().isLandscape();
	}

	@Override
	public PageSize getPageSize() {
		return defaultPrintRequestDataProvider.getTemplateBuilderDataProvider().getPageSize();
	}

	@Override
	public boolean isWithArrow() {
		return defaultPrintRequestDataProvider.getTemplateBuilderDataProvider().isWithArrow();
	}

	@Override
	public boolean isWithScaleBar() {
		return defaultPrintRequestDataProvider.getTemplateBuilderDataProvider().isWithScaleBar();
	}

	@Override
	public Integer getRasterDpi() {
		return defaultPrintRequestDataProvider.getTemplateBuilderDataProvider().getRasterDpi();
	}

	@Override
	public String getTitle() {
		return MESSAGES.defaultPrintTitle();
	}
}
