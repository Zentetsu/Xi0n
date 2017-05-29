package physic.robot;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import physic.RotableRectangle;

public class LateralSensor extends Sensor {

	//ultrason sur plateforme : 4.5*1.8
	
	public static final float WIDTH = 5.4f;
	public static final float HEIGHT = 13.5f;
	public static final float WARNING_LENGTH = 100;
	public static final float STOP_LENGTH = 60;

	private RotableRectangle warningZone;
	private RotableRectangle stopZone;
	private RotableRectangle sensor;

	public LateralSensor(float x, float y) {
		super();
		this.sensor = new RotableRectangle(x, y, WIDTH, HEIGHT);
		this.warningZone = new RotableRectangle(x, y, WARNING_LENGTH, 10);
		this.stopZone = new RotableRectangle(x, y, STOP_LENGTH, 10);
	}

	public void setPosition(float x, float y, float angle) {
		this.sensor.setPosition(x, y);
		this.warningZone.setPosition(x, y);
		this.stopZone.setPosition(x, y);
		float cos = (float) Math.cos(Math.toRadians(angle-90));
		float sin = (float) Math.sin(Math.toRadians(angle-90));
		this.updateObstaclePosition(x , y, sin*(RobotConstant.WIDTH_2 + this.getDistance()), cos*(RobotConstant.WIDTH_2 + this.getDistance()));
	}
	

	@Override
	public boolean collide(Rectangle rectangle) {
		return this.warningZone.collide(rectangle) || this.stopZone.collide(rectangle);
	}

	@Override
	public void render(ShapeRenderer sr) {
		super.render(sr);
		this.sensor.render(sr, Color.RED);
		this.warningZone.render(sr, Color.GREEN);
		this.stopZone.render(sr, Color.RED);
	}

	@Override
	public void rotate(float angle) {
		this.sensor.rotate(angle);
		this.warningZone.rotate(angle);
		this.stopZone.rotate(angle);
	}

	@Override
	public float getDistance(Rectangle rectangle) {
		if (this.stopZone.collide(rectangle)) {
			return LateralSensor.STOP_LENGTH;
		}
		if (this.warningZone.collide(rectangle)) {
			return LateralSensor.WARNING_LENGTH;
		}
		return LateralSensor.WARNING_LENGTH + LateralSensor.STOP_LENGTH;
	}

}
