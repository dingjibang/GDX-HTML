package script.ui.view


import com.badlogic.gdx.Input
import groovy.transform.CompileStatic
import team.rpsg.html.HTMLStage
import team.rpsg.html.dom.layout.TableLayout
import team.rpsg.htmlTest.core.Game
import team.rpsg.htmlTest.ui.view.View

@CompileStatic
class GameView extends View{

    void create() {
        init()
    }

    void init() {
        TableLayout.first  =true
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