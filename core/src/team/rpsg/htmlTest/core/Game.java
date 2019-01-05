package team.rpsg.htmlTest.core;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import script.ui.view.GameView;

/**
 *	游戏上下文
 */
public class Game {
	public static final int STAGE_WIDTH = 1280, STAGE_HEIGHT = 720;

	public static int width(){
		return Gdx.graphics.getWidth();
	}
	
	public static int height(){
		return Gdx.graphics.getHeight();
	}


	/**
	 * 获取一个默认{@link Stage}，他将和{@link Views#batch}共用一个画笔。<br>
	 * 要注意的是，这个画笔的{@link com.badlogic.gdx.graphics.g2d.Batch#setTransformMatrix(com.badlogic.gdx.math.Matrix4) transform}被改变的话，将可能导致接下来绘制的东西坐标出现异常。
	 */
	public static Stage stage(){
		return new Stage(viewport(), Views.batch);
	}
	
	/**
	 * 获取一个默认的{@link ScalingViewport Viewport}，他将根据玩家设置，来选择缩放是“适应”还是“填充”模式。
	 */
	public static ScalingViewport viewport(){
		return new ScalingViewport(Scaling.fit, Game.STAGE_WIDTH, Game.STAGE_HEIGHT, new OrthographicCamera());
	}
}
