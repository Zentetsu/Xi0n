package gui.buttons;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import gui.Xi0nSimulation;

public class LockButton extends UIButton {

	private final static String GRAPHIC_ELEMENT = "lock";

	public LockButton(float x, float y) {
		super(x, y, GRAPHIC_ELEMENT);
		down(GRAPHIC_ELEMENT);
	}

	private class LockButtonListener extends ClickListener {
		@Override
		public void clicked(InputEvent input, float x, float y) {
			if (isChecked()) {
				Xi0nSimulation.INSTANCE.startRobot();
			} else {
				Xi0nSimulation.INSTANCE.pauseRobot();
			}
		}
	}

	public String toString() {
		return GRAPHIC_ELEMENT;
	}

	@Override
	protected ClickListener getButtonListener() {
		return new LockButtonListener();
	}
	
	public void lock(){
		this.setChecked(false);
		Xi0nSimulation.INSTANCE.pauseRobot();
	}

}
