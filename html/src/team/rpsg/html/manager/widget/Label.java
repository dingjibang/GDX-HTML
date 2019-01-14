package team.rpsg.html.manager.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;

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

	private List<GlyphAndSize> glyphList = new ArrayList<>();

	private boolean textChanged = true;

	private boolean wrap = false;

	private boolean isInvalidateHierarchy = true;

	private boolean prefSizeInvalid = true;

	private boolean disabledHeightHook = false;

	public void setWrap(boolean wrap) {
		this.wrap = wrap;
		super.setWrap(wrap);
	}

	public void invalidate () {
		super.invalidate();
		prefSizeInvalid = true;
	}

	@Override
	public void setText(CharSequence newText) {
		super.setText(newText);
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
		if(glyphList == null || this.letterSpacing == AUTO_LETTER_SPACING)
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
			GlyphAndSize glyphAndSize = new GlyphAndSize(data.getGlyph(c));
			if(!glyphList.contains(glyphAndSize))
				glyphList.add(glyphAndSize.to(letterSpacing));
		}
	}

	private void endSetXAdvance(){
		if(glyphList == null || this.letterSpacing == AUTO_LETTER_SPACING)
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
		disabledHeightHook = true;
		super.layout();
		disabledHeightHook = false;
		isInvalidateHierarchy = true;
		endSetXAdvance();
		endSetLineHeight();
	}

	public float getPrefHeight() {
		//fix a single line but gdx thinks it's a ZERO line bug
		if(getLines() == 0 && getText().toString().length() != 0 && lineHeight != AUTO_LINE_HEIGHT){
			if(!wrap && isInvalidateHierarchy){
				((Layout)getParent()).invalidateHierarchy();
				isInvalidateHierarchy = false;
			}
			return lineHeight;
		}

		if(!prefSizeInvalid)
			return super.getPrefHeight();

		if(disabledHeightHook)
			return super.getPrefHeight();


		beginSetLineHeight();
		beginSetXAdvance();
		float result = super.getPrefHeight();
		endSetXAdvance();
		endSetLineHeight();


		return result;
	}

	public float getPrefWidth() {
		if(!prefSizeInvalid)
			return super.getPrefWidth();

		prefSizeInvalid = false;

		beginSetLineHeight();
		beginSetXAdvance();
		float result = super.getPrefWidth();
		endSetXAdvance();
		endSetLineHeight();

		if(!wrap && isInvalidateHierarchy){
			((Layout)getParent()).invalidateHierarchy();
			isInvalidateHierarchy = false;
		}

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

		@Override
		public int hashCode() {
			return glyph.id;
		}

		@Override
		public boolean equals(Object obj) {
			if(obj instanceof GlyphAndSize)
				return glyph.id == ((GlyphAndSize) obj).glyph.id;
			return super.equals(obj);
		}
	}
}
