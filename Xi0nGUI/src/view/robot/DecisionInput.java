package view.robot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import view.Obstacle;
import view.Room;

public class DecisionInput extends CustomInput {

	private boolean found;
	private int cpt;

	public DecisionInput(Robot robot, Room room) {
		super(robot, room);
		this.found = false;
		this.cpt = 0;
	}

	@Override
	public void updateInput() {
		this.cpt += 1;
		
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.R)) {
			this.robot.initialise(0, 0);
		}

		// Move forward
		this.robot.input.AXIS_Y = 1;
		this.robot.input.AXIS_X = 0;
		// Don't turn yet
		System.out.println(this.robot.getSensorAngle());
		if (this.found && /*this.cpt % 3 == 0 &&*/ this.robot.getSensorAngle()<10) {
			this.robot.input.AXIS_X = -1;
		}

		for (Obstacle obstacle : this.room.getObstacles()) {
			if (this.robot.detect(obstacle)) {
				// Turn right
				if(this.robot.getSensorAngle()<-15){
					System.out.println("Slow down");
					this.robot.input.AXIS_Y = (float) 0.1;
				}
				this.robot.input.AXIS_X = 1;
				found = true;
			}
		}
	}

}
