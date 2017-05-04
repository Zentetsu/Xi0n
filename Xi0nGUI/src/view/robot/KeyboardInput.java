package view.robot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import view.Room;

public class KeyboardInput extends CustomInput {
	
	public KeyboardInput(Robot robot, Room room) {
		super(robot, room);
	}
	
	@Override
	public void updateInput() {
		
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.R)) {
			this.robot.initialise(0, 0);
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
			this.robot.input.AXIS_Y = 1;
		}
		else{
			if (Gdx.input.isKeyPressed(Input.Keys.S)) {
				this.robot.input.AXIS_Y = -1;
			}
			else
				this.robot.input.AXIS_Y = 0;
		}		
		
		
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			this.robot.input.AXIS_X = 1;
		}
		else{
			if (Gdx.input.isKeyPressed(Input.Keys.D)) {
				this.robot.input.AXIS_X = -1;
			}
			else{
				this.robot.input.AXIS_X = 0;
			}
		}
	}

}
