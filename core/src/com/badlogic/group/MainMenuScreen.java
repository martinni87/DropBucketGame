package com.badlogic.group;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final Drop game;

    OrthographicCamera camera;

    String titleText, normalText;
    GlyphLayout layoutTitle, layoutNormal;
    float titlePosX, titlePosY, normalPosX, normalPosY;
    private Texture bucketImage;
    private Texture background;

    public MainMenuScreen(final Drop game) {
        this.game = game;
//        backgroundMenu = new Texture(Gdx.files.internal("img/background_menu.png"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        titleText = "THE DROP BUCKET GAME";
        normalText = "Pulsa la pantalla para comenzar";
        layoutTitle = new GlyphLayout(game.titleFont,titleText);
        layoutNormal = new GlyphLayout(game.normalFont,normalText);
        titlePosX = (Gdx.graphics.getWidth()-layoutTitle.width)/2;
        titlePosY = (Gdx.graphics.getHeight()-layoutTitle.height)/2;
        normalPosX = (Gdx.graphics.getWidth()-layoutNormal.width)/2;
        normalPosY = (Gdx.graphics.getHeight()-layoutNormal.height)/2;
        background = new Texture(Gdx.files.internal("img/background_menu.JPG"));
        bucketImage = new Texture(Gdx.files.internal("img/bucket.png"));
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
//        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
//        game.batch.setColor(Color.GRAY);
        game.batch.draw(background, 0, 0);
        game.titleFont.draw(game.batch, titleText, 50,100);

        game.batch.enableBlending();
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        game.normalFont.draw(game.batch, normalText, 50,300);
        Gdx.gl.glDisable(GL20.GL_BLEND);
        game.batch.disableBlending();



        game.batch.draw(bucketImage, 650, 200);

        game.batch.end();

        game.rainMusic.setVolume(0.3f);
        game.rainMusic.play();
        game.menuMusic.setVolume(0.3f);
        game.menuMusic.play();

        if (Gdx.input.isTouched()) {
            game.rainMusic.stop();
            game.rainMusic.dispose();
            game.menuMusic.stop();
            game.menuMusic.dispose();
            game.setScreen(new GameScreen(game));
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
    }

    public void blendingText(){

    }
}
