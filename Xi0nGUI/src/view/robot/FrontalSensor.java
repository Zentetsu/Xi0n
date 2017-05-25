package view.robot;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import view.robot.RobotConstant;

public class FrontalSensor extends Sensor {

	private final float speed = (float) 1.5 * 3 ;
	private final int maxAngle = 45;

	private Polygon cone;
	private float angle;
	private int sens;
	private float x;
	private float y;

	public FrontalSensor(float x, float y) {
		this.type = SensorType.FRONTAL;

		this.cone = new Polygon(
				new float[] { x, y, x - 5 * 2, y + RobotConstant.HEIGHT, x + 5 * 2, y + RobotConstant.HEIGHT });
		this.sens = 1;
		this.angle = 0;

		this.x = x;
		this.y = y;

	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		this.cone.setPosition(x, y);
	}

	public boolean collide(Rectangle rectangle) {
		return this.cone.getBoundingRectangle().overlaps(rectangle);
	}

	public void update() {
		if (Math.abs(this.angle) > this.maxAngle) {
			this.sens *= -1;
		}
		this.angle += this.speed * this.sens;
		this.cone.rotate(this.speed * this.sens);
	}

	public void render(ShapeRenderer sr) {
		sr.polygon(this.cone.getTransformedVertices());
	}

	public void rotate(float angle) {
		this.cone.rotate(angle);
	}

	public float getAngle() {
		return this.angle;
	}

	@Override
	public int getDistance(Rectangle rectangle) {
		if(this.collide(rectangle)){
			return RobotConstant.HEIGHT;
		}
		return RobotConstant.HEIGHT + 20;
	}

}
