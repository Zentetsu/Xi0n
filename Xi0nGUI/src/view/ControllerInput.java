package view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

import view.robot.CustomInput;
import view.robot.Robot;

public class ControllerInput extends CustomInput implements ControllerListener {
	
	public ControllerInput(Robot robot, Room room) {
		super(robot, room);
		Controllers.addListener(this);
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
			this.robot.initialize(0, 0);
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

	@Override
	public boolean accelerometerMoved(Controller arg0, int arg1, Vector3 arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void connected(Controller arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnected(Controller arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean povMoved(Controller arg0, int arg1, PovDirection arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	
}
