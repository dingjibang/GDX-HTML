package team.rpsg.html.dom.layout

import com.badlogic.gdx.scenes.scene2d.Actor
import groovy.transform.CompileStatic
import team.rpsg.html.dom.Dom

@CompileStatic
abstract class AbstractLayout {
	Dom dom

	AbstractLayout(Dom dom){
		this.dom = dom
	}

	abstract void parse();
	abstract Actor build();

	void addQuirks(Dom dom){

	}


}
