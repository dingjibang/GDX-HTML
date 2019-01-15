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

			def container = new Container(table)
			container.width(new Value() {
				float get(Actor context) {
					dom.width != 0 ? dom.width : Math.max(context.width, (context as Table).prefWidth)
				}
			})
			container.height(new Value() {
				float get(Actor context) {
					dom.height != 0 ? dom.height : Math.max(context.height, (context as Table).prefHeight)
				}
			})
			dom.current.addActor(container)

			return dom

		} else if(isTR || isTBody || isTHead){
			table.row()
			return null
		} else if(isTD){
			dom.parse()
			def c = add(dom)
			dom.debugStrings.add { "cell: pw" + c.getPrefWidth() + ", ph" + c.getPrefHeight() }

			return null
		}

		return dom
	}

	static def first = true

	Cell<Dom> add(Dom dom){
		current = table.add(dom).align(AlignParser.join(dom.textAlign, dom.verticalAlign))

		if(dom.widthValue){
			if(dom.widthValue instanceof Value.Fixed){
				current.width(new Value() {
					float get(Actor context) {
						Math.max((context as Dom).prefWidth, context.width)
					}
				})
			}

			if(dom.widthValue instanceof SizeParser.percentInnerValue){
				def percent = (dom.widthValue as SizeParser.percentInnerValue).percent
				current.width(new Value() {
					float get(Actor context) {
						parent.table.width * percent
					}
				})
			}
		}else {
//			current.width(Value.prefWidth)
			current.expandX()
		}
		if(dom.heightValue){
			if(dom.heightValue instanceof Value.Fixed){
				current.height(new Value() {
					float get(Actor context) {
						Math.max((context as Dom).prefHeight, context.height)
					}
				})
			}
			if(dom.heightValue instanceof SizeParser.percentInnerValue){
				def percent = (dom.heightValue as SizeParser.percentInnerValue).percent
				current.height(new Value() {
					float get(Actor context) {
						parent.table.height * percent
					}
				})
			}
		}else{
//			current.height(Value.prefHeight)
			current.expandY()
		}
	}
}
