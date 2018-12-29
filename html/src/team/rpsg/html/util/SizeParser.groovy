package team.rpsg.html.util

import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.steadystate.css.dom.CSSValueImpl
import com.steadystate.css.parser.LexicalUnitImpl

/**
 * unit(px or %) parser
 */
class SizeParser {
	static int parsePX(Object property){
		if(property.toString().indexOf('%') > 0)
			return 16
		property.toString().replace("px", "").toInteger()
	}

	static Value parse(Object property){
		if(property instanceof CSSValueImpl && property.value instanceof LexicalUnitImpl){
			def unit = property.value as LexicalUnitImpl

			if(unit.lexicalUnitType == 17)
				return new Value.Fixed(unit.floatValue)

			if(unit.lexicalUnitType == 23)
				return Value.percentWidth(unit.floatValue)
		}


		return null
	}
}
