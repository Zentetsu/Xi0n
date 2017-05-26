package physic.robot;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public abstract class Sensor {

	public abstract void setPosition(float x, float y);

	public abstract boolean collide(Rectangle rectangle);

	public abstract void render(ShapeRenderer sr);

	public abstract void rotate(float angle);

	public abstract int getDistance(Rectangle rectangle);
}
