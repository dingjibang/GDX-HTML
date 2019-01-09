package team.rpsg.html.manager.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 */
public class Label extends com.badlogic.gdx.scenes.scene2d.ui.Label{

	public static final Float AUTO_LINE_HEIGHT = null;
	private Float lineHeight = AUTO_LINE_HEIGHT;

	public static final Integer AUTO_LETTER_SPACING = null;
	private Integer letterSpacing = AUTO_LETTER_SPACING;

	private float lastHeight = 0;

	private Set<GlyphAndSize> glyphList = new HashSet<>();

	private boolean textChanged = true;

	@Override
	public void setText(CharSequence newText) {
		super.setText(newText);
		glyphList = new HashSet<>(getText().length() + 10);
		textChanged = true;
	}

	public Label(CharSequence text, Skin skin) {
		super(text, skin);
	}

	public Label(CharSequence text, Skin skin, String styleName) {
		super(text, skin, styleName);
	}

	public Label(CharSequence text, Skin skin, String fontName, Color color) {
		super(text, skin, fontName, color);
	}

	public Label(CharSequence text, Skin skin, String fontName, String colorName) {
		super(text, skin, fontName, colorName);
	}

	public Label(CharSequence text, LabelStyle style) {
		super(text, style);
	}

	public Float getLineHeight() {
		return lineHeight;
	}

	public void setLineHeight(Float lineHeight) {
		this.lineHeight = lineHeight;
	}

	public Integer getLetterSpacing() {
		return letterSpacing;
	}

	public void setLetterSpacing(Integer letterSpacing) {
		this.letterSpacing = letterSpacing;
	}

	private void beginSetXAdvance(){
		if(glyphList == null)
			return;

		if(!textChanged){
			for(GlyphAndSize glyphAndSize : glyphList)
				glyphAndSize.to(letterSpacing);

			return;
		}

		textChanged = false;
		glyphList.clear();

		BitmapFont.BitmapFontData data = getStyle().font.getData();

		char[] chars = getText().chars;
		for(char c : chars){
			if(c == 0 || c == ' ')
				continue;
			glyphList.add(new GlyphAndSize(data.getGlyph(c)).to(letterSpacing));
		}
	}

	private void endSetXAdvance(){
		if(glyphList == null)
			return;

		for(GlyphAndSize glyphAndSize : glyphList)
			glyphAndSize.reset();
	}

	private void beginSetLineHeight(){
		if(lineHeight != null){
			BitmapFont.BitmapFontData data = getStyle().font.getData();
			lastHeight = data.lineHeight;

			data.setLineHeight(lineHeight);
		}
	}

	private void endSetLineHeight(){
		if(lineHeight != null){
			getStyle().font.getData().setLineHeight(lastHeight);
		}
	}

	public void layout() {
		beginSetLineHeight();
		beginSetXAdvance();
		super.layout();
		endSetXAdvance();
		endSetLineHeight();
	}

	public float getPrefHeight() {
		beginSetLineHeight();
		beginSetXAdvance();
		float result = super.getPrefHeight();
		endSetLineHeight();
		endSetXAdvance();

		if(getLines() == 0 && getText().toString().length() != 0 && lineHeight != AUTO_LINE_HEIGHT)//fix a single line but gdx thinks it's a ZERO line bug
			return lineHeight;

		return result;
	}

	public float getPrefWidth() {
		beginSetLineHeight();
		beginSetXAdvance();
		float result = super.getPrefWidth();
		endSetLineHeight();
		endSetXAdvance();

		if(getLines() == 0 && getText().toString().length() != 0 && lineHeight != AUTO_LINE_HEIGHT)//fix a single line but gdx thinks it's a ZERO line bug
			return lineHeight;

		return result;
	}

	/**
	 * to calculate lines, you must call this after layout()
	 */
	public float getLines() {
		BitmapFont.BitmapFontData data = getStyle().font.getData();
		return (getGlyphLayout().height - data.capHeight) / data.lineHeight;
	}

	private static class GlyphAndSize{
		final BitmapFont.Glyph glyph;
		final int originXAdvance;

		GlyphAndSize(BitmapFont.Glyph glyph){
			this.glyph = glyph;
			this.originXAdvance = glyph.xadvance;
		}

		GlyphAndSize to(int xAdvance){
			glyph.xadvance = originXAdvance + xAdvance;
			return this;
		}

		GlyphAndSize reset(){
			glyph.xadvance = originXAdvance;
			return this;
		}
	}
}
