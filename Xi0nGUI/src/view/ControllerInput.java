package view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;

import view.robot.CustomInput;
import view.robot.Robot;

public class ControllerInput extends ControllerAdapter implements CustomInput{

	private Robot robot;
	
	public ControllerInput(Robot robot) {
		this.robot = robot;
		Controllers.addListener(this);
	}
	
	@Override
	public void updateInput() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean axisMoved(Controller controller, int axisIndex, float value) {
		
		if (axisIndex == 0){
			if (Math.abs(value) < 0.2)
				this.robot.input.AXIS_X = 0;
			else
				this.robot.input.AXIS_X = -value;
		}
			
		if (axisIndex == 1){
			if (Math.abs(value) < 0.2)
				this.robot.input.AXIS_Y = 0;
			else
				this.robot.input.AXIS_Y = -value;
		}
		
			
		return true;
	}
	
	@Override
	public boolean buttonDown(Controller controller, int buttonIndex) {
		if (buttonIndex == 0)
			this.robot.input.SOUTH = true;
		if (buttonIndex == 1)
			this.robot.input.EST = true;
		if (buttonIndex == 9)
			this.robot.initialise(0, 0);
		return true;
	}
	
	@Override
	public boolean buttonUp(Controller controller, int buttonIndex) {
		if (buttonIndex == 0)
			this.robot.input.SOUTH = false;
		if (buttonIndex == 1)
			this.robot.input.EST = false;
		if (buttonIndex == 3)
			Gdx.app.exit();
		return true;
	}

	
	
	
}