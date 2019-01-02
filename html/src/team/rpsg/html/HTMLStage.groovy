package team.rpsg.html

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.steadystate.css.dom.CSSRuleListImpl
import com.steadystate.css.dom.CSSStyleDeclarationImpl
import com.steadystate.css.dom.CSSStyleRuleImpl
import com.steadystate.css.parser.CSSOMParser
import com.steadystate.css.parser.SACParserCSS3
import groovy.transform.CompileStatic
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.w3c.css.sac.InputSource
import org.w3c.css.sac.Selector
import org.w3c.dom.css.CSSStyleSheet
import team.rpsg.html.dom.Root
import team.rpsg.html.dom.Dom
import team.rpsg.html.manager.ResourceManager

class HTMLStage extends Stage{

	Document document
	Dom rootDom
	ResourceManager res
	private HTMLStage(Document document) {
		this.res = new ResourceManager()
		this.document = document

		InputSource source = new InputSource(new StringReader("h1{background: #ffcc44; margin-top: 0; color: green; font-size: 20px} h1 div{color: red}"))
		CSSOMParser parser = new CSSOMParser(new SACParserCSS3())
		CSSStyleSheet styleSheet = parser.parseStyleSheet(source, null, null)

		CSSRuleListImpl rules = styleSheet.getCssRules() as CSSRuleListImpl
		for(def i = 0; i < rules.getLength(); i++){
			CSSStyleRuleImpl item = rules.item(i) as CSSStyleRuleImpl

			for(def j = 0; j < item.selectors.getLength(); j++) {
				Selector selector = item.selectors.item(j)

				document.select(selector.toString()).childNodes.each { it.each {node ->
					node.styles << ((item.style as CSSStyleDeclarationImpl).properties)
				}}
			}
		}

		debugAll = document.getElementsByTag("html").attr("debug").equalsIgnoreCase("true")

		forceBuild()
	}

	def forceBuild(){
		this.clear()

		if(rootDom)
			rootDom.dispose()

		addActor(rootDom = new Root(document.body(), this))
	}



	static HTMLStage buildHTML(String html){
		buildDocument Jsoup.parse(html)
	}

	static HTMLStage buildDocument(Document document){
		new HTMLStage(document)
	}

	static HTMLStage buildPath(String path){
		buildHTML Gdx.files.internal(path).readString("utf-8")
	}

	void dispose() {
		res.dispose()
		super.dispose()
	}
}
