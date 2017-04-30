package view.robot;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import view.Direction;
import view.Obstacle;
import view.RobotMode;
import view.Room;

public class Robot {

	public InputManager input;
	private Map<Direction, Integer> match;
	private Vector2 position;
	private Polygon body;
	private Sensor sensor;
	private float rotation;
	private RobotMode mode;
	private float speed;
	private Direction direction;
	private boolean destroyed;
	private Room room;

	public Robot(Room room, float x, float y) {
		this.input = new InputManager(this, Mode.KEYBOARD);
		this.room = room;
		this.match = new HashMap<>();
		this.match.put(Direction.FORWARD, 1);
		this.match.put(Direction.BACKWARD, -1);
		this.match.put(Direction.NONE, 1);
		this.initialise(x, y);
	}

	public void initialise(float x, float y) {

		this.position = new Vector2(x, y);

		this.body = new Polygon(new float[] { -RobotConstant.WIDTH_2, -RobotConstant.HEIGHT_2, -RobotConstant.WIDTH_2,
				RobotConstant.HEIGHT_2 - 10, -RobotConstant.WIDTH_2 + 10, RobotConstant.HEIGHT_2,
				RobotConstant.WIDTH_2 - 10, RobotConstant.HEIGHT_2, RobotConstant.WIDTH_2, RobotConstant.HEIGHT_2 - 10,
				RobotConstant.WIDTH_2, -RobotConstant.HEIGHT_2, });

		this.sensor = new Sensor(x, y);

		this.rotation = 0;
		this.speed = 0;
		this.destroyed = false;

		//this.mode = RobotMode.AUTOMATIC;
		this.mode = RobotMode.MANUAL;

		this.direction = Direction.NONE;

		if (this.mode == RobotMode.AUTOMATIC) {
			try {
				Thread.sleep(2 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.start();
		}
	}

	private void start() {
		this.direction = Direction.FORWARD;
	}

	public void render(ShapeRenderer sr) {
		sr.setColor(Color.RED);
		this.sensor.render(sr);
		sr.setColor(Color.GREEN);
		sr.polygon(this.body.getTransformedVertices());
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

	public void rotate(float angle) {
		if (Math.abs(angle) < 5) {
			this.setRotation(this.rotation + angle);
		}
	}

	public void setPosition(float x, float y) {
		this.position.x = x;
		this.position.y = y;
		this.sensor.setPosition(this.getDirectionX(0), this.getDirectionY(0));
		this.body.setPosition(x, y);
	}

	public void setRotation(float speed) {
		float angle = 3*speed * this.match.get(this.direction);
		this.rotation += angle;
		this.body.rotate(angle);
		this.sensor.rotate(angle);
	}

	private void updateInputs() {
		this.input.update();
	}
	
	private void updatePosition() {
		this.speed = this.input.AXIS_Y * 3;
		this.setPosition(this.getDirectionX(this.speed), this.getDirectionY(this.speed));
	}
	
	private void updateRotation() {
		this.setRotation(this.input.AXIS_X/2);
	}
	
	public void update(float distance) {
		this.updateInputs();
		this.updatePosition();
		this.updateRotation();
		this.sensor.update();
	}

	

	public boolean detect() {
		for (Obstacle obstacle : this.room.getObstacles()) {
			if (this.sensor.collide(obstacle.getBoundingRectangle())) {
				return true;
			}
		}
		return false;
	}

	public boolean collide(Vector2 pos) {
		return this.body.contains(pos);
	}

	public void accelerate(float acceleration) {
		if (this.speed + acceleration > -5 && this.speed + acceleration < 5) {
			this.speed += acceleration;
		}
	}

	public void setAutomaticMode() {
		this.mode = RobotMode.AUTOMATIC;
	}

	public void setManualMode() {
		this.mode = RobotMode.MANUAL;
	}

	public void crash() {
		this.destroyed = true;
	}

	public Rectangle getHitbox() {
		return this.body.getBoundingRectangle();
	}
}
