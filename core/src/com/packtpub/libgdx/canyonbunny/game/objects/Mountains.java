package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.packtpub.libgdx.canyonbunny.game.Assets;

/**
 * @auther SHI Zhancheng
 * @create 2021-03-09 0:09
 */
public class Mountains extends AbstractGameObject{
    private TextureRegion regMountainLeft;
    private TextureRegion regMountainRight;

    private int length;

    public Mountains(int length) {
        this.length = length;
        init();
    }

    private void init() {
        dimension.set(10,2);

        regMountainLeft = Assets.instance.levelDecoration.mountainLeft;
        regMountainRight = Assets.instance.levelDecoration.mountainRight;

        // 转换mountain并扩张长度
        origin.x = -dimension.x * 2;
        length += dimension.x *2;
    }

    public void updateScrollPosition (Vector2 camPosition) {
        position.set(camPosition.x, position.y);
    }

    private void drawMountain (SpriteBatch batch,float offsetX,float offsetY,float tintColor,float parallaxSpeedX) {
        TextureRegion reg = null;
        batch.setColor(tintColor, tintColor, tintColor, 1);
        float xRel = dimension.x * offsetX;
        float yRel = dimension.y * offsetY;

        // mountains跨越整个关卡
        int mountainLength = 0;
        mountainLength += MathUtils.ceil(length / (2 * dimension.x) * (1 - parallaxSpeedX));
        mountainLength += MathUtils.ceil(0.5f + offsetX);
        for (int i = 0; i < length; i++) {
            // 渲染左侧mountain
            reg = regMountainLeft;
            batch.draw(reg.getTexture(),
                    origin.x + xRel + position.x * parallaxSpeedX,
                    position.y + origin.y + yRel, origin.x, origin.y,
                    dimension.x, dimension.y, scale.x, scale.y,
                    rotation, reg.getRegionX(), reg.getRegionY(),
                    reg.getRegionWidth(), reg.getRegionHeight(), false, false);
            xRel += dimension.x;

            // 渲染右侧mountains
            reg = regMountainRight;
            batch.draw(reg.getTexture(),
                    position.x * parallaxSpeedX + origin.x + xRel,
                    position.y + origin.y + yRel,
                    origin.x, origin.y,
                    dimension.x, dimension.y, scale.x, scale.y,
                    rotation, reg.getRegionX(), reg.getRegionY(),
                    reg.getRegionWidth(), reg.getRegionHeight(), false, false);
            xRel += dimension.x;
        }

        // 重置为白色
        batch.setColor(1, 1, 1, 1);

    }

    @Override
    public void render(SpriteBatch batch) {
        // 远处的山丘 （dark gray）
        drawMountain(batch,0.5f,0.5f,0.5f,0.8f);
        // 远处的山丘 （gray）
        drawMountain(batch,0.25f,0.25f,0.7f,0.5f);
        // 远处的山丘 （light gray）
        drawMountain(batch,0.0f,0.0f,0.9f,0.3f);

    }

}
