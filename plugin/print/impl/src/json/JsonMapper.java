package org.geomajas.gwt2.plugin.print.client.json;

import org.geomajas.plugin.printing.command.dto.PrintTemplateInfo;

import com.google.gwt.core.shared.GWT;

public class JsonMapper {

	private static JsonPrintTemplateInfoMapper printTemplateInfoMapper = GWT.create(JsonPrintTemplateInfoMapper.class);

	private JsonMapper() {

	}

	public static String toJson(PrintTemplateInfo printTemplate) {
		return printTemplateInfoMapper.write(printTemplate);
	}
}
