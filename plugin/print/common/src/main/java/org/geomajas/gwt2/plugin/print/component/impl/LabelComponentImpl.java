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

package org.geomajas.gwt2.plugin.print.component.impl;

import java.awt.Font;

import org.geomajas.gwt2.plugin.print.component.PrintComponentVisitor;
import org.geomajas.gwt2.plugin.print.parser.FontConverter;
import org.geomajas.gwt2.plugin.print.component.LabelComponent;
import org.geomajas.gwt2.plugin.print.component.PdfContext;
import org.geomajas.gwt2.plugin.print.component.dto.LabelComponentInfo;
import org.geomajas.gwt2.plugin.print.component.service.PrintDtoConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lowagie.text.Rectangle;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Inclusion of label in printed document.
 * 
 * @author Jan De Moerloose
 * @author An Buyle
 */
@Component()
@Scope(value = "prototype")
public class LabelComponentImpl extends AbstractPrintComponent<LabelComponentInfo> implements LabelComponent {

	private static final String ELLIPSES_STRING = "...";

	/** The font for the text. */
	@XStreamConverter(FontConverter.class)
	private Font font;

	/** Label text. */
	private String text;

	/** Color of the text. */
	private String fontColor;

	/** Background color. */
	private String backgroundColor;

	/** Line color. */
	private String borderColor;

	/** Only text, no border? */
	private boolean textOnly;

	/** Border line width. */
	private float lineWidth;

	/** Margin around text. */
	private float margin;


	/** Max. width of rendered text (in point units) Default is 0.0, meaning there's no limit imposed by the client. */
	private float maxWidthText;

	@Autowired
	@XStreamOmitField
	private PrintDtoConverterService converterService;

	public LabelComponentImpl() {
		this("white", "black", new Font("Dialog", Font.PLAIN, 12), "black", "<missing text>", false, 1f);
	}

	public LabelComponentImpl(Font font, String fontColor, String text) {
		this("white", "black", font, fontColor, text, true, 1f);
	}

	public LabelComponentImpl(String backgroundColor, String borderColor, Font font, String fontColor, String text,
			boolean textOnly, float lineWidth) {
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
		this.font = font;
		this.fontColor = fontColor;
		this.text = text;
		this.textOnly = textOnly;
		this.lineWidth = lineWidth;
	}

	/**
	 * Call back visitor.
	 * 
	 * @param visitor
	 *            visitor
	 */
	public void accept(PrintComponentVisitor visitor) {
		visitor.visit(this);
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.print.component.impl.LabelComponent#getText()
	 */
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.print.component.impl.LabelComponent#getFont()
	 */
	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.print.component.impl.LabelComponent#isTextOnly()
	 */
	public boolean isTextOnly() {
		return textOnly;
	}

	public void setTextOnly(boolean textOnly) {
		this.textOnly = textOnly;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.print.component.impl.LabelComponent#getLineWidth()
	 */
	public float getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}

	/**
	 * Get the maximum width of the rendered text (in point units).
	 * Default is 0.0, meaning there's no limit imposed by the client. 
	 * 
	 * @return The maximum width of the rendered text (in point units)
	 */
	public float getMaxWidthText() {
		return this.maxWidthText;

	}

	/**
	 * Set the maximum width of the rendered text (in point units).
	 * A value of 0.0, means there's no limit imposed by the client. 
	 * 
	 * @param maxWidthText
	 * 			The maximum width of the rendered text (in point units).

	 */
	public void setMaxWidthText(float maxWidthText) {
		this.maxWidthText = maxWidthText;
	}
	
	/**
	 * Get the margin around the text.
	 * 
	 * @return The margin around the text
	 */
	public float getMargin() {
		return margin;
	}

	/**
	 * Set the margin around the text.
	 * 
	 * @param margin
	 * 			The margin around the text.
	 */
	public void setMargin(float margin) {
		this.margin = margin;
	}

	public void render(PdfContext context) {
		assert (null != getText()) : "getText() must be non-null";
		if (!isTextOnly()) {
			context.fillRectangle(getSize(), context.getColor(getBackgroundColor(), 1f));
			context.strokeRectangle(getSize(), context.getColor(getBorderColor(), 1f), getLineWidth());
		}
		if (getText() != null) {
			context.drawText(getText(), getFont(), getSize(), context.getColor(getFontColor(), 1f));
		}
	}

	@Override
	public void calculateSize(PdfContext context) {
		assert (null != getText()) : "getText() must be non-null";

		if (getMaxWidthText() > 0.0) {
			limitTextWidth(context, getMaxWidthText());
		}
		Rectangle textSize = context.getTextSize(getText(), getFont());
		
		float margin = getMargin(); // Get the specified value
		if (margin <= 0.0f) {
			margin = 0.5f * getFont().getSize(); 
		}
		float width = textSize.getWidth() + 2.0f * margin;
		float height = textSize.getHeight() + 2.0f * margin;
		
		if (null != getConstraint()) {
			if (getConstraint().getWidth() > 0.0f) {
				width = getConstraint().getWidth();
			}
			if (getConstraint().getHeight() > 0.0f) {
				height = getConstraint().getHeight();
			}
		}
		setBounds(new Rectangle(width, height));
	}

	public void fromDto(LabelComponentInfo labelInfo) {
		super.fromDto(labelInfo);
		setBackgroundColor(labelInfo.getBackgroundColor());
		setBorderColor(labelInfo.getBorderColor());
		setFont(converterService.toInternal(labelInfo.getFont()));
		setFontColor(labelInfo.getFontColor());
		setLineWidth(labelInfo.getLineWidth());
		setTextOnly(labelInfo.isTextOnly());
		setText(labelInfo.getText());
		// support for max-width of rendered text (in point units?)
		setMaxWidthText(labelInfo.getMaxWidthText());
		setMargin(labelInfo.getMargin());
	}

	private void limitTextWidth(PdfContext context, float maxWidth) {
		assert (null != getText()) : "getText() must be non-null";
		if (getMaxWidthText() > 0.0) {

			float elipsesWidth = context.getTextSize(ELLIPSES_STRING, getFont()).getWidth();
			if (context.getTextSize(getText(), getFont()).getWidth() > getMaxWidthText() && getText().length() > 2) {
				StringBuffer text = new StringBuffer(getText());
				do {
					text.deleteCharAt(text.length() - 1);
				} while (context.getTextSize(text.toString(), getFont()).getWidth() + elipsesWidth > getMaxWidthText()
						&& text.length() > 2);

				setText(text.append("...").toString());
			}
		}
	}
}