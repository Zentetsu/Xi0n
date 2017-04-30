package view.robot;

import java.util.HashMap;
import java.util.Map;

import view.ControllerInput;

public class InputManager {

	public boolean SOUTH;
	public boolean EST;
	public boolean START;
	public boolean NORTH;
	
	public float AXIS_X;
	public float AXIS_Y;
	
	private Map<Mode, CustomInput> inputs;
	private Mode currentMode;
	
	public InputManager(Robot robot, Mode currentMode) {
		this.inputs = new HashMap<>();
		this.inputs.put(Mode.CONTROLLER, new ControllerInput(robot));
		this.inputs.put(Mode.KEYBOARD, new KeyboardInput(robot));
		this.setMode(currentMode);
	}
	
	public void setMode(Mode mode){
		this.currentMode = mode;
	}

	public void update() {
		this.inputs.get(currentMode).updateInput();
	}
}


