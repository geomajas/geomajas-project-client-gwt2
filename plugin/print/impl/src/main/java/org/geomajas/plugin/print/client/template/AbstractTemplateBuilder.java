/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.print.client.template;

import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.plugin.print.client.util.PrintLayout;
import org.geomajas.plugin.print.command.dto.PrintTemplateInfo;
import org.geomajas.plugin.print.component.dto.ImageComponentInfo;
import org.geomajas.plugin.print.component.dto.LabelComponentInfo;
import org.geomajas.plugin.print.component.dto.LayoutConstraintInfo;
import org.geomajas.plugin.print.component.dto.LegendComponentInfo;
import org.geomajas.plugin.print.component.dto.MapComponentInfo;
import org.geomajas.plugin.print.component.dto.PageComponentInfo;
import org.geomajas.plugin.print.component.dto.ScaleBarComponentInfo;

/**
 * Builder pattern for templates.
 * 
 * @author Jan De Moerloose
 */
public abstract class AbstractTemplateBuilder implements TemplateBuilder {
	
	protected PrintConfiguration printConfiguration;

	public PrintTemplateInfo buildTemplate(PrintConfiguration printConfiguration) {
		this.printConfiguration = printConfiguration;
		PrintTemplateInfo template = new PrintTemplateInfo();
		template.setPage(buildPage());
		return template;
	}

	protected PageComponentInfo buildPage() {
		PageComponentInfo page = new PageComponentInfo();
		page.addChild(buildMap());
		page.addChild(buildTitle());
		page.setTag("page");
		return page;
	}

	protected MapComponentInfo buildMap() {
		return buildMap(null);
	}

	protected MapComponentInfo buildMap(Bbox bounds) {
		MapComponentInfo map = new MapComponentInfo();
		if (PrintLayout.templateIncludeScaleBar) {
			map.addChild(buildScaleBar());
		}
		if (PrintLayout.templateIncludeLegend) {
			LegendComponentInfo legend;
			if (null == bounds) {
				legend = buildLegend();
			} else {
				legend = buildLegend(bounds);
			}
			map.addChild(legend);
		}
		if (PrintLayout.templateIncludeNorthArrow) {
			map.addChild(buildArrow());
		}
		return map;
	}

	protected ImageComponentInfo buildArrow() {
		return new ImageComponentInfo();
	}

	protected LegendComponentInfo buildLegend() {
		return new LegendComponentInfo();
	}

	protected LegendComponentInfo buildLegend(Bbox bounds) {
		return new LegendComponentInfo();
	}

	protected ScaleBarComponentInfo buildScaleBar() {
		return new ScaleBarComponentInfo();
	}

	protected LabelComponentInfo buildTitle() {
		LabelComponentInfo label = new LabelComponentInfo();
		FontStyleInfo style = new FontStyleInfo();
		style.setFamily(PrintLayout.templateDefaultFontFamily);
		style.setStyle(PrintLayout.templateDefaultFontStyle);
		style.setSize((int) PrintLayout.templateDefaultFontSize);
		label.setFont(style);
		label.setBackgroundColor(PrintLayout.templateDefaultBackgroundColor);
		label.setBorderColor(PrintLayout.templateDefaultBorderColor);
		label.setFontColor(PrintLayout.templateDefaultColor);
		label.getLayoutConstraint().setAlignmentY(LayoutConstraintInfo.TOP);
		label.getLayoutConstraint().setAlignmentX(LayoutConstraintInfo.CENTER);
		label.setTag("title");
		return label;
	}
}