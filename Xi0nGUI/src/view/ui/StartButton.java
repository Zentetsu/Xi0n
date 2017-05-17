package view.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class StartButton extends UIButton {

	private final static String GRAPHIC_ELEMENT = "start.png";

	public StartButton(float x, float y) {
		super(x, y, GRAPHIC_ELEMENT);
	}

	private class StartButtonListener extends ClickListener {
		@Override
		public void clicked(InputEvent input, float x, float y) {
			System.out.println("click on button");
		}
	}

	@Override
	protected ClickListener getButtonListener() {
		return new StartButtonListener();
	}

}
