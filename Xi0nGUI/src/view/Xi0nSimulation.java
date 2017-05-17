package view;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import view.ui.QuitButton;
import view.ui.RestartButton;
import view.ui.StartButton;
import view.ui.UIButton;
import view.ui.PauseButton;

public enum Xi0nSimulation implements ApplicationListener {

	INSTANCE;

	private ShapeRenderer sr;
	private OrthographicCamera camera;
	private Room room;
	private Stage stage;
	private Texture bar;
	private SpriteBatch batch;

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
		this.stage.addActor(new QuitButton(30, 180));
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
}
