package view.robot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import decisional.FilterCalibration;
import decisional.StateMachineTransitionForDecisionV1;
import view.Obstacle;
import view.Room;

public class DecisionInput extends CustomInput {

	private boolean found;
	private int cpt;
	StateMachineTransitionForDecisionV1 SMT;
	FilterCalibration FT;
	int cpt_simu = 0;

	public DecisionInput(Robot robot, Room room) {
		super(robot, room);
		this.found = false;
		this.cpt = 0;
		SMT = new StateMachineTransitionForDecisionV1();
		FT = new FilterCalibration();
		boolean testLoad = FT.loadCalibrationFile();
	}

	@Override
	public void updateInput() {
		super.updateInput();
		this.cpt += 1;
		// this.oldSensorAlgorithm();
		this.decisionAlgorithm();

	}

	private void decisionAlgorithm() {
		RobotConfig speeds = new RobotConfig(0, 0, 0, 0);
		RobotConfig calibratedSpeeds = new RobotConfig(FT.filter(speeds));
		SMT.readSensorsSimulation(cpt_simu);
		SMT.FBloc();
		SMT.MBloc();
		speeds = SMT.GBloc();
		calibratedSpeeds = FT.filter(speeds);
		System.out.println(SMT.getState());
		if (speeds.equals(StateMachineTransitionForDecisionV1.WALL_FINDER_SPEED)) {
			//System.out.println("WALL_FINDER_SPEED");
			this.robot.input.AXIS_Y = 1;
			this.robot.input.AXIS_X = 0;
		} else if (speeds.equals(StateMachineTransitionForDecisionV1.WALL_RIDER_SPEED)) {
			//System.out.println("WALL_RIDER_SPEED");
			this.robot.input.AXIS_Y = 1;
			this.robot.input.AXIS_X = 0;
		} else if (speeds.equals(StateMachineTransitionForDecisionV1.EMERGENCY_STANDING_STILL_SPEED)) {
			//System.out.println("EMERGENCY_STANDING_STILL_SPEED");
			this.robot.input.AXIS_Y = 0;
			this.robot.input.AXIS_X = 0;
		} else if (speeds.equals(StateMachineTransitionForDecisionV1.STANDING_STILL_SPEED)) {
			//System.out.println("STANDING_STILL_SPEED");
			this.robot.input.AXIS_Y = 0;
			this.robot.input.AXIS_X = 0;
		} else if (speeds.equals(StateMachineTransitionForDecisionV1.STANDING_LEFT_ROTATION_SPEED)) {
			//System.out.println("STANDING_LEFT_ROTATION_SPEED");
			this.robot.input.AXIS_Y = 0;
			this.robot.input.AXIS_X = 1;
		} else if (speeds.equals(StateMachineTransitionForDecisionV1.STANDING_RIGHT_ROTATION_SPEED)) {
			//System.out.println("STANDING_RIGHT_ROTATION_SPEED");
			this.robot.input.AXIS_Y = 0;
			this.robot.input.AXIS_X = -1;
		} else {
			//System.out.println("ERROR");
			this.robot.input.AXIS_Y = 0;
			this.robot.input.AXIS_X = 0;
		}
		
		cpt_simu++;
	}

	private void newAlgorithm() {
		// Move forward
		this.robot.input.AXIS_Y = 1;
		this.robot.input.AXIS_X = 0;
		// Don't turn yet
		System.out.println(this.robot.getSensorAngle());
		if (this.found && /* this.cpt % 3 == 0 && */ this.robot.getSensorAngle() < 10) {
			this.robot.input.AXIS_X = -1;
		}

		for (Obstacle obstacle : this.room.getObstacles()) {
			if (this.robot.detect(obstacle, SensorType.FRONTAL)) {
				int distance = this.robot.getFrontalDistance(obstacle.getBoundingRectangle());
				// Turn right
				/*
				 * if (this.robot.getSensorAngle() < -15) {
				 * System.out.println("Slow down"); this.robot.input.AXIS_Y = 1;
				 * }
				 */
				// this.robot.input.AXIS_X = 1;
				found = true;
			} else if (this.robot.detect(obstacle, SensorType.LATERAL)) {
				if (this.robot.getLateralDistance(obstacle.getBoundingRectangle()) < 20) {
					this.robot.input.AXIS_X = 1;
				}
			}
		}
	}

	private void oldSensorAlgorithm() {
		// Move forward
		this.robot.input.AXIS_Y = 1;
		this.robot.input.AXIS_X = 0;
		// Don't turn yet
		System.out.println(this.robot.getSensorAngle());
		if (this.found && /* this.cpt % 3 == 0 && */ this.robot.getSensorAngle() < 10) {
			this.robot.input.AXIS_X = -1;
		}

		for (Obstacle obstacle : this.room.getObstacles()) {
			if (this.robot.detect(obstacle, SensorType.FRONTAL)) {
				// Turn right
				if (this.robot.getSensorAngle() < -15) {
					System.out.println("Slow down");
					this.robot.input.AXIS_Y = (float) 1;
				}
				this.robot.input.AXIS_X = 1;
				found = true;
			}
		}
	}

}
