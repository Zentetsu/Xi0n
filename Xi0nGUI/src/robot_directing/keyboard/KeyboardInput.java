package robot_directing.keyboard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import physic.robot.Robot;
import robot_directing.AbstractInput;

public class KeyboardInput extends AbstractInput {

	public KeyboardInput(Robot robot) {
		super(robot);
	}

	@Override
	public void updateInput() {

		super.updateInput();

		this.robot.input.LEFT = 0;
		this.robot.input.RIGHT = 0;
		
		if (this.paused) {
			this.robot.input.LEFT = 0;
			this.robot.input.RIGHT = 0;
			return;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
			this.robot.input.LEFT = 255;
			this.robot.input.RIGHT = 255;
		}
		else{
			if (Gdx.input.isKeyPressed(Input.Keys.S)) {
				this.robot.input.LEFT = -255;
				this.robot.input.RIGHT = -255;
			}
		}		


		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			this.robot.input.LEFT = -255;
			this.robot.input.RIGHT = 255;
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			this.robot.input.LEFT = 255;
			this.robot.input.RIGHT = -255;
		}
	}
}

