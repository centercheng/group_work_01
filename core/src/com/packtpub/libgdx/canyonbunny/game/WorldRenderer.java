package com.packtpub.libgdx.canyonbunny.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.libgdx.canyonbunny.util.Constants;

/**
 * @auther SHI Zhancheng
 * @create 2021-03-03 22:23
 * @function 游戏场景的渲染
 */
public class WorldRenderer implements Disposable {
    private OrthographicCamera camera;
    private OrthographicCamera cameraGUI;
    private SpriteBatch batch;
    private WorldController worldController;

    public WorldRenderer(WorldController worldController) {
        this.worldController = worldController;
        init();
    }

    private void init() {
        batch = new SpriteBatch();
        // 创建正交投影相机并配置它的视口参数
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(0,0,0);
        camera.update();
        cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        cameraGUI.position.set(0,0,0);
        cameraGUI.setToOrtho(true);
        cameraGUI.update();
    }

    public void render() {
        renderWorld(batch);
        renderGui(batch);
    }

    public void renderGuiScore(SpriteBatch batch) {
        float x = -15;
        float y = -15;
        float offsetX = 50;
        float offsetY = 50;
        if (worldController.scoreVisual < worldController.score) {
            long shakeAlpha = System.currentTimeMillis() % 360;
            float shakeDist = 1.5f;
            offsetX += MathUtils.sinDeg(shakeAlpha * 2.2f) * shakeDist;
            offsetY += MathUtils.sinDeg(shakeAlpha * 2.9f) * shakeDist;
        }
        batch.draw(Assets.instance.goldCoin.goldCoin,x,y,offsetX,offsetY,100,100,
                0.35f,-0.35f,0);
        Assets.instance.fonts.defaultBig.draw(batch,""+(int) worldController.scoreVisual,x+75,y+37);
    }

    public void renderGuiExtraLive(SpriteBatch batch){
        float x = cameraGUI.viewportWidth - 50 - Constants.LIVES_START * 50;
        float y = -15;
        for (int i = 0; i< Constants.LIVES_START; i++){
            if (i > worldController.lives)
                batch.setColor(0.5f,0.5f,0.5f,0.5f);
            batch.draw(Assets.instance.bunny.head,
                    x+i * 50,y,50,50,120,100,0.35f,-0.35f,0);
            batch.setColor(1,1,1,1);
        }

        if (worldController.lives >= 0
        && worldController.livesVisual > worldController.lives) {
            int i = worldController.lives;
            float alphaColor = Math.max(0,worldController.livesVisual
            - worldController.lives - 0.5f);
            float alphaScale = 0.35f * (2 + worldController.lives
            - worldController.livesVisual) * 2;
            float alphaRote = -45 * alphaColor;
            batch.setColor(1.0f,0.7f,0.7f,alphaColor);
            batch.draw(Assets.instance.bunny.head, x + i * 50,
                    y,50,50,120,100,
                    alphaScale,-alphaScale,alphaRote);
            batch.setColor(1,1,1,1);
        }
    }

    public void renderGuiFpsCounter(SpriteBatch batch) {
        float x = cameraGUI.viewportWidth - 55;
        float y = cameraGUI.viewportHeight - 15;
        int fps = Gdx.graphics.getFramesPerSecond();
        BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
        if (fps >=45) {
            fpsFont.setColor(0,1,0,1);
        }else if (fps >=30 ) {
            fpsFont.setColor(1,1,0,1);
        }else  {
            fpsFont.setColor(1,0,0,1);
        }
        fpsFont.draw(batch,"FPS: "+fps,x,y);
        fpsFont.setColor(1,1,1,1);// 恢复至白色
    }

    public void renderGui(SpriteBatch batch) {
        batch.setProjectionMatrix(cameraGUI.combined);
        batch.begin();
        // 绘制金币图标和玩家得分(左上角)
        renderGuiScore(batch);
        // 绘制羽毛道具
        renderGuiFeatherPowerUp(batch);
        // 绘制剩余的生命数（右上角）
        renderGuiExtraLive(batch);
        // 绘制FPS计数器（右下角）
        renderGuiFpsCounter(batch);
        // 绘制GAME OVER文本
        renderGuiGameOverMessage(batch);
        batch.end();
    }

    private void renderWorld (SpriteBatch batch) {
        worldController.cameraHelper.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        worldController.level.render(batch);
        batch.end();
    }

    private void renderGuiGameOverMessage(SpriteBatch batch) {
        float x = cameraGUI.viewportWidth/2;
        float y = cameraGUI.viewportHeight/2;
        if (worldController.isGameOver()) {
            BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
            fontGameOver.setColor(1,0.75f,0.25f,1);
            fontGameOver.draw(batch,"GAME OVER",x,y,0,Align.center,false);
            fontGameOver.setColor(1,1,1,1);
        }
    }


    private void renderGuiFeatherPowerUp (SpriteBatch batch) {
        float x = -15;
        float y = 30;
        float timeLeftFeatherPowerUp = worldController.level.bunnyHead.timeLeftFeatherPowerup;
        if (timeLeftFeatherPowerUp > 0) {
            // 剩余时间小于4秒时开始闪烁，闪烁频率5ci/s
            if (timeLeftFeatherPowerUp < 4) {
                if (((int)(timeLeftFeatherPowerUp * 5) % 2) != 0) {
                    batch.setColor(1, 1, 1, 0.5f);
                }
            }
            batch.draw(Assets.instance.feather.feather, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
            batch.setColor(1, 1, 1, 1);
            Assets.instance.fonts.defaultSmall.draw(batch, "" + (int)timeLeftFeatherPowerUp, x + 60, y + 57);
        }
    }


    public void resize(int width,int height){
        camera.viewportWidth = (Constants.VIEWPORT_HEIGHT/height) * width;
        camera.update();
        cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
        cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT/ (float)height) * (float)width;
        cameraGUI.position.set(cameraGUI.viewportWidth/2,cameraGUI.viewportHeight/2,0);
        cameraGUI.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
