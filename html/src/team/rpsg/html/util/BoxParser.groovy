package team.rpsg.html.util


import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.utils.Align
import com.steadystate.css.dom.CSSValueImpl
import groovy.transform.CompileStatic
import team.rpsg.html.dom.Dom

@CompileStatic
class BoxParser {
	static void parsePadding(Dom dom){
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

		padLeft = parseValueInt(dom, "padding-left", padLeft)
		padRight = parseValueInt(dom, "padding-right", padRight)
		padTop = parseValueInt(dom, "padding-top", padTop)
		padBottom = parseValueInt(dom, "padding-bottom", padBottom)

		dom.pad(padTop, padLeft, padBottom, padRight)

	}

	/**
	 * @return parsed
	 */
	public static final int NEEDS_SET_ALIGN = 1

	static int parseMargin(Dom dom){
		if(dom.display != "block" && dom.display != "inline-block" && !dom.parentContainer)
			return -1

		float mLeft = 0, mRight = 0, mTop = 0, mBottom = 0
		boolean mLeftAuto = false, mRightAuto = false

		def mAll = dom.style("margin", null, {r -> r}, false) as CSSValueImpl

		if(mAll) {

			List values = []

			if (mAll.value instanceof List){
				values = mAll.value as List
			}else{
				values << mAll
			}

			if(values.size() == 1) {
				def v = SizeParser.parse(values[0], true)
				if(v instanceof SizeParser.AutoValue){
					mLeftAuto = mRightAuto = true
					mLeft = mRight = 0
				} else if(v){
					mLeft = mRight = mTop = mBottom = v.get(dom)
				}
			}else if(values.size() == 2){
				def v0 = SizeParser.parse(values[0]), v1 = SizeParser.parse(values[1], true)
				if(v0)
					mTop = mBottom = v0.get(dom)

				if(v1 instanceof SizeParser.AutoValue){
					mLeftAuto = mRightAuto = true
					mLeft = mRight = 0
				}else if(v1){
					mLeft = mRight = v1.get(dom)
				}
			}
		}

		Object _l = parseValue(dom, "margin-left", mLeft)
		if(_l instanceof SizeParser.AutoValue){
			mLeftAuto = true
			mLeft = 0
		} else {
			mLeft = _l as int
		}

		Object _r = parseValue(dom, "margin-right", mRight)
		if(_r instanceof SizeParser.AutoValue){
			mRightAuto = true
			mRight = 0
		} else {
			mRight = _r as int
		}

		mTop = parseValueInt(dom, "margin-top", mTop)
		mBottom = parseValueInt(dom, "margin-bottom", mBottom)

		dom.parentContainer.pad(mTop, mLeft, mBottom, mRight)

		int align = Align.left
		if(mLeftAuto)
			align = Align.right
		if(mLeftAuto && mRightAuto)
			align = Align.center

		def result = 0
		if(align != Align.left)
			result = NEEDS_SET_ALIGN

		dom.boxAlign = align

		return result

	}

	private static int parseValueInt(Dom dom, String name, float orDefault = 0){
		def result = dom.style(name, orDefault, SizeParser.&parse, false) as Value

		if(!result)
			return orDefault

		return result.get(dom)
	}

	private static Object parseValue(Dom dom, String name, float orDefault = 0){
		def result = dom.style(name, orDefault, {p -> SizeParser.parse(p, true)}, false) as Value

		if(!result)
			return orDefault

		if(result instanceof SizeParser.AutoValue)
			return result

		return result.get(dom)
	}
}
