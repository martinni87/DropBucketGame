package com.badlogic.drop.views;

import com.badlogic.drop.Drop;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOverView implements Screen {
    Drop game;
    GameScreen gameScreen;
    SpriteBatch batch;
    Texture background;
    BitmapFont text1, text2, text3;
    Music goMusic;
    OrthographicCamera camera;

    public GameOverView(Drop game, GameScreen gameScreen){
        this.game = game;
        this.gameScreen = gameScreen;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        background = new Texture(Gdx.files.internal("img/gameover.png"));
        text1 = new BitmapFont();
        text2 = new BitmapFont();
        text3 = new BitmapFont();
        goMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/gameover.m4a"));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
//Render screen clear with dark blue color
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();

        //Set camera render its matrix
        batch.setProjectionMatrix(camera.combined);

        //Begin to render calling the batch
        //Spritebatch to use, start...
        batch.begin();
        //Draw bucketImage in coordinates x y defined in bucket rectangle.
        batch.draw(background, 0, 0);
        text1.getData().setScale(2.2f);
        text1.setColor(Color.BROWN);
        text1.draw(batch, "GAME OVER", background.getWidth()/2-300, background.getHeight()/2+30);
        text2.getData().setScale(1.2f);
        text2.setColor(Color.BROWN);
        text2.draw(batch, "Pulsa la pantalla para volver al inicio\npresiona cualquier tecla\no quédate disfrutando de la música.", background.getWidth()/2+90,background.getHeight()/2+50);
        text3.getData().setScale(1.5f);
        text3.setColor(Color.BROWN);
        text3.draw(batch, "Puntuación máxima: " + gameScreen.totalDropsGathered, background.getWidth()/2-100,75);
        //End batch
        batch.end();

        // Starts the playback of the background music when the screen is shown
        if (!goMusic.isPlaying()){
            goMusic.play();
            goMusic.setLooping(true);
        }

        actionForESCKey();

        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) || Gdx.input.justTouched()) {
            Gdx.app.log("MARTIN DEBUG", "going back after game over");
            goMusic.setLooping(false);
            goMusic.stop();
            game.setScreen(new MainMenu(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        background.dispose();
        text1.dispose();
        text2.dispose();
        batch.dispose();
    }

    public void actionForESCKey(){
        //Creating an inputProcessor to handle back key actions
        InputProcessor backProcessor = new InputAdapter(){
            @Override
            public boolean keyDown(int keycode){
                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK)) {
                    Gdx.app.log("MARTIN DEBUG", "EXIT GAME");
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
