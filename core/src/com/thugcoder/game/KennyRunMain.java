package com.thugcoder.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.thugcoder.game.ads.AdsController;
import com.thugcoder.game.screens.DirectedGame;
import com.thugcoder.game.screens.MenuScreen;
import com.thugcoder.game.utils.AudioManager;
import com.thugcoder.game.utils.GamePreferences;

public class KennyRunMain extends DirectedGame {


    private AdsController adsController;

    public KennyRunMain(AdsController adsController) {
        this.adsController = adsController;
    }

    @Override
    public void create() {
        // Set Libgdx log level
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        // Load assets
        Assets.instance.init(new AssetManager());
        // Load preferences for audio settings and start playing music
        GamePreferences.instance.load();
        AudioManager.instance.play(Assets.instance.music.song01);
        // Start game at menu screen
        setScreen(new MenuScreen(this, adsController));
        //If we are connected via wifi the game will display ads
        if (adsController.isConnected()) {
            adsController.showBannerAd();
        }
    }

}
