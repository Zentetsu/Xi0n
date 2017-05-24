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
		
		super.updateInput();
		
		if (this.paused) {
			this.robot.input.LEFT = 0;
			this.robot.input.RIGHT = 0;
			return;
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
			this.robot.input.LEFT = 200;
			this.robot.input.RIGHT = 200;
		}
		else{
			if (Gdx.input.isKeyPressed(Input.Keys.S)) {
				this.robot.input.LEFT = -200;
				this.robot.input.RIGHT = -200;
			}
			else
				this.robot.input.DIRECTION = 0;
		}		
		
		
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			this.robot.input.LEFT = -200;
			this.robot.input.RIGHT = 200;
		}
		else{
			if (Gdx.input.isKeyPressed(Input.Keys.D)) {
				this.robot.input.LEFT = 200;
				this.robot.input.RIGHT = -200;
			}
			else{
				this.robot.input.LEFT = 0;
				this.robot.input.RIGHT = 0;
			}
		}
	}

}
