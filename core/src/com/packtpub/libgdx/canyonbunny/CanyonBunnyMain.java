package com.packtpub.libgdx.canyonbunny;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.packtpub.libgdx.canyonbunny.game.WorldController;
import com.packtpub.libgdx.canyonbunny.game.WorldRenderer;


public class CanyonBunnyMain implements ApplicationListener {
	private static final String TAG = CanyonBunnyMain.class.getName();

	private WorldController worldController;
	private WorldRenderer worldRenderer;
	private boolean paused;

	@Override
	public void create () {
		// 将日志级别设置为debug模式
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// 初始化控制器和渲染器
		worldController = new WorldController();
		worldRenderer = new WorldRenderer(worldController);
		paused = false;
	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.resize(width, height);

	}

	@Override
	public void render () {
		//根据最后一帧的增量时间更新游戏世界
		//当游戏暂停时，不再进行更新操作
		if (!paused){
			worldController.update(Gdx.graphics.getDeltaTime());
		}

		//设置清屏颜色为浅蓝色
		Gdx.gl.glClearColor(0x64/255.0f,0x95/255.0f,0xed/255.0f,0xff/255.0f);
		//清屏
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//将游戏世界渲染到屏幕上

		worldRenderer.render();
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		paused = false;
	}

	@Override
	public void dispose () {
		worldRenderer.dispose();
	}
}
