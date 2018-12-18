package team.rpsg.html.dom

import com.steadystate.css.dom.CSSStyleDeclarationImpl
import com.steadystate.css.dom.Property
import com.steadystate.css.parser.CSSOMParser
import com.steadystate.css.parser.SACParserCSS3
import groovy.transform.CompileStatic
import org.w3c.css.sac.InputSource

@CompileStatic
enum HTMLDomPreset {
	DIV("display: block;"),
	SPAN("")

	private List<Property> styles

	private HTMLDomPreset(String stylesString){
		if(stylesString.length() == 0){
			styles = new ArrayList<>()
			return
		}

		CSSStyleDeclarationImpl dec = (CSSStyleDeclarationImpl)new CSSOMParser(new SACParserCSS3()).parseStyleDeclaration(new InputSource(new StringReader(stylesString)))
		styles = dec.properties
	}

	HTMLDom dom(org.jsoup.nodes.Node node){
		return new HTMLDom(node, styles, name())
	}
}
