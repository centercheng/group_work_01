package com.packtpub.libgdx.canyonbunny.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.libgdx.canyonbunny.util.Constents;

/**
 * @auther SHI Zhancheng
 * @create 2021-03-03 22:23
 * @function 游戏场景的渲染
 */
public class WorldRenderer implements Disposable {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private WorldController worldController;

    public WorldRenderer(WorldController worldController) {
        this.worldController = worldController;
        init();
    }

    private void init() {
        batch = new SpriteBatch();
        // 创建正交投影相机并配置它的视口参数
        camera = new OrthographicCamera(Constents.VIEWPORT_WIDTH,Constents.VIEWPORT_HEIGHT);
        camera.position.set(0,0,0);
        camera.update();
    }

    public void render() {
        renderTestObjects();
    }

    private void renderTestObjects() {
        worldController.cameraHelper.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Sprite sprite :
                worldController.testSprite) {
            sprite.draw(batch);
        }
        batch.end();
    }



    public void resize(int width,int height){
        camera.viewportWidth = (Constents.VIEWPORT_HEIGHT/height) * width;
        camera.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
