package view.robot;

import decisional.FilterCalibration;
import decisional.StateMachineTransitionForDecisionV1;
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
		if (this.paused) {
			this.robot.input.LEFT = 0;
			this.robot.input.RIGHT = 0;
			return;
		}
		this.cpt += 1;
		//this.oldSensorAlgorithm();
		//this.newAlgorithm();
		this.decisionAlgorithm();
	}

	private void decisionAlgorithm() {
		this.SMT.readSensorsSimulation(cpt_simu);
		this.SMT.FBloc();
		this.SMT.MBloc();
		RobotConfig speeds = SMT.GBloc();
		RobotConfig calibratedSpeeds = new RobotConfig(FT.filter(speeds));
		this.robot.input.RIGHT = speeds.getRightPower0to255() * speeds.getRightDirection();
		this.robot.input.LEFT = speeds.getLeftPower0to255() * speeds.getLeftDirection();
		this.robot.input.STATE = this.SMT.getState();
		this.cpt_simu++;
	}

	/*
	private void newAlgorithm() {
		// Move forward
		this.robot.input.AXIS_Y = 1;
		this.robot.input.RIGHT = 0;
		this.robot.input.LEFT = 0;
		// Don't turn yet
		if (this.found && this.robot.getSensorAngle() < 10) {
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

	// this.robot.input.AXIS_X = 1;
	this.found = true;
} else if (this.robot.detect(obstacle, SensorType.LATERAL)) {
	if (this.robot.getLateralDistance(obstacle.getBoundingRectangle()) < 20) {

		/*
	 * if (this.robot.getSensorAngle() < -15) {
	 * System.out.println("Slow down"); this.robot.input.AXIS_Y
	 * = 1; }

		// this.robot.input.AXIS_X = 1;
		this.found = true;
	} else if (this.robot.detect(obstacle, SensorType.LATERAL)) {
		if (this.robot.getLateralDistance(obstacle.getBoundingRectangle()) < 20) {

			this.robot.input.AXIS_X = 1;
		}
	}
}
}
}
	 */
/*
	private void oldSensorAlgorithm() {
		// Move forward
		this.robot.input.AXIS_Y = 1;
		this.robot.input.AXIS_X = 0;
		// Don't turn yet
		System.out.println(this.robot.getSensorAngle());
		if (this.found && /* this.cpt % 3 == 0 &&  this.robot.getSensorAngle() < 10) {
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
*/
}


