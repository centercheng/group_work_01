package com.packtpub.libgdx.canyonbunny.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.packtpub.libgdx.canyonbunny.game.objects.BunnyHead;
import com.packtpub.libgdx.canyonbunny.game.objects.Feather;
import com.packtpub.libgdx.canyonbunny.game.objects.GoldCoin;
import com.packtpub.libgdx.canyonbunny.game.objects.Rock;
import com.packtpub.libgdx.canyonbunny.screens.DirectedGame;
import com.packtpub.libgdx.canyonbunny.screens.MenuScreen;
import com.packtpub.libgdx.canyonbunny.screens.transitions.ScreenTransition;
import com.packtpub.libgdx.canyonbunny.screens.transitions.ScreenTransitionSlide;
import com.packtpub.libgdx.canyonbunny.util.AudioManager;
import com.packtpub.libgdx.canyonbunny.util.CameraHelper;
import com.packtpub.libgdx.canyonbunny.util.Constants;

/**
 * @auther SHI Zhancheng
 * @create 2021-03-03 22:17
 * @function 用于初始化游戏和切换游戏状态的所有逻辑
 */



public class WorldController extends InputAdapter {
    private static final String TAG = WorldController.class.getName();

    public Level level;
    public int lives;
    public int score;

    public CameraHelper cameraHelper;

    // 用于碰撞检测的临时变量
    private Rectangle r1 = new Rectangle();
    private Rectangle r2 = new Rectangle();
    public float livesVisual;
    public float scoreVisual;

    private float timeLeftGameOverDelay;

    private DirectedGame game;

    public void backToMenu() {
        // 切换到菜单界面
        ScreenTransition transition = ScreenTransitionSlide.init(0.75f,
                ScreenTransitionSlide.DOWN,false, Interpolation.bounceOut);
        game.setScreen(new MenuScreen(game),transition);
    }

    public boolean isGameOver() {
        return lives<0;
    }

    public boolean isPlayerInWater() {
        return level.bunnyHead.position.y < -5;
    }



    private void testCollisions() {
        r1.set(level.bunnyHead.position.x,level.bunnyHead.position.y,
                level.bunnyHead.bounds.width,level.bunnyHead.bounds.height);
        // 碰撞检测： Bunny head <-> Rocks
        for (Rock rock:
                level.rocks) {
            r2.set(rock.position.x,rock.position.y,
                    rock.bounds.width,rock.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionBunnyHeadWidthRock(rock);
            // IMPORTENT: 必须检测所有rock对象
        }

        // 碰撞检测：bunny head <-> Gold Coins
        for (GoldCoin goldCoin:
                level.goldCoins) {
            if (goldCoin.collected) continue;
            r2.set(goldCoin.position.x,goldCoin.position.y,
                    goldCoin.bounds.width,goldCoin.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionBunnyHeadWidthGoldCoin(goldCoin);
            break;
        }

        // 碰撞检测：bunny head <-> feather
        for (Feather feather:
                level.feathers) {
            if (feather.collected) continue;
            r2.set(feather.position.x,feather.position.y,
                    feather.bounds.width,feather.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionBunnyHeadWidthFeather(feather);
            break;
        }
    }

    /***
     * 该方法根据碰撞的深度将bunnyhead 对象平移至与rock平台刚好分离的位置
     * @param rock
     */
    private void onCollisionBunnyHeadWidthRock(Rock rock) {
        BunnyHead bunnyHead = level.bunnyHead;
        float heightDifference = Math.abs(bunnyHead.position.y - (rock.position.y + rock.bounds.height));

        if (heightDifference > 0.25f) {
            boolean hitRightEdge = bunnyHead.position.x > (rock.position.x +rock.bounds.width/2.0f);
            if (hitRightEdge){
                bunnyHead.position.x = rock.position.x + rock.bounds.width;
            } else {
                bunnyHead.position.x = rock.position.x - rock.bounds.width;
            }
            return;
        }

        switch (bunnyHead.jumpState) {
            case GROUNDED:
                break;
            case FALLING:
            case JUMP_FALLING:
                bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
                bunnyHead.jumpState = BunnyHead.JUMP_STATE.GROUNDED;
                break;
            case JUMP_RISING:
                bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
                break;
        }
    }

    private void onCollisionBunnyHeadWidthGoldCoin(GoldCoin goldCoin) {
        goldCoin.collected = true;
        AudioManager.instance.play(Assets.instance.sounds.pickupCoin);
        score += goldCoin.getScore();
        Gdx.app.log(TAG, "Gold coin collected");
    }
    private void onCollisionBunnyHeadWidthFeather(Feather feather) {
        feather.collected = true;
        AudioManager.instance.play(Assets.instance.sounds.pickupFeather);
        score += feather.getScore();
        level.bunnyHead.setFeatherPowerup(true);
        Gdx.app.log(TAG, "Feather collected");
    }


    public WorldController(DirectedGame game) {
        this.game = game;
        init();
    }

    private void init() {
        cameraHelper = new CameraHelper();
        lives = Constants.LIVES_START;
        livesVisual = lives;
        timeLeftGameOverDelay =0;
        initLevel();
    }


    private  void initLevel() {
        score = 0;
        scoreVisual = score;
        level = new Level(Constants.LEVEL_01);
        cameraHelper.setTarget(level.bunnyHead);
    }

    private Pixmap createProceduralPixmap(int width,int height) {
        Pixmap pixmap = new Pixmap(width,height, Pixmap.Format.RGBA8888);
        // 以50%透明的红色填充矩形区域
        pixmap.setColor(1,0,0,0.5f);
        pixmap.fill();
        // 在矩形区域绘制一个黄色的X形状
        pixmap.setColor(1,1,0,1);
        pixmap.drawLine(0,0,width,height);
        pixmap.drawLine(width,0,0,height);
        // 为矩形边框绘制一个青色的边框
        pixmap.setColor(0,1,1,1);
        pixmap.drawRectangle(0,0,width,height);
        return pixmap;
    }


    public void update(float deltaTime) {
        handleDebugInput(deltaTime);
        if (isGameOver()) {
            timeLeftGameOverDelay -= deltaTime;
            if (timeLeftGameOverDelay < 0) backToMenu();
        } else {
            handleInputGame(deltaTime);
        }
        level.update(deltaTime);
        testCollisions();
        cameraHelper.update(deltaTime);
        if (!isGameOver() && isPlayerInWater()) {
            AudioManager.instance.play(Assets.instance.sounds.liveLost);
            lives--;
            if (isGameOver())
                timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
            else
                initLevel();
        }
        level.mountains.updateScrollPosition(cameraHelper.getPosition());

        if (livesVisual >lives) {
            livesVisual = Math.max(lives,livesVisual - 1 * deltaTime);
        }

        if (scoreVisual < score)
            scoreVisual = Math.max(score,scoreVisual + 250 * deltaTime);
    }

    private void handleDebugInput(float deltaTime) {
        // 限制适用平台为桌面
        if (Gdx.app.getType() != Application.ApplicationType.Desktop)
            return;

        if (!cameraHelper.hasTarget(level.bunnyHead)){
            // 相机控制（移动）
            float camMoveSpeed = 5 * deltaTime;
            float camMoveSpeedAccelerationFactor = 5;
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
                camMoveSpeed *= camMoveSpeedAccelerationFactor;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                moveCamera(-camMoveSpeed,0);
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                moveCamera( camMoveSpeed,0);
            if (Gdx.input.isKeyPressed(Input.Keys.UP))
                moveCamera(0, camMoveSpeed);
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
                moveCamera(0,-camMoveSpeed);
            if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE))
                cameraHelper.setPosition(0,0);
        }


        // 相机控制（缩放）
        float camZoomSpeed = 1 * deltaTime;
        float camZoomSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
            camZoomSpeed *= camZoomSpeedAccelerationFactor;
        if (Gdx.input.isKeyPressed(Input.Keys.COMMA))
            cameraHelper.addZoom(camZoomSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.PERIOD))
            cameraHelper.addZoom(-camZoomSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.SLASH))
            cameraHelper.setZoom(1);
    }


    private void moveCamera(float x,float y) {
        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x,y);
    }

    // 控制角色左右移动
    private void handleInputGame (float deltaTime) {
        if (cameraHelper.hasTarget(level.bunnyHead)) {
            // Player Movement
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                level.bunnyHead.velocity.x = -level.bunnyHead.terminalVelocity.x;
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
            } else {
                // Execute auto-forward movement on non-desktop platform
                if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
                    level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
                }
            }

            // Bunny Jump
            if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE))
                level.bunnyHead.setJumping(true);
            else
                level.bunnyHead.setJumping(false);
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
            cameraHelper.setTarget(cameraHelper.hasTarget()?null: level.bunnyHead);
            Gdx.app.debug(TAG, "camera follow enabled:"+cameraHelper.hasTarget());
        }
        else if (keyCode == Input.Keys.ESCAPE|| keyCode == Input.Keys.BACK ) {
            backToMenu();
        }
        return false;

    }

}
