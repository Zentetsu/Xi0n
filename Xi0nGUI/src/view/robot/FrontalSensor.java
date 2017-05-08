package view.robot;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import view.robot.RobotConstant;

public class FrontalSensor extends Sensor {

	private final float speed = (float) 1.5 * 3;
	private final int maxAngle = 45;

	private Polygon cone;
	private float angle;
	private int sens;
	private float x;
	private float y;

	public FrontalSensor(float x, float y) {
		this.type = SensorType.FRONTAL;

		this.cone = new Polygon(new float[] { x, y, x - 10 * 2, y + RobotConstant.HEIGHT_2 * 8 + 40, x + 10 * 2,
				y + RobotConstant.HEIGHT_2 * 8 + 40 });
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
		Rectangle rect = new Rectangle();
		boolean intersect = Intersector.intersectRectangles(rectangle, new Rectangle(this.x, this.y, 5, 50), rect);
		System.out.println(intersect + " | " + rect);
		return 0;
	}

}
