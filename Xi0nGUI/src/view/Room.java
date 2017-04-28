package view;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Room {
	
	private Robot robot;
	
	public Room() {
		this.robot = new Robot(0, 0);
	}

	public void render(ShapeRenderer sr) {
		sr.setColor(Color.BLACK);
		sr.rect(-500, -500, 1000, 1000);
		this.robot.render(sr);
	}
	
	public void update(){
		this.robot.update();
	}

	public Vector2 getCameraPosition() {
		return this.robot.getPosition();
	}
	
}
