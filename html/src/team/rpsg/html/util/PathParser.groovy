package team.rpsg.html.util

import com.badlogic.gdx.Gdx
import groovy.transform.CompileStatic
import org.jsoup.nodes.Document


/**
 * parse path
 */
@CompileStatic
class PathParser {
	/**
	 * parse path by document baseURI, return absolute(assets root) path<br/>
	 * <pre>
	 * assets
	 * ├── img
	 * │   └── 1.jpg
	 * ├── html
	 * │   ├── 1.html
	 * │   └── 2.html
	 * </pre>
	 * assert PathParser.parse(doc, "../img/1.jpg") == "/img/1.jpg"; <br/>
	 * assert PathParser.parse(doc, "2.html") == "/html/2.html"; <br/>
	 */
	static String parse(Document document, String path){
		return new URI(document.baseUri() ?: "/").resolve(new URI(path)).toString()
	}
}
