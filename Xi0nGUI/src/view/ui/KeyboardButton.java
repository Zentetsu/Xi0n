package view.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import view.Xi0nSimulation;
import view.robot.Mode;

public class KeyboardButton extends UIButton {

	private final static String GRAPHIC_ELEMENT = "keyboard";

	public KeyboardButton(float x, float y) {
		super(x, y, GRAPHIC_ELEMENT);
		down(GRAPHIC_ELEMENT);
	}

	private class StartButtonListener extends ClickListener {

		private String key = GRAPHIC_ELEMENT;

		public StartButtonListener() {

		}

		@Override
		public void clicked(InputEvent input, float x, float y) {
			Xi0nSimulation.INSTANCE.setMode(Mode.KEYBOARD);
		}
	}

	public String toString() {
		return GRAPHIC_ELEMENT;
	}

	@Override
	protected ClickListener getButtonListener() {
		return new StartButtonListener();
	}

	public void down() {
		super.down(GRAPHIC_ELEMENT);
	}

}
