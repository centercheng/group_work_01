package com.packtpub.libgdx.canyonbunny.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.packtpub.libgdx.canyonbunny.game.Assets;
import com.packtpub.libgdx.canyonbunny.screens.transitions.ScreenTransition;
import com.packtpub.libgdx.canyonbunny.screens.transitions.ScreenTransitionFade;
import com.packtpub.libgdx.canyonbunny.util.CharacterSkin;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.packtpub.libgdx.canyonbunny.util.GamePreferences;
import com.packtpub.libgdx.canyonbunny.util.AudioManager;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.Touchable;


/**
 * @auther SHI Zhancheng
 * @create 2021-03-23 22:34
 */
public class MenuScreen extends AbstractGameScreen{
    private static final String TAG = MenuScreen.class.getName();

    private Stage stage;
    private Skin skinCanyonBunny;

    private Skin skinLibgdx;

    // 菜单
    private Image imgBackground;
    private Image imgLogo;
    private Image imgInfo;
    private Image imgCoins;
    private Image imgBunny;
    private Button btnMenuPlay;
    private Button btnMenuOptions;

    // 选项
    private Window winOptions;
    private TextButton btnWinOptSave;
    private TextButton btnWinOptCancel;
    private CheckBox chkSound;
    private Slider sldSound;
    private CheckBox chkMusic;
    private Slider sldMusic;
    private SelectBox<CharacterSkin> selCharSkin;
    private Image imgCharSkin;
    private CheckBox chkShowFpsCounter;
    private CheckBox chkUseMonochromeShader;

    // 调试
    private final float DEBUG_REBUILD_INTERVAL = 5.0f;
    private boolean debugEnabled = false;
    private float debugRebuildStage;


    public MenuScreen(DirectedGame game) {
        super(game);
    }

    @Override
    public InputProcessor getInputProcessor() {
        return stage;
    }

    @Override
    public void render(float deltaTime) {
        // 使用纯黑色清掉屏幕
        Gdx.gl.glClearColor(0.0f,0.0f,0.0f,0.0f);
        // 有触碰或者点击 => 切换屏幕
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (debugEnabled) {
            debugRebuildStage -= deltaTime;
            if (debugRebuildStage <= 0) {
                debugRebuildStage = DEBUG_REBUILD_INTERVAL;
                rebuildStage();
            }
        }
        stage.act(deltaTime);
        stage.draw();
        stage.setDebugAll(true);//********//
//        Table.drawDebug();  // 不再使用
    }

    @Override
    public void resize(int width,int height) {
        stage.getViewport().update(width,height,true);
    }

    @Override
    public void show(){
        stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH,Constants.VIEWPORT_GUI_HEIGHT));
        rebuildStage();
    }

    @Override
    public void hide(){
        stage.dispose();
        skinCanyonBunny.dispose();
    }

    public void pause(){}

    private void rebuildStage() {
        skinCanyonBunny = new Skin(Gdx.files.internal(Constants.SKIN_CANYONBUNNY_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
        skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));

        // 构建所有的层
        Table layerBackground = buildBackgroundLayer();
        Table layerObjects = buildObjectsLayer();
        Table layerLogos = buildLogosLayer();
        Table layerControls = buildControlsLayer();
        Table layerOptionsWindow = buildOptionsWindowLayer();

        // 为菜单屏幕组装舞台
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(layerBackground);
        stack.add(layerObjects);
        stack.add(layerLogos);
        stack.add(layerControls);
        stage.addActor(layerOptionsWindow);
    }

    private Table buildBackgroundLayer() {
        Table layer = new Table();
        // 添加background层
        imgBackground = new Image(skinCanyonBunny, "background");
        layer.add(imgBackground);
        return layer;
    }

    private Table buildObjectsLayer() {
        Table layer = new Table();
        // + Coins
        imgCoins = new Image(skinCanyonBunny, "coins");
        layer.addActor(imgCoins);
        imgCoins.setOrigin(imgCoins.getWidth()/2,imgCoins.getHeight()/2);
        imgCoins.addAction(sequence(
                moveTo(135, -20),
                scaleTo(0,0),
                fadeOut(0),
                delay(2.5f),
                parallel(moveBy(0,100,0.5f,Interpolation.swingOut),
                        scaleTo(1.0f,1.0f,0.25f,Interpolation.linear),alpha(1.0f,0.5f))
        ));
        // + Bunny
        imgBunny = new Image(skinCanyonBunny, "bunny");
        layer.addActor(imgBunny);
        imgBunny.addAction(sequence(
                moveTo(655,510),
                delay(4.0f),
                moveBy(-70,-100,0.5f,Interpolation.fade),
                moveBy(-100,-50,0.5f,Interpolation.fade),
                moveBy(-150,-300,1.0f,Interpolation.elastic)
        ));
        return layer;
    }

    private Table buildLogosLayer() {
        Table layer = new Table();
        layer.left().top();
        // + Game Logo
        imgLogo = new Image(skinCanyonBunny, "logo");
        layer.add(imgLogo);
        layer.row().expandY();
        // + Info Logos
        imgInfo = new Image(skinCanyonBunny, "info");
        layer.add(imgInfo).bottom();
        if (debugEnabled)
            layer.debug();
        return layer;
    }

    private Table buildControlsLayer() {
        Table layer = new Table();
        layer.right().bottom();
        // 添加 Play Button 按钮
        btnMenuPlay = new Button(skinCanyonBunny, "play");
        layer.add(btnMenuPlay);
        btnMenuPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onPlayClicked();
            }
        });
        layer.row();
        // 添加 Options Button 按钮
        btnMenuOptions = new Button(skinCanyonBunny, "options");
        layer.add(btnMenuOptions);
        btnMenuOptions.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onOptionsClicked();
            }
        });
        if (debugEnabled)
            layer.debug();
        return layer;
    }

    /**
     * 创建一个包含音频设置的table层
     * 首先添加一个橘红色的audio标题
     * 另起一行添加用于声音设置的复选框,Sound标签和滑动控件
     * @return
     */
    private Table buildOptWinAudioSettings() {
        Table tbl = new Table();
        // 添加标题: "Audio"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Audio", skinLibgdx, "default-font", Color.ORANGE)).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        // 添加复选框, "Sound" label, 声音音量滑动控件
        chkSound = new CheckBox("", skinLibgdx);
        tbl.add(chkSound);
        tbl.add(new Label("Sound", skinLibgdx));
        sldSound = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
        tbl.add(sldSound);
        tbl.row();
        // 添加复选框, "Music" label, 音乐音量控件
        chkMusic = new CheckBox("", skinLibgdx);
        tbl.add(chkMusic);
        tbl.add(new Label("Music", skinLibgdx));
        sldMusic = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
        tbl.add(sldMusic);
        tbl.row();
        return tbl;
    }

    private Table buildOptWinSkinSelection() {
        Table tbl = new Table();
        // 添加主题: "Character Skin"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Character Skin", skinLibgdx, "default-font", Color.ORANGE)).colspan(2);
        tbl.row();
        // 添加已经初始化的皮肤选项下拉列表控件
        selCharSkin = new SelectBox<CharacterSkin>(skinLibgdx);

        // if 条件语句解决GWT平台运行的反射冲突问题
        if (Gdx.app.getType() == ApplicationType.WebGL) {
            Array<CharacterSkin> items = new Array<CharacterSkin>();
            CharacterSkin[] arr = CharacterSkin.values();
            for (int i = 0; i < arr.length; i++) {
                items.add(arr[i]);
            }
            selCharSkin.setItems(items);
        } else {
            selCharSkin.setItems(CharacterSkin.values());
        }
        selCharSkin.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onCharSkinSelected(((SelectBox<CharacterSkin>) actor).getSelectedIndex());
            }
        });
        tbl.add(selCharSkin).width(120).padRight(20);
        // 添加皮肤预览图片
        imgCharSkin = new Image(Assets.instance.bunny.head);
        tbl.add(imgCharSkin).width(50).height(50);
        return tbl;
    }

    private Table buildOptWinDebug() {
        Table tbl = new Table();
        // 添加标题: "Debug"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Debug", skinLibgdx, "default-font", Color.RED)).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        // 添加复选框, "Show FPS Counter" label
        chkShowFpsCounter = new CheckBox("", skinLibgdx);
        tbl.add(new Label("Show FPS Counter", skinLibgdx));
        tbl.add(chkShowFpsCounter);
        tbl.row();

        // 添加复选框"Use Monochrome Shader" Label
        chkUseMonochromeShader = new CheckBox("",skinLibgdx);
        tbl.add(new Label("Use Monochrome Shader",skinLibgdx));
        tbl.add(chkUseMonochromeShader);
        tbl.row();
        return tbl;
    }

    private Table buildOptWinButtons() {
        Table tbl = new Table();
        // 添加分割线
        Label lbl = null;
        lbl = new Label("", skinLibgdx);
        lbl.setColor(0.75f, 0.75f, 0.75f, 1);
        lbl.setStyle(new LabelStyle(lbl.getStyle()));
        lbl.getStyle().background = skinLibgdx.newDrawable("white");
        tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1);
        tbl.row();
        lbl = new Label("", skinLibgdx);
        lbl.setColor(0.5f, 0.5f, 0.5f, 1);
        lbl.setStyle(new LabelStyle(lbl.getStyle()));
        lbl.getStyle().background = skinLibgdx.newDrawable("white");
        tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
        tbl.row();
        // 添加保存按钮并初始化事件处理器
        btnWinOptSave = new TextButton("Save", skinLibgdx);
        tbl.add(btnWinOptSave).padRight(30);
        btnWinOptSave.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onSaveClicked();
            }
        });
        // 添加Cancel按钮并初始化事件处理器
        btnWinOptCancel = new TextButton("Cancel", skinLibgdx);
        tbl.add(btnWinOptCancel);
        btnWinOptCancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onCancelClicked();
            }
        });
        return tbl;
    }

    private Table buildOptionsWindowLayer() {
        winOptions = new Window("Options", skinLibgdx);
        // + Audio Settings: Sound/Music CheckBox and Volume Slider
        // 添加音频设置：音乐/声音复选框和音量滑动控件
        winOptions.add(buildOptWinAudioSettings()).row();
        // + Character Skin: Selection Box (White, Gray, Brown)
        // 添加角色皮肤：下拉列表（white，gray，brown）
        winOptions.add(buildOptWinSkinSelection()).row();
        // + Debug: Show FPS Counter
        // 添加调试控件：FPS计数器
        winOptions.add(buildOptWinDebug()).row();
        // + Separator and Buttons (Save, Cancel)
        winOptions.add(buildOptWinButtons()).pad(10, 0, 10, 0);

        // Make options window slightly transparent
        // 选项窗口透明化
        winOptions.setColor(1, 1, 1, 0.8f);
        // Hide options window by default
        // 默认隐藏选项窗口
        showOptionsWindow(false,false);
        if (debugEnabled)
            winOptions.debug();
        // Let TableLayout recalculate widget sizes and positions
        // 重新计算控件的尺寸和位置
        winOptions.pack();
        // Move options window to bottom right corner
        winOptions.setPosition(Constants.VIEWPORT_GUI_WIDTH - winOptions.getWidth() - 50, 50);
        return winOptions;
    }

    /**
     * 切换屏幕的方法在这里！
     */
    private void onPlayClicked() {
        ScreenTransition transition = ScreenTransitionFade.init(0.75f);
        game.setScreen(new TestScreen(game),transition);
    }

    private void onOptionsClicked() {
        loadSettings();
        showMenuButtons(false);
        showOptionsWindow(true,true);
    }

    /**
     * 保存Option窗口的配置参数
     */
    private void onSaveClicked() {
        saveSettings();
        onCancelClicked();
    }

    /**
     * 用于隐藏Option窗口并显示菜单控制层的功能
     */
    private void onCancelClicked() {
        showMenuButtons(true);
        showOptionsWindow(false,true);
        AudioManager.instance.onSettingsUpdated();
    }

    /**
     * 更新预览图标的着色
     * @param index
     */
    private void onCharSkinSelected(int index) {
        CharacterSkin skin = CharacterSkin.values()[index];
        imgCharSkin.setColor(skin.getColor());
    }

    private void loadSettings() {
        GamePreferences prefs = GamePreferences.instance;
        prefs.load();
        chkSound.setChecked(prefs.sound);
        sldSound.setValue(prefs.volSound);
        chkMusic.setChecked(prefs.music);
        sldMusic.setValue(prefs.volMusic);
        selCharSkin.setSelectedIndex(prefs.charSkin);
        onCharSkinSelected(prefs.charSkin);
        chkShowFpsCounter.setChecked(prefs.showFpsCounter);
        chkUseMonochromeShader.setChecked(prefs.useMonochromeShader);
    }

    private void saveSettings() {
        GamePreferences prefs = GamePreferences.instance;
        prefs.sound = chkSound.isChecked();
        prefs.volSound = sldSound.getValue();
        prefs.music = chkMusic.isChecked();
        prefs.volMusic = sldMusic.getValue();
        prefs.charSkin = selCharSkin.getSelectedIndex();
        prefs.showFpsCounter = chkShowFpsCounter.isChecked();
        prefs.useMonochromeShader = chkUseMonochromeShader.isChecked();
        prefs.save();
    }

    /**
     * 菜单按钮和选项栏的动画
     * @param visible
     */
    private void showMenuButtons (boolean visible) {
        float moveDuration = 1.0f;
        Interpolation moveEasing = Interpolation.swing;
        float delayOptionsButton = 0.25f;

        float moveX = 300 * (visible ? -1 : 1);
        float moveY = 0 * (visible ? -1 : 1);
        final Touchable touchEnabled = visible ? Touchable.enabled
                : Touchable.disabled;
        btnMenuPlay.addAction(moveBy(moveX, moveY, moveDuration, moveEasing));
        btnMenuOptions.addAction(sequence(delay(delayOptionsButton),
                moveBy(moveX, moveY, moveDuration, moveEasing)));

        SequenceAction seq = sequence();
        if (visible)
            seq.addAction(delay(delayOptionsButton + moveDuration));
        seq.addAction(run(new Runnable() {
            public void run() {
                btnMenuPlay.setTouchable(touchEnabled);
                btnMenuOptions.setTouchable(touchEnabled);
            }
        }));
        stage.addAction(seq);
    }

    private void showOptionsWindow(boolean visible, boolean animated) {
        float alphaTo = visible ? 0.8f : 0.0f;
        float duration = animated ? 1.0f : 0.0f;
        Touchable touchEnabled = visible ? Touchable.enabled
                : Touchable.disabled;
        winOptions.addAction(sequence(touchable(touchEnabled),
                alpha(alphaTo, duration)));
    }
}
