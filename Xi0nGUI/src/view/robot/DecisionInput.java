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

	public DecisionInput(Robot robot, Room room) {
		super(robot, room);
		this.found = false;
		this.cpt = 0;
		SMT = new StateMachineTransitionForDecisionV1 ();
		FT = new FilterCalibration();
		boolean testLoad = FT.loadCalibrationFile();
	}

	@Override
	public void updateInput() {
		super.updateInput();
		briceAlgorithm ();
		
		this.cpt += 1;
		this.oldSensorAlgorithm();

	}
	
	private RobotConfig briceAlgorithm () {
		RobotConfig speeds = new RobotConfig (0,0,0,0);;
		RobotConfig calibratedSpeeds = new RobotConfig ( FT.filter(speeds) );
		SMT.readSensors ();
		SMT.FBloc();
		SMT.MBloc();
		speeds = SMT.GBloc();
		return ( FT.filter(speeds) ) ;
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
				/*if (this.robot.getSensorAngle() < -15) {
					System.out.println("Slow down");
					this.robot.input.AXIS_Y = 1;
				}*/
				//this.robot.input.AXIS_X = 1;
				found = true;
			}else if (this.robot.detect(obstacle, SensorType.LATERAL)) {
				if(this.robot.getLateralDistance(obstacle.getBoundingRectangle())<20){
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
