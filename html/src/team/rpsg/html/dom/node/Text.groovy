package team.rpsg.html.dom.node

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.utils.Align
import groovy.transform.CompileStatic
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import team.rpsg.html.dom.Dom
import team.rpsg.html.manager.widget.Label
import team.rpsg.html.util.AlignParser
import team.rpsg.html.util.ColorParser
import team.rpsg.html.util.SizeParser

/**
 * HTML Text Node.<br/>
 * It is implemented by the label of LibGDX.<br/>
 * It defaults to not enabling wrap and can be enabled with "-gdx-wrap: true;"<br/>
 * To enabling wrap requires the parent container to set the width, but if the container contains multiple child elements, it will cause a width bug.
 * If you need to use multiple colored child elements in the wrap-enabled container, it is recommended to use "-gdx-markup: true; "
 * <br>
 * <br>
 * HTML文字Node<br>
 * 它使用gdx的Label组件实现的，默认情况下它不会开启换行，你可以在它的Dom设置"-gdx-wrap: true;"<br/>
 * 但是，开启换行会导致一个宽度的bug（所有子元素宽度一致），目前没有办法解决除非重写一个文档流。<br/>
 * 所以如果想启用换行，还想让该段文字是多彩的，我们推荐你使用"-gdx-markup: true;"，启用gdx的markup颜色语言，而不是使用一堆子元素。
 */
@CompileStatic
class Text extends Dom {

	int fontSize = 30
	def text = ""
	Color textColor = Color.WHITE
	Float lineHeight = Label.AUTO_LINE_HEIGHT
	Integer letterSpacing = Label.AUTO_LETTER_SPACING

	private String wrap
	private boolean markup


	Text(Node node) {
		super(node)
	}

	void parse() {
		super.parse()

		text = (node as TextNode).text()
		textColor = style("color", "white", ColorParser.&parse) as Color
		fontSize = style("font-size", "16px", SizeParser.&parseFontPX) as int

		wrap = style("-gdx-wrap", "false", {p -> p?.toString()})
		markup = style("-gdx-markup", "false", {p -> p?.toString()?.toBoolean()})

		lineHeight = style("line-height", null, {v -> v ? SizeParser.parse(v)?.get(parentDom)?.toFloat() : Label.AUTO_LINE_HEIGHT}) as Float
		letterSpacing = style("letter-spacing", null, {v -> v ? SizeParser.parse(v)?.get(parentDom)?.toInteger() : Label.AUTO_LETTER_SPACING}) as Integer
	}

	void build() {
		super.build()
		def label = res.text.getLabel(text, fontSize)
		label.color = textColor ?: Color.WHITE
		label.lineHeight = lineHeight
		label.letterSpacing = letterSpacing


		if(markup)
			label.style.font.data.markupEnabled = markup

		if(wrap && wrap == "true"){
			label.wrap = true

			def container = new Container(label).align(textAlign)
			container.width(parentDom.width)
			current.addActor container
		}else {
			current.addActor label
		}





	}

	void dispose() {

	}
}
