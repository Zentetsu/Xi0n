package view.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import view.robot.Robot;

public class RestartButton extends UIButton {

	private final static String GRAPHIC_ELEMENT = "restart.png";

	public RestartButton(float x, float y) {
		super(x, y, GRAPHIC_ELEMENT);
	}

	private class RetartButtonListener extends ClickListener {
		@Override
		public void clicked(InputEvent input, float x, float y) {
			Robot.getInstance(null).initialize(0, 0);
			System.out.println("Restart");
		}
	}

	@Override
	protected ClickListener getButtonListener() {
		return new RetartButtonListener();
	}

}
