package com.packtpub.libgdx.canyonbunny.game.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.libgdx.canyonbunny.game.Assets;

/**
 * @auther SHI Zhancheng
 * @create 2021-05-22 20:28
 */
public class TestActor extends Actor implements Disposable {
    TextureRegion region;

    //需要绑定物理实体


    public TestActor() {
        region = new TextureRegion(Assets.instance.bunny.head);

        setSize(this.region.getRegionWidth(), this.region.getRegionHeight());

        // 设置监听函数，可以处理一些事情
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setVisible(false);
                super.clicked(event, x, y);
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.draw(region, getX(), getY(),
                getOriginX(), getOriginY(),
                getWidth(), getHeight(),
                getScaleX(), getScaleY(),
                getRotation());
    }

    @Override
    public void dispose() {
        region.getTexture().dispose();
    }

}
