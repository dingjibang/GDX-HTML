package team.rpsg.html.dom

import com.steadystate.css.dom.CSSStyleDeclarationImpl
import com.steadystate.css.dom.Property
import com.steadystate.css.parser.CSSOMParser
import com.steadystate.css.parser.SACParserCSS3
import groovy.transform.CompileStatic
import org.w3c.css.sac.InputSource
import team.rpsg.html.util.StyleParser

@CompileStatic
enum HTMLDomPreset {
	ADDRESS("display: block;font-style: italic;"),
	AREA("display: none;"),
	ARTICLE("display: block; "),
	ASIDE("display: block; "),
	B("font-weight: bold;"),
	BDO("unicode-bidi: bidi-override;"),
	BLOCKQUOTE("display: block;margin-top: 1em;margin-bottom: 1em;margin-left: 40px;margin-right: 40px;"),
	BODY("display: block;margin: 8px;"),
	BR("display: block;"),
	CAPTION("display: table-caption;text-align: center;"),
	CITE("font-style: italic;"),
	CODE("font-family: monospace;"),
	COL("display: table-column;"),
	COLGROUP("display: table-column-group"),
	DATALIST("display: none;"),
	DD("display: block;margin-left: 40px;"),
	DEL("text-decoration: line-through;"),
	DETAILS("display: block;"),
	DFN("font-style: italic;"),
	DIV("display: block;"),
	DL("display: block;margin-top: 1em;margin-bottom: 1em;margin-left: 0;margin-right: 0;"),
	DT("display: block;"),
	EM("font-style: italic;"),
	FIELDSET("display: block;margin-left: 2px;margin-right: 2px;padding-top: 0.35em;padding-bottom: 0.625em;padding-left: 0.75em;padding-right: 0.75em;border: 2px groove (internal value);"),
	FIGCAPTION("display: block;"),
	FIGURE("display: block;margin-top: 1em;margin-bottom: 1em;margin-left: 40px;margin-right: 40px;"),
	FOOTER("display: block;"),
	FORM("display: block;margin-top: 0em;"),
	H1("display: block;font-size: 2em;margin-top: 0.67em;margin-bottom: 0.67em;margin-left: 0;margin-right: 0;font-weight: bold;"),
	H2("display: block;font-size: 1.5em;margin-top: 0.83em;margin-bottom: 0.83em;margin-left: 0;margin-right: 0;font-weight: bold;"),
	H3("display: block;font-size: 1.17em;margin-top: 1em;margin-bottom: 1em;margin-left: 0;margin-right: 0;font-weight: bold;"),
	H4("display: block;margin-top: 1.33em;margin-bottom: 1.33em;margin-left: 0;margin-right: 0;font-weight: bold;"),
	H5("display: block;font-size: .83em;margin-top: 1.67em;margin-bottom: 1.67em;margin-left: 0;margin-right: 0;font-weight: bold;"),
	H6("display: block;font-size: .67em;margin-top: 2.33em;margin-bottom: 2.33em;margin-left: 0;margin-right: 0;font-weight: bold;"),
	HEAD("display: none;"),
	HEADER("display: block;"),
	HR("display: block;margin-top: 0.5em;margin-bottom: 0.5em;margin-left: auto;margin-right: auto;border-style: inset;border-width: 1px;"),
	HTML("display: block;"),
	I("font-style: italic;"),
	IMG("display: inline-block;"),
	IFRAME("display: block;"),
	INS("text-decoration: underline;"),
	KBD("font-family: monospace;"),
	LABEL("cursor: default;"),
	LEGEND("display: block;padding-left: 2px;padding-right: 2px;border: none;"),
	LI("display: list-item;"),
	LINK("display: none;"),
	MAP("display: inline;"),
	MARK("background-color: yellow;color: black;"),
	MENU("display: block;list-style-type: disc;margin-top: 1em;margin-bottom: 1em;margin-left: 0;margin-right: 0;padding-left: 40px;"),
	NAV("display: block;"),
	OL("display: block;list-style-type: decimal;margin-top: 1em;margin-bottom: 1em;margin-left: 0;margin-right: 0;padding-left: 40px;"),
	OUTPUT("display: inline;"),
	P("display: block;margin-top: 1em;margin-bottom: 1em;margin-left: 0;margin-right: 0;"),
	PARAM("display: none;"),
	PRE("display: block;font-family: monospace;white-space: pre;margin: 1em 0;"),
	Q("display: inline;"),
	RT("line-height: normal;"),
	S("text-decoration: line-through;"),
	SAMP("font-family: monospace;"),
	SCRIPT("display: none;"),
	SECTION("display: block;"),
	SMALL("font-size: smaller;"),
	STRIKE("text-decoration: line-through;"),
	STRONG("font-weight: bold;"),
	STYLE("display: none;"),
	SUB("vertical-align: sub;font-size: smaller;"),
	SUMMARY("display: block;"),
	SUP("vertical-align: super;font-size: smaller;"),
	TABLE("display: table;border-collapse: separate;border-spacing: 2px;border-color: gray;"),
	TBODY("display: table-row-group;vertical-align: middle;border-color: inherit;"),
	TD("display: table-cell;vertical-align: inherit;"),
	TFOOT("display: table-footer-group;vertical-align: middle;border-color: inherit;"),
	TH("display: table-cell;vertical-align: inherit;font-weight: bold;text-align: center;"),
	THEAD("display: table-header-group;vertical-align: middle;border-color: inherit;"),
	TITLE("display: none;"),
	TR("display: table-row;vertical-align: inherit;border-color: inherit;"),
	U("text-decoration: underline;"),
	UL("display: block;list-style-type: disc;margin-top: 1em;margin-bottom: 1 em;margin-left: 0;margin-right: 0;padding-left: 40px;"),
	VAR("font-style: italic;")

	private List<Property> styles

	private HTMLDomPreset(String stylesString){
		if(stylesString.length() == 0){
			styles = new ArrayList<>()
			return
		}

		styles = StyleParser.parse(stylesString).properties
	}

	HTMLDom dom(org.jsoup.nodes.Node node){
		return new HTMLDom(node, styles, name())
	}
}
