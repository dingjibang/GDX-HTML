package team.rpsg.html.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Net
import groovy.transform.CompileStatic
import org.jsoup.nodes.Document


/**
 * parsePadding path
 */
@CompileStatic
class PathParser {
	/**
	 * parsePadding path by document baseURI, return absolute(assets root) path<br/>
	 * <pre>
	 * assets
	 * ├── img
	 * │   └── 1.jpg
	 * ├── html
	 * │   ├── 1.html
	 * │   └── 2.html
	 * </pre>
	 * assert PathParser.parsePadding(doc, "../img/1.jpg") == "/img/1.jpg"; <br/>
	 * assert PathParser.parsePadding(doc, "2.html") == "/html/2.html"; <br/>
	 */
	static String parse(Document document, String path){
		return new URI(document.baseUri() ?: "/").resolve(new URI(path)).toString()
	}


	static void get(Document document, String path, String charset, Closure callback){
		if(!charset || charset.length() == 0)
			charset = "utf-8"

		def uri = new URI(document.baseUri() ?: "/").resolve(new URI(path))

		try{
			def request = new Net.HttpRequest(url: uri.toURL().toString(), method: "GET")

			Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener(){
				void handleHttpResponse(Net.HttpResponse httpResponse) {
					callback(httpResponse.resultAsString, true)
				}
				void failed(Throwable t) {
					t.printStackTrace()
				}
				void cancelled() {}
			})


		}catch(ignored){
			callback(Gdx.files.internal(uri.toString()).readString(charset), false)
		}


	}
}
