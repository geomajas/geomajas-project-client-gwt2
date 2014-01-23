package org.geomajas.plugin.print.client.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.plugin.print.client.template.PrintableLayerBuilder;
import org.geomajas.plugin.print.client.template.TemplateBuilder;

public class PrintServiceImpl implements PrintService {

	private List<PrintableLayerBuilder> layerBuilders = new ArrayList<PrintableLayerBuilder>();

	private Map<String, TemplateBuilder> templateBuilders = new HashMap<String, TemplateBuilder>();

	@Override
	public void registerLayerBuilder(PrintableLayerBuilder builder) {
		if (!layerBuilders.contains(builder)) {
			layerBuilders.add(builder);
		}
	}

	@Override
	public void registerTemplateBuilder(String name, TemplateBuilder builder) {
		templateBuilders.put(name, builder);
	}

}
