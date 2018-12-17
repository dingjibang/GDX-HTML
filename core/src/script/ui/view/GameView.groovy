package script.ui.view

import com.badlogic.gdx.Input
import org.jsoup.nodes.Document
import team.rpsg.html.HTMLStage
import team.rpsg.htmlTest.ui.view.View

class GameView extends View{

    Document doc

    void create() {
        init()
    }

    void init() {
        this.stage = HTMLStage.buildPath("html/test.html")
    }


    void draw(){
        stage.draw()
    }

    void act() {
        stage.act()
    }

    boolean keyDown (int keycode){
        if(keycode == Input.Keys.R)
            init()

        super.keyDown(keycode)
    }

}