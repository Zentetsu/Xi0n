import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Robot {

	private final static int WIDTH = 50;
	private final static int HEIGHT = 50;
	private Vector2 position;
	private Rectangle body;
	private Polygon body2;
	private Circle sensor;
	private float rotation;

	public Robot(float x, float y) {
		this.position = new Vector2(x, y);
		this.body = new Rectangle(x - WIDTH/2, y - HEIGHT/2, WIDTH, HEIGHT);
		this.body2 = new Polygon(new float[]{-WIDTH/2, -HEIGHT/2,
				-WIDTH/2, HEIGHT/2 - 10,
				-WIDTH/2 + 10, HEIGHT/2,
				WIDTH/2 - 10, HEIGHT/2,
				WIDTH/2, HEIGHT/2 - 10,
				WIDTH/2, -HEIGHT/2,});
		this.sensor = new Circle(x, y + HEIGHT/2, 10);
		this.rotation = 0;
	}

	public void render(ShapeRenderer sr) {
		sr.setColor(Color.RED);
		//sr.rect(this.body.x, this.body.y, this.body.width, this.body.height);
		sr.circle(this.sensor.x, this.sensor.y, this.sensor.radius);
		sr.setColor(Color.GREEN);
		sr.polygon(this.body2.getTransformedVertices());
	}

	public Vector2 getPosition() {
		return new Vector2(this.body.x, this.body.y);
	}

	public void setPosition(float x, float y) {
		this.position.x = x;
		this.position.y = y;
		this.body.setPosition(x - WIDTH/2, y - HEIGHT/2);
		this.sensor.setPosition(x, y + HEIGHT/2);
		this.body2.setPosition(x, y);
	}
	
	public void setRotation(float angle){
		this.rotation += angle;
		this.body2.rotate(angle);
	}

	public void update() {
		
		if (Gdx.input.isKeyPressed(Input.Keys.Z)){
			this.setPosition(this.position.x, this.position.y + 3);			
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)){
			this.setPosition(this.position.x, this.position.y - 3);	
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)){
			this.setPosition(this.position.x - 3, this.position.y);	
			this.setRotation(2);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)){
			this.setPosition(this.position.x + 3, this.position.y);	
			this.setRotation(-2);
		}
	}
}
