package robot_directing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import mapping.Room;
import physic.robot.Robot;

public class AbstractInput {

	protected Robot robot;
	protected Room room;
	protected boolean paused;

	public AbstractInput(Robot robot) {
		this.robot = robot;
		this.paused = true;
	}

	public void reset(){
		System.out.println("restart");
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
