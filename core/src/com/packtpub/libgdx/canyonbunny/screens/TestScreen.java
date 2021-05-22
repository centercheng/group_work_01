package com.packtpub.libgdx.canyonbunny.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.packtpub.libgdx.canyonbunny.MyContactListener;
import com.packtpub.libgdx.canyonbunny.PhysicalEntityDefine;
import com.packtpub.libgdx.canyonbunny.PublicData;
import com.packtpub.libgdx.canyonbunny.game.Assets;
import com.packtpub.libgdx.canyonbunny.stage.Stage1;


/**
 * @auther SHI Zhancheng
 * @create 2021-05-22 19:31
 */
public class TestScreen extends AbstractGameScreen{

    Stage stage1;

    World world;


    InputMultiplexer inputMultiplexer;
    public TestScreen(DirectedGame game) {
        super(game);
        Assets.instance.init(new AssetManager());

        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);//可以把监听信息分发给所有的stage

        PublicData.currentStage = PublicData.Stage0;

        world = new World(new Vector2(0,-10),true);
        world.setContactListener(new MyContactListener());

        PhysicalEntityDefine.boundWorld(world);

        stage1 = new Stage1(inputMultiplexer,world);

    }

    /**
     * 这个函数用于设置监听的stage场景，哪个场景需要读取监听就把stage放进去
     * @return 需要监听的场景stage
     */
    @Override
    public InputProcessor getInputProcessor() {
        return stage1;
    }




    @Override
    public void render(float deltaTime) {
        // 使用纯黑色清掉屏幕
        Gdx.gl.glClearColor(0.0f,0.0f,0.0f,0.0f);
        // 有触碰或者点击 => 切换屏幕
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage1.act(deltaTime);
        stage1.draw();

//        // 切换screen
//        ScreenTransition transition = ScreenTransitionFade.init(0.75f);
//        game.setScreen(new GameScreen(game),transition);

//        stage.setDebugAll(true);//********//
    }

    @Override
    public void resize(int width,int height) {

    }

    @Override
    public void show(){
//        stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH,Constants.VIEWPORT_GUI_HEIGHT));
    }

    @Override
    public void hide(){
        stage1.dispose();
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
