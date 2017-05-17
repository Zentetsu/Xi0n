package view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class QuitButton extends UIButton {

	private final static String GRAPHIC_ELEMENT = "quit.png";

	public QuitButton(float x, float y) {
		super(x, y, GRAPHIC_ELEMENT);
	}

	private class StartButtonListener extends ClickListener {
		@Override
		public void clicked(InputEvent input, float x, float y) {
			Gdx.app.exit();
		}
	}

	@Override
	protected ClickListener getButtonListener() {
		return new StartButtonListener();
	}

}
