package physic.robot;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import physic.Mode;
import physic.RotableRectangle;
import robot_directing.InputManager;

public class Robot {

	public InputManager input;
	private Vector2 position;
	private Polygon body;

	private FrontalSensor frontalSensor;
	private LateralSensor lateralSensor;

	private float rotation;
	private float speed;
	private boolean destroyed;

	private RotableRectangle crazyWheel;
	private RotableRectangle leftWheel;
	private RotableRectangle rightWheel;

	private List<Circle> visited;
	private static Robot instance;

	private Robot(float x, float y) {
		this.input = new InputManager(this, Mode.CONTROLLER);
		this.setMode(Mode.KEYBOARD);
		this.initialize(x, y);
	}

	public static Robot getInstance() {
		if (instance == null) {
			instance = new Robot(0, 0);
		}
		return instance;
	}

	public void initialize(float x, float y) {
		this.input.reset();
		this.visited = new ArrayList<>();
		this.position = new Vector2(x, y);

		this.crazyWheel = new RotableRectangle(x - 2, y - 20, RobotConstant.WIDTH_2 / 2, RobotConstant.WIDTH_2 / 4);
		
		// roue = margin 0.7 : 6.5*2.3
		this.leftWheel = new RotableRectangle(x - RobotConstant.WIDTH_2 - 2, y - 14, 7, 20);
		this.rightWheel = new RotableRectangle(x + RobotConstant.WIDTH_2 - 7 + 2, y - 14, 7, 20);

		this.body = new Polygon(new float[] { -RobotConstant.WIDTH_2, -RobotConstant.HEIGHT_2, -RobotConstant.WIDTH_2,
				RobotConstant.HEIGHT_2 - 10, -RobotConstant.WIDTH_2 + 10, RobotConstant.HEIGHT_2,
				RobotConstant.WIDTH_2 - 10, RobotConstant.HEIGHT_2, RobotConstant.WIDTH_2, RobotConstant.HEIGHT_2 - 10,
				RobotConstant.WIDTH_2, -RobotConstant.HEIGHT_2, });
		this.frontalSensor = new FrontalSensor(0, RobotConstant.HEIGHT_2 + 11.5f);
		this.lateralSensor = new LateralSensor(x + RobotConstant.WIDTH_2 - LateralSensor.WIDTH, y);

		this.rotation = 0;
		this.speed = 0;
		this.destroyed = false;
	}

	public void render(ShapeRenderer sr) {
		sr.setColor(Color.ORANGE);
		for (Circle circle : this.visited) {
			sr.circle(circle.x, circle.y, circle.radius);
		}
		sr.setColor(Color.RED);
		this.frontalSensor.render(sr);
		this.lateralSensor.render(sr);
		if (!this.destroyed)
			sr.setColor(Color.GREEN);
		sr.polygon(this.body.getTransformedVertices());
		this.leftWheel.render(sr);
		this.rightWheel.render(sr);
		this.crazyWheel.render(sr);
		sr.setColor(Color.DARK_GRAY);
	}

	private float getDirectionX(float value) {
		return (float) (this.position.x - value * Math.sin(Math.toRadians(this.rotation)));
	}

	private float getDirectionY(float value) {
		return (float) (this.position.y + value * Math.cos(Math.toRadians(this.rotation)));
	}

	public Vector2 getPosition() {
		return this.position;
	}

	public void setPosition(float x, float y) {
		this.position.x = x;
		this.position.y = y;

		this.frontalSensor.setPosition(x, y, this.rotation);
		this.lateralSensor.setPosition(x, y, this.rotation);

		this.body.setPosition(x, y);

		this.leftWheel.setPosition(x, y);
		this.rightWheel.setPosition(x, y);
		this.crazyWheel.setPosition(x, y);
		
		if (this.visited.size() > 1000){
			this.visited.remove(0);
		}
		this.visited.add(new Circle(this.position.x, this.position.y, 2));
	}

	private void updateInputs() {
		this.input.update();
	}

	private void updatePosition() {
		this.speed = (this.input.RIGHT + this.input.LEFT) / 200;
		this.setPosition(this.getDirectionX(this.speed), this.getDirectionY(this.speed));
	}

	private void updateRotation() {
		float angle = (this.input.RIGHT - this.input.LEFT) / 200;
		this.rotation += angle;
		this.body.rotate(angle);

		this.frontalSensor.rotate(angle);
		this.lateralSensor.rotate(angle);

		this.leftWheel.rotate(angle);
		this.rightWheel.rotate(angle);
		this.crazyWheel.rotate(angle);
	}

	public void update(float angle, float frontalDistance, float lateralDistance) {
		this.updateInputs();
		this.updatePosition();
		this.updateRotation();
		this.frontalSensor.update(angle, frontalDistance);
		this.lateralSensor.setDistance(lateralDistance);
	}

	public boolean collide(Vector2 pos) {
		return this.body.contains(pos);
	}

	public void crash() {
		this.destroyed = true;
	}

	public Rectangle getBodyHitbox() {
		return this.body.getBoundingRectangle();
	}

	public float getSensorAngle() {
		return this.frontalSensor.getAngle();
	}

	public float getLateralDistance(Rectangle rectangle) {
		return this.lateralSensor.getDistance(rectangle);
	}

	public float getFrontalDistance(Rectangle rectangle) {
		return this.frontalSensor.getDistance(rectangle);
	}

	public void setMode(Mode mode) {
		this.input.setMode(mode);
	}

	public void pause() {
		this.input.pause();
	}

	public void start() {
		this.input.start();
	}

	public float getOrientation() {
		return this.rotation;
	}

	public LateralSensor getLateralSensor() {
		return this.lateralSensor;
	}

	public FrontalSensor getFrontalSensor() {
		return this.frontalSensor;
	}
}
