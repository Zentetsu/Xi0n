package physic.mapping;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import physic.RotableRectangle;

public class Obstacle {

	private RotableRectangle obstacle;

	public Obstacle(float x, float y, float width, float height) {
		this.obstacle = new RotableRectangle(x, y, width, height);
	}

	public boolean collide(Rectangle rectangle) {
		return this.obstacle.overlaps(rectangle);
	}

	public void render(ShapeRenderer sr) {
		sr.rect(this.obstacle.getRectangle().x, this.obstacle.getRectangle().y, this.obstacle.getRectangle().width,
				this.obstacle.getRectangle().height);
	}

	public Rectangle getBoundingRectangle() {
		return this.obstacle.getRectangle();
	}
	
	public Polygon getPolygon(){
		return this.obstacle.getPolygon();
	}

}
