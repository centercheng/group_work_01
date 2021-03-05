package com.packtpub.libgdx.canyonbunny.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.packtpub.libgdx.canyonbunny.util.CameraHelper;
import org.graalvm.compiler.loop.MathUtil;
import sun.java2d.pipe.PixelDrawPipe;

/**
 * @auther SHI Zhancheng
 * @create 2021-03-03 22:17
 * @function 用于初始化游戏和切换游戏状态的所有逻辑
 */



public class WorldController extends InputAdapter {
    private static final String TAG = WorldController.class.getName();
    public Sprite[] testSprite;
    public int selectedSprite;
    public CameraHelper cameraHelper;


    public WorldController() {
        init();
    }

    private void init() {
        Gdx.input.setInputProcessor(this);
        cameraHelper = new CameraHelper();
        initTestObjects();
    }

    private void initTestObjects() {
        //创建一个长度为5的 精灵数组
        testSprite = new Sprite[5];
        //创建一个POT尺寸的8bit RGBA色值的Pixemap对象
        int width = 32;
        int height = 32;
        Pixmap pixmap = createProceduralPixmap(width,height);
        // 使用Pixmap对象数据创建纹理
        Texture texture = new Texture(pixmap);
        // 使用上面创建的纹理创建精灵对象
        //for (Sprite s: testSprite) {        }
        for (int i=0;i<testSprite.length;i++){
            Sprite spr = new Sprite(texture);
            // 将精灵在游戏世界中的尺寸设置为1*1
            spr.setSize(1,1);
            // 将精灵对象的原点设置为中心
            spr.setOrigin(spr.getWidth()/2.0f,spr.getHeight()/2.0f);
            float randomX = MathUtils.random(-2.0f,2.0f);
            float randomY = MathUtils.random(-2.0f,2.0f);
            spr.setPosition(randomX,randomY);
            // 将精灵添加到数组中
            testSprite[i] = spr;
        }
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
        updateTestObjects(deltaTime);
        cameraHelper.update(deltaTime);

    }

    private void handleDebugInput(float deltaTime) {
        // 限制适用平台为桌面
        if (Gdx.app.getType() != Application.ApplicationType.Desktop)
            return;

        // 控制选中的精灵
        float sprMoveSpeed = 5 * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            moveSelectedSprite(-sprMoveSpeed,0);
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            moveSelectedSprite( sprMoveSpeed,0);
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            moveSelectedSprite(0,-sprMoveSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            moveSelectedSprite(0, sprMoveSpeed);

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

    private void moveSelectedSprite(float x,float y) {
        testSprite[selectedSprite].translate(x,y);
    }

    private void moveCamera(float x,float y) {
        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x,y);
    }
    private void updateTestObjects(float deltaTime) {
        // 获得的选中精灵对象的旋转角度
        float rotation = testSprite[selectedSprite].getRotation();
        // 以90°/s的速度旋转精灵对象
        rotation += 90 * deltaTime;
        // 将旋转角度限制在369°以内；
        rotation %= 360;
        // 为选中的精灵对象设置新的旋转角度
        testSprite[selectedSprite].setRotation(rotation);
    }

    // 处理事件响应
    @Override
    public boolean keyUp(int keyCode) {
        // 重置游戏世界
        if (keyCode == Input.Keys.R) {
            init();
            Gdx.app.debug(TAG, "game world reset");
        }
        // 选中下一个精灵
        else if (keyCode == Input.Keys.SPACE) {
            selectedSprite = (selectedSprite +1) % testSprite.length;
            // 更新相机的跟踪目标
            if (cameraHelper.hasTarget()) {
                cameraHelper.setTarget(testSprite[selectedSprite]);
            }
            Gdx.app.debug(TAG,"SPrite #"+ selectedSprite + "selected");
        }
        // 跟踪相机开关
        else if (keyCode == Input.Keys.ENTER) {
            cameraHelper.setTarget(cameraHelper.hasTarget()? null:testSprite[selectedSprite]);
            Gdx.app.debug(TAG,"Camera follow enabled:"+ cameraHelper.hasTarget());
        }
        return false;
    }

}
