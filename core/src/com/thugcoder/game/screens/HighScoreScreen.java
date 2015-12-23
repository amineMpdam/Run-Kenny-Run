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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by Landolsi on 17/11/2015.
 */

public class HighScoreScreen extends AbstractGameScreen {
    private final String background = "congRats";
    private Stage stage;
    private Skin skinLibgdx;
    private TextField playerNameTextFeild;
    private Label scoreLabel;
    private Button menuButton;
    private Image imgBackground;
    private int position = 0;

    private Label scoreOneLabel;
    private Label playerOne;
    private Label scoreTwoLabel;
    private Label playerTwo;
    private Label scoreThreeLabel;
    private Label playerThree;
    private Label scoreFourLabel;
    private Label playerFour;
    private Label scoreFiveLabel;
    private Label playerFive;

    private TextureAtlas myAtlas;
    private ImageButton.ImageButtonStyle retryButtonStyle, menuButtonStyle;
    private Skin mySkin;
    private int currentScore;
    private com.thugcoder.game.ads.AdsController adsController;
    private com.thugcoder.game.utils.GamePreferences preferences;
    private String playerNameString  ;

    public HighScoreScreen(DirectedGame game, int score) {
        super(game);
        currentScore = score;
    }

    public HighScoreScreen(DirectedGame game, int score, com.thugcoder.game.ads.AdsController adsController) {
        super(game);
        this.adsController = adsController;
        currentScore = score;
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
        stage = new Stage(new StretchViewport(com.thugcoder.game.utils.Constants.VIEWPORT_GUI_WIDTH,
                com.thugcoder.game.utils.Constants.VIEWPORT_GUI_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        rebuildStage();

    }

    private void rebuildStage() {

        skinLibgdx = new Skin
                (Gdx.files.internal(com.thugcoder.game.utils.Constants.SKIN_LIBGDX_UI),
                        new TextureAtlas(com.thugcoder.game.utils.Constants.TEXTURE_ATLAS_LIBGDX_UI));

        // build all layers
        Table layerBackground = buildBackgroundLayer();
        Table layerControls = buildControlsLayer();
        Table layerHighScores = buildHighScoresTableLayer();
        // assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();

        stack.setSize(com.thugcoder.game.utils.Constants.VIEWPORT_GUI_WIDTH,
                com.thugcoder.game.utils.Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(layerBackground);
        stack.add(layerControls);
        stack.add(layerHighScores);
        stage.addActor(stack);
    }

    //This method
    private Table buildHighScoresTableLayer() {
        skinLibgdx = new Skin
                (Gdx.files.internal(com.thugcoder.game.utils.Constants.SKIN_LIBGDX_UI),
                        new TextureAtlas(com.thugcoder.game.utils.Constants.TEXTURE_ATLAS_LIBGDX_UI));

        preferences = com.thugcoder.game.utils.GamePreferences.instance;
        int[] highScores = preferences.getScores();
        String[] highRollers = preferences.getPlayerNames();

        String[] playersNames = preferences.getPlayerNames();

        for (int i = 0; i < highScores.length; i++) {
            if (highScores[i] == currentScore) {
                position = i;
            }
        }

        Table layer = new Table();
        layer.center().right();
        layer.pad(0f, 0f, 0f, 50f);
        myAtlas = new TextureAtlas(com.thugcoder.game.utils.Constants.TEXTURE_ATLAS_UI);
        mySkin = new Skin(myAtlas);
        Label separator = new Label("  :  ", skinLibgdx, com.thugcoder.game.utils.Constants.myStyle);
        Label separatorOne = new Label("  :  ", skinLibgdx, com.thugcoder.game.utils.Constants.myStyle);
        Label separatorTwo = new Label("  :  ", skinLibgdx, com.thugcoder.game.utils.Constants.myStyle);
        Label separatorThree = new Label("  :  ", skinLibgdx, com.thugcoder.game.utils.Constants.myStyle);
        Label separatorFour = new Label("  :  ", skinLibgdx, com.thugcoder.game.utils.Constants.myStyle);


        scoreOneLabel = new Label(String.valueOf(highScores[4]), skinLibgdx, com.thugcoder.game.utils.Constants.myStyle);
        playerOne = new Label(playersNames[4], skinLibgdx, com.thugcoder.game.utils.Constants.myStyle);
        scoreTwoLabel = new Label(String.valueOf(highScores[3]), skinLibgdx, com.thugcoder.game.utils.Constants.myStyle);
        playerTwo = new Label(playersNames[3], skinLibgdx, com.thugcoder.game.utils.Constants.myStyle);
        scoreThreeLabel = new Label(String.valueOf(highScores[2]), skinLibgdx, com.thugcoder.game.utils.Constants.myStyle);
        playerThree = new Label(playersNames[2], skinLibgdx, com.thugcoder.game.utils.Constants.myStyle);
        scoreFourLabel = new Label(String.valueOf(highScores[1]), skinLibgdx, com.thugcoder.game.utils.Constants.myStyle);
        playerFour = new Label(playersNames[1], skinLibgdx, com.thugcoder.game.utils.Constants.myStyle);
        scoreFiveLabel = new Label(String.valueOf(highScores[0]), skinLibgdx, com.thugcoder.game.utils.Constants.myStyle);
        playerFive = new Label(playersNames[0], skinLibgdx, com.thugcoder.game.utils.Constants.myStyle);

        layer.add(scoreOneLabel);
        layer.add(separator);
        layer.add(playerOne);
        layer.row();
        layer.add(scoreTwoLabel);
        layer.add(separatorOne);
        layer.add(playerTwo);
        layer.row();
        layer.add(scoreThreeLabel);
        layer.add(separatorTwo);
        layer.add(playerThree);
        layer.row();
        layer.add(scoreFourLabel);
        layer.add(separatorThree);
        layer.add(playerFour);
        layer.row();
        layer.add(scoreFiveLabel);
        layer.add(separatorFour);
        layer.add(playerFive);
        layer.row();


        return layer;
    }

    private Table buildControlsLayer() {

        com.thugcoder.game.utils.GamePreferences preferences = com.thugcoder.game.utils.GamePreferences.instance;
        Table layer = new Table();
        layer.top().left();
        layer.pad(0f, 50f, 0f, 0f);
        myAtlas = new TextureAtlas(com.thugcoder.game.utils.Constants.TEXTURE_ATLAS_UI);
        mySkin = new Skin(myAtlas);
        retryButtonStyle = new ImageButton.ImageButtonStyle();
        retryButtonStyle.up = mySkin.getDrawable("retryup");
        retryButtonStyle.down = mySkin.getDrawable("retrydn");


        scoreLabel = new Label(String.valueOf(currentScore), skinLibgdx);
        layer.add(scoreLabel);

        layer.row();

        playerNameTextFeild = new TextField("Type Your Name...", skinLibgdx);
        playerNameTextFeild.setWidth(700);
        playerNameTextFeild.setHeight(150);
        layer.add(playerNameTextFeild);
        // + Submit Button


        layer.row();
        menuButton = new Button(retryButtonStyle);
        menuButton.setHeight(2500f);
        menuButton.setWidth(1000f);
        layer.add(menuButton);
        playerNameTextFeild.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                playerNameString  += c ;
            }
        });
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onRetryClicked();
            }
        });
// + Options Button

        return layer;
    }





    private void onRetryClicked() {

        preferences = com.thugcoder.game.utils.GamePreferences.instance;
        switch (position) {
            case 0:
                playerFive.setText(playerNameString);
                break;
            case 1:
                playerFour.setText(playerNameString);
                break;
            case 2:
                playerThree.setText(playerNameString);
                break;
            case 3:
                playerTwo.setText(playerNameString);
                break;
            case 4:
                playerOne.setText(playerNameString);
                break;

        }
        preferences.savePlayerNameAtPosition(position, playerNameString);
        goToMenu();


    }

    private void goToMenu() {
        game.setScreen(new MenuScreen(game, adsController));
    }


    private Table buildBackgroundLayer() {

        Table layer = new Table();
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(com.thugcoder.game.utils.Constants.TEXTURE_ATLAS_UI));
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
