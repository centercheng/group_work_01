package com.packtpub.libgdx.canyonbunny;

import com.packtpub.libgdx.canyonbunny.actor.MainCharacter;

import java.util.HashMap;

public class PublicData {
    public static final int SCREEN_WIDTH = 1920;
    public static final int SCREEN_HEIGHT = 1080;

    public static final char Stage0 = 0;
    public static final char Stage1 = 1;

    public static char currentStage = 100;

    public static float worldSize_pAndPhysic = 50;
    public static float worldSize_shapeAndPhysics = 2f;

    //这些都是老状态，需要新的适配多种移动和战斗状态
    public static int NoLevelMotion =1;

    public static int marioIdle =1;


    public static float MainCharacterUpImpulse = 600f;
    public static float MainCharacterSpeed = 5;

    public static int CameraCharacterControl=5;

    public static HashMap<String,Boolean> MainCharacterState;
    static
    {
        MainCharacterState = new HashMap<String, Boolean>();
        MainCharacterState.put("goLeft",false);
        MainCharacterState.put("goRight",false);
        MainCharacterState.put("repulse",false);
        MainCharacterState.put("slowDown",false);
        MainCharacterState.put("onGround",true);
        MainCharacterState.put("noControl",true);
        MainCharacterState.put("froze",false);
        MainCharacterState.put("jumping",false);
    }

//这两个是物理实体块的类型
    public static String ground = "ground";
    public static String wall = "wall";

    public static MainCharacter mainCharacter;



}
