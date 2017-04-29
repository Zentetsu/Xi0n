package view.robot;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import view.Axe;
import view.Direction;
import view.Obstacle;
import view.RobotMode;
import view.Room;

public class Robot {

	private final float lateralSpeed = 3;
	private Map<Direction, Integer> match;
	private Vector2 position;
	private Polygon body;
	private Sensor sensor;
	private float rotation;
	private RobotMode mode;
	private int speed;
	private Direction direction;
	private Axe axe;
	private boolean destroyed;
	private Room room;

	public Robot(Room room, float x, float y) {

		this.room = room;
		this.match = new HashMap<>();
		this.match.put(Direction.FORWARD, 1);
		this.match.put(Direction.BACKWARD, -1);
		this.match.put(Direction.NONE, 1);

		this.initialise(x, y);

	}

	private void initialise(float x, float y) {

		this.position = new Vector2(x, y);

		this.body = new Polygon(new float[] { -RobotConstant.WIDTH_2, -RobotConstant.HEIGHT_2, -RobotConstant.WIDTH_2,
				RobotConstant.HEIGHT_2 - 10, -RobotConstant.WIDTH_2 + 10, RobotConstant.HEIGHT_2,
				RobotConstant.WIDTH_2 - 10, RobotConstant.HEIGHT_2, RobotConstant.WIDTH_2, RobotConstant.HEIGHT_2 - 10,
				RobotConstant.WIDTH_2, -RobotConstant.HEIGHT_2, });

		this.sensor = new Sensor(x, y);

		this.rotation = 0;
		this.speed = 3;
		this.destroyed = false;

		this.mode = RobotMode.AUTOMATIC;
		 this.mode = RobotMode.MANUAL;

		this.direction = Direction.NONE;
		this.axe = Axe.NONE;

		if (this.mode == RobotMode.AUTOMATIC) {
			try {
				Thread.sleep(2*1000);
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
		// sr.circle(this.sensor.x, this.sensor.y, this.sensor.radius);
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

	public void setRotation(float angle) {
		this.rotation += angle;
		this.body.rotate(angle);
		this.sensor.rotate(angle);
	}

	public void update(float distance) {

		this.sensor.update();

		if (Gdx.input.isKeyPressed(Input.Keys.T)) {
			Gdx.app.exit();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.R)) {
			this.initialise(0, 0);
		}

		// If it hits a wall
		if (this.destroyed) {
			this.speed = 0;
		}
		// Auto Mode
		else if (this.mode == RobotMode.AUTOMATIC) {
			if (this.detect()) {
				this.axe = Axe.LEFT;
			} else {
				this.axe = Axe.NONE;
			}
			// Manual Mode
		} else {
			this.getDirection();
		}

		switch (this.axe) {
		case LEFT:
			this.setRotation(this.lateralSpeed * this.match.get(this.direction));
			this.axe = Axe.NONE;
			break;
		case RIGHT:
			this.setRotation(-this.lateralSpeed * this.match.get(this.direction));
			this.axe = Axe.NONE;
			break;
		case NONE:
			break;
		}

		switch (this.direction) {
		case FORWARD:
			this.setPosition(this.getDirectionX(this.speed), this.getDirectionY(this.speed));
			break;
		case BACKWARD:
			this.setPosition(this.getDirectionX(this.speed), this.getDirectionY(this.speed));
			break;
		case NONE:
			break;
		default:
			break;
		}

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

	public void accelerate(int acceleration) {
		if (this.speed + acceleration > -5 && this.speed + acceleration < 5) {
			this.speed += acceleration;
		}
	}

	private void getDirection() {
		if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
			this.direction = Direction.FORWARD;
			this.accelerate(1);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			this.direction = Direction.BACKWARD;
			this.accelerate(-1);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			this.axe = Axe.LEFT;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			this.axe = Axe.RIGHT;
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
