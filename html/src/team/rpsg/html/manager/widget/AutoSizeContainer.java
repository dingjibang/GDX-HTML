package team.rpsg.html.manager.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import team.rpsg.html.dom.Dom;

public class AutoSizeContainer extends Container<Dom> {
    
    public AutoSizeContainer(Dom dom){
        super(dom);
    }

    public float getWidth() {
        return getPrefWidth();
    }

    public float getHeight() {
        return getPrefHeight();
    }
}
