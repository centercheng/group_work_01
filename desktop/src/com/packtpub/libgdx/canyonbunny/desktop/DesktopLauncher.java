package com.packtpub.libgdx.canyonbunny.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.packtpub.libgdx.canyonbunny.CanyonBunnyMain;
import com.packtpub.libgdx.canyonbunny.PublicData;

public class DesktopLauncher {
	private static boolean rebuildAtlas = true;
	private static boolean drawDebugOutline = false;


	public static void main (String[] arg) {
		if (rebuildAtlas) {
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings,"core/assets/images","core/assets/images-out",
					"canyonBunny");
			TexturePacker.process(settings,"core/assets/images-ui","core/assets/images-out",
					"canyonBunny-ui");
		}

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new CanyonBunnyMain(), config);
		config.width = PublicData.SCREEN_WIDTH/2;
		config.height = PublicData.SCREEN_HEIGHT/2;
	}
}
