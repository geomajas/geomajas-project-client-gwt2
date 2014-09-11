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

package org.geomajas.gwt2.example.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * Specific messages for the many samples.
 * 
 * @author Pieter De Graef
 */
public interface SampleMessages extends Messages {

	// ------------------------------------------------------------------------
	// Category: general samples
	// ------------------------------------------------------------------------

	String generalNavOptionTitle();

	String generalNavOptionShort();

	String generalNavOptionDescription();

	String generalResizeMapTitle();

	String generalResizeMapShort();

	String generalResizeMapDescription();

	String generalMapFillTitle();

	String generalMapFillShort();

	String generalMapFillDescription();

	String generalVpEventTitle();

	String generalVpEventShort();

	String generalVpEventDescription();

	String generalServerExceptionTitle();

	String generalServerExceptionShort();

	String generalServerExceptionDescription();

	String generalListnerTitle();

	String generalListnerShort();

	String generalListnerDescription();

	String generalListnerScreenPosition();

	String generalListnerWorldPosition();

	String generalListnerSubtitle();

	String generalControlsTitle();

	String generalControlsShort();

	String generalControlsDescription();

	String generalTraceNavigationTitle();

	String generalTraceNavigationShort();

	String generalTraceNavigationDescription();

	// ------------------------------------------------------------------------
	// Category: Layers
	// ------------------------------------------------------------------------

	String layerAddRemoveTitle();

	String layerAddRemoveShort();

	String layerAddRemoveDescription();

	String layerOrderTitle();

	String layerOrderShort();

	String layerOrderDescription();

	String layerVisibilityTitle();

	String layerVisibilityShort();

	String layerVisibilityDescription();

	String layerRefreshTitle();

	String layerRefreshShort();

	String layerRefreshDescription();

	String layerOpacityTitle();

	String layerOpacityShort();

	String layerOpacityDescription();

	// ------------------------------------------------------------------------
	// Category: Features
	// ------------------------------------------------------------------------

	String featureSelectionTitle();

	String featureSelectionShort();

	String featureSelectionDescription();

	// ------------------------------------------------------------------------
	// Category: Rendering
	// ------------------------------------------------------------------------

	String renderingInteractionTitle();

	String renderingInteractionShort();

	String renderingInteractionDescription();

	String renderingScreenSpaceTitle();

	String renderingScreenSpaceShort();

	String renderingScreenSpaceDescription();

	String renderingWorldSpaceTitle();

	String renderingWorldSpaceShort();

	String renderingWorldSpaceDescription();

	String renderingWorldSpaceFixedTitle();

	String renderingWorldSpaceFixedShort();

	String renderingWorldSpaceFixedDescription();

	String renderingMissingCanvas();

	String renderingCanvasTitle();

	String renderingCanvasShort();

	String renderingCanvasDescription();

	String markerPanelTitle();

	String markerPanelShort();

	String markerPanelDescription();

	String renderingCanvasImageTitle();

	String renderingCanvasImageShort();

	String renderingCanvasImageDescription();

	String renderingCanvasImageDrawingOptionsLabel();
	String renderingCanvasImageSourceLabel();
	String renderingCanvasImageLocalImageRadio();
	String renderingCanvasImageRemoteImageRadio();
	String renderingCanvasImageRemoteImageUrlLabel();
	String renderingCanvasImageRemoteImageUrlTooltip();
	String renderingCanvasImageLocationLabel();
	String renderingCanvasImageLocationCenterLabel();
	String renderingCanvasImageLocationCustomLabel();
	String renderingCanvasImageDrawButton();
	String renderingCanvasImageMultipleLabel();
	String renderingCanvasImageDrawMultipleRowsLabel();
	String renderingCanvasImageDrawMultipleColumnsLabel();
	String renderingCanvasImageDrawMultipleButton();
	String renderingCanvasImageDrawMultipleButtonTooltip();
	String renderingCanvasRectangleDrawMultipleButton();
	String renderingCanvasRectangleDrawMultipleButtonTooltip();
	String renderingCanvasImageClearLabel();
}
