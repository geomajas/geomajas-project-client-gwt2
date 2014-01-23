package org.geomajas.plugin.print.client.service;

import org.geomajas.plugin.print.client.template.PrintableLayerBuilder;
import org.geomajas.plugin.print.client.template.TemplateBuilder;

public interface PrintService {

	public void registerLayerBuilder(PrintableLayerBuilder builder);

	public void registerTemplateBuilder(String name, TemplateBuilder builder);
}
