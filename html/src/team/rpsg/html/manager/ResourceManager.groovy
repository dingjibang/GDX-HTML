package team.rpsg.html.manager

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import team.rpsg.html.manager.widget.AsyncLoadImage
import team.rpsg.html.manager.widget.Image

class ResourceManager {

	/**资源管理线程*/
	public AssetManager assetManager
	/**字体管理*/
	public TextManager text

	/**初始化*/
	ResourceManager(){
		assetManager = new AssetManager()
		/**添加字体管理器*/
		text = new TextManager()
	}

	/**
	 * 获取一张图片，它是异步加载的，如果需要同步加载，请调用{@link #sync(String) sync}方法
	 */
	AsyncLoadImage get(String path){
		return new AsyncLoadImage(path)
	}

	/**
	 * 立即获取一张图片
	 */
	Image sync(String path){
		return new Image(getTexture(path))
	}

	/**
	 * 立即获取一张drawable
	 */
	Drawable getDrawable(String path) {
		return new TextureRegionDrawable(new TextureRegion(getTexture(path)))
	}

	/**
	 * 更新资源管理器
	 */
	boolean act(){
		return assetManager.update()
	}

	/**
	 * 立即获取一张纹理
	 */
	Texture getTexture(String path){
		assetManager.load(path, Texture.class)
		while(!assetManager.update())
		return assetManager.get(path, Texture.class)
	}

	/**
	 * 预加载纹理
	 */
	void preInit(String[] paths){
		for(String path : paths)
			assetManager.load(path, Texture.class)
	}

	/**卸载全部纹理*/
	void dispose(){
		assetManager.dispose()
	}

}
