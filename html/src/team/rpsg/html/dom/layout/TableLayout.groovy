package team.rpsg.html.dom.layout

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.utils.GdxRuntimeException
import org.jsoup.nodes.Element
import team.rpsg.html.dom.Dom
import team.rpsg.html.util.AlignParser
import team.rpsg.html.util.SizeParser

class TableLayout extends AbstractLayout{
	static String[] DISPLAY_NAME = ["table", "table-row", "table-cell", "table-row-group", "table-header-group"]

	TableLayout parent

	boolean isTable, isTR, isTD, isTHead, isTBody


	Table table
	Cell<Dom> current

	TableLayout(Dom dom) {
		super(dom)
		if(!(dom.node instanceof Element))
			throw new GdxRuntimeException("must case **DOM ELEMENT** to table.")
	}

	void parse(){
		if(dom.parentDom.layout instanceof TableLayout)
			parent = dom.parentDom.layout as TableLayout

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

	Actor build() {
		table = parent?.table

		if(!isTable && table == null)
			return dom

		if(isTable){

			dom.parse()

			table = new Table().left().top()
			table.fillParent = true

			dom.debugStrings.add { "table:${Math.random()}" + table.width + ", " + table.height }

			def container = new Container(table)
			container.width(new Value() {
				float get(Actor context) {
					dom.widthValue != null ? dom.width : Math.max(context.width, (context as Table).prefWidth)
				}
			})
			container.height(new Value() {
				float get(Actor context) {
					dom.heightValue != null ? dom.height : Math.max(context.height, (context as Table).prefHeight)
				}
			})
			dom.addActor(container)

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

	private void setCellWidth(){
		current.width(new Value() {
			float get(Actor context) {
				Math.max((context as Dom).prefWidth, context.width)
			}
		})
	}

	private void setCellHeight(){
		current.height(new Value() {
			float get(Actor context) {
				Math.max((context as Dom).prefHeight, context.height)
			}
		})
	}


	Cell<Dom> add(Dom dom){
		current = table.add(dom).align(AlignParser.join(dom.textAlign, dom.verticalAlign))

		if(dom.widthValue){
			if(dom.widthValue instanceof Value.Fixed){
				setCellWidth()
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
			setCellWidth()
			current.expandX()
		}
		if(dom.heightValue){
			if(dom.heightValue instanceof Value.Fixed){
				setCellHeight()
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
			setCellHeight()
			current.expandY()
		}
	}

	void addQuirks(Dom dom) {

	}
}
