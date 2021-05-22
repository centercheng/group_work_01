package com.packtpub.libgdx.canyonbunny.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.packtpub.libgdx.canyonbunny.game.Assets;
import com.badlogic.gdx.InputProcessor;

/**
 * @auther SHI Zhancheng
 * @create 2021-03-23 22:24
 */
public abstract class AbstractGameScreen implements Screen {
    protected DirectedGame game;

    public AbstractGameScreen(DirectedGame game){
        this.game = game;
    }
    public abstract InputProcessor getInputProcessor ();

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        Assets.instance.init(new AssetManager());
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        Assets.instance.dispose();
    }
}
