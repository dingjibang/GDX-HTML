package team.rpsg.html.dom

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Disposable
import com.steadystate.css.dom.Property
import groovy.transform.CompileStatic
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import team.rpsg.html.HTMLStage
import team.rpsg.html.manager.ResourceManager

class Dom extends Table implements Disposable{
	Node node
	ResourceManager res


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
			case TextNode.class: return new Text(node)
			default: return new UnknownDom(node)
		}
	}

	void parse(){}
	void build(){}
	void dispose(){}

	String toString(){
		node.toString()
	}

	def style(String name, orDefault = null, parser = {r -> r}){
		def result = orDefault

		node.styles.each {
			it.findAll({it.name == name}).each({result = it.value})
		}

		return parser(result)
	}

	def buildChild(){
		node.childNodes().each {
			if(it instanceof TextNode && it.toString().trim().length() == 0)
				return

			Dom child = parse(it)
			add(child)
			child.parse()
			child.build()
			child.buildChild()
		}
	}

}
