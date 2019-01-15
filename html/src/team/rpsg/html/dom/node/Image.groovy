package team.rpsg.html.dom.node

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.utils.Scaling
import groovy.transform.CompileStatic
import org.jsoup.nodes.Node
import team.rpsg.html.HTMLStage
import team.rpsg.html.dom.HTMLDom
import team.rpsg.html.manager.widget.AsyncLoadImage
import team.rpsg.html.util.PathParser
import team.rpsg.html.util.StyleParser

/**
 * HTML Image Node.<br/>
 * It uses lazy loading to load images asynchronously.
 * <br>
 * <br>
 * HTML的图片实现<br>
 * 它是异步加载的哦。
 */
@CompileStatic
class Image extends HTMLDom {

	private static final String DEFAULT_STYLE = "display: inline-block;"
	String src = ""
	Scaling scaling

	Image(Node node) {
		super(node, StyleParser.parse(DEFAULT_STYLE).properties, "img")
	}

	void parse() {
		super.parse()

		src = node.attr "src"
		if(src){
			src = PathParser.parse((stage as HTMLStage).document, src)
		}

		scaling = style("-gdx-image-scaling", null, {str ->
			if(!str)
				return null
			return Scaling.valueOf(str.toString())
		}) as Scaling
	}

	void build() {
		super.build()
		if(src && src.length() != 0){

			Container<AsyncLoadImage> container

			boolean lw = false, lh = false

			def callback = {AsyncLoadImage img ->
				img.layout()
				
				if(lh)
					container.height(new Value() {
						float get(Actor context) {
							img.imageHeight
						}
					})

				if(lw)
					container.width(new Value() {
						float get(Actor context) {
							img.imageWidth
						}
					})

				if(lw || lh)
					img.invalidateHierarchy()
				
			}

			def img = new AsyncLoadImage(src, callback, res)
			container = new Container(img)


			def setScaling = true

			if(width != 0 && height == 0){
				container.width(width)
				lh = true
			}else if(height != 0 && width == 0){
				container.height(height)
				lw = true
			}else if(width != 0 && height != 0){
				container.width(width)
				container.height(height)
				img.scaling = Scaling.stretch
				setScaling = false
			}

			if(setScaling || (!setScaling && scaling != null))
				img.scaling = scaling ?: Scaling.fit



			current.addActor container
		}
	}

	void dispose() {

	}
}
