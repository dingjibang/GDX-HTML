package team.rpsg.html.util

import com.badlogic.gdx.graphics.Color
import com.steadystate.css.dom.CSSValueImpl
import com.steadystate.css.dom.RGBColorImpl
import com.steadystate.css.parser.LexicalUnitImpl

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * color(#hex or rgb or rgba) parser
 */
class ColorParser {
	static Color parse(Object value){
		if(value instanceof CSSValueImpl){
			if(value.value instanceof LexicalUnitImpl){
				if(value.value.toString().toLowerCase().startsWith("rgba"))
					return parseRGBA(value.value.toString().toLowerCase())

				return Color.(value.value.toString().toUpperCase())
			}
			if(value.value instanceof RGBColorImpl){
				def rgb = value.value as RGBColorImpl
				return new Color(value(rgb.red), value(rgb.green), value(rgb.blue), 1)
			}
		}else if (value instanceof String){
			return Color.(value.toString().toUpperCase())
		}

		return Color.WHITE
	}

	private static float value(Object value){
		(value.toString().toFloat() / 255f).toFloat()
	}

	private static Color parseRGBA(String rgba){
		Pattern c = Pattern.compile("rgba *\\( *([0-9]+), *([0-9]+), *([0-9]+), *([0-9]+) *\\)")
		Matcher m = c.matcher(rgba)

		if (m.matches())
			return new Color(value(m.group(1)), value(m.group(2)), value(m.group(3)), value(m.group(4)))
	}

}
