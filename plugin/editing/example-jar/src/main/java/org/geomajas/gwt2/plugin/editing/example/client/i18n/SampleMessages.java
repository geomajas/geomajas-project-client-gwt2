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

package org.geomajas.gwt2.plugin.editing.example.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * Specific messages for the many samples.
 * 
 * @author Pieter De Graef
 */
public interface SampleMessages extends Messages {

	String editingTitle();

	String editingShort();

	String editingDescription();

	// General messages:

	String generalCreate();

	String generalEdit();

	String generalStopEditing();

	String generalExplanation();

	String generalUndo();

	String generalRedo();

	String generalClear();

	// EditPointPanel:

	String editPointTitle();

	String editPointShort();

	String editPointDescription();

	String editPointSubTitle();

	String editPointCreate();

	String editPointEdit();

	// EditLinePanel:

	String editLineTitle();

	String editLineShort();

	String editLineDescription();

	String editLineSubTitle();

	String editLineCreate();

	String editLineEdit();

	// EditPolygonPanel:

	String editPolygonTitle();

	String editPolygonShort();

	String editPolygonDescription();

	String editPolygonSubTitle();

	String editPolygonCreate();

	String editPolygonEdit();

	String editPolygonAddRing();

	// UndoRedoPanel:

	String undoRedoTitle();

	String undoRedoShort();

	String undoRedoDescription();

	String undoRedoSubTitle();

	// SnapToCountriesPanel:

	String snapToCountriesTitle();

	String snapToCountriesShort();

	String snapToCountriesDescription();

	String snapToCountriesSubtitle();

	String snapToCountriesOnOff();

	String snapToCountriesDistance();

	// SplitCountryPanel:

	String splitCountryTitle();

	String splitCountryShort();

	String splitCountryDescription();

	String splitCountrySubTitle();

	String splitCountrySelect();

	String splitCountryDraw();

	String splitCountrySplit();

	// MergeCountriesPanel:

	String mergeCountriesTitle();

	String mergeCountriesShort();

	String mergeCountriesDescription();

	String mergeCountriesSubTitle();

	String mergeCountriesSelect();

	String mergeCountriesMerge();
}