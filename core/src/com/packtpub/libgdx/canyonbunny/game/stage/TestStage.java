package com.packtpub.libgdx.canyonbunny.game.stage;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.libgdx.canyonbunny.game.Assets;
import com.packtpub.libgdx.canyonbunny.game.Level;
import com.packtpub.libgdx.canyonbunny.game.WorldController;
import com.packtpub.libgdx.canyonbunny.game.objects.*;
import com.packtpub.libgdx.canyonbunny.screens.DirectedGame;
import com.packtpub.libgdx.canyonbunny.screens.MenuScreen;
import com.packtpub.libgdx.canyonbunny.screens.transitions.ScreenTransition;
import com.packtpub.libgdx.canyonbunny.screens.transitions.ScreenTransitionSlide;
import com.packtpub.libgdx.canyonbunny.util.AudioManager;
import com.packtpub.libgdx.canyonbunny.util.CameraHelper;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * @auther SHI Zhancheng
 * @create 2021-05-22 19:54
 */
public class TestStage extends Stage implements Disposable {
    private static final String TAG = WorldController.class.getName();

    public CameraHelper cameraHelper;
    private DirectedGame game;
    public World b2world;

    private void testCollisions() {
        // 判断所有物体之间的碰撞
    }


    public TestStage(DirectedGame game) {
        this.game = game;
        init();
    }

    private void init() {
    }


    private  void initLevel() {
    }

    /**
     * 初始化使用box2d 物理引擎
     */
    private void initPhysics () {
        if (b2world != null) b2world.dispose();
        b2world = new World(new Vector2(0, -9.81f), true);
    }

    public void update(float deltaTime) {

    }


    // 控制角色左右移动
    private void handleInputGame (float deltaTime) {

        // Player Movement
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//                level.bunnyHead.velocity.x = -level.bunnyHead.terminalVelocity.x;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//                level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
        } else {
            // Execute auto-forward movement on non-desktop platform
            if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
//                    level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
            }
        }

    }

    // 处理事件响应
    @Override
    public boolean keyUp(int keyCode) {
        // 重置游戏世界
        if (keyCode == Input.Keys.R) {
            init();
            Gdx.app.debug(TAG, "game world reset");
        }
        else if (keyCode == Input.Keys.ENTER) {
//            cameraHelper.setTarget(cameraHelper.hasTarget()?null: level.bunnyHead);
            Gdx.app.debug(TAG, "camera follow enabled:"+cameraHelper.hasTarget());
        }
        else if (keyCode == Input.Keys.ESCAPE|| keyCode == Input.Keys.BACK ) {
            backToMenu();
        }
        return false;

    }

    public void backToMenu() {
        // 切换到菜单界面
        ScreenTransition transition = ScreenTransitionSlide.init(0.75f,
                ScreenTransitionSlide.DOWN,false, Interpolation.bounceOut);
        game.setScreen(new MenuScreen(game),transition);
    }


    @Override
    public void dispose() {
        if (b2world != null) b2world.dispose();
    }
}
