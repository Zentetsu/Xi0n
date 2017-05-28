package physic.robot;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import physic.mapping.Obstacle;

public abstract class Sensor {
	
	public float distance;
	private Obstacle obstacle;
	
	public Sensor() {
		this.distance = 0;
		this.obstacle = new Obstacle(0, 0, 4, 4);
	}
	
	public Obstacle getObstacle() {
		// TODO Auto-generated method stub
		return this.obstacle;
	}

	public float getDistance() {
		return this.distance + 50;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public void updateObstaclePosition(float x, float y, float sin, float cos){
		this.obstacle.setPosition(x - sin, y + cos);
	}
	
	public abstract boolean collide(Rectangle rectangle);

	public void render(ShapeRenderer sr){
		if (this.distance >= 0 && this.distance < 500){
			sr.setColor(Color.DARK_GRAY);
			sr.circle(this.obstacle.getX(), this.obstacle.getY(), 3);
		}
	}

	public abstract void rotate(float angle);

	public abstract float getDistance(Rectangle rectangle);
}
