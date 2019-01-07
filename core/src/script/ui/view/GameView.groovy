package script.ui.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.utils.Timer
import groovy.transform.CompileStatic
import org.jsoup.nodes.Document
import team.rpsg.html.HTMLStage
import team.rpsg.htmlTest.core.Game
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

        super.keyDown(keycode)
    }

    void resize() {
        if(this.stage != null){
            (this.stage as HTMLStage).resize(Game.width(), Game.height())
        }
    }
}