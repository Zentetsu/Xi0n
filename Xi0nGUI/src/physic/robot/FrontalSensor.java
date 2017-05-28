package physic.robot;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import physic.RotableRectangle;

public class FrontalSensor extends Sensor {

	//axe infra = centré et limite plateforme : margin de 3.5cm
	//infra = 4.5*1.5
	private final float speed = 1 ;
	private final int maxAngle = 20;
	
	public static final int FRONTAL_LENGTH = 20;
	public static final int FRONTAL_WIDTH = 4;
	public static final float WIDTH = 13.5f;
	public static final float HEIGHT = 4.5f;
	
	private Polygon cone;
	private float angle;
	private int sens;
	private RotableRectangle sensor;

	public FrontalSensor(float x, float y) {
		super();
		this.sensor = new RotableRectangle(-WIDTH/2, -HEIGHT/2, WIDTH, HEIGHT);
		this.cone = new Polygon(new float[] {0, 0, x-FRONTAL_WIDTH * 1.5f, y+FRONTAL_LENGTH, x+FRONTAL_WIDTH * 1.5f, y+FRONTAL_LENGTH });
		this.sens = 1;
		this.angle = 0;
		
	}

	public void setPosition(float x, float y, float angle) {
		float cos = (float) Math.cos(Math.toRadians(angle));
		float sin = (float) Math.sin(Math.toRadians(angle));
		this.sensor.setPosition(x - sin*(6+RobotConstant.HEIGHT_2), y + cos*(6+RobotConstant.HEIGHT_2));
		this.cone.setPosition(x - sin*(6+RobotConstant.HEIGHT_2),  y + cos*(6+RobotConstant.HEIGHT_2));
		float cos2 = (float) Math.cos(Math.toRadians(this.angle + angle));
		float sin2 = (float) Math.sin(Math.toRadians(this.angle + angle));
		this.updateObstaclePosition(x, y, sin*(RobotConstant.HEIGHT_2 + 11.5f) + sin2*this.getDistance(), cos*(RobotConstant.HEIGHT_2 + + 11.5f) + cos2*this.getDistance());
	}

	public boolean collide(Rectangle rectangle) {
		return this.cone.getBoundingRectangle().overlaps(rectangle);
	}

	public void update(float angle, float distance) {
		if (Math.abs(this.angle) > this.maxAngle) {
			this.sens *= -1;
		}
		//this.angle += this.speed * this.sens;
		//this.rotate(this.speed * this.sens);
		
		float dif = angle - 90 - this.angle;
		this.angle = angle - 90;
		this.setDistance(distance);
		this.rotate(dif);
	}

	public void render(ShapeRenderer sr) {
		super.render(sr);
		sr.polygon(this.cone.getTransformedVertices());
		this.sensor.render(sr, Color.DARK_GRAY);
	}

	public void rotate(float angle) {
		this.cone.rotate(angle);
		this.sensor.rotate(angle);
	}

	public float getAngle() {
		return this.angle;
	}

	@Override
	public float getDistance(Rectangle rectangle) {
		if(this.collide(rectangle)){
			return FRONTAL_LENGTH;
		}
		return FRONTAL_LENGTH + 20;
	}
}
