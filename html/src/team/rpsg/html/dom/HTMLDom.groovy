package team.rpsg.html.dom

import com.steadystate.css.dom.Property
import org.jsoup.nodes.Node

class HTMLDom extends Dom {
	private String tagName

	HTMLDom(Node node, List<Property> styles, String tagName){
		super(node)
		this.node.defaultStyles = styles
		this.tagName = tagName
	}

	String toString() {
		return "HTMLDom(${tagName}) ${super.toString()}"
	}
}
