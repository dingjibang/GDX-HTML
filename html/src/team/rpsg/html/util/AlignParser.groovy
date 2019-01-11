package team.rpsg.html.util

import com.badlogic.gdx.utils.Align
import groovy.transform.CompileStatic

@CompileStatic
class AlignParser {
	static int textAlign(Object align, int defaultAlign = Align.left){
		if(!align || align.toString().length() == 0)
			return defaultAlign

		def s = align.toString()

		switch (s.toLowerCase()){
			case "left": return Align.left
			case "right": return Align.right
			case "center": return Align.center
		}

		return defaultAlign
	}

	static int verticalAlign(Object align, int defaultAlign = Align.bottom){

		if(!align || align.toString().length() == 0)
			return defaultAlign

		def s = align.toString()

		switch (s.toLowerCase()){
			case "top": return Align.top
			case "middle": return Align.center
			case "center": return Align.center
			case "bottom": return Align.bottom
		}

		return defaultAlign
	}

	static int join(int lr, int tb){
		if(lr == Align.center && tb == Align.center)
			return Align.center
		if(lr == Align.center)
			return tb
		if(tb == Align.center)
			return lr

		return tb | lr
	}

	static String toString(Integer align){
		switch (align){
			case Align.center : return "center"
			case Align.left : return "left"
			case Align.right : return "right"
			case Align.bottom : return "bottom"
			case Align.bottomLeft : return "bottomLeft"
			case Align.bottomRight : return "bottomRight"
			case Align.top : return "top"
			case Align.topLeft : return "topLeft"
			case Align.topRight : return "topRight"
			default: return "UNKNOWN(" + align + ")"
		}
	}
}
