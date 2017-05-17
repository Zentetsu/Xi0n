package view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public abstract class UIButton extends ImageButton {

	private final static String DATA_PATH = "assets/";

	public UIButton(float x, float y, String fileName) {
		super(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(DATA_PATH + fileName)))));
		this.setPosition(x, y);
		this.addListener(this.getButtonListener());
	}

	protected abstract ClickListener getButtonListener();

}
