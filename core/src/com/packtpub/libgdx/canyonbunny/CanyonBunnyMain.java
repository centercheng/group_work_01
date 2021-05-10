package com.packtpub.libgdx.canyonbunny;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Interpolation;
import com.packtpub.libgdx.canyonbunny.game.Assets;
import com.packtpub.libgdx.canyonbunny.screens.DirectedGame;
import com.packtpub.libgdx.canyonbunny.screens.MenuScreen;
import com.packtpub.libgdx.canyonbunny.screens.transitions.ScreenTransition;
import com.packtpub.libgdx.canyonbunny.screens.transitions.ScreenTransitionSlice;
import com.packtpub.libgdx.canyonbunny.util.AudioManager;
import com.packtpub.libgdx.canyonbunny.util.GamePreferences;


public class CanyonBunnyMain extends DirectedGame {
	private static final String TAG = CanyonBunnyMain.class.getName();

	@Override
	public void create () {
		// 设置debug日志
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		// 加载数据资源
		Assets.instance.init(new AssetManager());

		// 加载声音设置并开始播放音乐
		GamePreferences.instance.load();
		AudioManager.instance.play(Assets.instance.music.song01);

		// 启动菜单屏幕
		ScreenTransition transition = ScreenTransitionSlice.init(2,
				ScreenTransitionSlice.UP_DOWN,10, Interpolation.exp5Out);
		setScreen(new MenuScreen(this),transition);
	}
}
