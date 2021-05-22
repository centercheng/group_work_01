package com.packtpub.libgdx.canyonbunny;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class LoadTiledMap {

    static FixtureDef fixtureDef;
    static BodyDef bodyDef;
    static PolygonShape shape;

    public LoadTiledMap(){

    }

    public static void doScene2(World world, Stage stage, TiledMap tiledMap){
        //地面

//        PhysicalEntityDefine.defineWall();
//
//        bodyDef = PhysicalEntityDefine.getBd();
//        shape = new PolygonShape();
//        fixtureDef = PhysicalEntityDefine.getFd();
//
//        shape.setAsBox(9.8f/ PublicData.worldSize_shapeAndPhysics,2.4f/ PublicData.worldSize_shapeAndPhysics);//worldsize左边的数值表示物理世界中的米
//        bodyDef.position.set(6.7f,6);//物体以中心作为自己的坐标，表示物理世界中的米
//
//
//        fixtureDef.shape = shape;
//
//
//        Body floor = world.createBody(bodyDef);
//        floor.createFixture(fixtureDef).setUserData("ground");//后面的setuserdata可以是任意的object，这里放文本是为了在碰撞时区分不同的刚体


        int rectangleCount = tiledMap.getLayers().get("object-rectangle").getObjects().getCount();
        float mapHeight = (float)Double.parseDouble(tiledMap.getProperties().get("height").toString());
        float positionX;
        float positionY;
        float width;
        float height;
        float angle=0;
        for(int i=0;i<rectangleCount;i++){

            width = (float)Double.parseDouble(tiledMap.getLayers().get("object-rectangle").getObjects().get(i).getProperties().get("width").toString());
            height = (float)Double.parseDouble(tiledMap.getLayers().get("object-rectangle").getObjects().get(i).getProperties().get("height").toString());

            positionX = (float)Double.parseDouble(tiledMap.getLayers().get("object-rectangle").getObjects().get(i).getProperties().get("x").toString())+width*0.5f;
            positionY = (float)Double.parseDouble(tiledMap.getLayers().get("object-rectangle").getObjects().get(i).getProperties().get("y").toString())+height*0.5f;
            if(tiledMap.getLayers().get("object-rectangle").getObjects().get(i).getProperties().get("rotation") != null){
                angle = (float)Double.parseDouble(tiledMap.getLayers().get("object-rectangle").getObjects().get(i).getProperties().get("rotation").toString());

            }else{
                angle = 0;
            }
//            System.out.println("转换");
//            System.out.println(positionX);
//            System.out.println(positionY);
//            System.out.println(width);
//            System.out.println(height);
//            System.out.println(angle);
//            System.out.println("原始");
//            System.out.println(tiledMap.getLayers().get("object-rectangle").getObjects().get(i).getProperties().get("x").toString());
//            System.out.println(tiledMap.getLayers().get("object-rectangle").getObjects().get(i).getProperties().get("y").toString());
//            System.out.println(tiledMap.getLayers().get("object-rectangle").getObjects().get(i).getProperties().get("width").toString());
//            System.out.println(tiledMap.getLayers().get("object-rectangle").getObjects().get(i).getProperties().get("height").toString());
//            System.out.println(tiledMap.getLayers().get("object-rectangle").getObjects().get(i).getProperties().get("rotation").toString());

            PhysicalEntityDefine.createStandardBlock(positionX/PublicData.worldSize_pAndPhysic,positionY/PublicData.worldSize_pAndPhysic,width/PublicData.worldSize_pAndPhysic,height/PublicData.worldSize_pAndPhysic,-angle);
        }

        //PhysicalEntityDefine.createStandardBlock(6.7f,5,9.8f,2.4f,15);//前面都是物理世界的量，最后一个是弧度，物理世界用的弧度





    }
}
