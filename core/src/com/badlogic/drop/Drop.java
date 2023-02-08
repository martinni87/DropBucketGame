package com.badlogic.drop;

import com.badlogic.drop.views.MainMenu;
import com.badlogic.gdx.Game;

public class Drop extends Game {

	public void create() {
		this.setScreen(new MainMenu(this));
	}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		//A este dispose no vamos a llegar a entrar nunca, porque se hace desde MainMenu.java
	}
}