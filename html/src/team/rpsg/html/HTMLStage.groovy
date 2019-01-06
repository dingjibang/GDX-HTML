package team.rpsg.html

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.steadystate.css.dom.CSSRuleListImpl
import com.steadystate.css.dom.CSSStyleDeclarationImpl
import com.steadystate.css.dom.CSSStyleRuleImpl
import com.steadystate.css.dom.CSSUnknownRuleImpl
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
import team.rpsg.html.util.PathParser

/**
 * GDX-HTML<br>
 *     using HTMLStage.buildPath(path-to-file) to create your first stage.
 */


@CompileStatic
class HTMLStage extends Stage{

	Document document
	Dom rootDom
	ResourceManager res

	boolean needsRefresh = false

	List<String> styles = []

	private HTMLStage(Document document) {
		this.res = new ResourceManager()
		this.document = document

		def base = document.getElementsByTag("base")
		if(base.size() != 0){
			this.document.baseUri = base.last().attr("href")
		}

		forceBuild()
	}

	void forceBuild(){
		this.clear()
		styles.clear()

		document.getElementsByTag("style").each {styles << it.childNode(0).toString()}
		document.select("link[rel=\"stylesheet\"]").each {
			PathParser.get(document, it.attr("href"), it.attr("charset"), {String s, boolean needsUpdate ->
				styles << s
				if(needsUpdate)
					joinStyles(s)
			})
		}


		joinStyles()

		debugAll = document.getElementsByTag("html").attr("debug").equalsIgnoreCase("true")

		refresh()
	}

	void refresh(){
		addActor(rootDom = new Root(document.body(), this))
	}


	void joinStyles(String style = null){
		InputSource source = new InputSource(new StringReader(style ?: styles.join("\n")))
		CSSOMParser parser = new CSSOMParser(new SACParserCSS3())
		CSSStyleSheet styleSheet = parser.parseStyleSheet(source, null, null)

		CSSRuleListImpl rules = styleSheet.getCssRules() as CSSRuleListImpl
		for(def i = 0; i < rules.getLength(); i++){
			if(!(rules.item(i) instanceof CSSStyleRuleImpl))
				continue

			CSSStyleRuleImpl item = rules.item(i) as CSSStyleRuleImpl

			for(def j = 0; j < item.selectors.getLength(); j++) {
				Selector selector = item.selectors.item(j)

				def s = selector.toString().trim()

				if(s.indexOf(':') >= 0)
					continue

				if(s.length() == 0)
					continue

				//println "s: " + s + ", default: " + selector.toString()

				try{
					document.select(s)*.styles*.add ((item.style as CSSStyleDeclarationImpl).properties)
				}catch(e){
					e.printStackTrace()
				}

			}
		}

		needsRefresh = true
	}

	void draw() {
		if(needsRefresh){
			needsRefresh = false
			refresh()
		}
		super.draw()
	}

	void act() {
		res.act()
		super.act()
	}

	static HTMLStage buildHTML(String html, String uriPath = null){
		def doc = Jsoup.parse(html)

		if(uriPath)
			doc.baseUri = uriPath

		buildDocument doc
	}

	static HTMLStage buildDocument(Document document){
		new HTMLStage(document)
	}

	static HTMLStage buildPath(String path){
		buildHTML(Gdx.files.internal(path).readString("utf-8"), path)
	}

	void dispose() {
		document = null
		rootDom = null
		res.dispose()
		super.dispose()
	}
}
