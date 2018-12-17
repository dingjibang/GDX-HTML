package team.rpsg.html.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.graphics.Color;
import team.rpsg.htmlTest.core.Views;

/**
 * desktop launcher
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
        
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = 1280;
		config.height = 720;

		config.foregroundFPS = 0; // Setting to 0 disables foreground fps throttling
		config.backgroundFPS = 0;

		//程序未进入之前，先显示灰色的背景
        config.initialBackgroundColor= Color.valueOf("2c2c2c");
        
        //预先创建Gdx.files以提前读取配置文件
        Gdx.files = new LwjglFiles();

        //进入入口
		new LwjglApplication(new Views(), config);//gameviews就是咱们游戏的核心了。

	}
	
}
