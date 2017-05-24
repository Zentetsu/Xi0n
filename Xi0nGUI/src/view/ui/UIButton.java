package view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public abstract class UIButton extends ImageButton {

	private final static String DATA_PATH = "assets/ui/";
	private final static String EXT = ".png";
	private final static String DOWN = "_down";

	private ImageButton.ImageButtonStyle style;

	public UIButton(float x, float y, String fileName) {
		super(new TextureRegionDrawable(
				new TextureRegion(new Texture(Gdx.files.internal(DATA_PATH + fileName + EXT)))));

		this.style = new ImageButton.ImageButtonStyle();

		this.style.up = new TextureRegionDrawable(
				new TextureRegion(new Texture(Gdx.files.internal(DATA_PATH + fileName + EXT))));

		this.setPosition(x, y);
		this.addListener(this.getButtonListener());
	}

	protected void down(String fileName) {

		this.style.checked = new TextureRegionDrawable(
				new TextureRegion(new Texture(Gdx.files.internal(DATA_PATH + fileName + DOWN + EXT))));
		setStyle(this.style);
	}
	
	protected void setDown(String fileName) {

		this.style.checked = new TextureRegionDrawable(
				new TextureRegion(new Texture(Gdx.files.internal(DATA_PATH + fileName + EXT))));
		setStyle(this.style);
	}
	
	protected abstract ClickListener getButtonListener();

}
