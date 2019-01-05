package team.rpsg.html.util

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.steadystate.css.dom.CSSValueImpl
import groovy.transform.CompileStatic
import team.rpsg.html.dom.Dom

@CompileStatic
class BoxParser {
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
				def v = SizeParser.parse(values[0])
				if(v)
					padLeft = padRight = padTop = padBottom = v.get(dom)
			}else if(values.size() == 2){
				def v0 = SizeParser.parse(values[0]), v1 = SizeParser.parse(values[1])
				if(v0)
					padTop = padBottom = v0.get(dom)
				if(v1)
					padLeft = padRight = v1.get(dom)
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
