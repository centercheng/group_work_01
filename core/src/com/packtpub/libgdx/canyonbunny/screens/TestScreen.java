package com.packtpub.libgdx.canyonbunny.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.packtpub.libgdx.canyonbunny.game.Assets;
import com.packtpub.libgdx.canyonbunny.game.actor.TestActor;
import com.packtpub.libgdx.canyonbunny.game.stage.TestStage;
import com.packtpub.libgdx.canyonbunny.screens.transitions.ScreenTransition;
import com.packtpub.libgdx.canyonbunny.screens.transitions.ScreenTransitionFade;
import com.packtpub.libgdx.canyonbunny.util.Constants;

/**
 * @auther SHI Zhancheng
 * @create 2021-05-22 19:31
 */
public class TestScreen extends AbstractGameScreen{

    TestStage stage;

    TestActor actor;

    private float debugRebuildStage;


    public TestScreen(DirectedGame game) {
        super(game);
    }

    /**
     * 这个函数用于设置监听的stage场景，哪个场景需要读取监听就把stage放进去
     * @return 需要监听的场景stage
     */
    @Override
    public InputProcessor getInputProcessor() {
        return null;
    }


    @Override
    public void render(float deltaTime) {
        // 使用纯黑色清掉屏幕
        Gdx.gl.glClearColor(0.0f,0.0f,0.0f,0.0f);
        // 有触碰或者点击 => 切换屏幕
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(deltaTime);
        stage.draw();

//        // 切换screen
//        ScreenTransition transition = ScreenTransitionFade.init(0.75f);
//        game.setScreen(new GameScreen(game),transition);

//        stage.setDebugAll(true);//********//
    }

    @Override
    public void resize(int width,int height) {
        stage.addActor(actor);
//        stage.getViewport().update(width,height,true);
    }

    @Override
    public void show(){
//        stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH,Constants.VIEWPORT_GUI_HEIGHT));
    }

    @Override
    public void hide(){
        stage.dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        Assets.instance.init(new AssetManager());
    }


    @Override
    public void dispose() {
        Assets.instance.dispose();
    }
}
