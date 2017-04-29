package view.robot;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public class Sensor {

	private final float speed = (float) 1.5;
	private final int maxAngle = 45;

	private Polygon cone;
	private float angle;
	private int sens;

	public Sensor(float x, float y) {
		this.cone = new Polygon(new float[] { x, y + RobotConstant.HEIGHT_2, x - 10, y + RobotConstant.HEIGHT_2 + 40,
				x + 10, y + RobotConstant.HEIGHT_2 + 40 });
		this.sens = 1;
		this.angle = 0;
	}

	public void setPosition(float x, float y) {
		this.cone.setPosition(x, y);
	}
	
	public boolean collide(Rectangle rectangle){
		return this.cone.getBoundingRectangle().overlaps(rectangle);
	}

	public void update() {
		if (Math.abs(this.angle) > this.maxAngle) {
			this.sens *= -1;
		}
		this.angle += this.speed * this.sens;
		this.cone.rotate(this.speed * this.sens);
		System.out.println(this.angle);
	}

	public void render(ShapeRenderer sr) {
		sr.polygon(this.cone.getTransformedVertices());
	}

	public void rotate(float angle) {
		this.cone.rotate(angle);
	}

}
