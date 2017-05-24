package view.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import view.Xi0nSimulation;
import view.robot.Mode;

public class KeyboardButton extends UIButton {

	private final static String GRAPHIC_ELEMENT = "keyboard";

	public KeyboardButton(float x, float y) {
		super(x, y, GRAPHIC_ELEMENT);
	}

	private class StartButtonListener extends ClickListener {
		@Override
		public void clicked(InputEvent input, float x, float y) {
			Xi0nSimulation.INSTANCE.setMode(Mode.KEYBOARD);
			down(GRAPHIC_ELEMENT);
		}
	}
	
	public String toString() {
		return "keyboard";
	}

	@Override
	protected ClickListener getButtonListener() {
		return new StartButtonListener();
	}

}
