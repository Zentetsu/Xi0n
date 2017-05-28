package physic.mapping;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import physic.robot.FrontalSensor;
import physic.robot.LateralSensor;
import physic.robot.Robot;

public class Room {

	private Robot robot;
	private List<Obstacle> obstacles;

	public Room() {
		this.robot = Robot.getInstance();
		this.obstacles = new ArrayList<Obstacle>();
		//this.createRoom();
	}
	
	private void createRoom(){
		//walls
		this.obstacles.add(new Obstacle(-500, -500, 1000, 20));
		this.obstacles.add(new Obstacle(-500, -500, 20, 1000));
		this.obstacles.add(new Obstacle(500, -500, 20, 1000));
		this.obstacles.add(new Obstacle(-500, 500, 1000, 20));
		this.obstacles.add(new Obstacle(0, -480, 20, 150));
	}

	public void render(ShapeRenderer sr) {
		sr.setColor(Color.BLACK);
		for(Obstacle obstacle : this.obstacles){
			obstacle.render(sr);
			if(obstacle.collide(this.robot.getBodyHitbox())){
				this.robot.crash();
			}
		}
		this.robot.render(sr);
	}

	public void renderHUD(ShapeRenderer shud) {
		shud.setColor(Color.BLACK);
		for(Obstacle obstacle : this.obstacles){
			Rectangle rect = obstacle.getBoundingRectangle();
			shud.rect(150 + (rect.x) / 10, 150 + (rect.y) / 10, rect.width/10, rect.height/10);
		}
		shud.setColor(Color.RED);
		shud.circle(150+this.robot.getPosition().x/10, 150+this.robot.getPosition().y/10, 3);
		
	}
	
	public List<Obstacle> getObstacles(){
		return obstacles;
	}

	public void update(float angle, float FDistance, float LDistance){
		//TODO: Calculate the distance between the obstacle and the sensor to simulate it before mathieu's work
		this.robot.update(angle, FDistance, LDistance);
		LateralSensor ls = this.robot.getLateralSensor();
		FrontalSensor fs = this.robot.getFrontalSensor();
		if (ls.getDistance() >= 5 && ls.getDistance() <= 100)
			this.obstacles.add(new Obstacle(ls.getObstacle()));
	}

	public Vector2 getCameraPosition() {
		return this.robot.getPosition();
	}

	public Robot getRobot() {
		return this.robot;
	}
}
