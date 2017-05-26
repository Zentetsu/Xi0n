package view.robot;

import java.util.HashMap;
import java.util.Map;

import decisional.State1;
import decisional.State2;
import decisional.State3;
import decisional.State4;
import view.Room;

public class InputManager {

	public boolean START; // RESTART
	public boolean NORTH; // QUIT

	public float LEFT;
	public float RIGHT;
	
	public float DIRECTION;

	private Map<Mode, CustomInput> inputs;
	private Mode currentMode;
	public State4 STATE;

	public InputManager(Robot robot, Mode currentMode, Room room) {
		this.inputs = new HashMap<>();
		this.inputs.put(Mode.CONTROLLER, new ControllerInput(robot, room));
		this.inputs.put(Mode.KEYBOARD, new KeyboardInput(robot, room));
		this.inputs.put(Mode.AUTOMATIC, new DecisionInput(robot, room));
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
		this.DIRECTION = 0;
		this.LEFT = 0;
		this.RIGHT = 0;
		this.NORTH = false;
		this.START = false;
		for (CustomInput input : this.inputs.values()) {
			input.reset();
		}
		
	}

	public void pause() {
		for (CustomInput input : this.inputs.values()) {
			input.pause();
		}
	}

	public void start() {
		for (CustomInput input : this.inputs.values()) {
			input.start();
		}
	}

}
