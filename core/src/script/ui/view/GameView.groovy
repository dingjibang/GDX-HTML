package script.ui.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.utils.Timer
import groovy.transform.CompileStatic
import org.jsoup.nodes.Document
import team.rpsg.html.HTMLStage
import team.rpsg.htmlTest.ui.view.View

@CompileStatic
class GameView extends View{

    void create() {
        init()
    }

    void init() {
        if(this.stage != null)
            ((HTMLStage)this.stage).dispose()
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

        if(keycode == Input.Keys.L)
            team.rpsg.htmlTest.util.Timer.add(team.rpsg.htmlTest.util.Timer.TimeType.frame, 20, team.rpsg.htmlTest.util.Timer.FOREVER, this.&init)

        super.keyDown(keycode)
    }

    void resize() {
//        if(this.stage != null)
//            init()
//        super.resize()
    }
}