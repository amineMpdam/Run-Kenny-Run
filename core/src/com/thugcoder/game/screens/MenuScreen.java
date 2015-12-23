package com.thugcoder.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.thugcoder.game.Assets;
import com.thugcoder.game.ads.AdsController;
import com.thugcoder.game.screens.transitions.ScreenTransition;
import com.thugcoder.game.screens.transitions.ScreenTransitionFade;
import com.thugcoder.game.utils.AudioManager;
import com.thugcoder.game.utils.CharacterSkin;
import com.thugcoder.game.utils.Constants;
import com.thugcoder.game.utils.GamePreferences;

/**
 * Created by Landolsi on 20/10/2015.
 */
public class MenuScreen extends AbstractGameScreen {

    // debug
    private final float DEBUG_REBUILD_INTERVAL = 5.0f;
    private Stage stage;
    // menu
    private Image imgBackground;
    private Image imgLogo;
    private Image imgCoins;
    private Image imgKenny;
    private ImageButton btnMenuPlay;
    private ImageButton btnMenuOptions;
    //option
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
    private Skin skinLibgdx;
    private boolean debugEnabled = false;
    private float debugRebuildStage;

    //controls names
    private String play = "play";
    private String options = "options";
    private String kenny = "kenny";
    private String cheese = "cheese";
    private String background = "background";
    private String logo = "logo";

    private TextureAtlas myAtlas ;
    private ImageButton.ImageButtonStyle playButtonStyle, optionButtonStyle;
    private  Skin mySkin ;

    private AdsController adsController;



    public MenuScreen(com.thugcoder.game.screens.DirectedGame game,AdsController adsController) {
        super(game);
        this.adsController = adsController;
        if (adsController.isConnected()) {
            adsController.showBannerAd();
        }
    }


    @Override
    public InputProcessor getInputProcessor () {
        return stage;
    }


    @Override
    public void render(float deltaTime) {

        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
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

    }


    private void rebuildStage() {

        skinLibgdx = new Skin
                (Gdx.files.internal(Constants.SKIN_LIBGDX_UI),
                        new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));


        //Objet table de libGdx dans le cadre de SCENE2D
        // build all layers
        Table layerBackground = buildBackgroundLayer();
        // Table layerObjects = buildObjectsLayer();
        Table layerLogos = buildLogosLayer();
        Table layerControls = buildControlsLayer();
        Table layerOptionsWindow = buildOptionsWindowLayer();
        // assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH,
                Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(layerBackground);
        // stack.add(layerObjects);
        stack.add(layerLogos);
        stack.add(layerControls);
        stage.addActor(layerOptionsWindow);
    }

    private Table buildOptionsWindowLayer() {

        winOptions = new Window("Options", skinLibgdx);
// + Audio Settings: Sound/Music CheckBox and Volume Slider
        winOptions.add(buildOptWinAudioSettings()).row();
// + Character Skin: Selection Box (White, Gray, Brown)
        winOptions.add(buildOptWinSkinSelection()).row();
// + Debug: Show FPS Counter
        winOptions.add(buildOptWinDebug()).row();
// + Separator and Buttons (Save, Cancel)
        winOptions.add(buildOptWinButtons()).pad(10, 0, 10, 0);
// Make options window slightly transparent
        winOptions.setColor(1, 1, 1, 0.8f);
// Hide options window by default
        winOptions.setVisible(false);
        if (debugEnabled) winOptions.debug();
// Let TableLayout recalculate widget sizes and positions
        winOptions.pack();
// Move options window to bottom right corner
        winOptions.setPosition
                (Constants.VIEWPORT_GUI_WIDTH - winOptions.getWidth() - 50,
                        50);
        return winOptions;

    }

    private Table buildControlsLayer() {



        myAtlas = new TextureAtlas(Constants.TEXTURE_ATLAS_UI);
        mySkin = new Skin(myAtlas);
        playButtonStyle = new ImageButton.ImageButtonStyle();
        playButtonStyle.up = mySkin.getDrawable("playup");
        playButtonStyle.down = mySkin.getDrawable("playdn");
        optionButtonStyle = new ImageButton.ImageButtonStyle();
        optionButtonStyle.up = mySkin.getDrawable("optup");
        optionButtonStyle.down= mySkin.getDrawable("optdn");


        Table layer = new Table();
        layer.right();
        // + Play Button

        btnMenuPlay = new ImageButton(playButtonStyle);

        layer.add(btnMenuPlay);
        btnMenuPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onPlayClicked();
            }
        });
        layer.row();
// + Options Button
        btnMenuOptions = new ImageButton(optionButtonStyle);
        layer.add(btnMenuOptions);
        btnMenuOptions.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onOptionsClicked();
            }
        });
        if (debugEnabled) layer.debug();
        return layer;
    }


    private Table buildOptWinSkinSelection() {
        Table tbl = new Table();
// + Title: "Character Skin"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Character Skin", skinLibgdx,
                "default-font", Color.ORANGE)).colspan(2);
        tbl.row();
// + Drop down box filled with skin items
        selCharSkin = new SelectBox<CharacterSkin>(skinLibgdx);
        selCharSkin.setItems(CharacterSkin.values());
        selCharSkin.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onCharSkinSelected(((SelectBox<CharacterSkin>)
                        actor).getSelectedIndex());
            }
        });
        tbl.add(selCharSkin).width(120).padRight(20);
// + Skin preview image
        imgCharSkin = new Image(Assets.instance.kenny.head);
        tbl.add(imgCharSkin).width(50).height(50);
        return tbl;
    }

    private Table buildOptWinDebug() {
        Table tbl = new Table();
// + Title: "Debug"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Debug", skinLibgdx, "default-font",
                Color.RED)).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
// + Checkbox, "Show FPS Counter" label
        chkShowFpsCounter = new CheckBox("", skinLibgdx);
        tbl.add(new Label("Show FPS Counter", skinLibgdx));
        tbl.add(chkShowFpsCounter);
        tbl.row();
        return tbl;
    }

    private Table buildOptWinButtons() {
        Table tbl = new Table();
// + Separator
        Label lbl = null;
        lbl = new Label("", skinLibgdx);
        lbl.setColor(0.75f, 0.75f, 0.75f, 1);
        lbl.setStyle(new Label.LabelStyle(lbl.getStyle()));
        lbl.getStyle().background = skinLibgdx.newDrawable("white");
        tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1);
        tbl.row();
        lbl = new Label("", skinLibgdx);
        lbl.setColor(0.5f, 0.5f, 0.5f, 1);
        lbl.setStyle(new Label.LabelStyle(lbl.getStyle()));
        lbl.getStyle().background = skinLibgdx.newDrawable("white");
        tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
        tbl.row();
// + Save Button with event handler
        btnWinOptSave = new TextButton("Save", skinLibgdx);
        tbl.add(btnWinOptSave).padRight(30);
        btnWinOptSave.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onSaveClicked();
            }
        });
// + Cancel Button with event handler
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


    private void onPlayClicked() {
        adsController.hideBannerAd();
        ScreenTransition transition = ScreenTransitionFade.init(1.5f);
        game.setScreen(new com.thugcoder.game.screens.GameScreen(game,adsController), transition);

    }

    private void onOptionsClicked() {
        loadSettings();
        btnMenuPlay.setVisible(false);
        btnMenuOptions.setVisible(false);
        winOptions.setVisible(true);
    }

    private Table buildLogosLayer() {
        Table layer = new Table();
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(Constants.TEXTURE_ATLAS_UI));
        TextureRegion region = atlas.findRegion(logo);
        layer.left().top();
// + Game Logo
        imgLogo = new Image(region);
        layer.add(imgLogo);
        layer.row().expandY();
        if (debugEnabled) layer.debug();
        return layer;
    }

    private Table buildObjectsLayer() {
        Table layer = new Table();
// + chesse
        imgCoins = new Image(skinLibgdx, cheese);
        layer.addActor(imgCoins);
        imgCoins.setPosition(135, 80);
// + kenny
        imgKenny = new Image(skinLibgdx, kenny);
        layer.addActor(imgKenny);
        imgKenny.setPosition(355, 40);
        return layer;
    }

    private Table buildBackgroundLayer() {
        Table layer = new Table();
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(Constants.TEXTURE_ATLAS_UI));
        TextureRegion region = atlas.findRegion(background);
        // + Background
        imgBackground = new Image(region);
        layer.add(imgBackground);
        return layer;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH,
                Constants.VIEWPORT_GUI_HEIGHT));
        rebuildStage();
    }

    @Override
    public void hide() {
        stage.dispose();
        skinLibgdx.dispose();
    }

    @Override
    public void pause() {

    }

    private Table buildOptWinAudioSettings() {
        Table tbl = new Table();
// + Title: "Audio"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Audio", skinLibgdx, "default-font",
                Color.ORANGE)).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
// + Checkbox, "Sound" label, sound volume slider
        chkSound = new CheckBox("", skinLibgdx);
        tbl.add(chkSound);
        tbl.add(new Label("Sound", skinLibgdx));
        sldSound = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
        tbl.add(sldSound);
        tbl.row();
// + Checkbox, "Music" label, music volume slider
        chkMusic = new CheckBox("", skinLibgdx);
        tbl.add(chkMusic);
        tbl.add(new Label("Music", skinLibgdx));
        sldMusic = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
        tbl.add(sldMusic);
        tbl.row();
        return tbl;
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
    }


    private void saveSettings() {
        GamePreferences prefs = GamePreferences.instance;
        prefs.sound = chkSound.isChecked();
        prefs.volSound = sldSound.getValue();
        prefs.music = chkMusic.isChecked();
        prefs.volMusic = sldMusic.getValue();
        prefs.charSkin = selCharSkin.getSelectedIndex();
        prefs.showFpsCounter = chkShowFpsCounter.isChecked();
        prefs.save();
    }

    private void onCharSkinSelected(int index) {
        CharacterSkin skin = CharacterSkin.values()[index];
        imgCharSkin.setColor(skin.getColor());
    }

    private void onSaveClicked() {
        saveSettings();
        onCancelClicked();
        AudioManager.instance.onSettingsUpdated();
    }

    private void onCancelClicked() {
        btnMenuPlay.setVisible(true);
        btnMenuOptions.setVisible(true);
        winOptions.setVisible(false);
        AudioManager.instance.onSettingsUpdated();
    }
}
