package team.rpsg.html.manager.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import team.rpsg.html.dom.Dom;

public class AutoWidthContainer extends Container<Dom> {
    
    public AutoWidthContainer(Dom dom){
        super(dom);
    }

    public float getWidth() {
        return getPrefWidth();
    }
}
