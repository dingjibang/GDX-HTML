package script.ui.view;

import team.rpsg.htmlTest.core.Game;
import team.rpsg.htmlTest.ui.view.View;

/**
 * 快速View
 */
public abstract class UIView extends View {
	public UIView() {
		this.stage = Game.stage();
	}

	public void act() {
		stage.act();
	}

	public void draw() {
		stage.draw();
	}
}
