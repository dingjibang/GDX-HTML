package team.rpsg.html.util

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.steadystate.css.dom.CSSValueImpl
import groovy.transform.CompileStatic
import team.rpsg.html.dom.Dom

class BoxParser {
	@CompileStatic
	static void parse(Dom dom){
		float padLeft = 0, padRight = 0, padTop = 0, padBottom = 0

		def padAll = dom.style("padding", null, {r -> r}, false) as CSSValueImpl

		if(padAll) {

			List values = []

			if (padAll.value instanceof List){
				values = padAll.value as List
			}else{
				values << padAll
			}

			if(values.size() == 1) {
				padLeft = padRight = padTop = padBottom = SizeParser.parse(values[0]).get(dom)
			}else if(values.size() == 2){
				padTop = padBottom = SizeParser.parse(values[0]).get(dom)
				padLeft = padRight = SizeParser.parse(values[1]).get(dom)
			}
		}

		padLeft = parseValue(dom, "padding-left", padLeft)
		padRight = parseValue(dom, "padding-right", padRight)
		padTop = parseValue(dom, "padding-top", padTop)
		padBottom = parseValue(dom, "padding-bottom", padBottom)

		dom.pad(padTop, padLeft, padBottom, padRight)

	}

	private static int parseValue(Dom dom, String name, float orDefault = 0){
		def result = dom.style(name, orDefault, SizeParser.&parse, false) as Value
		if(!result)
			return orDefault

		return result.get(dom)
	}
}
