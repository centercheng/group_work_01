package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.canyonbunny.game.Assets;

/**
 * @auther SHI Zhancheng
 * @create 2021-03-20 17:17
 */
public class Feather extends AbstractGameObject{
    private TextureRegion regFeather;

    public boolean collected;

    public Feather() {
        init();
    }

    private void init() {
        dimension.set(0.5f,0.5f);
        regFeather = Assets.instance.feather.feather;

        // 设置碰撞检测的矩形边界
        bounds.set(0,0,dimension.x,dimension.y);

        collected = false;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (collected) return;

        TextureRegion reg = null;
        reg = regFeather;
        batch.draw(reg.getTexture(),position.x,position.y,origin.x,origin.y,dimension.x,dimension.y,
                scale.x,scale.y,rotation,reg.getRegionX(),reg.getRegionY(),reg.getRegionWidth(),reg.getRegionHeight(),
                false,false);
    }

    public int getScore() {
        return 250;
    }
}
