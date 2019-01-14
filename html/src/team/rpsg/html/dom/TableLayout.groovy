package team.rpsg.html.dom

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.GdxRuntimeException
import org.jsoup.nodes.Element
import team.rpsg.html.manager.widget.AutoSizeContainer
import team.rpsg.html.util.AlignParser
import team.rpsg.html.util.SizeParser

class TableLayout {
	static String[] DISPLAY_NAME = ["table", "table-row", "table-cell", "table-row-group", "table-header-group"]

	TableLayout parent
	boolean isTable, isTR, isTD, isTHead, isTBody

	Dom dom

	Table table
	Cell<Dom> current


	TableLayout() {

	}

	void set(Dom dom){

		if(!(dom.node instanceof Element))
			throw new GdxRuntimeException("must case **DOM ELEMENT** to table.")

		this.dom = dom

		def display = dom.display

		switch (display.toLowerCase()){
			case "table":
				isTable = true
				break
			case "table-row":
				isTR = true
				break
			case "table-row-group":
				isTBody = true
				break
			case "table-header-group":
				isTHead = true
				break
			case "table-cell":
				isTD = true
				break
		}
	}

	Actor apply() {
		table = parent?.table

		if(!isTable && table == null)
			return dom

		if(isTable){

			def stage = dom.stage
			dom.stage = stage

			dom.parse()

			table = new Table()
			table.fillParent = true

			dom.debugStrings.add { "table:${Math.random()}" + table.width + ", " + table.height }

			dom.current.addActor(new Container(table).width(dom.width).height(dom.height))

			return dom

		} else if(isTR || isTBody || isTHead){
			table.row()
			return null
		} else if(isTD){
			dom.parse()
			def c = add(dom)
			dom.debugStrings.add { "cell: pw" + c.getPrefWidth() + ", ph" + c.getPrefHeight() }

//			if(dom.heightValue){
//				c.height(new Value() {
//					float get(Actor context) {
//						return dom.heightValue.get(parent.dom)
//					}
//				})
//			}
//			if(dom.widthValue){
//				c.width(new Value() {
//					float get(Actor context) {
//						return dom.widthValue.get(parent.dom)
//					}
//				})
//			}
			return null
		}

		return dom
	}

	static def first = true

	Cell<Dom> add(Dom dom){
		current = table.add(dom).align(AlignParser.join(dom.textAlign, dom.verticalAlign))
		current.expand()

		if(dom.widthValue || dom.heightValue){
			if(dom.widthValue){
				current.growX()
				current.width(new Value() {
					float get(Actor context) {
						return dom.widthValue.get(table)
					}
				})
			}
			if(dom.heightValue) {
				current.growY()
				current.height(new Value() {
					float get(Actor context) {
						return dom.heightValue.get(table)
					}
				})
			}
		}else{

		}

	}
}
