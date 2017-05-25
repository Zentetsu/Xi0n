package view.robot;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class LateralSensor extends Sensor {

	public static final int WARNING_LENGTH = 70;
	public static final int STOP_LENGTH = 40;

	private RotableRectangle warningZone;
	private RotableRectangle stopZone;

	public LateralSensor(float x, float y) {
		this.type = SensorType.LATERAL;

		this.warningZone = new RotableRectangle(x, y, 10, this.WARNING_LENGTH);
		this.stopZone = new RotableRectangle(x, y, 10, this.STOP_LENGTH);
	}

	@Override
	public void setPosition(float x, float y) {
		this.warningZone.setPosition(x, y);
		this.stopZone.setPosition(x, y);
	}

	@Override
	public boolean collide(Rectangle rectangle) {
		return this.warningZone.collide(rectangle) || this.stopZone.collide(rectangle);
	}

	@Override
	public void render(ShapeRenderer sr) {
		this.warningZone.render(sr, Color.GREEN);
		this.stopZone.render(sr, Color.RED);
	}

	@Override
	public void rotate(float angle) {
		this.warningZone.rotate(angle);
		this.stopZone.rotate(angle);
	}

	@Override
	public int getDistance(Rectangle rectangle) {
		if (this.stopZone.collide(rectangle)) {
			return LateralSensor.STOP_LENGTH;
		}
		if (this.warningZone.collide(rectangle)) {
			return LateralSensor.WARNING_LENGTH;
		}
		return LateralSensor.WARNING_LENGTH + LateralSensor.STOP_LENGTH;
	}

}
