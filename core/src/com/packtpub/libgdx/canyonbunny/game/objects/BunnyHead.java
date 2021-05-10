package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.packtpub.libgdx.canyonbunny.game.Assets;
import com.packtpub.libgdx.canyonbunny.util.AudioManager;
import com.packtpub.libgdx.canyonbunny.util.Constants;

/**
 * @auther SHI Zhancheng
 * @create 2021-03-20 17:22
 */
public class BunnyHead extends AbstractGameObject{

    public static final String TAG = BunnyHead.class.getName();

    private final float JUMP_TIME_MAX = 0.3f;
    private final float JUMP_TIME_MIN = 0.3f;
    private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f;

    public enum VIEW_DIRECTION {
        LEFT,RIGHT
    }

    public enum JUMP_STATE {
        GROUNDED,FALLING,JUMP_RISING,JUMP_FALLING
    }

    private TextureRegion regHead;

    public VIEW_DIRECTION viewDirection;
    public float timeJumping;
    public JUMP_STATE jumpState;
    public boolean hasFeaterPowerup;
    public float timeLeftFeatherPowerup;

    public ParticleEffect dustParticles = new ParticleEffect();
    public BunnyHead() {
        init();
    }

    public void init() {
        dimension.set(1,1);
        regHead = Assets.instance.bunny.head;
        // 将原点设置为对象中心
        origin.set(dimension.x/2,dimension.y/2);
        // 设置边界矩形的尺寸
        bounds.set(0,0,dimension.x,dimension.y);
        // 设置物理属性
        terminalVelocity.set(3.0f,4.0f);
        friction.set(12.0f,0.0f);
        acceleration.set(0.0f,-25.0f);
        // 初始化观察方向
        viewDirection = VIEW_DIRECTION.RIGHT;
        // 初始化跳跃状态
        jumpState = JUMP_STATE.FALLING;
        timeJumping =0;

        // 飞跃特效
        hasFeaterPowerup = false;
        timeLeftFeatherPowerup =0;

        // 粒子特效
        dustParticles.load(Gdx.files.internal("particles/dust.pfx"), Gdx.files.internal("particles"));
    }

    public void setJumping (boolean jumpKeyPressed) {
        switch (jumpState){
            case GROUNDED: //玩家角色站在rock平台上
                if (jumpKeyPressed) {
                    AudioManager.instance.play(Assets.instance.sounds.jump);
                    // 从0开始计算经过的跳跃时间
                    timeJumping = 0;
                    jumpState = JUMP_STATE.JUMP_RISING;
                }
                break;
            case JUMP_RISING: // 上升状态
                if (!jumpKeyPressed)
                    jumpState = JUMP_STATE.FALLING;
                break;
            case FALLING: //掉落状态
            case JUMP_FALLING: // 完成一个跳跃后的下降状态
                if (jumpKeyPressed && hasFeaterPowerup){
                    AudioManager.instance.play(Assets.instance.sounds.jumpWithFeather,1, MathUtils.random(1.0f,1.0f));
                    timeJumping = JUMP_TIME_OFFSET_FLYING;
                    jumpState = JUMP_STATE.JUMP_RISING;
                }
                break;
        }
    }

    public void setFeatherPowerup (boolean pickedUp) {
        hasFeaterPowerup = pickedUp;
        if (pickedUp) {
            timeLeftFeatherPowerup = Constants.ITEM_FEARHER_POWERUP_DURATION;
        }
    }

    public boolean hasFeatherPowerup (){
        return hasFeaterPowerup && timeLeftFeatherPowerup>0;
    }


    @Override
    public void render(SpriteBatch batch) {
        TextureRegion reg = null;

        // 如果激活了飞行效果，设置一个特殊着色
        if (hasFeaterPowerup) {
            batch.setColor(1.0f,0.8f,0.0f,1.0f);
        }

        // 渲染图片
        reg = regHead;
        batch.draw(reg.getTexture(),position.x,position.y,origin.x,origin.y,dimension.x,dimension.y,
                scale.x,scale.y,rotation,reg.getRegionX(),reg.getRegionY(),reg.getRegionWidth(),
                reg.getRegionHeight(),viewDirection == VIEW_DIRECTION.LEFT,false);

        // 重置着色
        batch.setColor(1,1,1,1);
        // 渲染粒子特效
        dustParticles.draw(batch);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (velocity.x !=0) {
            viewDirection = velocity.x < 0 ?VIEW_DIRECTION.LEFT:VIEW_DIRECTION.RIGHT;
        }
        if (timeLeftFeatherPowerup > 0) {
            timeLeftFeatherPowerup -= deltaTime;
            if (timeLeftFeatherPowerup <0){
                // 关闭feather特效
                timeLeftFeatherPowerup = 0;
                setFeatherPowerup(false);
            }
        }
        dustParticles.update(deltaTime);

    }

    @Override
    protected void updateMotionY (float deltaTime) {
        switch (jumpState){
            case GROUNDED:
                jumpState = JUMP_STATE.FALLING;
                if (velocity.x !=0) {
                    dustParticles.setPosition(position.x + dimension.x/2,
                            position.y);
                    dustParticles.start();
                }
                break;
            case JUMP_RISING: // 上升状态
                // 跳跃计时
                timeJumping += deltaTime;
                // 如果没到达最大高度
                if (timeJumping <= JUMP_TIME_MAX) {
                    // 继续上升
                    velocity.y = terminalVelocity.y;
                }
                break;
            case FALLING:
                break;
            case JUMP_FALLING:
                // 跳跃计时
                timeJumping += deltaTime;
                // 如果跳跃按键被释放过快，则应该保持一个最低高度
                if (timeJumping > 0&& timeJumping <= JUMP_TIME_MIN  ){
                    // 依旧上升
                    velocity.y = terminalVelocity.y;
                }
        }

        if (jumpState != JUMP_STATE.GROUNDED){
            dustParticles.allowCompletion();
            super.updateMotionY(deltaTime);

        }

    }
}
