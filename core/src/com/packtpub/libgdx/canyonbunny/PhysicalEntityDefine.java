package com.packtpub.libgdx.canyonbunny;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class PhysicalEntityDefine {//定义了各种物理实体，在各个类中需要加入物理实体时使用
    static BodyDef bd =new BodyDef();
    static FixtureDef fd = new FixtureDef();
    static World world;

    public PhysicalEntityDefine(){

    }

    public static void boundWorld(World w){
        world = w;
    }

//这个函数单位是米
    public static void createStandardBlock(float positionX, float positionY, float width, float height, float angle){
        //做主体块
        BodyDef bd1 = new BodyDef();
        bd1.type = BodyDef.BodyType.StaticBody;
        bd1.active = true;
        bd1.allowSleep = true;
        bd1.awake = true;
        bd1.bullet = false;
        bd1.angle = (float)(angle*Math.PI/180);



        bd1.position.set(positionX,positionY);

        Vector2 oldVector;
        Vector2 newVector;
        Vector2 translate;


        oldVector = new Vector2(-(0.5f*width),0.5f*height);

        newVector = new Vector2((float)(Math.cos(bd1.angle)*oldVector.x-Math.sin(bd1.angle)*oldVector.y),(float)(Math.sin(bd1.angle)*oldVector.x+Math.cos(bd1.angle)*oldVector.y));

        translate = newVector.sub(oldVector);

        bd1.position.set(bd1.position.sub(translate));




        System.out.println(bd1.angle);

        FixtureDef fd1 = new FixtureDef();
        fd1.density = 1;
        fd1.friction = 1f;
        fd1.restitution = 0;
        //碰撞默认全碰撞
        //sensor默认否
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width-0.1f)/PublicData.worldSize_shapeAndPhysics,height/PublicData.worldSize_shapeAndPhysics);
        fd1.shape = shape;

        Body b1 = world.createBody(bd1);
        b1.createFixture(fd1).setUserData(PublicData.ground);


        //做侧面的墙小块
//        fd1.friction = 1;
//        shape.setAsBox(0.01f,height/PublicData.worldSize_shapeAndPhysics);
//        fd1.shape = shape;
//
//
//
//        bd1.position.set((float)(positionX+0.5f*width*Math.cos(bd1.angle)),(float)(positionY+Math.sin(bd1.angle)*0.5*width));
//        bd1.position.set(bd1.position.sub(translate));
//        Body b2 = world.createBody(bd1);
//        b2.createFixture(fd1).setUserData(PublicData.wall);
//
//
//
//
//
//        bd1.position.set((float)(positionX-0.5f*width*Math.cos(bd1.angle)),(float)(positionY-Math.sin(bd1.angle)*0.5*width));
//        bd1.position.set(bd1.position.sub(translate));
//        Body b3 = world.createBody(bd1);
//        b3.createFixture(fd1).setUserData(PublicData.wall);








    }


    public static void defineWall(){

        bd.type = BodyDef.BodyType.StaticBody;


        fd.restitution = 0; //弹性，越大越弹
        fd.friction = 1f;
    }


    public static void defineCharacter(){

        bd.type = BodyDef.BodyType.DynamicBody;
        bd.active = true;//激活
        bd.allowSleep = false;//是否允许在不运动时停止模拟，停止模拟后之前施加的力也会消失
        bd.bullet = true;//不需要用于高速运动的高精度检测
        bd.fixedRotation = true;//禁止刚体旋转
        bd.linearDamping = 0;//线性阻尼，类似空气摩擦，在很黏的水里运行



        fd.restitution = 0f;//主角有点弹性，可以调整地面的软硬设置弹簧区域（0-1）
        fd.friction = 1f;//摩擦力（0-1）
        fd.density = 40;

    }

    public static BodyDef getBd(){ return bd; }

    public static FixtureDef getFd(){
        return fd;
    }
}
