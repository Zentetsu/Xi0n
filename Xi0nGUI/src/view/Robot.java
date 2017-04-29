package view;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Robot {

	private final static int WIDTH = 40;
	private final static int WIDTH_2 = WIDTH / 2;
	private final static int HEIGHT = 50;
	private final static int HEIGHT_2 = HEIGHT / 2;

	private Map<Direction, Integer> match;
	private Vector2 position;
	private Polygon body;
	private Polygon sensor2;
	private float rotation;
	private RobotMode mode;
	private int speed;
	private Direction direction;
	private Axe axe;
	private boolean destroyed;

	public Robot(float x, float y) {
		this.position = new Vector2(x, y);
		this.body = new Polygon(new float[] { -WIDTH_2, -HEIGHT_2, -WIDTH_2, HEIGHT_2 - 10, -WIDTH_2 + 10, HEIGHT_2,
				WIDTH_2 - 10, HEIGHT_2, WIDTH_2, HEIGHT_2 - 10, WIDTH_2, -HEIGHT_2, });
		
		this.sensor2 = new Polygon(new float[] {x, y + HEIGHT_2, x -10, y+HEIGHT_2 + 40, x + 10, y + HEIGHT_2 + 40});

		this.match = new HashMap<>();
		this.match.put(Direction.FORWARD, 1);
		this.match.put(Direction.BACKWARD, -1);

		this.rotation = 0;
		this.speed = 3;
		this.destroyed = false;

		// this.mode = RobotMode.AUTOMATIC;
		this.mode = RobotMode.MANUAL;

		this.direction = Direction.NONE;
		this.axe = Axe.NONE;

		if (this.mode == RobotMode.AUTOMATIC) {
			this.direction = Direction.FORWARD;
			this.start();
		}
	}

	private void start() {

	}

	public void render(ShapeRenderer sr) {
		sr.setColor(200, 0, 0, 0.5f);
		sr.polygon(this.sensor2.getTransformedVertices());
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
		this.sensor2.setPosition(this.getDirectionX(0), this.getDirectionY(0));
		this.body.setPosition(x, y);
	}

	public void setRotation(float angle) {
		this.rotation += angle;
		this.body.rotate(angle);
		this.sensor2.rotate(angle);
	}

	public void update(float distance) {

		if (Gdx.input.isKeyPressed(Input.Keys.T)) {
			Gdx.app.exit();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.R)) {
			this.destroyed = false;
			this.speed = 3;
		}
		
		//If it hits a wall
		if(this.destroyed){
			this.speed = 0;
		}
		// Auto Mode
		else if (this.mode == RobotMode.AUTOMATIC) {
			if (distance < 30) {
				this.axe = Axe.LEFT;
			} else {
				this.axe = Axe.NONE;
			}

		} else {

			// Manual Mode
			/*
			 * if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
			 * this.setPosition(this.getDirectionX(this.speed),
			 * this.getDirectionY(this.speed)); } if
			 * (Gdx.input.isKeyPressed(Input.Keys.S)) {
			 * this.setPosition(this.getDirectionX(-this.speed),
			 * this.getDirectionY(-this.speed)); if
			 * (Gdx.input.isKeyPressed(Input.Keys.Q)) { this.setRotation(2); }
			 * if (Gdx.input.isKeyPressed(Input.Keys.D)) { this.setRotation(-2);
			 * } } if (Gdx.input.isKeyPressed(Input.Keys.Q) &&
			 * !Gdx.input.isKeyPressed(Input.Keys.S)) {
			 * this.setPosition(this.getDirectionX(3), this.getDirectionY(3));
			 * this.setRotation(2); } if (Gdx.input.isKeyPressed(Input.Keys.D)
			 * && !Gdx.input.isKeyPressed(Input.Keys.S)) {
			 * this.setPosition(this.getDirectionX(3), this.getDirectionY(3));
			 * this.setRotation(-2); }
			 */

		}

		this.getDirection();

		switch (this.axe) {
		case LEFT:
			this.setRotation(2 * this.match.get(this.direction));
			this.axe = Axe.NONE;
			break;
		case RIGHT:
			this.setRotation(-2 * this.match.get(this.direction));
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
	
	public boolean collide(Vector2 pos) {
		return this.body.contains(pos);
	}

	public void accelerate(int acceleration) {
		if (this.speed + acceleration > -10 && this.speed + acceleration < 10) {
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
		if (Gdx.input.isKeyPressed(Input.Keys.Q) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
			this.axe = Axe.LEFT;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
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
