package view;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Obstacle {


	private Rectangle obstacle;

	public Obstacle(float x, float y, float width, float height) {
		this.obstacle = new Rectangle(x, y, width, height);
	}

	public boolean collide(Rectangle rectangle) {
		return this.obstacle.overlaps(rectangle);
	}

	public void render(ShapeRenderer sr) {
		sr.rect(this.obstacle.x, this.obstacle.y, this.obstacle.width, this.obstacle.height);
	}

	public Vector2 getPosition() {
		return null;
	}
	
	public Rectangle getBoundingRectangle(){
		return obstacle;
	}

}
