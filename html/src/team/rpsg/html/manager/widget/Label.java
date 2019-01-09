package team.rpsg.html.manager.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 */
public class Label extends com.badlogic.gdx.scenes.scene2d.ui.Label{

	public static final Float AUTO_LINE_HEIGHT = null;
	private Float lineHeight = AUTO_LINE_HEIGHT;

	public static final Float AUTO_LETTER_SPACING = null;
	private Float letterSpacing = AUTO_LETTER_SPACING;

	private float lastHeight = 0;

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

	public Float getLetterSpacing() {
		return letterSpacing;
	}

	public void setLetterSpacing(Float letterSpacing) {
		this.letterSpacing = letterSpacing;
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
		super.layout();
		endSetLineHeight();
	}

	public float getPrefHeight() {
		beginSetLineHeight();
		float result = super.getPrefHeight();
		endSetLineHeight();

		if(getWrappedLinesCount() == 0 && getText().toString().length() != 0 && lineHeight != AUTO_LINE_HEIGHT)//fix a single line but gdx thinks it's a ZERO line bug
			return lineHeight;

		return result;
	}

	/**
	 * haha
	 */
	public float getWrappedLinesCount() {
		BitmapFont.BitmapFontData data = getStyle().font.getData();
		return (getGlyphLayout().height - data.capHeight) / data.lineHeight;
	}
}
