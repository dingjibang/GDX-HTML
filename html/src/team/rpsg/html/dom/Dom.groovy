package team.rpsg.html.dom

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.utils.Align
import com.steadystate.css.dom.CSSValueImpl
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import team.rpsg.html.HTMLStage
import team.rpsg.html.manager.ResourceManager
import team.rpsg.html.util.BoxParser
import team.rpsg.html.util.ColorParser
import team.rpsg.html.util.SizeParser

/**
 * HTML Dom<br/>
 * Dom has a VerticalGroup(col) that manages multiple HorizontalGroup(row).<br/>
 * It contains some basic css properties (such as width, padding, margin, display...)<br/>
 * The implementation of Dom's "display: block;" property is ugly.^.^
 * <br/>
 * <br/>
 * HTML的Dom的实现，每个Dom类似一个Table表格，拥有一个VerticalGroup(行)和多个HorizontalGroup(列)，用来装他的子元素。<br/>
 * Dom包含了一些基本的css属性，比如宽度，padding，margin，display之类的。<br/>
 * 顺便一提的是，"display: block;"的实现是很丑陋的hhhh
 */
class Dom extends VerticalGroup {
	HorizontalGroup current
	Node node
	ResourceManager res

	String display
	Value widthValue
	boolean needsRow = false

	Color backgroundColor = null
	SpriteDrawable backgroundDrawable = null

	Dom(){

	}

	void setBackgroundColor(Color color){
		backgroundColor = color
		backgroundDrawable = color ? getRes().defaultDrawable(color) : null
	}

	void draw(Batch batch, float parentAlpha) {
		backgroundDrawable?.draw(batch, x, y, width, prefHeight)
		super.draw(batch, parentAlpha)
	}

	Dom(Node node){
		this.node = node
		align(Align.topLeft)
		row()

		Dom that = this
		this.addListener(new ClickListener(){
			boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				event.bubbles = false
				println("==============")
				println(that.node)
				println(that.x + ", " + that.y + ", " + that.width + ", " + that.height)

			}
		})
	}

	ResourceManager getRes(){
		(stage as HTMLStage).res
	}

	static Dom parse(Node node){
		switch (node.class){

			case TextNode.class:
				return new Text(node)


			case Element.class:
				def ele = node as Element
				try{
					return HTMLDomPreset.valueOf(ele.tagName().toUpperCase()).dom(node)
				}catch (ignored){
					return new UnknownDom(node)
				}


			default:
				return new UnknownDom(node)

		}
	}

	void row(){
		addActor(current = new HorizontalGroup())
		current.align(Align.bottomLeft)
	}

	void parse(){
        display = style("display", "inline", {r -> r}, false)

		switch (display.toLowerCase()){
			case "block":
				needsRow = true
		}


		def bg = style("background-color", null, {r -> ColorParser.parse(r, null)}, false)
		if(bg)
			setBackgroundColor(bg as Color)

		//parse padding
		BoxParser.parse this


		widthValue = style("width", display == "block" ? "100%" : "auto", SizeParser.&parse, false)

		def p = getParentDom()
		if(!p)
			return

		if(widthValue != null){
			width = widthValue.get(p)
		}else if(widthValue == null){
			width = p.width
		}

		println(node)
		println(width)
		println("===============")

	}

	float getWidth() {
		super.width
	}

	float getInnerWidth() {
		super.width - padLeft - padRight
	}

	void build(){
	}

	Dom getParentDom(_parent = null){
		if(!_parent)
			_parent = this

		if(!_parent.parent)
			return null

		if(_parent.parent instanceof Dom)
			return _parent.parent as Dom

		return getParentDom(_parent.parent)
	}

	String toString(){
		node.toString()
	}

	def style(String name, orDefault = null, parser = {r -> r}, inherit = true){
		def result = null

		node.allStyles.each {it.findAll({it.name == name}).each({result = it.value})}

		if(result)
			return parser(result)

		if(!inherit)
			return parser(orDefault)

		def pDom = parentDom

		if(!pDom)
			return parser(orDefault)

		return pDom.style(name, orDefault, parser, inherit)
	}

	def buildChild(){
		node.childNodes().each {
			Dom child = parse(it)
			child.parent = this
			child.stage = stage
			child.parse()

			def container = new Container(child)
			container.align(Align.bottomLeft)

			if(child.widthValue)
				container.width(Value.percentWidth(1))

			if(child.needsRow){
				row()
				current.addActor(container)
				row()
			}else{
				current.addActor(child)
			}



			child.build()
			child.buildChild()
		}
	}

}
