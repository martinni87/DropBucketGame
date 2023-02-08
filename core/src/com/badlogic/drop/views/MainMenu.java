package com.badlogic.drop.views;

import com.badlogic.drop.Drop;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;


public class MainMenu implements Screen {
    public Drop game;


    public MainMenu(Drop game){
        game.background = new Texture(Gdx.files.internal("img/rain_dark.jpg"));
        game.titleBmp = new BitmapFont(Gdx.files.internal("fonts/mifuente.fnt"));
        game.music = Gdx.audio.newMusic(Gdx.files.internal("sounds/looping_calm.mp3"));
        game.rain = Gdx.audio.newMusic(Gdx.files.internal("sounds/rain.mp3"));
        game.music.setVolume(0.3f);
        game.rain.setVolume(0.2f);
        game.music.setLooping(true);
        game.rain.setLooping(true);
        game.titleMsg = "THE DROP BUCKET GAME";
        game.subtitleMsg = "Toca la pantalla o pulsa una tecla para comenzar";
        game.titleLayout = new GlyphLayout(game.titleBmp,game.titleMsg);
        game.subtitleLayout = new GlyphLayout(game.subtitleBmp,game.subtitleMsg);
        game.titlePosX = (Gdx.graphics.getWidth()-game.titleLayout.width)/2;
        game.titlePosY = (Gdx.graphics.getHeight()-game.titleLayout.height)/2;
        game.subtitlePosX = (Gdx.graphics.getWidth()-game.subtitleLayout.width)/2;
        game.subtitlePosY = (Gdx.graphics.getHeight()-game.subtitleLayout.height)/2;

        this.game = game;
    }
    @Override
    public void show() {
        game.music.play();
        game.rain.play();
    }

    @Override
    public void render(float delta) {
        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);
        game.batch.begin();

        game.batch.draw(game.background, 0, 0);
        game.titleBmp.draw(game.batch, game.titleMsg, 50,100);
        game.subtitleBmp.draw(game.batch, game.subtitleMsg, 100,240);

        game.batch.end();

        actionForESCKey();

        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) || Gdx.input.justTouched()) {
            Gdx.app.log("MARTIN DEBUG", "any key or touch pressed");
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
        game.music.pause();
        game.rain.pause();
    }

    @Override
    public void resume() {
        Gdx.app.log("MARTIN DEBUG","App resume");
        game.music.play();
        game.rain.play();
    }

    @Override
    public void hide() {
        Gdx.app.log("MARTIN DEBUG","App hide mode");
        game.music.pause();
        game.rain.pause();
    }

    @Override
    public void dispose() {
        Gdx.app.log("MARTIN DEBUG", "disposed at main menu");
        game.batch.dispose();
        game.titleBmp.dispose();
        game.subtitleBmp.dispose();
        game.background.dispose();
        game.music.dispose();
        game.rain.dispose();
        game.dispose();
    }

    public void actionForESCKey(){
        //Creating an inputProcessor to handle back key actions
        InputProcessor backProcessor = new InputAdapter(){
            @Override
            public boolean keyDown(int keycode){
                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK)) {
                    Gdx.app.log("MARTIN DEBUG", "EXIT GAME");
                    dispose();
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
