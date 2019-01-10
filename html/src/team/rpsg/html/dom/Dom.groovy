package team.rpsg.html.dom

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.utils.Align
import groovy.transform.CompileStatic
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import team.rpsg.html.HTMLStage
import team.rpsg.html.dom.node.Image
import team.rpsg.html.dom.node.Text
import team.rpsg.html.manager.ResourceManager
import team.rpsg.html.manager.widget.AutoSizeContainer
import team.rpsg.html.util.AlignParser
import team.rpsg.html.util.BoxParser
import team.rpsg.html.util.ColorParser
import team.rpsg.html.util.DomParser
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
@CompileStatic
class Dom extends VerticalGroup {
	HorizontalGroup current
	Node node
	ResourceManager res

	String display
	Value widthValue, heightValue
	boolean needsRow = false

	Color backgroundColor = null
	SpriteDrawable backgroundDrawable = null

	Container<Dom> parentContainer
	Dom parentDom

	int textAlign = Align.left
	int boxAlign = Align.left
	Integer verticalAlign = null

	TableLayout tableLayout

	private boolean needsCalcWidth = false, needsCalcHeight = false
	private boolean isParseDisplay = false


	Dom(){

	}

	void applyBackgroundColor(Color color){
		backgroundDrawable = color ? getRes().defaultDrawable(color) : null

		if(this.clipBegin())
			this.clipEnd()
	}

	void draw(Batch batch, float parentAlpha) {
		backgroundDrawable?.draw(batch, x, y, width, height)
		super.draw(batch, parentAlpha)
	}

	Dom(Node node){
		this.node = node
		align(AlignParser.join(boxAlign, verticalAlign ?: Align.top))
		row()

		Dom that = this
		this.touchable = Touchable.enabled
		this.addListener(new ClickListener(){
			boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

				println("==============")
				println(that.node)
				println(that.x + ", " + that.y + ", " + that.width + ", " + that.height)
				println("align: ${AlignParser.toString(that.align)}, " +
						"current: ${AlignParser.toString(current.align)}, " +
						"parent: ${AlignParser.toString(parentDom ? parentDom.align : -1)}, " +
						"cparent: ${AlignParser.toString(parentDom ? parentDom.current.align : -1)}" +
					"")
				println("textAlign: ${AlignParser.toString(that.textAlign)}, " +
						"mAlign: ${AlignParser.toString(that.boxAlign)}, " +
						"vAlign: ${AlignParser.toString(that.verticalAlign)}" +
					"")

				if(that instanceof Text)
					return true
				event.cancel()
				return false
			}
		})
	}

	ResourceManager getRes(){
		(stage as HTMLStage).res
	}


	void row(){
		addActor(current = new HorizontalGroup())
		current.align(AlignParser.join(textAlign, verticalAlign ?: Align.bottom))
	}

	private void parseDisplay() {
		isParseDisplay = true

		display = style("display", "inline", {r -> r}, false)

		switch (display.toLowerCase()){
			case "block":
				needsRow = true;
				break
			case "table":
			case "table-row":
				tableLayout = new TableLayout(this)
				needsRow = true
				break
			case "table-cell":
				tableLayout = new TableLayout(this)
				break
			case "none":
				visible = false; break
		}
	}

	void parse(){
		if(!isParseDisplay)
			parseDisplay()


		this.backgroundColor = style("background-color", null, {r -> ColorParser.parse(r, null)}, false) as Color

		//parsePadding padding
		BoxParser.parsePadding this
		def parsed = BoxParser.parseMargin this
		if(parsed == BoxParser.NEEDS_SET_ALIGN){
			parentDom?.current?.align(AlignParser.join(boxAlign, verticalAlign ?: Align.bottom))
			parentDom?.align(AlignParser.join(boxAlign, verticalAlign ?: Align.top))
		}


		def widthParser = {v -> SizeParser.parse(v, false, SizeParser.&percentInnerWidth)}
		widthValue = style("width", display == "block" ? "100%" : "auto", widthParser, false) as Value
		if(!widthValue) widthValue = style("max-width", display == "block" ? "100%" : "auto", widthParser, false) as Value
		if(!widthValue) widthValue = style("min-width", display == "block" ? "100%" : "auto", widthParser, false) as Value


		def heightParser = {v -> SizeParser.parse(v, false, SizeParser.&percentInnerHeight)}
		heightValue = style("height", "auto", heightParser, false) as Value
		if(!heightValue) heightValue = style("max-height", "auto", heightParser, false) as Value
		if(!heightValue) heightValue = style("min-height", "auto", heightParser, false) as Value

		textAlign = style("text-align", "left", AlignParser.&textAlign) as int
		verticalAlign = style("vertical-align", null, AlignParser.&verticalAlign) as Integer

		current.align(AlignParser.join(textAlign, verticalAlign ?: Align.bottom))
		align(AlignParser.join(textAlign, verticalAlign ?: Align.top))

		if(!parentDom)
			return

		if(widthValue == null){
			needsCalcWidth = true
		}else{
			width = (widthValue.get(parentDom) - marginLeftRight) as float
		}

		if(heightValue == null){
			needsCalcHeight = true
		}else{
			height = (heightValue.get(parentDom) - marginTopBottom) as float
		}

	}

	void layout() {
		super.layout()
		if(needsCalcWidth)
			width = prefWidth
		if(needsCalcHeight)
			height = prefHeight
	}

	float getInnerWidth() {
		super.width - padLeft - padRight
	}

	float getInnerHeight() {
		super.height - padTop - padBottom
	}

	float getMarginLeftRight(){
		parentContainer ? parentContainer.padLeft + parentContainer.padRight : 0
	}

	float getMarginTopBottom(){
		parentContainer ? parentContainer.padTop + parentContainer.padBottom : 0
	}

	void build(){
		if(backgroundColor)
			applyBackgroundColor(backgroundColor)
	}

	String toString(){
		node.toString()
	}

	def style(String name, orDefault = null, Closure parser = {r -> r}, inherit = true){
		def result = null

		node.allStyles.each {it.findAll({it.name == name}).each({result = it.value})}

		if(result && result.toString() == "inherit"){
			inherit = true
			result = null
		}

		if(result)
			return parser(result)

		if(!inherit)
			return parser(orDefault)

		if(!parentDom)
			return parser(orDefault)

		return parentDom.style(name, orDefault, parser, inherit)
	}

	def buildChild(){
		node.childNodes().each {
			Dom child = DomParser.parse(it)

			child.parent = child.parentDom = this
			child.stage = stage

			child.parseDisplay()

			if(child.display in TableLayout.TAG_NAME){
				if(this.tableLayout)
					tableLayout.parent = this.tableLayout

			} else if(child.needsRow || child.display == "inline-block"){
				def container = new AutoSizeContainer(child)
				child.parentContainer = container
				child.stage = stage

				child.parse()

				container.width(new Value() {
					float get(Actor context) {
						return Math.max((context as Dom).prefWidth, context.width)
					}
				})

				container.height(new Value() {
					float get(Actor context) {
						return Math.max((context as Dom).prefHeight, context.height)
					}
				})

				if(child.needsRow){
					row()
					current.addActor(container)
					row()
				}else if(child.display == "inline-block"){
					current.addActor(container)
				}

			}else{
				child.parse()
				current.addActor(child)
			}





			child.build()
			child.buildChild()
		}
	}

}
