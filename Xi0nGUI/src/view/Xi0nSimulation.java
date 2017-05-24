package view;

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

<<<<<<< HEAD
import view.robot.Robot;
=======
import view.robot.Mode;
import view.ui.AutomaticButton;
import view.ui.ControllerButton;
import view.ui.KeyboardButton;
>>>>>>> branch 'dev' of https://github.com/haze-sama/Xi0n.git
import view.ui.QuitButton;
import view.ui.RestartButton;
import view.ui.StartButton;
import view.ui.UIButton;

public enum Xi0nSimulation implements ApplicationListener {

	INSTANCE;

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

	ButtonGroup<UIButton> buttonGroup;

	@Override
	public void create() {
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false);
		this.room = new Room();
		this.sr = new ShapeRenderer();
		this.shud = new ShapeRenderer();
		this.stage = new Stage();
		this.batch = new SpriteBatch();
		this.HUD = new Texture("assets/HUD.png");
		this.font = new BitmapFont();

		Gdx.input.setInputProcessor(this.stage);
		this.stage.addActor(new StartButton(500, 30));
		this.stage.addActor(new RestartButton(30, 130));
		this.stage.addActor(new QuitButton(1860, 1025));

		this.keyboardButton = new KeyboardButton(100, 300);
		this.controllerButton = new ControllerButton(100, 400);
		this.automaticButton = new AutomaticButton(100, 500);

		this.stage.addActor(this.keyboardButton);
		this.stage.addActor(this.controllerButton);
		this.stage.addActor(this.automaticButton);

		this.buttonGroup = new ButtonGroup<UIButton>(keyboardButton, controllerButton, automaticButton);

		this.buttonGroup.setMaxCheckCount(1);
		this.buttonGroup.setMinCheckCount(0);
		// it may be useful to use this method:
		this.buttonGroup.setUncheckLast(true);
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

		this.room.update();
		this.sr.begin(ShapeType.Line);
		this.sr.setProjectionMatrix(this.camera.combined);
		this.room.render(this.sr);
		this.sr.end();
		// TODO: extract
		Robot robot = this.room.getRobot();
		
		float left = robot.input.AXIS_Y;
		float right = robot.input.AXIS_Y;
		this.shud.begin(ShapeType.Filled);
		this.shud.rect(800, 40, 500, 100, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY);
		if (left > 0){
			Color forwardLeft = new Color(right/4, right, right/4, 0);
			this.shud.rect(1060, 105, left*250, 30, forwardLeft, forwardLeft, forwardLeft, forwardLeft);
		}
		else if (left < 0){
			Color backwardLeft = new Color(-right, -right/4, -right/4, 0);
			this.shud.rect(1050+left*250, 105, -left*250, 30, backwardLeft, backwardLeft, backwardLeft, backwardLeft);
		}
		if (right > 0){
			Color forwardRight = new Color(right/4, right, right/4, 0);
			this.shud.rect(1060, 40, right*250, 30, forwardRight, forwardRight, forwardRight, forwardRight);
		}
		else if (right < 0){
			Color backwardRight = new Color(-right, -right/4, -right/4, 0);
			this.shud.rect(1050+right*250, 40, -right*250, 30, backwardRight, backwardRight, backwardRight, backwardRight);
		}
		this.shud.rect(1050, 40, 10, 100, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
		this.shud.end();
		this.batch.begin();
		this.batch.draw(this.HUD, 0, 0);
		this.font.draw(this.batch, robot.getPosition().toString(), 1600, 100);
		this.batch.end();
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

}
