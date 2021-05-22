package com.packtpub.libgdx.canyonbunny.stage;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.packtpub.libgdx.canyonbunny.CameraFocus;
import com.packtpub.libgdx.canyonbunny.LoadTiledMap;
import com.packtpub.libgdx.canyonbunny.MyInputListener;
import com.packtpub.libgdx.canyonbunny.PublicData;
import com.packtpub.libgdx.canyonbunny.actor.MainCharacter;
import com.packtpub.libgdx.canyonbunny.game.Assets;

public class Stage1 extends Stage {
    MainCharacter mainCharacter;

    World world;


    TextureRegion currentFrame;//当前该播放的图片（这个类是从texture中切一块出来）

    Box2DDebugRenderer boxRender;//物理世界绘制器
    OrthographicCamera cameraPhysic;//物理世界相机
    OrthographicCamera stageCamera;//舞台用的摄像机

    CameraFocus cameraFocus;

    char k;

    TiledMap tiledMap;
    OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;


    public Stage1(InputMultiplexer inputMultiplexer, World world){


        inputMultiplexer.addProcessor(this);//加入监听,接受来自最外层的信息。最外层的用户动作信息通过这个分配到各个stage

        this.world = world;

       // scene1 = new Scene1(world,this);//**************************在scene内部创建actor并且添加到舞台
        //看起来actor内部的draw会统一调用，用同一个batch把所有actor要画的东西都画了



        mainCharacter = new MainCharacter(world,0*PublicData.worldSize_pAndPhysic,3.8f*PublicData.worldSize_pAndPhysic);
        PublicData.mainCharacter = mainCharacter;

        boxRender = new Box2DDebugRenderer();//绘制虚拟形状
        cameraPhysic = new OrthographicCamera();//摄像机
        cameraPhysic.setToOrtho(false, (PublicData.SCREEN_WIDTH)/PublicData.worldSize_pAndPhysic,(PublicData.SCREEN_HEIGHT)/PublicData.worldSize_pAndPhysic);//这个是摄像机的框的大小，摄像机取景后还要经过最外层界面的压缩得到最终效果

        stageCamera = (OrthographicCamera) this.getCamera();
        stageCamera.setToOrtho(false, PublicData.SCREEN_WIDTH,PublicData.SCREEN_HEIGHT);

        cameraFocus = new CameraFocus(cameraPhysic,mainCharacter, stageCamera);

        this.addActor(mainCharacter);

        this.addListener(new MyInputListener(mainCharacter));//这个监听是对从最外层传入的输入信息的响应


        tiledMap = new TmxMapLoader().load("core/assets/map begin/map begin.tmx");
        //System.out.println("object x = "+tiledMap.getLayers().get("object1").getObjects().get(0).getProperties().get("x"));
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        LoadTiledMap.doScene2(world,this,tiledMap);

//        Texture texture = new Texture(Gdx.files.internal("ground.png"));//加载九宫格图片
//        land = new Image(texture);//image和texture区别？？
//
//        land.setSize(15*50, 2.5f*50);
//        land.setPosition(-13f*50, 7.3f*50);//舞台坐标是左下角
//        this.addActor(land);
        //这个是因为图本身很大，有些部分是透明的，所以看起来位置不对




    }

    @Override
    public void act() {
        world.step(1.0f/60.0f, 6,6);//进行一次物理世界模拟，第一个 时间步，和系统时间同步；速度和位置的模拟精度，越大越准
        //上面这个要放在各个stage里，因为不到这个stage运行的时候stage对应的物理世界是不动的



        //调整相机
        if(k==0){//初始设定相机中心点位置，相机最开始默认摆在左下角，其中心点是（19，10），相机的position成员是相机的中心点
            cameraFocus.focusOn(0,5,1);
            //cameraFocus1.focusOn1(0*Data.worldSize_pAndPhysic,5*Data.worldSize_pAndPhysic,1);
        }
        k++;
        if(k>1){//防治k溢出归零再次重置相机位置
            k=1;
        }

        cameraFocus.innerBoundary(6,7,19,6);//进行两个世界相机的调整，在屏幕中划出一个区域作为触发相机调整的边框

        cameraPhysic.update();//主角内存图片的相机就不用了，应为舞台自己有一个相机，舞台内新做的内存机替换了已有的，所以stage类每次会自动调用舞台新作的内存相机的update


        super.act();
    }

    @Override
    public void draw() {


        orthogonalTiledMapRenderer.setView(this.stageCamera);
        orthogonalTiledMapRenderer.render();

        boxRender.render(world, cameraPhysic.combined);//结合相机进行绘制

        super.draw();
    }
}
