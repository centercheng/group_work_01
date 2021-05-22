package com.packtpub.libgdx.canyonbunny.screens.transitions;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @auther SHI Zhancheng
 * @create 2021-05-10 11:32
 */
public interface ScreenTransition {
    public float getDuration();
    // 查询切换效果的时间和周期
    public void render (SpriteBatch batch, Texture currScreen,
                        Texture nextScreen, float alpha);
    // render用于渲染切换效果，alpha表示切换效果的渲染进度

}
