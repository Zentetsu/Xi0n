package view;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class Robot {

	private final static int WIDTH = 40;
	private final static int WIDTH_2 = WIDTH / 2;
	private final static int HEIGHT = 50;
	private final static int HEIGHT_2 = HEIGHT / 2;
	private Vector2 position;
	private Polygon body;
	private Circle sensor;
	private float rotation;

	public Robot(float x, float y) {
		this.position = new Vector2(x, y);
		this.body = new Polygon(new float[] { -WIDTH_2, -HEIGHT_2, 
											  -WIDTH_2, HEIGHT_2 - 10,
											  -WIDTH_2 + 10, HEIGHT_2,
											   WIDTH_2 - 10, HEIGHT_2, 
											   WIDTH_2, HEIGHT_2 - 10, 
											   WIDTH_2, -HEIGHT_2, });
		this.sensor = new Circle(x, y + HEIGHT_2, 5);
		this.rotation = 0;
	}

	public void render(ShapeRenderer sr) {
		sr.setColor(Color.RED);
		sr.circle(this.sensor.x, this.sensor.y, this.sensor.radius);
		sr.setColor(Color.GREEN);
		sr.polygon(this.body.getTransformedVertices());
	}

	private float getDirectionX(float value) {
		return (float) (this.position.x - value*Math.sin(Math.toRadians(this.rotation)));
	}
	
	private float getDirectionY(float value) {
		return (float) (this.position.y + value*Math.cos(Math.toRadians(this.rotation)));
	}

	public Vector2 getPosition() {
		return this.position;
	}

	public void setPosition(float x, float y) {
		this.position.x = x;
		this.position.y = y;
		this.sensor.setPosition(this.getDirectionX(WIDTH_2), this.getDirectionY(HEIGHT_2));
		this.body.setPosition(x, y);
	}

	public void setRotation(float angle) {
		this.rotation = (this.rotation + angle);
		this.body.rotate(angle);
	}

	public void update() {

		if (Gdx.input.isKeyPressed(Input.Keys.Z) ) {
			this.setPosition(this.getDirectionX(3), this.getDirectionY(3));
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			this.setPosition(this.getDirectionX(-3), this.getDirectionY(-3));
			if (Gdx.input.isKeyPressed(Input.Keys.Q)){
				this.setRotation(2);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.D)){
				this.setRotation(-2);
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
			this.setPosition(this.getDirectionX(3), this.getDirectionY(3));
			this.setRotation(2);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
			this.setPosition(this.getDirectionX(3), this.getDirectionY(3));
			this.setRotation(-2);
		}
	}
}
