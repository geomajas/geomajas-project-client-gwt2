/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.client.gwt.manager.blueprints;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.BlueprintEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.BlueprintHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.Whiteboard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.CommService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * @author Kristof Heirwegh
 */
public class BlueprintGrid extends ListGrid implements BlueprintHandler {

	public static final String FLD_ID = "id";

	public static final String FLD_PUBLIC = "public";

	public static final String FLD_LOKETTENACTIVE = "lokettenActive";

	private ListGridRecord rollOverRecord;

	private HLayout rollOverCanvas;

	private Map<String, BlueprintDto> blueprints = new HashMap<String, BlueprintDto>();

	public BlueprintGrid() {
		super();
		setWidth100();
		setHeight100();
		setAlternateRecordStyles(true);
		setSelectionType(SelectionStyle.SINGLE);
		setShowRollOverCanvas(true);
		setShowAllRecords(true);

		// -- Fields --------------------------------------------------------

		ListGridField name = new ListGridField("name", "Naam Blauwdruk");
		name.setWidth("*");
		name.setType(ListGridFieldType.TEXT);

		ListGridField limitTerritory = new ListGridField("limitToLoketTerritory", "Beveiliging grondgebied");
		limitTerritory.setType(ListGridFieldType.BOOLEAN);
		limitTerritory.setWidth(135);
		limitTerritory.setPrompt("Aan: betekent dat beveiliging geldt voor het grondgebied"
				+ " van de entiteit loketbeheerder.<br />Uit: geen beveiliging.");

		ListGridField publicUse = new ListGridField(FLD_PUBLIC, "Publiek");
		publicUse.setType(ListGridFieldType.BOOLEAN);
		publicUse.setWidth(100);
		publicUse.setPrompt("Aan: loket kan geraadpleegd worden zonder aanmelden.<br />"
				+ "Uit: loket kan enkel geraadpleegd worden na aanmelden (LB of VO).");

		ListGridField active = new ListGridField("active", "Actief");
		active.setType(ListGridFieldType.BOOLEAN);
		active.setWidth(100);
		active.setPrompt("Aan: betekent dat er nieuwe lokketten kunnen gebouwd worden gebaseerd"
				+ " op deze blauwdruk.<br />Merk op dat dit geen invloed heeft op reeds bestaande"
				+ " loketten gebaseerd op deze blauwdruk.");

		ListGridField lokettenActive = new ListGridField(FLD_LOKETTENACTIVE, "Geodesks Actief");
		lokettenActive.setType(ListGridFieldType.BOOLEAN);
		lokettenActive.setWidth(135);
		lokettenActive.setPrompt("Uit: betekent dat de loketten gebaseerd op deze blauwdruk"
				+ " niet actief zijn (niet kunnen gebruikt worden).");

		setFields(name, limitTerritory, publicUse, active, lokettenActive);
		// initially sort on blueprint name
		setSortField(0);
		setSortDirection(SortDirection.ASCENDING);

		// ----------------------------------------------------------

		Whiteboard.registerHandler(this);
		readData();
	}

	@Override
	public void destroy() {
		Whiteboard.unregisterHandler(this);
		super.destroy();
	}

	@Override
	protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
		rollOverRecord = this.getRecord(rowNum);

		if (rollOverCanvas == null) {
			rollOverCanvas = new HLayout(3);
			rollOverCanvas.setSnapTo("TR");
			rollOverCanvas.setWidth(22);
			rollOverCanvas.setHeight(22);

			ImgButton deleteImg = new ImgButton();
			deleteImg.setShowDown(false);
			deleteImg.setShowRollOver(false);
			deleteImg.setLayoutAlign(Alignment.CENTER);
			deleteImg.setSrc(WidgetLayout.iconRemove);
			deleteImg.setPrompt("Verwijder blauwdruk");
			deleteImg.setHeight(16);
			deleteImg.setWidth(16);
			deleteImg.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					SC.ask("Verwijderen", "Blauwdruk \"" + rollOverRecord.getAttribute("name")
							+ "\" verwijderen?<br /><br />Deze actie heeft geen invloed op eventueel "
							+ "bestaande loketten gebaseerd op deze blauwdruk."
							+ "<br />Deze actie kan niet ongedaan gemaakt worden.", new BooleanCallback() {

						public void execute(Boolean value) {
							if (value) {
								CommService.deleteBlueprint(blueprints.get(rollOverRecord.getAttribute(FLD_ID)));
							}
						}
					});
				}
			});
			rollOverCanvas.addMember(deleteImg);
		}
		return rollOverCanvas;
	}

	// ----------------------------------------------------------

	private void readData() {
		clearData();

		setShowEmptyMessage(true);
		setEmptyMessage("<i>Blauwdrukken worden geladen... <img src='" + Geomajas.getIsomorphicDir()
				+ "/images/circle.gif' style='height: 1em' /></i>");
		redraw();

		CommService.getBlueprints(new DataCallback<List<BlueprintDto>>() {

			public void execute(List<BlueprintDto> result) {
				for (BlueprintDto bp : result) {
					blueprints.put(bp.getId(), bp);
					ListGridRecord record = toGridRecord(bp);
					addData(record);
				}
				setShowEmptyMessage(false);
				redraw();
			}
		});
	}

	void clearData() {
		deselectAllRecords();
		setData(new ListGridRecord[] {});
		blueprints.clear();
	}

	private ListGridRecord toGridRecord(BlueprintDto blueprint) {
		ListGridRecord record = new ListGridRecord();
		record.setAttribute(FLD_ID, blueprint.getId());
		record.setAttribute("name", blueprint.getName());
		record.setAttribute("limitToLoketTerritory", blueprint.isLimitToCreatorTerritory());
		record.setAttribute(FLD_PUBLIC, blueprint.isPublic());
		record.setAttribute("active", blueprint.isActive());
		record.setAttribute(FLD_LOKETTENACTIVE, blueprint.isLokettenActive());
		return record;
	}

	public void onBlueprintChange(BlueprintEvent bpe) {
		BlueprintDto old = blueprints.remove(bpe.getBlueprint().getId());
		if (old != null && getRecordList() != null) {
			Record oldr = getRecordList().find(FLD_ID, old.getId());
			removeData(oldr);
		}
		if (!bpe.isDeleted()) {
			blueprints.put(bpe.getBlueprint().getId(), bpe.getBlueprint());
			ListGridRecord record = toGridRecord(bpe.getBlueprint());
			addData(record);
			if (bpe.isNewInstance()) {
				deselectAllRecords();
				selectRecord(record);
			}
		}
	}
}