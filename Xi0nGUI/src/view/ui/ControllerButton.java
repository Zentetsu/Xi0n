package view.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import view.Xi0nSimulation;
import view.robot.Mode;

public class ControllerButton extends UIButton {

	private final static String GRAPHIC_ELEMENT = "controller";

	public ControllerButton(float x, float y) {
		super(x, y, GRAPHIC_ELEMENT);
	}

	private class StartButtonListener extends ClickListener {
		@Override
		public void clicked(InputEvent input, float x, float y) {
			Xi0nSimulation.INSTANCE.setMode(Mode.CONTROLLER);
			down(GRAPHIC_ELEMENT);
		}
	} 
	
	public String toString() {
		return "controller";
	}

	@Override
	protected ClickListener getButtonListener() {
		return new StartButtonListener();
	}

}
