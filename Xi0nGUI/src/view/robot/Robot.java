package view.robot;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import view.Room;

public class Robot {

	public InputManager input;
	private Vector2 position;
	private Polygon body;
	private Sensor sensor;
	private float rotation;
	private float speed;
	private boolean destroyed;
	private Room room;

	public Robot(Room room, float x, float y) {
		this.input = new InputManager(this, Mode.AUTOMATIC);
		this.room = room;
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

		if (this.input.isMode(Mode.AUTOMATIC)) {
			try {
				Thread.sleep(2 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void render(ShapeRenderer sr) {
		sr.setColor(Color.RED);
		this.sensor.render(sr);
		if (!this.destroyed)
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

	public void setPosition(float x, float y) {
		this.position.x = x;
		this.position.y = y;
		this.sensor.setPosition(this.getDirectionX(0), this.getDirectionY(0));
		this.body.setPosition(x, y);
	}

	private void updateInputs() {
		this.input.update();
	}

	private void updatePosition() {
		this.speed = this.input.AXIS_Y;
		this.setPosition(this.getDirectionX(this.speed * 2), this.getDirectionY(this.speed * 2));
	}

	private void updateRotation() {
		float angle = this.input.AXIS_X * this.speed;
		this.rotation += angle;
		this.body.rotate(angle);
		this.sensor.rotate(angle);
	}

	public void update() {
		this.updateInputs();
		this.updatePosition();
		this.updateRotation();
		this.sensor.update();
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
	
	/*
	private boolean detect() {
		for (Obstacle obstacle : this.room.getObstacles()) {
			if (this.sensor.collide(obstacle.getBoundingRectangle())) {
				return true;
			}
		}
		return false;
	}
	*/
}
