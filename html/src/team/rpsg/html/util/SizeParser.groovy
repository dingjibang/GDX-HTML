package team.rpsg.html.util

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.steadystate.css.dom.CSSValueImpl
import com.steadystate.css.parser.LexicalUnitImpl
import groovy.transform.CompileStatic
import team.rpsg.html.dom.Dom

/**
 * unit(px or %) parser
 */
class SizeParser {
	/**
	 * font-size: 22px;
	 * font-size: auto;
	 */
	static int parseFontPX(Object property){
		if(property.toString().indexOf('%') > 0)
			return 16
		property.toString().replace("px", "").toInteger()
	}

	/**
	 * width: 80%;
	 * width: 65px;
	 * width: auto;
	 */
	static Value parse(Object property){
		if(property instanceof CSSValueImpl && property.value instanceof LexicalUnitImpl){
			def unit = property.value as LexicalUnitImpl

			if(unit.lexicalUnitType == 17)
				return new Value.Fixed(unit.floatValue)

			if(unit.lexicalUnitType == 23)
				return percentInnerWidth((unit.floatValue / 100f) as float)
		}

		if(property instanceof String){
			if(property.indexOf('%') > 0)
				return percentInnerWidth((property.toString().replace("%", "").toFloat() / 100f) as float)
			if(property.indexOf("px") > 0)
				return new Value.Fixed(property.toString().replace("px", "").toInteger())
		}


		return null
	}

	@CompileStatic
	static Value percentInnerWidth(percent){
		return new Value() {
			float get (Actor actor) {
				(actor as Dom).innerWidth * percent
			}
		}
	}
}
