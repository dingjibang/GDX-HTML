package team.rpsg.html.util

import com.badlogic.gdx.graphics.Color
import com.steadystate.css.dom.CSSValueImpl
import com.steadystate.css.dom.RGBColorImpl
import com.steadystate.css.parser.LexicalUnitImpl
import groovy.transform.CompileStatic

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * color(#hex or rgb or rgba) parser
 */
@CompileStatic
class ColorParser {
	static Color DEFAULT_VALUE = Color.WHITE

	static Color parse(Object value, Color defaultColor = DEFAULT_VALUE){
		if(value instanceof CSSValueImpl){
			if(value.value instanceof LexicalUnitImpl){
				if(value.value.toString().toLowerCase().startsWith("rgba"))
					return parseRGBA(value.value.toString().toLowerCase())

				return parse(value.value.toString())
			}
			if(value.value instanceof RGBColorImpl){
				def rgb = value.value as RGBColorImpl
				return new Color(valueOf(rgb.red), valueOf(rgb.green), valueOf(rgb.blue), 1)
			}
		}else if (value instanceof String){
			try{
				return HTMLColor.valueOf(value.toString().toUpperCase()).color()
			}catch (e){
				return defaultColor
			}
		}

		return defaultColor
	}

	private static float valueOf(Object value){
		(value.toString().toFloat() / 255f).toFloat()
	}

	private static Color parseRGBA(String rgba){
		Pattern c = Pattern.compile("rgba *\\( *([0-9]+), *([0-9]+), *([0-9]+), *([0-9]+) *\\)")
		Matcher m = c.matcher(rgba)

		if (m.matches())
			return new Color(valueOf(m.group(1)), valueOf(m.group(2)), valueOf(m.group(3)), valueOf(m.group(4)))
	}

	@CompileStatic
	static enum HTMLColor{
		INDIANRED(205, 92, 92),
		LIGHTCORAL(240, 128, 128),
		SALMON(250, 128, 114),
		DARKSALMON(233, 150, 122),
		CRIMSON(220, 20, 60),
		RED(255, 0, 0),
		FIREBRICK(178, 34, 34),
		DARKRED(139, 0, 0),
		PINK(255, 192, 203),
		LIGHTPINK(255, 182, 193),
		HOTPINK(255, 105, 180),
		DEEPPINK(255, 20, 147),
		MEDIUMVIOLETRED(199, 21, 133),
		PALEVIOLETRED(219, 112, 147),
		LIGHTSALMON(255, 160, 122),
		CORAL(255, 127, 80),
		TOMATO(255, 99, 71),
		ORANGERED(255, 69, 0),
		DARKORANGE(255, 140, 0),
		ORANGE(255, 165, 0),
		GOLD(255, 215, 0),
		YELLOW(255, 255, 0),
		LIGHTYELLOW(255, 255, 224),
		LEMONCHIFFON(255, 250, 205),
		LIGHTGOLDENRODYELLOW(250, 250, 210),
		PAPAYAWHIP(255, 239, 213),
		MOCCASIN(255, 228, 181),
		PEACHPUFF(255, 218, 185),
		PALEGOLDENROD(238, 232, 170),
		KHAKI(240, 230, 140),
		DARKKHAKI(189, 183, 107),
		LAVENDER(230, 230, 250),
		THISTLE(216, 191, 216),
		PLUM(221, 160, 221),
		VIOLET(238, 130, 238),
		ORCHID(218, 112, 214),
		FUCHSIA(255, 0, 255),
		MAGENTA(255, 0, 255),
		MEDIUMORCHID(186, 85, 211),
		MEDIUMPURPLE(147, 112, 219),
		REBECCAPURPLE(102, 51, 153),
		BLUEVIOLET(138, 43, 226),
		DARKVIOLET(148, 0, 211),
		DARKORCHID(153, 50, 204),
		DARKMAGENTA(139, 0, 139),
		PURPLE(128, 0, 128),
		INDIGO(75, 0, 130),
		SLATEBLUE(106, 90, 205),
		DARKSLATEBLUE(72, 61, 139),
		GREENYELLOW(173, 255, 47),
		CHARTREUSE(127, 255, 0),
		LAWNGREEN(124, 252, 0),
		LIME(0, 255, 0),
		LIMEGREEN(50, 205, 50),
		PALEGREEN(152, 251, 152),
		LIGHTGREEN(144, 238, 144),
		MEDIUMSPRINGGREEN(0, 250, 154),
		SPRINGGREEN(0, 255, 127),
		MEDIUMSEAGREEN(60, 179, 113),
		SEAGREEN(46, 139, 87),
		FORESTGREEN(34, 139, 34),
		GREEN(0, 128, 0),
		DARKGREEN(0, 100, 0),
		YELLOWGREEN(154, 205, 50),
		OLIVEDRAB(107, 142, 35),
		OLIVE(128, 128, 0),
		DARKOLIVEGREEN(85, 107, 47),
		MEDIUMAQUAMARINE(102, 205, 170),
		DARKSEAGREEN(143, 188, 139),
		LIGHTSEAGREEN(32, 178, 170),
		DARKCYAN(0, 139, 139),
		TEAL(0, 128, 128),
		AQUA(0, 255, 255),
		CYAN(0, 255, 255),
		LIGHTCYAN(224, 255, 255),
		PALETURQUOISE(175, 238, 238),
		AQUAMARINE(127, 255, 212),
		TURQUOISE(64, 224, 208),
		MEDIUMTURQUOISE(72, 209, 204),
		DARKTURQUOISE(0, 206, 209),
		CADETBLUE(95, 158, 160),
		STEELBLUE(70, 130, 180),
		LIGHTSTEELBLUE(176, 196, 222),
		POWDERBLUE(176, 224, 230),
		LIGHTBLUE(173, 216, 230),
		SKYBLUE(135, 206, 235),
		LIGHTSKYBLUE(135, 206, 250),
		DEEPSKYBLUE(0, 191, 255),
		DODGERBLUE(30, 144, 255),
		CORNFLOWERBLUE(100, 149, 237),
		MEDIUMSLATEBLUE(123, 104, 238),
		ROYALBLUE(65, 105, 225),
		BLUE(0, 0, 255),
		MEDIUMBLUE(0, 0, 205),
		DARKBLUE(0, 0, 139),
		NAVY(0, 0, 128),
		MIDNIGHTBLUE(25, 25, 112),
		CORNSILK(255, 248, 220),
		BLANCHEDALMOND(255, 235, 205),
		BISQUE(255, 228, 196),
		NAVAJOWHITE(255, 222, 173),
		WHEAT(245, 222, 179),
		BURLYWOOD(222, 184, 135),
		TAN(210, 180, 140),
		ROSYBROWN(188, 143, 143),
		SANDYBROWN(244, 164, 96),
		GOLDENROD(218, 165, 32),
		DARKGOLDENROD(184, 134, 11),
		PERU(205, 133, 63),
		CHOCOLATE(210, 105, 30),
		SADDLEBROWN(139, 69, 19),
		SIENNA(160, 82, 45),
		BROWN(165, 42, 42),
		MAROON(128, 0, 0),
		WHITE(255, 255, 255),
		SNOW(255, 250, 250),
		HONEYDEW(240, 255, 240),
		MINTCREAM(245, 255, 250),
		AZURE(240, 255, 255),
		ALICEBLUE(240, 248, 255),
		GHOSTWHITE(248, 248, 255),
		WHITESMOKE(245, 245, 245),
		SEASHELL(255, 245, 238),
		BEIGE(245, 245, 220),
		OLDLACE(253, 245, 230),
		FLORALWHITE(255, 250, 240),
		IVORY(255, 255, 240),
		ANTIQUEWHITE(250, 235, 215),
		LINEN(250, 240, 230),
		LAVENDERBLUSH(255, 240, 245),
		MISTYROSE(255, 228, 225),
		GAINSBORO(220, 220, 220),
		LIGHTGRAY(211, 211, 211),
		SILVER(192, 192, 192),
		DARKGRAY(169, 169, 169),
		GRAY(128, 128, 128),
		DIMGRAY(105, 105, 105),
		LIGHTSLATEGRAY(119, 136, 153),
		SLATEGRAY(112, 128, 144),
		DARKSLATEGRAY(47, 79, 79),
		BLACK(0, 0, 0)

		int r
		int g
		int b

		Color color
		private HTMLColor(int r, int g, int b){
			this.r = r
			this.g = g
			this.b = b
		}

		Color color(){
			return new Color((r / 255).toFloat(), (g / 255).toFloat(), (b / 255).toFloat(), 1)
		}
	}

}
