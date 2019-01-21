package team.rpsg.html.dom.node

import com.badlogic.gdx.graphics.g2d.Batch
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

	float size

	private static final String DEFAULT_STYLE = "display: inline-block;"
	String src = ""
	Scaling scaling

	team.rpsg.html.manager.widget.Image img

	boolean async = true


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

		if(node.attr("async"))
			async = node.attr("async").equalsIgnoreCase("true")
	}

	void build() {
		super.build()
		if(src && src.length() != 0){

			if(async){
				boolean needH = false
				boolean needW = false

				Container<AsyncLoadImage> container = new Container<>()

				def callback = {AsyncLoadImage img ->
					img.layout()

					if(needH){
						container.height(new Value() {
							float get(Actor context) {
								img.imageHeight
							}
						})
					}

					if(needW){
						container.width(new Value() {
							float get(Actor context) {
								img.imageWidth
							}
						})
					}

				}

				img = new AsyncLoadImage(src, callback, res)
				container = new Container(img)


				def setScaling = true

				if(width != 0 && height == 0){
					container.width(img.width = width)
					needH = true
				}else if(height != 0 && width == 0){
					container.height(img.height = height)
					needW = true
				}else if(width != 0 && height != 0){
					container.width(img.width = width)
					container.height(img.height = height)
					img.scaling = Scaling.stretch
					setScaling = false
				}

				if(setScaling || (!setScaling && scaling != null))
					img.scaling = scaling ?: Scaling.fit



				current.addActor container
			}else{
				img = new team.rpsg.html.manager.widget.Image(res.getTexture(src))
				if(width != 0 && height != 0){
					img.width = width
					img.height = height
				} else if(width != 0 && height == 0){
					img.width = width
				} else if(height != 0 && width == 0){
					img.height = height
				}

				img.scaling = scaling ?: Scaling.fit
				img.layout()

				width = img.imageWidth
				height = img.imageHeight


				current.addActor new Container(img)
									.width(new Value.Fixed(img.imageWidth))
									.height(new Value.Fixed(img.imageHeight))

			}

		}
	}


	void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha)
	}

}
