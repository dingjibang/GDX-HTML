package team.rpsg.html.util

import com.badlogic.gdx.scenes.scene2d.ui.Value

/**
 * unit(px or %) parser
 */
class SizeParser {
	static int parsePX(Object property){
		if(property.toString().indexOf('%') > 0)
			return 16
		property.toString().replace("px", "").toInteger()
	}
}
