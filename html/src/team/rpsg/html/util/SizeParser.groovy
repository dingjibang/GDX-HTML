package team.rpsg.html.util

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.scenes.scene2d.utils.Layout
import com.steadystate.css.dom.CSSValueImpl
import com.steadystate.css.parser.LexicalUnitImpl
import groovy.transform.CompileStatic
import team.rpsg.html.dom.Dom

/**
 * unit(px or %) parser
 */
@CompileStatic
class SizeParser {
	/**
	 * font-size: 22px;
	 * font-size: auto;
	 */
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
	static Value parse(Object property, parseAutoValue = false, Closure valueParser = this.&percentInnerWidth){

		if(property && property.toString().equalsIgnoreCase("auto")){
			if(parseAutoValue)
				return new AutoValue()
			return null
		}

		if(property instanceof Float)
			return new Value.Fixed(property.toFloat())

		if(property instanceof CSSValueImpl && property.value instanceof LexicalUnitImpl){
			def unit = property.value as LexicalUnitImpl

			if(unit.lexicalUnitType == 17)
				return new Value.Fixed(unit.floatValue)

			if(unit.lexicalUnitType == 13 || property.toString().equals("0"))
				return new Value.Fixed(0)

			if(unit.lexicalUnitType == 23)
				return valueParser((unit.floatValue / 100f) as float) as Value
		}

		if(property instanceof String){
			if(property.indexOf('%') > 0)
				return valueParser((property.toString().replace("%", "").toFloat() / 100f) as float) as Value
			if(property.indexOf("px") > 0)
				return new Value.Fixed(property.toString().replace("px", "").toInteger())
		}


		return null
	}

	static Value percentInnerWidth(float p){
		return new percentInnerValue() {
			float getPercent() {
				p
			}

			float get (Actor actor) {
				if(actor instanceof Dom)
					return (actor as Dom).innerWidth * p

				return (actor as Table).width * p
			}
		}
	}

	static Value percentInnerHeight(float p){
		return new percentInnerValue() {
			float getPercent() {
				p
			}

			float get (Actor actor) {
				if(actor instanceof Dom)
					return (actor as Dom).innerHeight * p

				return (actor as Table).height * p
			}
		}
	}

	static final Value innerWidth = percentInnerWidth(1)
	static final Value innerHeight = percentInnerHeight(1)

	static class AutoValue extends Value{
		float get(Actor context) {
			return 0
		}
	}

	abstract static class percentInnerValue extends Value{
		abstract float getPercent()
	}
}
