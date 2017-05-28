package robot_directing.decisional;

import physic.robot.Robot;
import physic.robot.RobotConfig;
import robot_directing.AbstractInput;

public class DecisionInput extends AbstractInput {

	private boolean found;
	private int cpt;
	StateMachineTransitionForDecisionV5 SMT;
	FilterCalibration FT;
	int cpt_simu = 0;

	public DecisionInput(Robot robot) {
		super(robot);
		this.found = false;
		this.cpt = 0;
		SMT = new StateMachineTransitionForDecisionV5();
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
		this.SMT.readSensorsSimu();
		this.SMT.FBloc();
		this.SMT.MBloc();
		RobotConfig speeds = SMT.GBloc();
		RobotConfig calibratedSpeeds = new RobotConfig(FT.filter(speeds));
		// TODO : communication de la vitesse en passant par l'homme mort
		if ( speeds.getRightDirection() != 2 && speeds.getLeftDirection() != 2 ) {
			this.robot.input.RIGHT = speeds.getRightPower0to255() * speeds.getRightDirection();
			this.robot.input.LEFT = speeds.getLeftPower0to255() * speeds.getLeftDirection();
		}
		else {
			this.robot.input.RIGHT = 0;
			this.robot.input.LEFT = 0;
		}
		this.robot.input.STATE = this.SMT.getState();
		this.cpt_simu++;
	}
}


