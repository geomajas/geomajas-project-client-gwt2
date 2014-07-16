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
package org.geomajas.gwt2.plugin.print.document;

import java.io.FileOutputStream;
import java.util.List;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.gwt2.plugin.print.component.LegendComponent;
import org.geomajas.gwt2.plugin.print.component.dto.LabelComponentInfo;
import org.geomajas.gwt2.plugin.print.component.dto.LegendComponentInfo;
import org.geomajas.gwt2.plugin.print.component.dto.LegendIconComponentInfo;
import org.geomajas.gwt2.plugin.print.component.dto.LegendItemComponentInfo;
import org.geomajas.gwt2.plugin.print.component.impl.PageComponentImpl;
import org.geomajas.gwt2.plugin.print.component.service.PrintConfigurationService;
import org.geomajas.gwt2.plugin.print.component.service.PrintDtoConverterService;
import org.geomajas.gwt2.plugin.print.configuration.DefaultConfigurationVisitor;
import org.geomajas.gwt2.plugin.print.configuration.MapConfigurationVisitor;
import org.geomajas.gwt2.plugin.print.configuration.PrintTemplate;
import org.geomajas.gwt2.plugin.print.document.Document.Format;
import org.geomajas.gwt2.plugin.print.service.PrintService;
import org.geomajas.security.SecurityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Coordinate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/gwt2/plugin/print/print.xml", "/org/geomajas/testdata/layerBluemarble.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/simplemixedContext.xml" })
@Transactional(rollbackFor = { org.geomajas.global.GeomajasException.class })
public class DefaultDocumentTest {

	@Autowired
	private PrintConfigurationService configurationService;

	@Autowired
	private PrintDtoConverterService printDtoService;

	@Autowired
	private PrintService printService;

	@Autowired
	private SecurityManager securityManager;

	@Before
	public void login() {
		// assure security context is set
		securityManager.createSecurityContext(null);
	}

	@Test
	public void testRender() throws Exception {
		DefaultDocument document = new DefaultDocument(printService.createDefaultTemplate("A4", true), null,
				getDefaultVisitor(-31.44, -37.43, 80.83f), new MapConfigurationVisitor(configurationService,
						printDtoService));
		FileOutputStream fo = new FileOutputStream("target/test.png");
		document.render(fo, Format.PNG);
		fo.flush();
		fo.close();
	}

	@Test
	public void testRenderLegend() throws Exception {
		LegendComponentInfo legend = new LegendComponentInfo();
		FontStyleInfo style = new FontStyleInfo();
		style.setFamily("Dialog");
		style.setStyle("Italic");
		style.setSize(12);
		legend.setFont(style);
		legend.setMapId("mainMap");
		legend.setTag("legend");
		legend.setTitle("legend");
		for (ClientLayerInfo layer : configurationService.getMapInfo("mainMap", "application").getLayers()) {
			if (layer instanceof ClientVectorLayerInfo) {
				ClientVectorLayerInfo layerInfo = (ClientVectorLayerInfo) layer;
				String label = layerInfo.getLabel();
				List<FeatureStyleInfo> defs = layerInfo.getNamedStyleInfo().getFeatureStyles();
				for (FeatureStyleInfo styleDefinition : defs) {
					String text = "";
					if (defs.size() > 1) {
						text = label + "(" + styleDefinition.getName() + ")";
					} else {
						text = label;
					}
					LegendItemComponentInfo item = new LegendItemComponentInfo();
					LegendIconComponentInfo icon = new LegendIconComponentInfo();
					icon.setLabel(text);
					icon.setStyleInfo(styleDefinition);
					icon.setLayerType(layerInfo.getLayerType());
					LabelComponentInfo legendLabel = new LabelComponentInfo();
					legendLabel.setBackgroundColor("0xFFFFFF");
					legendLabel.setBorderColor("0x000000");
					legendLabel.setFontColor("0x000000");
					legendLabel.setFont(legend.getFont());
					legendLabel.setText(text);
					legendLabel.setTextOnly(true);
					item.addChild(icon);
					item.addChild(legendLabel);
					legend.addChild(item);
				}
			} else if (layer instanceof ClientRasterLayerInfo) {
				ClientRasterLayerInfo layerInfo = (ClientRasterLayerInfo) layer;
				LegendItemComponentInfo item = new LegendItemComponentInfo();
				LegendIconComponentInfo icon = new LegendIconComponentInfo();
				icon.setLabel(layerInfo.getLabel());
				icon.setLayerType(layerInfo.getLayerType());
				LabelComponentInfo legendLabel = new LabelComponentInfo();
				legendLabel.setFont(legend.getFont());
				legendLabel.setBackgroundColor("0xFFFFFF");
				legendLabel.setBorderColor("black");
				legendLabel.setFontColor("0x000000");
				legendLabel.setText(layerInfo.getLabel());
				legendLabel.setTextOnly(true);
				item.addChild(icon);
				item.addChild(legendLabel);
				legend.addChild(item);
			}
		}
		LegendComponent comp = (LegendComponent) printDtoService.toInternal(legend);
		PageComponentImpl page = new PageComponentImpl();
		page.setSize("0 0", false);
		page.addComponent(comp);
		PrintTemplate template = new PrintTemplate();
		template.setPage(page);
		SinglePageDocument pdfDoc = new SinglePageDocument(page, null);
		FileOutputStream fo = new FileOutputStream("target/legend.png");
		pdfDoc.render(fo, Format.PNG);
		fo.flush();
		fo.close();
	}

	private DefaultConfigurationVisitor getDefaultVisitor(double x, double y, float widthInUnits) {
		// 842, 595
		DefaultConfigurationVisitor config = new DefaultConfigurationVisitor();
		config.setApplicationId("application");
		config.setMapId("mainMap");
		config.setMapRasterResolution(72);
		config.setMapPpUnit(822 / widthInUnits);
		config.setMapLocation(new Coordinate(x, y));
		config.setTitle("Africa");
		return config;
	}

}