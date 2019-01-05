package team.rpsg.html.dom

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.Align
import groovy.transform.CompileStatic
import org.jsoup.nodes.Node
import team.rpsg.html.HTMLStage

/**
 * rootDom = html document
 */
@CompileStatic
class Root extends Dom{

	Root(Node node, HTMLStage stage){
		super(node)

		this.stage = stage

		parse()

		width = stage.width
		height = stage.height

		x = 0
		y = 0

		buildChild()

		align(Align.topLeft)
	}

	void draw(Batch batch, float parentAlpha) {
		backgroundDrawable?.draw(batch, x, y, width, height)
		super.draw(batch, parentAlpha)
	}

	void dispose() {

	}
}
