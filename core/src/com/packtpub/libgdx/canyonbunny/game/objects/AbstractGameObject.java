package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * @auther SHI Zhancheng
 * @create 2021-03-08 23:15
 */
public abstract class AbstractGameObject {
    // 位置
    public Vector2 position;
    // 尺寸
    public Vector2 dimension;
    // 远点
    public Vector2 origin;
    // 缩放
    public Vector2 scale;
    // 旋转
    public float rotation;

    // 对象当前的移动速度m/s
    public Vector2 velocity;
    // 定义对象的政府最大移动速度，单位为m/s
    public Vector2 terminalVelocity;
    // 表示使对象减速的摩擦力，该变量使一个无量纲的系数。
    public Vector2 friction;
    // 表示加速度m^2/s
    public Vector2 acceleration;
    // 表示游戏对象的边界矩形
    public Rectangle bounds;



    public AbstractGameObject() {
        position = new Vector2();
        dimension = new Vector2(1,1);
        origin = new Vector2();
        scale = new Vector2(1,1);
        rotation = 0;
        velocity = new Vector2();
        terminalVelocity = new Vector2(1,1);
        friction = new Vector2();
        acceleration = new Vector2();
        bounds = new Rectangle();
    }

    public void update(float deltaTime) {
        updateMotionX(deltaTime);
        updateMotionY(deltaTime);
        // 移动到最新的位置
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;

    }

    public abstract void render(SpriteBatch batch);

    protected void updateMotionX (float deltaTime) {
        if (velocity.x !=0){
            // 应用摩擦力
            if (velocity.x > 0){
                velocity.x = Math.max(velocity.x - friction.x * deltaTime,0);
            }else {
                velocity.x = Math.min(velocity.x - friction.x * deltaTime,0);
            }
        }
        // 应用加速度
        velocity.x += acceleration.x * deltaTime;
        // 确保当前速度没有超过正负最大值
        velocity.x = MathUtils.clamp(velocity.x,-terminalVelocity.x,terminalVelocity.x);
    }

    protected void updateMotionY(float deltaTime) {
        if (velocity.y !=0){
            // 应用摩擦力
            if (velocity.y > 0){
                velocity.y = Math.max(velocity.y - friction.y * deltaTime,0);
            }else {
                velocity.y = Math.min(velocity.y - friction.y * deltaTime,0);
            }
        }
        // 应用加速度
        velocity.y += acceleration.y * deltaTime;
        // 确保当前速度没有超过正负最大值
        velocity.y = MathUtils.clamp(velocity.y,-terminalVelocity.y,terminalVelocity.y);
    }
}
