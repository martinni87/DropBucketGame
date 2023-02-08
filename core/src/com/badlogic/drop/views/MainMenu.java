package com.badlogic.drop.views;

import com.badlogic.drop.Drop;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;


public class MainMenu implements Screen {
    public Drop game;
    public OrthographicCamera camera;
    public Texture background;
    public SpriteBatch batch;

    public BitmapFont titleBmp, subtitleBmp;
    public String titleMsg, subtitleMsg;
    public GlyphLayout titleLayout, subtitleLayout;
    public float titlePosX,titlePosY,subtitlePosX,subtitlePosY;

    public Music music;
    public Music rain;


    public MainMenu(Drop game){
        //We start instantiating a Spritebatch, the fonts, the background for the menu, and music
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        subtitleBmp = new BitmapFont();

        background = new Texture(Gdx.files.internal("img/portada.png"));
        titleBmp = new BitmapFont(Gdx.files.internal("fonts/mifuente.fnt"));
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        rain = Gdx.audio.newMusic(Gdx.files.internal("sounds/rain.mp3"));

        titleMsg = "THE DROP BUCKET GAME";
        subtitleMsg = "Toca la pantalla o pulsa una tecla para comenzar";
        titleLayout = new GlyphLayout(titleBmp,titleMsg);
        subtitleLayout = new GlyphLayout(subtitleBmp,subtitleMsg);
        titlePosX = (Gdx.graphics.getWidth()-titleLayout.width)/2;
        titlePosY = (Gdx.graphics.getHeight()-titleLayout.height)/2;
        subtitlePosX = (Gdx.graphics.getWidth()-subtitleLayout.width)/2;
        subtitlePosY = (Gdx.graphics.getHeight()-subtitleLayout.height)/2;

        this.game = game;
    }
    @Override
    public void show() {
        if (!music.isPlaying() && !rain.isPlaying()){
            music.play();
            rain.play();
            music.setVolume(0.1f);
            rain.setVolume(0.1f);
            music.setLooping(true);
            rain.setLooping(true);
        }
        else{
            music.setVolume(0.1f);
            rain.setVolume(0.1f);
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(130, 130, 255, 1f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(background, 0, 0);
        titleBmp.draw(batch, titleMsg, 50,100);
        subtitleBmp.draw(batch, subtitleMsg, 100,240);

        batch.end();

        actionForESCKey();

        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) || Gdx.input.justTouched()) {
            Gdx.app.log("MARTIN DEBUG", "any key or touch pressed");
            music.setVolume(0.3f);
            rain.setVolume(0.3f);
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        Gdx.app.log("MARTIN DEBUG","App on pause");
        music.pause();
        rain.pause();
    }

    @Override
    public void resume() {
        Gdx.app.log("MARTIN DEBUG","App resume");
        music.play();
        rain.play();
    }

    @Override
    public void hide() {
        Gdx.app.log("MARTIN DEBUG","App hide mode");
        music.pause();
        rain.pause();
    }

    @Override
    public void dispose() {
        Gdx.app.log("MARTIN DEBUG", "disposed at main menu");
        batch.dispose();
        titleBmp.dispose();
        subtitleBmp.dispose();
        background.dispose();
        music.dispose();
        rain.dispose();
        game.dispose();
    }

    public void actionForESCKey(){
        //Creating an inputProcessor to handle back key actions
        InputProcessor backProcessor = new InputAdapter(){
            @Override
            public boolean keyDown(int keycode){
                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK)) {
                    Gdx.app.log("MARTIN DEBUG", "EXIT GAME");
//                    dispose();
                    Gdx.app.exit();
                    return false;
                }
                return false;
            }
        };
        //Adding it to a multiplexer
        InputMultiplexer multiplexer = new InputMultiplexer(backProcessor);
        Gdx.input.setInputProcessor(multiplexer);

        //Catch back key press to avoid bad exiting the app
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }
}
