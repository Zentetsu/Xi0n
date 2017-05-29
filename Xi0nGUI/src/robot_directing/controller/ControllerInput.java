package robot_directing.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

import physic.robot.Robot;
import robot_directing.AbstractInput;

public class ControllerInput extends AbstractInput implements ControllerListener {

	private boolean isTurning = false;
	private boolean isMoving = false;
	private float leftSaved;
	private float rightSaved;

	public ControllerInput(Robot robot) {
		super(robot);
		Controllers.addListener(this);
	}

	@Override
	public boolean axisMoved(Controller controller, int axisIndex, float value) {

		if(this.paused){
			return false;
		}

		// FORWARD AND BACKWARD
		if (axisIndex == 1 && !this.isTurning){
			if (Math.abs(value) > 0.2){
				this.leftSaved = this.robot.input.LEFT = 255 * -value;
				this.rightSaved = this.robot.input.RIGHT = 255 * -value;
				this.isMoving = true;
			}
			else{
				this.isMoving = false;
				this.leftSaved = this.robot.input.LEFT = 0;
				this.rightSaved = this.robot.input.RIGHT = 0;
			}
		}

		

		// LEFT AND RIGHT
		if (axisIndex == 2){
			if (Math.abs(value) > 0.3){
				this.robot.input.LEFT = 255 * value;
				this.robot.input.RIGHT = 255 * -value;
				this.isTurning = true;
			}
			else{
				this.isTurning = false;
				if (this.isMoving){
					this.robot.input.LEFT = this.leftSaved;
					this.robot.input.RIGHT = this.rightSaved;
				}
				else{
					this.robot.input.LEFT = 0;
					this.robot.input.RIGHT = 0;
				}
			}
		}

		return true;
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonIndex) {

		if(this.paused){
			return false;
		}
		if (buttonIndex == 9)
			this.robot.initialize(0, 0);
		return true;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonIndex) {

		if(this.paused){
			return false;
		}
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
