package view;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Xi0nSimulation implements ApplicationListener {

	//private SpriteBatch batch;
	private ShapeRenderer sr;
	private OrthographicCamera camera;
	private Room room;
	
	@Override
	public void create() {
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false);
		this.sr = new ShapeRenderer();
		this.room = new Room();
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
		this.room.render(sr);
		this.sr.end();
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
