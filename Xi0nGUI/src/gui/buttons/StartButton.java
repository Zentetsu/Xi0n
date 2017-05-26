package gui.buttons;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import gui.Xi0nSimulation;

public class StartButton extends UIButton {

	private final static String GRAPHIC_ELEMENT = "start";

	public StartButton(float x, float y) {
		super(x, y, GRAPHIC_ELEMENT);
	}

	private class StartButtonListener extends ClickListener {
		@Override
		public void clicked(InputEvent input, float x, float y) {
			Xi0nSimulation.INSTANCE.startRobot();
			Xi0nSimulation.INSTANCE.removeButton("start");
			Xi0nSimulation.INSTANCE.addButton(new PauseButton(getX(), getY()));
		}
	}
	
	public String toString() {
		return "start";
	}
	
	@Override
	protected ClickListener getButtonListener() {
		return new StartButtonListener();
	}

}
