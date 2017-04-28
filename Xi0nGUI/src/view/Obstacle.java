package view;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle {

	private final float WIDTH = 50;

	private Rectangle obstacle;

	public Obstacle(float x, float y, float height) {
		this.obstacle = new Rectangle(x, y, WIDTH, height);
	}

	public boolean collide(float x, float y) {
		
		return this.obstacle.contains(x, y);
	}

	public void render(ShapeRenderer sr) {
		sr.rect(this.obstacle.x, this.obstacle.y, this.obstacle.width, this.obstacle.height);
	}

}
