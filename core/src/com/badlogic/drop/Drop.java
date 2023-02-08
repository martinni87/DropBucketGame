package com.badlogic.drop;

import com.badlogic.drop.views.MainMenu;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Drop extends Game {

	public OrthographicCamera camera;
	public Texture background;
	public SpriteBatch batch;

	public BitmapFont titleBmp, subtitleBmp;
	public String titleMsg, subtitleMsg;
	public GlyphLayout titleLayout, subtitleLayout;
	public float titlePosX,titlePosY,subtitlePosX,subtitlePosY;

	public Music music;
	public Music rain;

	public void create() {
		//We start instantiating a Spritebatch, the fonts, the background for the menu, and music
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		subtitleBmp = new BitmapFont();

		this.setScreen(new MainMenu(this));
	}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		//A este dispose no vamos a llegar a entrar nunca, porque se hace desde MainMenu.java
//		background.dispose();
//		batch.dispose();
//		titleBmp.dispose();
//		subtitleBmp.dispose();
//		music.dispose();
//		rain.dispose();
	}
}