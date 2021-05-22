package com.packtpub.libgdx.canyonbunny;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.packtpub.libgdx.canyonbunny.actor.MainCharacter;

public class MyInputListener extends InputListener {
    MainCharacter mainCharacter;
    public MyInputListener(MainCharacter characterMain){
        this.mainCharacter = characterMain;
    }

    public boolean keyDown(InputEvent event, int keycode) {
        if(!PublicData.MainCharacterState.get("repulse")){
            if(event.getKeyCode()==Input.Keys.D) {
                PublicData.MainCharacterState.replace("goRight",true);
                PublicData.MainCharacterState.replace("goLeft",false);
                PublicData.MainCharacterState.replace("noControl",false);
            }
            if(event.getKeyCode()==Input.Keys.A) {
                PublicData.MainCharacterState.replace("goRight",false);
                PublicData.MainCharacterState.replace("goLeft",true);
                PublicData.MainCharacterState.replace("noControl",false);
            }
            if(event.getKeyCode()==Input.Keys.W&&PublicData.MainCharacterState.get("onGround")) {

                mainCharacter.jump();
                PublicData.MainCharacterState.replace("onGround",false);

            }
        }

        System.out.println("11122333112");


        return super.keyUp(event, keycode);
        //return false;//不知道干嘛的
    }

    @Override
    public boolean keyUp(InputEvent event, int keycode) {
        if(!PublicData.MainCharacterState.get("repulse")){
            if((event.getKeyCode()==Input.Keys.D&&PublicData.MainCharacterState.get("goRight"))||
                    ((event.getKeyCode()==Input.Keys.A&&PublicData.MainCharacterState.get("goLeft")))){
                PublicData.MainCharacterState.replace("goRight",false);
                PublicData.MainCharacterState.replace("goLeft",false);
                PublicData.MainCharacterState.replace("noControl",true);
                PublicData.mainCharacter.getMySimulation().setLinearVelocity(0,PublicData.mainCharacter.getMySimulation().getLinearVelocity().y);
            }

        }


        return super.keyUp(event, keycode);
    }



    @Override
    public boolean mouseMoved(InputEvent event, float x, float y) {//这里鼠标位置应该是像素个数，在desktopluncher里设定1900,1000的情况下基本和最右边是1900，最上边是100
        //当对话框调整的时候，在最左边和最上边的最大值会相应改变
        //System.out.println(x+"   "+y);

        return super.mouseMoved(event, x, y);
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        //0左键 1右键
        System.out.println(button);
        System.out.println(x+"  "+y);
        return super.touchDown(event, x, y, pointer, button);
    }
}
