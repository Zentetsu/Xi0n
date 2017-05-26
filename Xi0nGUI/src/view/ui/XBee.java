package view.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class XBee extends UIButton {

	private final static String GRAPHIC_ELEMENT = "xbee_ok";
	private final static int F = 40;
	private int counter;

	public XBee(float x, float y) {
		super(x, y, GRAPHIC_ELEMENT);
		this.setDown("xbee_nok");
		this.counter = 0;
	}

	private class StartButtonListener extends ClickListener {
		@Override
		public void clicked(InputEvent input, float x, float y) {
			// Uncheck
			toggle();
		}
	}

	public String toString() {
		return GRAPHIC_ELEMENT;
	}

	@Override
	protected ClickListener getButtonListener() {
		return new StartButtonListener();
	}

	public void blink() {
		this.counter += 1;

		if (this.counter == F) {
			this.toggle();
			this.counter = 0;
		}
	}
}
