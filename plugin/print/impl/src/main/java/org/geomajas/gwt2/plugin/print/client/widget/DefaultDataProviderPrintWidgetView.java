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
package org.geomajas.gwt2.plugin.print.client.widget;

import com.google.gwt.core.client.GWT;
import org.geomajas.gwt2.plugin.print.client.template.PageSize;
import org.geomajas.gwt2.plugin.print.client.util.PrintConfiguration;
import org.geomajas.gwt2.plugin.print.client.i18n.PrintMessages;
import org.geomajas.gwt2.plugin.print.client.template.DefaultPrintRequestDataProvider;
import org.geomajas.gwt2.plugin.print.client.template.impl.DefaultPrintRequestDataProviderImpl;
import org.geomajas.gwt2.plugin.print.client.template.TemplateBuilderDataProvider;

/**
 * Default abstract implementation of {@link PrintWidgetView}.
 * This class contains an instance of
 * {@link org.geomajas.gwt2.plugin.print.client.template.impl.DefaultPrintRequestDataProviderImpl},
 * that contains the default values.
 * An extension of this class may overwrite the {@link TemplateBuilderDataProvider} getter methods to prohibit
 * returning the default values.
 *
 * @author Jan Venstermans
 */
public abstract class DefaultDataProviderPrintWidgetView implements PrintWidgetView, TemplateBuilderDataProvider {

	protected final PrintWidgetResource resource;

	protected static final PrintMessages MESSAGES = GWT.create(PrintMessages.class);

	/**
	 * contains the default values.
	 */
	private DefaultPrintRequestDataProvider defaultPrintRequestDataProvider = new DefaultPrintRequestDataProviderImpl();

	protected PrintWidgetPresenter handler;

	/** Default constructor. Create an instance using the default resource bundle and layout. */
	public DefaultDataProviderPrintWidgetView() {
		this((PrintWidgetResource) GWT.create(PrintWidgetResource.class));
	}


	/**
	 * Create a new instance using a custom resource bundle.
	 *
	 * @param resource The custom resource bundle to use.
	 */
	public DefaultDataProviderPrintWidgetView(PrintWidgetResource resource) {
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
	public PrintConfiguration.PostPrintAction getPostPrintAction() {
		return defaultPrintRequestDataProvider.getPostPrintAction();
	}

	@Override
	public boolean isSync() {
		return defaultPrintRequestDataProvider.isSync();
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
	public int getRasterDpi() {
		return defaultPrintRequestDataProvider.getTemplateBuilderDataProvider().getRasterDpi();
	}

	@Override
	public int getDpi() {
		return defaultPrintRequestDataProvider.getTemplateBuilderDataProvider().getDpi();
	}


	@Override
	public String getTitle() {
		return MESSAGES.defaultPrintTitle();
	}
}
