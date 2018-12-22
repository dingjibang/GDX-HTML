package team.rpsg.html.dom

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
				}catch (e){
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
        display = style("display", "inline")

		switch (display.toLowerCase()){
			case "block":
				needsRow = true
		}



//		widthValue = style("width", inhe)

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
		def result = orDefault

		(isParent ? node.parent() : node).allStyles.each {
			it.findAll({it.name == name}).each({result = it.value})
		}

		return parser(result)
	}

	def buildChild(){
		if(parentDom)
			width = parentDom.width

		println width
		node.childNodes().each {
			Dom child = parse(it)
			child.parse()

			if(child.needsRow){
				row()

				def boxProxy = new Table().align(Align.bottomLeft)
				boxProxy.width = width
				boxProxy.add(child).width(Value.percentWidth(100)).align(Align.bottomLeft)
				current.addActor(boxProxy)

				row()
			}else{
				current.addActor(child)
			}



			child.build()
			child.buildChild()
		}

	}

}
