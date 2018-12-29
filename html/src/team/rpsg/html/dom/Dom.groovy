package team.rpsg.html.dom

import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Disposable
import groovy.transform.CompileStatic
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import team.rpsg.html.HTMLStage
import team.rpsg.html.manager.ResourceManager
import team.rpsg.html.util.SizeParser

class Dom extends VerticalGroup {
	HorizontalGroup current
	Node node
	ResourceManager res

	String display
	Value widthValue
	boolean needsRow = false



	Dom(){
	}

	Dom(Node node){
		this.node = node
		align(Align.topLeft)
		row()
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
        display = style("display", "inline", {r -> r}, false, false)

		switch (display.toLowerCase()){
			case "block":
				needsRow = true
		}



		widthValue = style("width", display == "block" ? "100%" : "auto", SizeParser.&parse, false, false)

		if(widthValue){
			width = widthValue.get(this)
		}else{
			def p = getParentDom()
			if(p)
				width = p.width
		}

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

	def style(String name, orDefault = null, parser = {r -> r}, isParent = false, inherit = true){
		def result = null

		(isParent ? node.parent() : node).allStyles.each {
			it.findAll({it.name == name}).each({result = it.value})
		}

		if(result)
			return parser(result)

		if(!inherit)
			return parser(orDefault)

		def pDom = parentDom

		if(!pDom)
			return parser(orDefault)

		return pDom.style(name, orDefault, parser, isParent, inherit)
	}

	def buildChild(){
		node.childNodes().each {
			Dom child = parse(it)
			child.parent = this
			child.parse()

			def container = new Container(child)
			container.align(Align.bottomLeft)

			if(child.widthValue)
				container.width(child.widthValue)

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
