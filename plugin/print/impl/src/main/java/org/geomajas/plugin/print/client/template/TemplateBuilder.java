package org.geomajas.plugin.print.client.template;

import org.geomajas.plugin.print.command.dto.PrintTemplateInfo;

public interface TemplateBuilder {

	PrintTemplateInfo buildTemplate(PrintConfiguration configuration);
}
