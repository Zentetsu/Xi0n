package view;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;

import view.robot.Mode;
import view.ui.AutomaticButton;
import view.ui.ControllerButton;
import view.ui.KeyboardButton;
import view.ui.QuitButton;
import view.ui.RestartButton;
import view.ui.StartButton;
import view.ui.UIButton;

public enum Xi0nSimulation implements ApplicationListener {

	INSTANCE;

	private ShapeRenderer sr;
	private OrthographicCamera camera;
	private Room room;
	private Stage stage;
	private Texture bar;
	private SpriteBatch batch;

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
		this.stage = new Stage();
		this.batch = new SpriteBatch();
		this.bar = new Texture("assets/test3.png");

		Gdx.input.setInputProcessor(this.stage);
		this.stage.addActor(new StartButton(30, 30));
		this.stage.addActor(new RestartButton(30, 130));
		this.stage.addActor(new QuitButton(1860, 1025));

		this.keyboardButton = new KeyboardButton(100, 300);
		this.controllerButton = new ControllerButton(100, 400);
		this.automaticButton = new AutomaticButton(100, 500);

		this.stage.addActor(this.keyboardButton);
		this.stage.addActor(this.controllerButton);
		this.stage.addActor(this.automaticButton);
		
		this.keyboardButton.setChecked(true);
		this.buttonGroup = new ButtonGroup<UIButton>(keyboardButton, controllerButton, automaticButton);

		this.buttonGroup.setMaxCheckCount(1);
		this.buttonGroup.setMinCheckCount(1);
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
		this.sr.rect(500, 200, 700, 50, Color.ORANGE, Color.ORANGE, Color.ORANGE, Color.ORANGE);
		this.sr.end();
		this.batch.begin();
		this.batch.draw(this.bar, 0, 0);
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
