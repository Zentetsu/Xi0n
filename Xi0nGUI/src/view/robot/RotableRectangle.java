package view.robot;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;

public class RotableRectangle {
	private Polygon polygon;

	public RotableRectangle(float x, float y, float width, float height) {
		this.polygon = new Polygon(new float[] { x, y, x + height, y, x + height, y + width, x, y + width });
	}
	
	public void rotate(float angle){
		this.polygon.rotate(angle);
	}
	
	public void setPosition(float x, float y){
		this.polygon.setPosition(x, y);
	}
	
	public void render(ShapeRenderer sr){
		sr.setColor(Color.BLUE);
		sr.polygon(this.polygon.getTransformedVertices());
	}

}
