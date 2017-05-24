package view.robot;

import java.util.HashMap;
import java.util.Map;

import view.Room;

public class InputManager {

	public boolean SOUTH;
	public boolean EST;
	public boolean START;
	public boolean NORTH;

	public float AXIS_X;
	public float AXIS_Y;

	private Map<Mode, CustomInput> inputs;
	private Mode currentMode;

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
