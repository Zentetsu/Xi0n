package gui.buttons;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import gui.Xi0nSimulation;
import physic.robot.Robot;

public class RestartButton extends UIButton {

	private final static String GRAPHIC_ELEMENT = "restart";

	public RestartButton(float x, float y) {
		super(x, y, GRAPHIC_ELEMENT);
	}

	private class RetartButtonListener extends ClickListener {
		@Override
		public void clicked(InputEvent input, float x, float y) {
			Robot.getInstance().initialize(0, 0);
			Xi0nSimulation.INSTANCE.restart();
		}
	}

	@Override
	protected ClickListener getButtonListener() {
		return new RetartButtonListener();
	}

}
