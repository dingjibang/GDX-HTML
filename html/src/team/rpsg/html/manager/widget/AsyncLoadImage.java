package team.rpsg.html.manager.widget;

import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import team.rpsg.gdxQuery.CustomRunnable;
import team.rpsg.html.manager.ResourceManager;

/**
 * GDX-RPG 异步加载纹理类<br>
 * 启用另一个OpenGL线程来达到异步加载纹理的效果，在加载的过程中，当前的图片是无法读取宽/高度的。
 */
public class AsyncLoadImage extends Image {
	
	private boolean loaded = false, loading = false;
	private int originAlignment;
	private String texturePath;
	
	private CustomRunnable<AsyncLoadImage> _onLoaded = null;

	private ResourceManager resourceManager = null;
	
	private AsyncLoadImage() {}
	
	public AsyncLoadImage(String texturePath, ResourceManager resourceManager) {
		this.texturePath = texturePath;
		this.resourceManager = resourceManager;
	}
	
	private AsyncLoadImage(String texturePath, CustomRunnable<AsyncLoadImage> onLoaded, ResourceManager resourceManager) {
		this(texturePath, resourceManager);
		this._onLoaded = onLoaded;
	}
	
	/**
	 * 初始化
	 */
	private void init(){
		/**如果开启纹理过滤，则设置*/
		((TextureRegionDrawable)getDrawable()).getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		/**重置自身大小*/
		if (getWidth() == 0)
			setWidth(getDrawable().getMinWidth());
		if (getHeight() == 0)
			setHeight(getDrawable().getMinHeight());
		if (originAlignment != -1)
			setOrigin(originAlignment);
	}
	
	/**当该纹理被调用绘制时，才开始懒加载纹理，否则歇着*/
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		loadTexture();
	}
	
	TextureParameter parameter = new TextureParameter();
	{
		/**当纹理加载完成后的回调*/
		parameter.loadedCallback = (assetManager, fileName, type) -> {
			setDrawable(new TextureRegionDrawable(new TextureRegion(assetManager.get(fileName, Texture.class))));
			loaded = true;
			init();
			if(_onLoaded != null)
				_onLoaded.run(AsyncLoadImage.this);
		};
	}
	
	/**加载纹理**/
	public void loadTexture(){
		if(texturePath == null || loading) 
			return;
		
		loading = true;
		
		resourceManager.assetManager.load(texturePath, Texture.class, parameter);
	}
	
	/**是否加载完成*/
	public boolean loaded(){
		return loaded;
	}
	
	public void setOrigin(int alignment) {
		super.setOrigin(originAlignment = alignment);
	}
	
	/**
	 * 异步加载一个drawable
	 */
	public void setDrawableAsync(String drawablePath) {
		new AsyncLoadImage(drawablePath, that -> {
			setDrawable(that.getDrawable());
			init();
		}, resourceManager).loadTexture();
	}
	
}
