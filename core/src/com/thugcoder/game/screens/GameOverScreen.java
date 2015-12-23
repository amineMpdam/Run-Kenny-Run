package com.thugcoder.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.thugcoder.game.ads.AdsController;
import com.thugcoder.game.utils.Constants;
import com.thugcoder.game.utils.GamePreferences;

/**
 * Created by Landolsi on 23/10/2015.
 */
public class GameOverScreen extends AbstractGameScreen {

    private Stage stage;
    private Skin skinLibgdx;
    private Button retryButton;
    private Button menuButton;
    private Image imgBackground;
    private String background = "grave";
    private TextureAtlas myAtlas;
    private ImageButton.ImageButtonStyle retryButtonStyle, menuButtonStyle;
    private Skin mySkin;
    private int actualScore, gameHighScore;
    private GamePreferences preferences;

    private Label scoreOneLabel;
    private Label separatorOne;
    private Label scoreTwoLabel;
    private Label seperatorTwo;
    private AdsController adsController;


    public GameOverScreen(com.thugcoder.game.screens.DirectedGame game, int actualScore) {
        super(game);
        gameHighScore = actualScore;
    }

    public GameOverScreen(com.thugcoder.game.screens.DirectedGame game, int actualScore, AdsController adsController) {
        super(game);
        this.adsController = adsController;
        gameHighScore = actualScore;
        if (adsController.isConnected()) {
            adsController.showBannerAd();
        }
    }

    @Override
    public InputProcessor getInputProcessor() {

        return stage;
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH,
                Constants.VIEWPORT_GUI_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        if (adsController != null) {
            adsController.showBannerAd();
        }
        rebuildStage();

    }

    private void rebuildStage() {

        skinLibgdx = new Skin
                (Gdx.files.internal(Constants.SKIN_LIBGDX_UI),
                        new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));


        // build all layers
        Table layerBackground = buildBackgroundLayer();
        Table layerControls = buildControlsLayer();
        Table layerScores = buildScoresLayer();
        // assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();

        stack.setSize(Constants.VIEWPORT_GUI_WIDTH,
                Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(layerBackground);
        stack.add(layerControls);
        stack.add(layerScores);
        stage.addActor(stack);
    }

    private Table buildControlsLayer() {

        Table layer = new Table();
        layer.left().top();

        myAtlas = new TextureAtlas(Constants.TEXTURE_ATLAS_UI);
        mySkin = new Skin(myAtlas);
        retryButtonStyle = new ImageButton.ImageButtonStyle();
        retryButtonStyle.up = mySkin.getDrawable("retryup");
        retryButtonStyle.down = mySkin.getDrawable("retrydn");
        menuButtonStyle = new ImageButton.ImageButtonStyle();
        menuButtonStyle.up = mySkin.getDrawable("menup");
        menuButtonStyle.down = mySkin.getDrawable("menudn");


        // + Play Button

        menuButton = new Button(retryButtonStyle);
        menuButton.setHeight(150f);
        menuButton.setWidth(300f);
        layer.add(menuButton);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onRetryClicked();
            }
        });
        layer.row();
// + Options Button
        retryButton = new Button(menuButtonStyle);
        layer.add(retryButton);
        retryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onMainMenuClicked();
            }
        });

        return layer;
    }


    private Table buildScoresLayer() {
        Table layer = new Table();
        layer.center().left();

        int[] highScores;

        skinLibgdx = new Skin
                (Gdx.files.internal(Constants.SKIN_LIBGDX_UI),
                        new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));

        preferences = GamePreferences.instance;
        highScores = preferences.getScores();
        //TODO : Test these modifications
        separatorOne = new Label("Your Score is : ", skinLibgdx, Constants.myStyle);
        seperatorTwo = new Label("The High Score is : ", skinLibgdx, Constants.myStyle);
        scoreOneLabel = new Label(String.valueOf(gameHighScore), skinLibgdx, Constants.myStyle);
        scoreTwoLabel = new Label(String.valueOf(highScores[4]), skinLibgdx, Constants.myStyle);


        layer.add(separatorOne);
        layer.add(scoreOneLabel);
        layer.row();
        layer.add(seperatorTwo);
        layer.add(scoreTwoLabel);
        layer.row();


        return layer;
    }

    private void onMainMenuClicked() {
        game.setScreen(new MenuScreen(game, adsController));
    }

    private void onRetryClicked() {
        game.setScreen(new com.thugcoder.game.screens.GameScreen(game));
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
    public void hide() {
        stage.dispose();
        skinLibgdx.dispose();
        adsController.hideBannerAd();


    }

    @Override
    public void pause() {

    }
}
