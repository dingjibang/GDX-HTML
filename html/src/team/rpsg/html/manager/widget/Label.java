package team.rpsg.html.manager.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import team.rpsg.gdxQuery.GdxQuery;
import team.rpsg.html.manager.ResourceManager;
import team.rpsg.html.manager.TextManager;
import team.rpsg.lazyFont.LazyBitmapFont;

/**
 */
public class Label extends com.badlogic.gdx.scenes.scene2d.ui.Label{

	public static final Float AUTO_LINE_HEIGHT = null;
	private Float lineHeight = AUTO_LINE_HEIGHT;

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

	public void layout() {
		BitmapFont.BitmapFontData data = null;
		float h = 0;

		if(lineHeight != null){
			data = getStyle().font.getData();
			h = data.lineHeight;

			data.setLineHeight(lineHeight);
		}

		super.layout();

		if(lineHeight != null)
			data.setLineHeight(h);

	}
}
