package view.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import view.Xi0nSimulation;

public class PauseButton extends UIButton {

	private final static String GRAPHIC_ELEMENT = "pause";

	public PauseButton(float x, float y) {
		super(x, y, GRAPHIC_ELEMENT);
	}

	private class StartButtonListener extends ClickListener {
		@Override
		public void clicked(InputEvent input, float x, float y) {
			Xi0nSimulation.INSTANCE.pauseRobot();
			Xi0nSimulation.INSTANCE.removeButton("pause");
			Xi0nSimulation.INSTANCE.addButton(new StartButton(getX(), getY()));
		}
	}

	public String toString() {
		return "pause";
	}

	@Override
	protected ClickListener getButtonListener() {
		return new StartButtonListener();
	}

}
