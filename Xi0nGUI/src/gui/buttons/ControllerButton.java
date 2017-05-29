package gui.buttons;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import gui.Xi0nSimulation;
import physic.Mode;

public class ControllerButton extends UIButton {

	private final static String GRAPHIC_ELEMENT = "controller";

	public ControllerButton(float x, float y) {
		super(x, y, GRAPHIC_ELEMENT);
		down(GRAPHIC_ELEMENT);
	}

	private class StartButtonListener extends ClickListener {
		@Override
		public void clicked(InputEvent input, float x, float y) {
			ControllerButton.this.resetRobotInputs();
			Xi0nSimulation.INSTANCE.setMode(Mode.CONTROLLER);
		}
	} 
	
	public String toString() {
		return GRAPHIC_ELEMENT;
	}

	@Override
	protected ClickListener getButtonListener() {
		return new StartButtonListener();
	}

}
