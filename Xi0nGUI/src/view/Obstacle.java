package view;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Obstacle {

	private final float WIDTH = 5;

	private Rectangle obstacle;

	public Obstacle(float x, float y, float height) {
		this.obstacle = new Rectangle(x, y, WIDTH, height);
	}

	public boolean collide(Vector2 pos) {
		return this.obstacle.contains(pos);
	}

	public void render(ShapeRenderer sr) {
		sr.rect(this.obstacle.x, this.obstacle.y, this.obstacle.width, this.obstacle.height);
	}

	public Vector2 getPosition() {
		return null;
	}

}
