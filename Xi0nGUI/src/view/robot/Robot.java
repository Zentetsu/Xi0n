package view.robot;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import view.Obstacle;
import view.Room;

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

	private Room room;

	private List<Circle> visited;
	private static Robot instance;

	private Robot(Room room, float x, float y) {
		this.input = new InputManager(this, Mode.CONTROLLER, room);
		this.room = room;
		this.setMode(Mode.KEYBOARD);
		this.initialize(x, y);
	}

	public static Robot getInstance(Room room){
		if(instance == null){
			instance =  new Robot(room, 0, 0);
		}
		return instance;
	}

	public void initialize(float x, float y) {
		this.visited = new ArrayList<>();
		this.position = new Vector2(x, y);

		this.crazyWheel = new RotableRectangle(x - 2, y - 20, RobotConstant.WIDTH_2 / 2, RobotConstant.WIDTH_2 / 4);
		this.leftWheel = new RotableRectangle(x - 8 - RobotConstant.WIDTH_2 / 2, y - 6, RobotConstant.WIDTH_2,
				RobotConstant.WIDTH_2 / 2);
		this.rightWheel = new RotableRectangle(x + 8, y - 6, RobotConstant.WIDTH_2, RobotConstant.WIDTH_2 / 2);

		this.body = new Polygon(new float[] { -RobotConstant.WIDTH_2, -RobotConstant.HEIGHT_2, -RobotConstant.WIDTH_2,
				RobotConstant.HEIGHT_2 - 10, -RobotConstant.WIDTH_2 + 10, RobotConstant.HEIGHT_2,
				RobotConstant.WIDTH_2 - 10, RobotConstant.HEIGHT_2, RobotConstant.WIDTH_2, RobotConstant.HEIGHT_2 - 10,
				RobotConstant.WIDTH_2, -RobotConstant.HEIGHT_2, });

		this.frontalSensor = new FrontalSensor(x, y + RobotConstant.HEIGHT_2);
		this.lateralSensor = new LateralSensor(x + RobotConstant.HEIGHT_2 - 5, y + 5);

		this.rotation = 0;
		this.speed = 0;
		this.destroyed = false;
	}

	public void render(ShapeRenderer sr) {
		sr.setColor(Color.YELLOW);
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

		this.frontalSensor.setPosition(this.getDirectionX(0), this.getDirectionY(0));
		this.lateralSensor.setPosition(this.getDirectionX(0), this.getDirectionY(0));

		this.body.setPosition(x, y);

		this.leftWheel.setPosition(x, y);
		this.rightWheel.setPosition(x, y);
		this.crazyWheel.setPosition(x, y);

		this.visited.add(new Circle(this.position.x, this.position.y, 3));
	}

	private void updateInputs() {
		this.input.update();
	}

	private void updatePosition() {
		this.speed = (this.input.RIGHT + this.input.LEFT)/400;
		this.setPosition(this.getDirectionX(this.speed*2), this.getDirectionY(this.speed*2));
	}

	private void updateRotation() {
		float angle = (this.input.RIGHT-this.input.LEFT)/400;
		this.rotation += angle;
		this.body.rotate(angle);

		this.frontalSensor.rotate(angle);
		this.lateralSensor.rotate(angle);

		this.leftWheel.rotate(angle);
		this.rightWheel.rotate(angle);
		this.crazyWheel.rotate(angle);
	}

	public void update() {
		this.updateInputs();
		this.updatePosition();
		this.updateRotation();
		this.frontalSensor.update();
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

	public boolean detect(Obstacle obstacle, SensorType type) {
		switch (type) {
		case FRONTAL:
			return this.frontalSensor.collide(obstacle.getBoundingRectangle());
		case LATERAL:
			return this.lateralSensor.collide(obstacle.getBoundingRectangle());
		default:
			return false;
		}
	}

	public float getSensorAngle() {
		return this.frontalSensor.getAngle();
	}

	public int getLateralDistance(Rectangle rectangle) {
		return this.lateralSensor.getDistance(rectangle);
	}

	public int getFrontalDistance(Rectangle rectangle) {
		return this.frontalSensor.getDistance(rectangle);
	}

	public void setMode(Mode mode){
		this.input = new InputManager(this, mode, this.room);
	}

	public void pause(){
		this.input.pause();
	}

	public void start(){
		this.input.start();
	}

	public float getOrientation() {
		return this.rotation;
	}

	/*
	 * private boolean detect() { for (Obstacle obstacle :
	 * this.room.getObstacles()) { if
	 * (this.sensor.collide(obstacle.getBoundingRectangle())) { return true; } }
	 * return false; }
	 */
}
