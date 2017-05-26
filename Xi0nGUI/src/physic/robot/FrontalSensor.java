package physic.robot;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public class FrontalSensor extends Sensor {

	private final float speed = 2.5f;
	private final int maxAngle = 45;
	
	public static final int FRONTAL_LENGTH = 40;
	public static final int FRONTAL_WIDTH = 5;

	private Polygon cone;
	private float angle;
	private int sens;
	private float x;
	private float y;

	public FrontalSensor(float x, float y) {
		this.cone = new Polygon(
				new float[] { x, y, x - FRONTAL_WIDTH * 2, y + FRONTAL_LENGTH, x + FRONTAL_WIDTH * 2, y + FRONTAL_LENGTH });
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
			return FRONTAL_LENGTH;
		}
		return FRONTAL_LENGTH + 20;
	}

}
