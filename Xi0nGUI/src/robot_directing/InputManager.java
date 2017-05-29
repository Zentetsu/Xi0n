package robot_directing;

import java.util.HashMap;
import java.util.Map;

import physic.Mode;
import physic.robot.Robot;
import robot_directing.controller.ControllerInput;
import robot_directing.decisional.DecisionInput;
import robot_directing.decisional.State5;
import robot_directing.keyboard.KeyboardInput;

public class InputManager {

	public boolean START; // RESTART
	public boolean QUIT; // QUIT

	public float LEFT;
	public float RIGHT;
	
	private Map<Mode, AbstractInput> inputs;
	private Mode currentMode;
	public State5 STATE;

	public InputManager(Robot robot, Mode currentMode) {
		this.inputs = new HashMap<>();
		this.inputs.put(Mode.CONTROLLER, new ControllerInput(robot));
		this.inputs.put(Mode.KEYBOARD, new KeyboardInput(robot));
		this.inputs.put(Mode.AUTOMATIC, new DecisionInput(robot));
		this.setMode(currentMode);
	}

	public void setMode(Mode mode) {
		this.currentMode = mode;
	}

	public void update() {
		this.inputs.get(this.currentMode).updateInput();
	}

	public boolean isMode(Mode mode) {
		return this.currentMode == mode;
	}
	
	public void reset(){
		this.LEFT = 0;
		this.RIGHT = 0;
		this.QUIT = false;
		this.START = false;
		for (AbstractInput input : this.inputs.values()) {
			input.reset();
		}
		
	}

	public void pause() {
		this.LEFT = 0;
		this.RIGHT = 0;
		this.QUIT = false;
		this.START = false;
		for (AbstractInput input : this.inputs.values()) {
			input.pause();
		}
	}

	public void start() {
		for (AbstractInput input : this.inputs.values()) {
			input.start();
		}
	}

}
