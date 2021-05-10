package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.packtpub.libgdx.canyonbunny.game.Assets;


/**
 * @auther SHI Zhancheng
 * @create 2021-03-08 23:28
 */
public class Rock extends AbstractGameObject{

    private TextureRegion regEdge;
    private TextureRegion regMiddle;
    private final float FLOAT_CYCLE_TIME = 2.0f;
    private final float FLOAT_AMPLITUFE = 0.25f;
    private float floatCycleTimeLeft;
    private boolean floatingDownwards;
    private Vector2 floatTargetPosition;


    private int length;

    public Rock() {
        init();
    }

    private void init() {
        dimension.set(1,1.5f);

        regEdge = Assets.instance.rock.edge;
        regMiddle = Assets.instance.rock.middle;
        // 设置rock对象的初始长度
        setLength(1);

        floatingDownwards = false;
        floatCycleTimeLeft = MathUtils.random(0,FLOAT_CYCLE_TIME/2);
        floatTargetPosition = null;
    }

    public void setLength(int length) {
        this.length = length;
        // 更新边界（碰撞）矩形的尺寸
        bounds.set(0,0,dimension.x * length,dimension.y);
    }

    public void increaseLength (int amount) {
        setLength(length + amount);
    }


    @Override
    public void render(SpriteBatch batch) {
        TextureRegion reg = null;

        float relX = 0;
        float relY = 0;

        // 渲染左侧边缘
        reg = regEdge;
        relX -= dimension.x / 4;
        batch.draw(reg.getTexture(),position.x + relX,position.y + relY,origin.x,origin.y,
                dimension.x /4,dimension.y,scale.x,scale.y,rotation,reg.getRegionX(),
                reg.getRegionY(),reg.getRegionWidth(),reg.getRegionHeight(),false,false);

        // 渲染中间部分
        relX = 0;
        reg = regMiddle;
        for (int i = 0 ;i < length; i ++ ) {
            batch.draw(reg.getTexture(),position.x + relX,position.y + relY,origin.x,origin.y,
                    dimension.x,dimension.y,scale.x,scale.y,rotation,reg.getRegionX(),
                    reg.getRegionY(),reg.getRegionWidth(),reg.getRegionHeight(),false,false);
            relX += dimension.x;
        }

        // 渲染右边部分
        reg = regEdge;
        batch.draw(reg.getTexture(),position.x + relX,position.y + relY,
                origin.x + dimension.x / 8,origin.y,
                dimension.x /4,dimension.y,scale.x,scale.y,rotation,reg.getRegionX(),
                reg.getRegionY(),reg.getRegionWidth(),reg.getRegionHeight(),true,false);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        floatCycleTimeLeft -= deltaTime;
        if (floatTargetPosition == null)
            floatTargetPosition = new Vector2(position);

        if (floatCycleTimeLeft <=0){
            floatCycleTimeLeft = FLOAT_CYCLE_TIME;
            floatingDownwards = !floatingDownwards;
            floatTargetPosition.y += FLOAT_AMPLITUFE*(floatingDownwards ? -1:1);
        }
        position.lerp(floatTargetPosition,deltaTime);
    }
}
