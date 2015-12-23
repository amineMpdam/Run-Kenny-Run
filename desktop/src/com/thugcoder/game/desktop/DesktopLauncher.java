package com.thugcoder.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

public class DesktopLauncher {

	private static boolean rebuildAtlas = false;
	private static boolean drawDebugOutline = true;

	public static void main (String[] arg) {

		if (rebuildAtlas) {
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings, "assets-raw/images", "../android/assets/images",
			"RunKenny.pack");

			TexturePacker.process(settings, "assets-raw/images-ui",
					"../android/assets/images",
					"RunKenny-ui.pack");
		}


		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Run Kenny Run";
		config.width = 800 ;
		config.height = 480 ;
		//new LwjglApplication(new KennyRunMain(), config);
	}
}
