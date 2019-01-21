package team.rpsg.html.manager.widget;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import team.rpsg.gdxQuery.TypedGdxQuery;

/**
 * GDX-RPG 图片类
 */
public class Image extends com.badlogic.gdx.scenes.scene2d.ui.Image{
	public TypedGdxQuery<Image> query(){
		return new TypedGdxQuery<>(this);
	}
	
	public Image (Texture texture){
		super(texture);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	public void setDrawable(Drawable drawable) {
		super.setDrawable(drawable);

		if(drawable == null)
			return;

		if(drawable instanceof TextureRegionDrawable)
			((TextureRegionDrawable)drawable).getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	public Image (Drawable drawable){
		super(drawable);
	}
	
	public Image(){
		super((Drawable)null);
	}
	
	/**使用整数坐标，以免导致纹理失真*/
	public void setWidth(float width) {
		super.setWidth((int)width);
	}
	
	public void setHeight(float height) {
		super.setHeight((int)height);
	}
	
	public void setX(float x) {
		super.setX((int)x);
	}
	
	public void setY(float y) {
		super.setY((int)y);
	}
	
	public void setPosition(float x, float y) {
		super.setPosition((int)x, (int)y);
	}
	
	public boolean isTransparent() {
		return getColor().a <= 0;
	}

	public Texture getTexture() {
		if(getDrawable() instanceof  TextureRegionDrawable)
			return ((TextureRegionDrawable) getDrawable()).getRegion().getTexture();

		return null;
	}
	
	public Image disableTouch(){
		setTouchable(null);
		return this;
	}

}
