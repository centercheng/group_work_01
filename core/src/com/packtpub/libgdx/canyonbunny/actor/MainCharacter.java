package com.packtpub.libgdx.canyonbunny.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.packtpub.libgdx.canyonbunny.PhysicalEntityDefine;
import com.packtpub.libgdx.canyonbunny.PublicData;
import com.packtpub.libgdx.canyonbunny.game.Assets;


public class MainCharacter extends Actor {
    World world;
    Body mySimulation;
    FixtureDef myFixtureDef;
    BodyDef myBodyDef;
    PolygonShape shape;


    float statetime;//用于替换主角动作图片的标记
    Texture texture;//装载图片的
    Animation aniRight;
    Animation aniLeft;
    Animation aniIdle;
    Animation test;

    //老状态
    float state1;
    float state;
    boolean ifStill;


    Sprite sprite;
    Texture texture1;

    TextureRegion currentFrame;//当前该播放的图片（这个类是从texture中切一块出来）
    public MainCharacter(World world, float x, float y){
        //获得物理世界引用
        this.world = world;

        //创建主角物理模拟
        PhysicalEntityDefine.defineCharacter();
        myBodyDef = PhysicalEntityDefine.getBd();
        myFixtureDef = PhysicalEntityDefine.getFd();



        shape = new PolygonShape();
       // shape.setRadius(1.5f/ PublicData.worldSize_shapeAndPhysics);//worldsize左边的数表示物理世界中的米
        shape.setAsBox(1f/PublicData.worldSize_shapeAndPhysics,1.5f/PublicData.worldSize_shapeAndPhysics);
        myFixtureDef.shape = shape;

        myBodyDef.position.set(10,30.9f);//这个表示物理世界中的米

        mySimulation = world.createBody(myBodyDef);
        System.out.print("shitiititt");
        mySimulation.createFixture(myFixtureDef).setUserData("main character");


        //内存显示区
        this.setX(x);
        this.setY(y);
        this.statetime = 0;
        prepareForPicture();


        //状态设置 这是老状态，需要新的
        state = PublicData.marioIdle;
        state1 = PublicData.NoLevelMotion;
        ifStill = true;



    }

    public void prepareForPicture() {


        test = Assets.instance.bunny.animNormal;


    }

    @Override
    public void act(float delta) {
        super.act(delta);

        //动作状态改变
        if(PublicData.MainCharacterState.get("goLeft")){
            mySimulation.setLinearVelocity(-PublicData.MainCharacterSpeed,mySimulation.getLinearVelocity().y);

        }
        if(PublicData.MainCharacterState.get("goRight")){
            mySimulation.setLinearVelocity(PublicData.MainCharacterSpeed,mySimulation.getLinearVelocity().y);
        }





        statetime += delta;//用于调整主角要展示的图片的时间标记,****************这里调整了，用了传入的delta，看看行不行

//        if(state == PublicData.marioLeft) {
//            currentFrame = (TextureRegion)aniLeft.getKeyFrame(statetime, true);//根据当前“时间”提供该播放的图片，后面的true是循环播放
//        }else if (state == PublicData.marioRight) {
//            currentFrame = (TextureRegion)aniRight.getKeyFrame(statetime, true);
//        }else if (state == PublicData.marioIdle) {
//            currentFrame = (TextureRegion)aniIdle.getKeyFrame(statetime, true);
//        }
        currentFrame = (TextureRegion)test.getKeyFrame(statetime,true);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(currentFrame, (mySimulation.getPosition().x-0.7f)*50f, (mySimulation.getPosition().y-0.45f)*50f);//把模拟物体的坐标拿出来，转换一下画上去

      //  batch.draw(currentFrame, 125, 200);
        //batch.draw(texture1,(mySimulation.getPosition().x-0.7f)*50f, (mySimulation.getPosition().y-0.45f)*50f);

    }

    public void jump(){
        mySimulation.applyLinearImpulse(new Vector2(0,PublicData.MainCharacterUpImpulse),mySimulation.getPosition(),true);
        System.out.println("678");
    }

    public Body getMySimulation(){
        return mySimulation;
    }


    //public Vector2 getPosition(){
    //    return new Vector2(this.getX(),this.getY());
    //}

    public Vector2 getPositionInSimulation(){
        return mySimulation.getPosition();
    }

}
