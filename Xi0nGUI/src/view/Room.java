package view;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import view.robot.Robot;

public class Room {

	private Robot robot;
	private List<Obstacle> obstacles;

	public Room() {
		this.robot = new Robot(this, 0, 0);
		this.obstacles = new ArrayList<Obstacle>();
		this.initialize();
	}
	
	private void initialize(){
		this.obstacles.add(new Obstacle(-500, -500, 1000, 20));
		this.obstacles.add(new Obstacle(-520, -500, 20, 1000));
		this.obstacles.add(new Obstacle(-520, 500, 1000, 20));
		this.obstacles.add(new Obstacle(480, -480, 20, 1000));
	}

	public void render(ShapeRenderer sr) {
		sr.setColor(Color.BLACK);
		//sr.rect(-500, -500, 1000, 1000);
		for(Obstacle obstacle : this.obstacles){
			obstacle.render(sr);
			if(obstacle.collide(this.robot.getHitbox())){
				this.robot.crash();
				Gdx.app.error("Robot", "destroyed");
			}
		}
		this.robot.render(sr);
	}
	
	public List<Obstacle> getObstacles(){
		return obstacles;
	}

	public void update() {
		//TODO: Calculate the distance between the obstacle and the sensor to simulate it before mathieu's work
		this.robot.update(0);
	}

	public Vector2 getCameraPosition() {
		return this.robot.getPosition();
	}

	public Robot getRobot() {
		return this.robot;
	}

}
