package com.packtpub.libgdx.canyonbunny.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;

/**
 * @auther SHI Zhancheng
 * @create 2021-03-06 14:22
 */
public class Assets implements Disposable, AssetErrorListener {
    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();
    private AssetManager assetManager;

    // 创建内部类变量
    public AssetBunny bunny;
    public AssetRock rock;
    public AssetGoldCoin goldCoin;
    public AssetFeather feather;
    public AssetLevelDecoration levelDecoration;

    // 加载字体
    public AssetFonts fonts;

    // 加载音乐
    public AssetSounds sounds;
    public AssetMusic music;

    public class AssetFonts {
        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultBig;


        public AssetFonts () {
            // 创建三个15磅的位图字体
            defaultSmall = new BitmapFont(
                    Gdx.files.internal("core/assets/images/arial-15.fnt"),true);
            defaultNormal = new BitmapFont(
                    Gdx.files.internal("core/assets/images/arial-15.fnt"),true);
            defaultBig = new BitmapFont(
                    Gdx.files.internal("core/assets/images/arial-15.fnt"),true);

            // 设置字体尺寸[新版本字体设置有改动]
            defaultSmall.getData().setScale(0.75f);
            defaultNormal.getData().setScale(1.0f);
            defaultBig.getData().setScale(2.0f);
            // 为字体激活线性纹理过滤模式
            defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear,TextureFilter.Linear);
            defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear,TextureFilter.Linear);
            defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear,TextureFilter.Linear);
        }
    }

    // 单例类
    private Assets() {

    }

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        // 设置资源管理的错误处理对象
        assetManager.setErrorListener(this);
        // 预加载纹理集资源
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
        // 预加载声音
        assetManager.load("core/assets/sounds/jump.wav", Sound.class);
        assetManager.load("core/assets/sounds/jump_with_feather.wav", Sound.class);
        assetManager.load("core/assets/sounds/pickup_coin.wav", Sound.class);
        assetManager.load("core/assets/sounds/pickup_feather.wav", Sound.class);
        assetManager.load("core/assets/sounds/live_lost.wav", Sound.class);
        // 预加载音乐
        assetManager.load("core/assets/music/keith303_-_brand_new_highscore.mp3", Music.class);
        // 开始加载资源
        assetManager.finishLoading();
        Gdx.app.debug(TAG,"# of assets loaded:"+assetManager.getAssetNames().size);

        for (String a :
                assetManager.getAssetNames()) {
            Gdx.app.debug(TAG, "Asset: "+a);
        }

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

        // 激活平滑纹理过滤
        for (Texture t :
                atlas.getTextures()) {
            t.setFilter(TextureFilter.Linear,TextureFilter.Linear);
        }

        // 创建游戏资源对象
        fonts = new AssetFonts();
        bunny = new AssetBunny(atlas);
        rock = new AssetRock(atlas);
        goldCoin = new AssetGoldCoin(atlas);
        feather = new AssetFeather(atlas);
        levelDecoration = new AssetLevelDecoration(atlas);
        sounds = new AssetSounds(assetManager);
        music = new AssetMusic(assetManager);


    }

    @Override
    public void dispose() {
        assetManager.dispose();
        fonts.defaultSmall.dispose();
        fonts.defaultNormal.dispose();
        fonts.defaultBig.dispose();
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.debug(TAG,"couldn't load asset '" + asset.fileName +
                "'",(Exception)throwable);
    }


    // 资源内部类：玩家角色
    public class AssetBunny {
        public final AtlasRegion head;
        public final Animation animNormal;
        public final Animation animCopterTransform;
        public final Animation animCopterTransformBack;
        public final Animation getAnimCopterRotate;
        public AssetBunny (TextureAtlas atlas) {
            head = atlas.findRegion("bunny_head");

            Array<AtlasRegion> regions = null;
            AtlasRegion region = null;

            // Animation : Bunny Normal
            regions = atlas.findRegions("anim_bunny_normal");
            animNormal = new Animation(1.0f/ 10.0f, regions,Animation.PlayMode.LOOP_PINGPONG);

            // Animation: Bunny Copter - knot earsnn
            regions = atlas.findRegions("anim_bunny_normal");
            animCopterTransform = new Animation(1.0f/ 10.0f, regions);

            // Animation: Bunny Copter - unknot ears
            regions = atlas.findRegions("anim_bunny_normal");
            animCopterTransformBack = new Animation(1.0f/ 10.0f, regions,Animation.PlayMode.LOOP_REVERSED);

            // Animation: Bunny Copter - rotate ears
            regions = new Array<AtlasRegion>();
            regions.add(atlas.findRegion("anim_bunny_copter",4));
            regions.add(atlas.findRegion("anim_bunny_copter",5));
            getAnimCopterRotate = new Animation(1.0f/ 15.0f, regions);
        }
    }

    // 资源内部类：石块
    public class AssetRock {
        public final AtlasRegion edge;
        public final AtlasRegion middle;

        public AssetRock (TextureAtlas atlas) {
            edge = atlas.findRegion("rock_edge");
            middle = atlas.findRegion("rock_middle");
        }
    }

    // 资源内部类：金币道具
    public class AssetGoldCoin {
        public final AtlasRegion goldCoin;
        public final Animation animGoldCoin;


        public AssetGoldCoin (TextureAtlas atlas) {
            goldCoin = atlas.findRegion("item_gold_coin");

            // Animation: Gold Coin
            Array<AtlasRegion> regions =
                    atlas.findRegions("anim_gold_coin");
            AtlasRegion region = regions.first();
            for (int i = 0; i<10;i++)
                regions.insert(0,region);
            animGoldCoin = new Animation(1.0f/20f,regions,
                    Animation.PlayMode.LOOP_PINGPONG);

        }

    }

    // 资源内部类：羽毛道具
    public class AssetFeather {
        public final AtlasRegion feather;

        public AssetFeather (TextureAtlas atlas) {
            feather = atlas.findRegion("item_feather");
        }
    }

    // 资源内部类：装饰资源
    public class AssetLevelDecoration {
        public final AtlasRegion cloud01;
        public final AtlasRegion cloud02;
        public final AtlasRegion cloud03;
        public final AtlasRegion mountainLeft;
        public final AtlasRegion mountainRight;
        public final AtlasRegion waterOverlay;
        public final AtlasRegion carrot;
        public final AtlasRegion goal;

        public AssetLevelDecoration (TextureAtlas atlas) {
            cloud01 = atlas.findRegion("cloud01");
            cloud02 = atlas.findRegion("cloud02");
            cloud03 = atlas.findRegion("cloud03");
            mountainLeft = atlas.findRegion("mountain_left");
            mountainRight = atlas.findRegion("mountain_right");
            waterOverlay = atlas.findRegion("water_overlay");
            carrot = atlas.findRegion("carrot");
            goal = atlas.findRegion("goal");
        }
    }

    // 资源内部类：音乐
    public class AssetSounds {
        public final Sound jump;
        public final Sound jumpWithFeather;
        public final Sound pickupCoin;
        public final Sound pickupFeather;
        public final Sound liveLost;

        public AssetSounds (AssetManager am) {
            jump = am.get("core/assets/sounds/jump.wav", Sound.class);
            jumpWithFeather = am.get("core/assets/sounds/jump_with_feather.wav", Sound.class);
            pickupCoin = am.get("core/assets/sounds/pickup_coin.wav", Sound.class);
            pickupFeather = am.get("core/assets/sounds/pickup_feather.wav", Sound.class);
            liveLost = am.get("core/assets/sounds/live_lost.wav", Sound.class);
        }
    }

    public class AssetMusic {
        public final Music song01;

        public AssetMusic(AssetManager am) {
            song01 = am.get("core/assets/music/keith303_-_brand_new_highscore.mp3", Music.class);
        }
    }
}
