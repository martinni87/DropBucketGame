package com.badlogic.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Drop extends Game {

	SpriteBatch batch;
	BitmapFont titleFont;
	BitmapFont normalFont;
	Texture backgroundMenu;
	Music menuMusic;
	Music rainMusic;

	public void create() {
		//We start instantiating a Spritebatch, the fonts, the background for the menu, and music
		batch = new SpriteBatch();
		titleFont = new BitmapFont(Gdx.files.internal("fonts/mifuente.fnt"));
		normalFont = new BitmapFont();
		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/looping_calm.mp3"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/rain.mp3"));
		this.setScreen(new MainMenuScreen(this));
	}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		batch.dispose();
		titleFont.dispose();
		rainMusic.dispose();
		menuMusic.dispose();
	}
}