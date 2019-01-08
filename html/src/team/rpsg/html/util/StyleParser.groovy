package team.rpsg.html.util

import com.steadystate.css.dom.CSSStyleDeclarationImpl
import com.steadystate.css.parser.CSSOMParser
import com.steadystate.css.parser.SACParserCSS3
import groovy.transform.CompileStatic
import org.w3c.css.sac.InputSource

@CompileStatic
class StyleParser {
	/**
	 * @param styles string, like "font-size: 20px; css-prop: value; ..."
	 * @return
	 */
	static CSSStyleDeclarationImpl parse(String stylesString){
		(CSSStyleDeclarationImpl)new CSSOMParser(new SACParserCSS3()).parseStyleDeclaration(new InputSource(new StringReader(stylesString)))
	}
}
