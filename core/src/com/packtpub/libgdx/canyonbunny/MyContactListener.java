package com.packtpub.libgdx.canyonbunny;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class MyContactListener implements ContactListener{
    @Override
    public void beginContact(Contact contact) {//出现新接触点
        //通过把这个my contact listener 添加到word里，进行监听，可以在函数里通过contact获得碰撞对象的fixturedefine（里面可以通过usedata设置编号来区分对象）
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        Object faData = fa.getUserData();
        Object fbData = fb.getUserData();
        

 //跳跃使用一个跳跃标记，踩到地面跳跃标记为2（二段跳）,摁一下上键就减一，地图边上用普通块贴一下，分为地面块和普通块

//两个指定物体碰撞后
        if( ((faData.equals("ground")|| fbData.equals("ground"))&&(faData.equals("main character") || fbData.equals("main character")))){
            PublicData.MainCharacterState.replace("onGround",true);
            PublicData.MainCharacterState.replace("repulse",false);
        }



        if( ((faData.equals("back")|| fbData.equals("back"))&&(faData.equals("main character") || fbData.equals("main character")))){
            PublicData.MainCharacterState.replace("repulse",true);
            PublicData.MainCharacterState.replace("goLeft",false);
            PublicData.MainCharacterState.replace("goRight",false);
            if(faData.equals("main character")){
                Vector2 direction = fb.getBody().getPosition().sub(fa.getBody().getPosition());

                if(direction.y>=0){
                    PublicData.mainCharacter.getMySimulation().applyLinearImpulse(new Vector2(0,-400),PublicData.mainCharacter.getMySimulation().getPosition(),true);
                    System.out.println("0"+"1");
                }else{
                    PublicData.mainCharacter.getMySimulation().applyLinearImpulse(new Vector2(0,400),PublicData.mainCharacter.getMySimulation().getPosition(),true);
                    System.out.println("`"+"1");
                }

                if(direction.x>=0){
                    //PublicData.mainCharacter.getMySimulation().applyLinearImpulse(new Vector2(400,0),PublicData.mainCharacter.getMySimulation().getPosition(),true);
                }else{
                    //PublicData.mainCharacter.getMySimulation().applyLinearImpulse(new Vector2(-400,0),PublicData.mainCharacter.getMySimulation().getPosition(),true);
                }


                System.out.println(direction+"1");
            }else{
                Vector2 direction = fa.getBody().getPosition().sub(fb.getBody().getPosition());

                if(direction.y>=0){
                    PublicData.mainCharacter.getMySimulation().setLinearVelocity(PublicData.mainCharacter.getMySimulation().getLinearVelocity().x,-5);
                    //PublicData.mainCharacter.getMySimulation().applyLinearImpulse(new Vector2(0,-400),PublicData.mainCharacter.getMySimulation().getPosition(),true);
                    System.out.println("0"+"2");
                }else{
                    //PublicData.mainCharacter.getMySimulation().applyLinearImpulse(new Vector2(0,400),PublicData.mainCharacter.getMySimulation().getPosition(),true);
                    PublicData.mainCharacter.getMySimulation().setLinearVelocity(PublicData.mainCharacter.getMySimulation().getLinearVelocity().x,5);
                    System.out.println("1"+"2");
                }

                if(direction.x>=0){
                    PublicData.mainCharacter.getMySimulation().setLinearVelocity(-5,PublicData.mainCharacter.getMySimulation().getLinearVelocity().y);
                    //PublicData.mainCharacter.getMySimulation().applyLinearImpulse(new Vector2(400,0),PublicData.mainCharacter.getMySimulation().getPosition(),true);
                }else{
                    PublicData.mainCharacter.getMySimulation().setLinearVelocity(5,PublicData.mainCharacter.getMySimulation().getLinearVelocity().y);
                    //PublicData.mainCharacter.getMySimulation().applyLinearImpulse(new Vector2(-400,0),PublicData.mainCharacter.getMySimulation().getPosition(),true);
                }

                System.out.println(direction+"2");
            }
        }

        //之后也可以获得碰撞点坐标等
        //还可以把刚体设置为传感器，就是会检测到碰撞但是物理世界里不与其它刚体产生相互作用
    }

    @Override
    public void endContact(Contact contact) {//新接触点消失
//        Fixture fa = contact.getFixtureA();
////        Fixture fb = contact.getFixtureB();
////
////        Object faData = fa.getUserData();
////        Object fbData = fb.getUserData();
////
//////两个指定物体碰撞后
////    if( ((faData.equals("wall")|| fbData.equals("wall"))&&(faData.equals("main character") || fbData.equals("main character")))){
////
////        PublicData.MainCharacterState.replace("onGround",true);
////    }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {//aabb框之间出现新接触点

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {//aabb框之间的新接触点消失

    }
}
