package team.rpsg.html.dom

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Disposable
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import team.rpsg.html.HTMLStage
import team.rpsg.html.manager.ResourceManager

class Dom extends Table implements Disposable{
	Node node
	ResourceManager res

	String display
	boolean needsRow = false


	Dom(){
	}

	Dom(Node node){
		this.node = node
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

	void parse(){
        display = style("display", "inline")

		switch (display.toLowerCase()){
			case "block": needsRow = true
		}

	}

	void build(){
	}

	Dom getParentDom(){
		parent ? (parent as Dom) : null
	}

	void dispose(){}

	String toString(){
		node.toString()
	}

	def style(String name, orDefault = null, parser = {r -> r}, isParent = false){
		def result = orDefault

		(isParent ? node.parent() : node).allStyles.each {
			it.findAll({it.name == name}).each({result = it.value})
		}

		return parser(result)
	}

	def buildChild(){
		node.childNodes().each {
			Dom child = parse(it)
			child.parse()

			if(child.needsRow)
				row()

			def res = add(child).align(Align.bottomLeft)

			if(child.needsRow)
				res.colspan(9999).row()

			child.build()
			child.buildChild()
		}
	}

}
