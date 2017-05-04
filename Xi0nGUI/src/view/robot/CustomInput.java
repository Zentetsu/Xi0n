package view.robot;

import view.Room;

public class CustomInput {

	protected Robot robot;
	protected Room room;

	public CustomInput(Robot robot, Room room) {
		this.room = room;
		this.robot = robot;
	}
	
	public void updateInput() {
		// Do nothing by default
	}
}
