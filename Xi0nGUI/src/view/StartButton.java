package view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class StartButton extends Actor{

	private Button button;

	public StartButton(float x, float y, float width, float height) {
		BitmapFont font = new BitmapFont();
		Skin skin = new Skin();
		TextureAtlas buttonAtlas = new TextureAtlas(Gdx.files.internal("assets/statusui.atlas"));
		skin.addRegions(buttonAtlas);
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.up = skin.getDrawable("dialog");
		textButtonStyle.down = skin.getDrawable("dialog");
		textButtonStyle.checked = skin.getDrawable("dialog");
		this.button = new Button(textButtonStyle);
		this.button.setPosition(x, y);
		this.button.setSize(width, height);
		button.addListener(new StartButtonListener());
	}

	private class StartButtonListener extends ClickListener{
		@Override
		public void clicked(InputEvent input,float x,float y )
		{
			System.out.println("click on button");
		}
	}

	public Button getButton() {
		return this.button;
	}

}
