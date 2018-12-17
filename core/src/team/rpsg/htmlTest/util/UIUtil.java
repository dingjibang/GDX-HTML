package team.rpsg.htmlTest.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import script.ui.widget.widget.Image;
import team.rpsg.gdxQuery.*;
import team.rpsg.htmlTest.core.UI;
import team.rpsg.htmlTest.core.Res;

public class UIUtil {

	public static GdxQuery $ (Object... a){
		return new GdxQuery(a);
	}

	public static <T extends Actor> TypedGdxQuery<T> $(T t){
		return new TypedGdxQuery<T>(t);
	}


	public static TypedGdxQuery<Image> $ (String param){
		if(param.equals("base"))
			return $();
		if(param.startsWith("base#"))
			return $().color(param.replaceAll("base#", ""));
		return null;
	}

	public static TypedGdxQuery<Image> $(){
		return new TypedGdxQuery<Image>(UI.base());
	}

	public static GdxFrame $(GdxQuery query, GdxQueryRunnable runnable){
		return new GdxFrame().add(query, runnable);
	}

	public static GdxQuery q(Object... a){
		if(a[0].equals("base"))
			return new GdxQuery(UI.base());
		return new GdxQuery(a);
	}

}