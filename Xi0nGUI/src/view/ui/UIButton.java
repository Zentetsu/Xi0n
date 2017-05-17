package view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public abstract class UIButton extends Actor {

	private final static String DATA_PATH = "assets/";
	protected ImageButton button;

	public UIButton(float x, float y, String fileName) {
		Texture myTexture = new Texture(Gdx.files.internal(DATA_PATH + fileName));
		Drawable drawable = new TextureRegionDrawable(new TextureRegion(myTexture));
		this.button = new ImageButton(drawable);

		this.button.setPosition(x, y);
		//this.button.setSize(width, height);
		this.button.addListener(this.getButtonListener());
	}

	protected abstract ClickListener getButtonListener();

	public Button getButton() {
		return this.button;
	}
}
