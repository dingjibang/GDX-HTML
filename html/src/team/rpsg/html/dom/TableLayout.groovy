package team.rpsg.html.dom

import com.badlogic.gdx.utils.GdxRuntimeException
import org.jsoup.nodes.Element

class TableLayout {
	static String[] TAG_NAME = ["table", "table-row", "table-cell"]

	TableLayout parent
	boolean isTable, isTR, isTD

	Dom dom

	TableLayout(Dom dom) {
		this.dom = dom

		if(!(dom.node instanceof Element))
			throw new GdxRuntimeException("must case **DOM ELEMENT** to table.")

		def tag = (dom.node as Element).tagName().toLowerCase()

		isTable = tag == "table"
		isTR = tag == "tr"
		isTD = tag == "td" || tag == "th"

	}

	void to() {

	}
}
