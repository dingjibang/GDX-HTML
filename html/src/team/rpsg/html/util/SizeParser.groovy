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
	@CompileStatic
	static int parseFontPX(Object property){

		if(property.toString().indexOf('%') > 0)
			return 16
		if(property.toString().equals("0"))
			return 0
		if(property.toString().indexOf('px') > 0)
			return property.toString().replace("px", "").toInteger()?: 16
		if(property.toString().indexOf('rem') > 0)
			return ((property.toString().replace("rem", "").toFloat() * 16f)?: 16) as int
		if(property.toString().indexOf('em') > 0)
			return ((property.toString().replace("em", "").toFloat() * 16f)?: 16) as int

		return 16

	}

	/**
	 * width: 80%;
	 * width: 65px;
	 * width: auto;
	 */
	static Value parse(Object property){
		if(property instanceof Float)
			return new Value.Fixed(property.toFloat())

		if(property instanceof CSSValueImpl && property.value instanceof LexicalUnitImpl){
			def unit = property.value as LexicalUnitImpl

			if(unit.lexicalUnitType == 17)
				return new Value.Fixed(unit.floatValue)

			if(unit.lexicalUnitType == 13 || property.toString().equals("0"))
				return new Value.Fixed(0)

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
