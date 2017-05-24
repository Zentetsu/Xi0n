package view.robot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import view.Room;
import view.ScreenshotFactory;

public class CustomInput {

	protected Robot robot;
	protected Room room;
	protected boolean paused;

	public CustomInput(Robot robot, Room room) {
		this.room = room;
		this.robot = robot;
		this.paused = true;
	}

	public void updateInput() {
		// Standard actions
		
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.R)) {
			this.robot.initialize(0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.F12)) {
			//ScreenshotFactory.saveScreenshot();
		}
	}
	
	public void pause() {
		this.paused = true;
	}

	public void start() {
		this.paused = false;
	}
}
