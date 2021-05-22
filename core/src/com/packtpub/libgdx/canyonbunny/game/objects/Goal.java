package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.canyonbunny.game.Assets;

/**
 * @auther SHI Zhancheng
 * @create 2021-05-12 12:24
 */
public class Goal extends AbstractGameObject{
    private TextureRegion regGoal;

    public Goal(){
        init();
    }

    private void init(){
        dimension.set(3.0f,3.0f);
        regGoal = Assets.instance.levelDecoration.goal;

        // 设置边界矩形
        bounds.set(1,Float.MIN_VALUE,10,Float.MAX_VALUE);
        origin.set(dimension.x/2, 0.0f);
    }
    @Override
    public void render(SpriteBatch batch) {
        TextureRegion reg = null;

        reg = regGoal;
        batch.draw(reg.getTexture(),position.x - origin.x,position.y-origin.y,
                origin.x,origin.y,dimension.x,dimension.y,
                scale.x,scale.y,rotation,reg.getRegionX(),reg.getRegionY(),reg.getRegionWidth(),reg.getRegionHeight(),
                false,false);
    }
}
