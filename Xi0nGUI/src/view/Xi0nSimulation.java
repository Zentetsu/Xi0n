package view;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Xi0nSimulation implements ApplicationListener {

	private ShapeRenderer sr;
	private OrthographicCamera camera;
	private Room room;
	private Stage stage;

	@Override
	public void create() {
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false);
		this.room = new Room();
		this.sr = new ShapeRenderer();
		this.stage = new Stage();
		
		
        Gdx.input.setInputProcessor(this.stage);
        BitmapFont font = new BitmapFont();
        Skin skin = new Skin();
        TextureAtlas buttonAtlas = new TextureAtlas(Gdx.files.internal("assets/statusui.atlas"));
        skin.addRegions(buttonAtlas);
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("dialog");
        textButtonStyle.down = skin.getDrawable("dialog");
        textButtonStyle.checked = skin.getDrawable("dialog");
        Button button = new Button(textButtonStyle);
        button.setPosition(500, 20);
        button.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent input,float x,float y )
            {
                System.out.println("hiii");
            }
            
        } );
        this.stage.addActor(button);
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
