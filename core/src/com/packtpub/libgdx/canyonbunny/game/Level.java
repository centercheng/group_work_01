package com.packtpub.libgdx.canyonbunny.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.canyonbunny.game.objects.*;

import java.awt.*;

/**
 * @auther SHI Zhancheng
 * @create 2021-03-09 15:24
 */
public class Level {
    public static final String TAG = Level.class.getName();

    public enum BLOCK_TYPE {
        EMPTY(0,0,0),   //黑色
        ROCK(0,255,0),  //绿色
        PLAYER_SPAWNPOINt(255,255,255), // 白色
        ITEM_FEATHER(255,0 ,255), // 紫色
        ITEM_GOLD_COIN(255,255,0); // 黄色

        private int color;

        private BLOCK_TYPE(int r,int g,int b) {
            color = r << 24 |g <<16 |b << 8| 0xff;
        }

        public Boolean sameColor (int color) {
            return this.color == color;
        }

        public int getColor () {
            return color;
        }
    }

    // 游戏对象
    public Array<Rock> rocks;

    // 装饰对象
    public Clouds clouds;
    public Mountains mountains;
    public WaterOverlay waterOverlay;

    public BunnyHead bunnyHead;
    public Array<GoldCoin> goldCoins;
    public Array<Feather> feathers;


    public Level (String filename) {
        init(filename);
    }

    private void init(String filename){
        // 游戏玩家
        bunnyHead = null;

        // 游戏对象
        rocks = new Array<Rock>();
        goldCoins = new Array<GoldCoin>();
        feathers = new Array<Feather>();

        // 加载关卡图片
        Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
        // 从图片的左上角逐行扫描至右下角
        int lastPixel = -1;
        for (int pixelY = 0; pixelY < pixmap.getHeight();pixelY++) {
            for (int pixelX = 0; pixelX < pixmap.getWidth();pixelX++) {
                AbstractGameObject obj = null;
                float offsetHeight = 0;
                // 计算底部高度
                float baseHeight = pixmap.getHeight() - pixelY;
                // 获取当前位置的RGBA颜色
                int currentPixel = pixmap.getPixel(pixelX,pixelY);
                // 找到与当前位置（x，y）颜色匹配的代码块并创建相应的对象

                // 空白空间
                if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
                    // 什么都不做
                }
                // rock 对象
                else if (BLOCK_TYPE.ROCK.sameColor(currentPixel)) {
                    if (lastPixel != currentPixel){
                        obj = new Rock();
                        float heightIncreaseFactor = 0.25f;
                        offsetHeight = -2.5f;
                        obj.position.set(pixelX,baseHeight * obj.dimension.y * heightIncreaseFactor + offsetHeight);
                        rocks.add((Rock)obj);
                    }
                    else {
                        rocks.get(rocks.size -1).increaseLength(1);
                    }
                }
                // 玩家初始位置
                else if (BLOCK_TYPE.PLAYER_SPAWNPOINt.sameColor(currentPixel)) {
                    obj = new BunnyHead();
                    offsetHeight = -3.0f;
                    obj.position.set(pixelX,baseHeight * obj.dimension.y+offsetHeight);
                    bunnyHead = (BunnyHead)obj;
                }
                // feather 对象
                else if (BLOCK_TYPE.ITEM_FEATHER.sameColor(currentPixel)) {
                    obj = new Feather();
                    offsetHeight = -1.5f;
                    obj.position.set(pixelX,baseHeight * obj.dimension.y+offsetHeight);
                    feathers.add((Feather)obj);
                }
                // 金币对象
                else if (BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel)) {
                    obj = new GoldCoin();
                    offsetHeight = -1.5f;
                    obj.position.set(pixelX,baseHeight * obj.dimension.y+offsetHeight);
                    goldCoins.add((GoldCoin)obj);
                }
                // 未定义颜色或对象
                else {
                    int r = 0xff & (currentPixel >> 24);// red通道
                    int g = 0xff & (currentPixel >> 16);// green通道
                    int b = 0xff & (currentPixel >> 8);// blue通道
                    int a = 0xff & currentPixel ;// alpha通道
                    Gdx.app.error(TAG, "Unknow object at x<"+pixelX+"> y<"+pixelY+
                            "> :r<"+r+"> :g<"+g+"> :b<"+b+"> :a<"+a+">");

                }
                lastPixel = currentPixel;
            }
        }

        // 装饰
        clouds = new Clouds(pixmap.getWidth());
        clouds.position.set(0,2);
        mountains = new Mountains(pixmap.getWidth());
        mountains.position.set(-1,-1);
        waterOverlay = new WaterOverlay(pixmap.getWidth());
        waterOverlay.position.set(0,-3.75f);

        // 释放内存
        pixmap.dispose();
        Gdx.app.debug(TAG,"Level'"+filename+"'load");
    }

    public void render (SpriteBatch batch) {
        // 渲染mountains
        mountains.render(batch);

        // 渲染Rocks
        for (Rock rock :
                rocks) {
            rock.render(batch);
        }
        // 渲染gold coins
        for (GoldCoin coins :
                goldCoins) {
            coins.render(batch);
        }

        // 渲染feathers
        for (Feather feather :
                feathers) {
            feather.render(batch);
        }

        // 渲染bunny head
        bunnyHead.render(batch);

        // 渲染water Overlay
        waterOverlay.render(batch);

        // 渲染Clouds
        clouds.render(batch);

    }

    public void update(float deltaTime) {
        bunnyHead.update(deltaTime);
        for (Rock rock :
                rocks) {
            rock.update(deltaTime);
        }
        for (GoldCoin coins :
                goldCoins) {
            coins.update(deltaTime);
        }
        for (Feather feather :
                feathers) {
            feather.update(deltaTime);
        }
        clouds.update(deltaTime);
    }
}
