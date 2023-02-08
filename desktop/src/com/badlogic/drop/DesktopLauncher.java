package com.badlogic.drop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * <strong>Please note that <u>on macOS</u> your application needs to be started with the <u>-XstartOnFirstThread JVM argument</u></strong>
 */

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Drop");
		config.setWindowedMode(800,480);
		config.useVsync(true);
		config.setForegroundFPS(60);
		new Lwjgl3Application(new Drop(), config);
	}
}
