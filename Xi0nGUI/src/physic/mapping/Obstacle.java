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

	public Obstacle(Obstacle newObstacle) {
		this.obstacle = new RotableRectangle(newObstacle.getX(), newObstacle.getY(), newObstacle.obstacle.getRectangle().width, newObstacle.obstacle.getRectangle().height);
	}

	public void setPosition(float x, float y){
		this.obstacle.setPosition(x, y);
	}

	public float getX(){
		return this.obstacle.getRectangle().x;
	}

	public float getY(){
		return this.obstacle.getRectangle().y;
	}

	public boolean collide(Rectangle rectangle) {
		return this.obstacle.overlaps(rectangle);
	}

	public void render(ShapeRenderer sr) {
		sr.rect(this.obstacle.getRectangle().x, this.obstacle.getRectangle().y, this.obstacle.getRectangle().width, this.obstacle.getRectangle().height);
	}

	public Rectangle getBoundingRectangle() {
		return this.obstacle.getRectangle();
	}

	public Polygon getPolygon(){
		return this.obstacle.getPolygon();
	}

}
