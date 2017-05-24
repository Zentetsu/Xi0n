package view.robot;


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
		//this.oldSensorAlgorithm();
		//this.newAlgorithm();
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
		System.out.print(SMT.getState());
		switch ( SMT.getState() ) {
		case EMERGENCY_STANDING_STILL :
			this.robot.input.AXIS_Y = 0;
			this.robot.input.AXIS_X = 0;
			break;
		case STANDING_STILL :
			this.robot.input.AXIS_Y = 0;
			this.robot.input.AXIS_X = 0;
			break;
		case MANUAL :
			this.robot.input.AXIS_Y = 0;
			this.robot.input.AXIS_X = 0;
			break;
		case WALL_FINDER :
			this.robot.input.AXIS_Y = 1;
			this.robot.input.AXIS_X = 0;
			break;
		case WALL_RIDER :
			this.robot.input.AXIS_Y = 1;
			this.robot.input.AXIS_X = 0;
			break;
		case FRONT_WALL_RIDER_ROTATION_POST_FINDER :
			this.robot.input.AXIS_Y = 0;
			this.robot.input.AXIS_X = 1;
			break;
		case FRONT_WALL_RIDER_ROTATION :
			this.robot.input.AXIS_Y = 0;
			this.robot.input.AXIS_X = 1;
			break;
		case NO_RIGHT_WALL_RIDER_ROTATION_1 :
			this.robot.input.AXIS_Y = 0;
			this.robot.input.AXIS_X = -1;
			break;
		case NO_RIGHT_WALL_RIDER_ROTATION_2 :
			this.robot.input.AXIS_Y = 0;
			this.robot.input.AXIS_X = -1;
			break;
		default :
			this.robot.input.AXIS_Y = 0;
			this.robot.input.AXIS_X = 0;
		}
		
		System.out.print("\n");
		
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
