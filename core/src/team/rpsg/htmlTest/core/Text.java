package team.rpsg.htmlTest.core;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Pools;
import script.ui.widget.widget.Label;
import team.rpsg.lazyFont.LazyBitmapFont;

/**
 * GDX-RPG 字体管理器<br>
 * 请在{@link Res#text}访问本类
 */
public class Text {
	private Map<Param, LazyBitmapFont> map = new HashMap<>();

	public FreeTypeFontGenerator NORMAL_GENERATOR = new FreeTypeFontGenerator(Gdx.files.internal(team.rpsg.htmlTest.core.Path.BASE_PATH + "font/xyj.ttf"));
	public FreeTypeFontGenerator ENGLISH_GENERATOR = new FreeTypeFontGenerator(Gdx.files.internal(team.rpsg.htmlTest.core.Path.BASE_PATH + "font/Coold.ttf"));

	public LazyBitmapFont get(int fontSize, FreeTypeFontGenerator gen) {

		LazyBitmapFont font = null;
		for(Param k : map.keySet())
			if(k.size == fontSize && k.gen == gen)
				font = map.get(k);

		if (font == null) {
			font = gen == null ? new LazyBitmapFont(NORMAL_GENERATOR, fontSize) : new LazyBitmapFont(gen, fontSize);
			map.put(new Param(fontSize, gen), font);
		}

		return font;
	}

	public LazyBitmapFont get(int fontSize) {
		return get(fontSize, null);
	}

	public Label getLabel(int fontSize) {
		return getLabel("", fontSize);
	}

	public Label getLabel(Object text, int fontSize) {
		return getLabel(text, fontSize, null);
	}

	public Label getLabel(Object text, int fontSize, FreeTypeFontGenerator gen) {
		LabelStyle ls = new LabelStyle();
		ls.font = gen == null ? get(fontSize) : get(fontSize, gen);

		return new Label(text.toString(), ls);
	}


	private static class Param{
		public int size;
		public FreeTypeFontGenerator gen;

		public Param(int size, FreeTypeFontGenerator gen) {
			this.size = size;
			this.gen = gen;
		}

	}

	public int getTextWidth(String str, int size) {
		return getTextWidth(get(size),str);	
		
	}
	
	public static int getTextWidth(BitmapFont font, String str){
		GlyphLayout layout = Pools.obtain(GlyphLayout.class);
		layout.setText(font, str);
		return (int) layout.width;
	}
}
