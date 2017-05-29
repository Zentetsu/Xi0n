package gui;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;

import gui.buttons.AutomaticButton;
import gui.buttons.ControllerButton;
import gui.buttons.KeyboardButton;
import gui.buttons.LockButton;
import gui.buttons.QuitButton;
import gui.buttons.RestartButton;
import gui.buttons.UIButton;
import gui.buttons.XBee;
import network.XbeeSerialCommunication;
import physic.Mode;
import physic.mapping.Obstacle;
import physic.mapping.Room;
import physic.robot.LateralSensor;
import physic.robot.Robot;
import physic.robot.RobotConstant;

public enum Xi0nSimulation implements ApplicationListener {

	INSTANCE;
	
	private static boolean SIMULATION = true;

	private XbeeSerialCommunication xbeeCommunation;
	private ShapeRenderer sr;
	private ShapeRenderer shud;
	private OrthographicCamera camera;
	private Room room;
	private Stage stage;
	private Texture HUD;
	private SpriteBatch batch;
	private BitmapFont font;

	private KeyboardButton keyboardButton;
	private ControllerButton controllerButton;
	private AutomaticButton automaticButton;

	private boolean connected;
	private XBee xbeeLogo;

	ButtonGroup<UIButton> buttonGroup;
	private LockButton lockButton;

	@Override
	public void create() {
		this.xbeeCommunation = new XbeeSerialCommunication(false);
		this.connected = this.xbeeCommunation.isConnected();
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false);
		this.room = new Room();
		this.sr = new ShapeRenderer();
		this.shud = new ShapeRenderer();
		this.font = new BitmapFont();
		this.font.setColor(0.1f, 0.1f, 0.1f, 1);
		this.stage = new Stage();
		this.batch = new SpriteBatch();
		this.HUD = new Texture("assets/ui/HUD.png");
		this.xbeeLogo = new XBee(1490, 50);

		Gdx.input.setInputProcessor(this.stage);
		this.lockButton = new LockButton(510, 30);
		this.stage.addActor(this.lockButton);
		this.stage.addActor(new RestartButton(1800, 1025));
		this.stage.addActor(new QuitButton(1860, 1025));

		this.keyboardButton = new KeyboardButton(320, 25);
		this.controllerButton = new ControllerButton(320, 75);
		this.automaticButton = new AutomaticButton(320, 125);

		this.stage.addActor(this.keyboardButton);
		this.stage.addActor(this.controllerButton);
		this.stage.addActor(this.automaticButton);

		this.keyboardButton.setChecked(true);
		this.buttonGroup = new ButtonGroup<UIButton>(this.keyboardButton, this.controllerButton, this.automaticButton);

		this.buttonGroup.setMaxCheckCount(1);
		this.buttonGroup.setMinCheckCount(1);
		this.buttonGroup.setUncheckLast(true);

		this.stage.addActor(this.xbeeLogo);
	}

	public void addButton(UIButton button) {
		this.stage.addActor(button);
	}

	public void removeButton(String name) {
		for (Actor actor : this.stage.getActors()) {
			if (actor.toString().equals(name)) {
				actor.remove();
			}
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.95f, 0.95f, 0.95f, 0.95f);
		this.camera.position.set(this.room.getCameraPosition(), 0);
		this.camera.update();

		this.room.update(this.xbeeCommunation.scanAngle, this.xbeeCommunation.infraredRemote, this.xbeeCommunation.ultrasoundRemote);
		this.xbeeCommunation.update(this.room.getRobot().input);
		this.sr.begin(ShapeType.Line);
		this.sr.setProjectionMatrix(this.camera.combined);
		this.room.render(this.sr);
		this.sr.end();
		// TODO: extract
		Robot robot = this.room.getRobot();
		float left = robot.input.LEFT;
		float right = robot.input.RIGHT;
		this.shud.begin(ShapeType.Filled);
		this.shud.rect(800, 40, 500, 100, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);

		if (left > 0) {
			Color forwardLeft = new Color(right / (left * 4), left / 255, left / (255 * 4), 0);
			this.shud.rect(1060, 105, left, 30, forwardLeft, forwardLeft, forwardLeft, forwardLeft);
		}

		else if (left < 0) {
			Color backwardLeft = new Color(-left / 255, -left / (255 * 4), -left / (255 * 4), 0);
			this.shud.rect(1050 + left, 105, -left, 30, backwardLeft, backwardLeft, backwardLeft, backwardLeft);
		}
		if (right > 0) {
			Color forwardRight = new Color(right / (255 * 4), right / 255, right / (255 * 4), 0);
			this.shud.rect(1060, 40, right, 30, forwardRight, forwardRight, forwardRight, forwardRight);
		}

		else if (right < 0) {
			Color backwardRight = new Color(-right / 255, -right / (255 * 4), -right / (255 * 4), 0);
			this.shud.rect(1050 + right, 40, -right, 30, backwardRight, backwardRight, backwardRight, backwardRight);
		}
		this.shud.rect(1050, 40, 10, 100, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY);
		this.shud.end();
		this.batch.begin();
		this.batch.draw(this.HUD, 0, 0);
		if (this.connected)
			this.font.draw(this.batch, this.xbeeCommunation.myPort.port.getPortName(), 1505, 40);
		else{
			this.font.draw(this.batch, "DISCONNECTED", 1470, 40);
			this.xbeeLogo.blink();
			//this.stage.addActor(new CellBar(320, 13));
		}
		this.font.draw(this.batch, "X = " + Math.round(robot.getPosition().x) + "   ;   Y = " + Math.round(robot.getPosition().y), 1720, 180);
		this.font.draw(this.batch, "ROTATION = " + Math.round(robot.getOrientation())%360 + " deg", 1720, 150);
		this.font.draw(this.batch, "STATE = " + robot.input.STATE, 1720, 120);
		this.font.draw(this.batch, "L SENSOR = " + this.getLateralDistanceFromRobot() + "cm", 1720, 90);
		this.font.draw(this.batch, "F SENSOR = " + this.getFrontalDistanceFromRobot() + "cm", 1720, 60);
		this.batch.end();
		this.shud.begin(ShapeType.Filled);
		this.room.renderHUD(this.shud);
		this.shud.end();
		this.stage.draw();
	}

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	public void setMode(Mode mode) {
		this.room.getRobot().setMode(mode);
	}

	public void startRobot() {
		this.room.getRobot().start();
	}

	public void pauseRobot() {
		this.room.getRobot().pause();
	}

	public void setConnect(boolean connected) {
		this.connected = connected;
	}

	public int getLateralDistanceFromRobot(){
		return this.room.getRobot().getLateralSensor().distance;
	}
	
	public int getFrontalDistanceFromRobot(){
		return this.room.getRobot().getFrontalSensor().distance;
	}
	
	public float getLateralDistance() {
		float min = LateralSensor.STOP_LENGTH + LateralSensor.WARNING_LENGTH + 20;
		if ( this.room != null ) {
			for (Obstacle obstacle : this.room.getObstacles()) {
				if (min > this.room.getRobot().getLateralDistance(obstacle.getBoundingRectangle())) {
					min = this.room.getRobot().getLateralDistance(obstacle.getBoundingRectangle());
				}
			}
		}
		return min;
	}

	public float getFrontalDistance() {
		float min = RobotConstant.HEIGHT * 2;
		if ( this.room != null ) {
			for (Obstacle obstacle : this.room.getObstacles()) {
				if (min > this.room.getRobot().getFrontalDistance(obstacle.getBoundingRectangle())) {
					min = this.room.getRobot().getFrontalDistance(obstacle.getBoundingRectangle());
				}
			}
		}
		return min;
	}
	
	public float getSensorAngle () {
		if ( this.room != null ) {
			return this.room.getRobot().getSensorAngle();
		}
		else return 0;
	}

	public void restart() {
		this.lockButton.lock();
		this.room.getRobot().input.reset();
	}
	
	public boolean getSimulation () {
		return this.SIMULATION;
	}
	
	public void setSimulation ( boolean simulation ) {
		this.SIMULATION = simulation;
	}
	
	

}
