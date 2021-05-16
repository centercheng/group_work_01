package com.packtpub.libgdx.canyonbunny.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.packtpub.libgdx.canyonbunny.game.WorldController;
import com.packtpub.libgdx.canyonbunny.game.WorldRenderer;

/**
 * @auther SHI Zhancheng
 * @create 2021-03-23 22:43
 */
public class GameScreen extends AbstractGameScreen{
    private static final String TAG = GameScreen.class.getName();

    private WorldController worldController;
    private WorldRenderer worldRenderer;

    private boolean paused;

    public GameScreen (DirectedGame game) {
        super(game);
    }

    @Override
    public InputProcessor getInputProcessor() {
        return worldController;
    }

    @Override
    public void render (float deltaTime) {
        // 如果暂停，则不更新游戏
        if (!paused) {
            // 根据增量时间更新游戏
            worldController.update(deltaTime);
        }
        // 设置清屏颜色浅蓝色
        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // 渲染游戏世界
        worldRenderer.render();
    }

    @Override
    public void resize (int width, int height) {
        worldRenderer.resize(width, height);
    }

    @Override
    public void show () {
//        GamePreferences.instance.load();
        worldController = new WorldController(game);
        worldRenderer = new WorldRenderer(worldController);
        Gdx.input.setCatchBackKey(true);    //桌面版本用不到
    }

    @Override
    public void hide () {
        worldController.dispose();
        worldRenderer.dispose();
        Gdx.input.setCatchBackKey(false);    //桌面版本用不到
    }

    @Override
    public void pause () {
        paused = true;
    }

    @Override
    public void resume () {
        super.resume();
        // 只有Android调用
        paused = false;
    }
}
