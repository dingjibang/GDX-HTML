package team.rpsg.html.dom

import com.badlogic.gdx.utils.Align
import org.jsoup.nodes.Node
import team.rpsg.html.HTMLStage

/**
 * rootDom = html document
 */
class Root extends Dom{

	Root(Node node, HTMLStage stage){
		super(node)

		this.stage = stage

		width = stage.width
		height = stage.height

		buildChild()

		align(Align.topLeft)
	}


	void dispose() {

	}
}
