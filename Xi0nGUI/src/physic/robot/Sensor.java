package physic.robot;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public abstract class Sensor {

	public abstract boolean collide(Rectangle rectangle);

	public abstract void render(ShapeRenderer sr);

	public abstract void rotate(float angle);

	public abstract float getDistance(Rectangle rectangle);
}
