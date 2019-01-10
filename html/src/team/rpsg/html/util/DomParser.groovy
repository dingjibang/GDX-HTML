package team.rpsg.html.util

import groovy.transform.CompileStatic
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import team.rpsg.html.dom.Dom
import team.rpsg.html.dom.HTMLDomPreset
import team.rpsg.html.dom.UnknownDom
import team.rpsg.html.dom.node.Image
import team.rpsg.html.dom.node.Text


@CompileStatic
class DomParser {
	/**
	 * parse jsoup node to renderable GDX-HTML dom.
	 */
	static Dom parse(Node node){
		switch (node.class){

			case TextNode.class:
				return new Text(node)


			case Element.class:
				def ele = node as Element

				def tagName = ele.tagName().toLowerCase()
				switch (tagName){
					case "img": return new Image(ele)
				}


				try{
					return HTMLDomPreset.valueOf(ele.tagName().toUpperCase()).dom(node)
				}catch (ignored){
					return new UnknownDom(node)
				}


			default:
				return new UnknownDom(node)

		}
	}

}
