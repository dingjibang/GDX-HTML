package team.rpsg.html.manager

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Pools
import groovy.transform.CompileStatic
import team.rpsg.lazyFont.LazyBitmapFont

@CompileStatic
class TextManager {
	private Map<Param, LazyBitmapFont> map = new HashMap<>()

	FreeTypeFontGenerator NORMAL_GENERATOR = new FreeTypeFontGenerator(Gdx.files.internal("font/xyj.ttf"))
	FreeTypeFontGenerator ENGLISH_GENERATOR = new FreeTypeFontGenerator(Gdx.files.internal("font/Coold.ttf"))


	LazyBitmapFont get(int fontSize, FreeTypeFontGenerator gen) {
		LazyBitmapFont font = null

		for(Param k : map.keySet())
			if(k.size == fontSize && k.gen == gen)
				font = map.get(k)

		if (font == null) {
			font = gen == null ? new LazyBitmapFont(NORMAL_GENERATOR, fontSize) : new LazyBitmapFont(gen, fontSize)
			map.put(new Param(fontSize, gen), font)
		}

		return font
	}

	LazyBitmapFont get(int fontSize) {
		return get(fontSize, null)
	}

	Label getLabel(int fontSize) {
		return getLabel("", fontSize)
	}

	Label getLabel(Object text, int fontSize) {
		return getLabel(text, fontSize, null)
	}

	Label getLabel(Object text, int fontSize, FreeTypeFontGenerator gen) {
		Label.LabelStyle ls = new Label.LabelStyle()
		ls.font = gen == null ? get(fontSize) : get(fontSize, gen)

		return new Label(text.toString(), ls)
	}


	private static class Param{
		int size
		FreeTypeFontGenerator gen

		Param(int size, FreeTypeFontGenerator gen) {
			this.size = size
			this.gen = gen
		}

	}

	int getTextWidth(String str, int size) {
		return getTextWidth(get(size),str)

	}

	static int getTextWidth(BitmapFont font, String str){
		GlyphLayout layout = Pools.obtain(GlyphLayout.class)
		layout.setText(font, str)
		return (int) layout.width
	}

	void dispose(){
		NORMAL_GENERATOR.dispose()
		ENGLISH_GENERATOR.dispose()

		map.each {k, v -> v.dispose()}
	}
}
