package com.thugcoder.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;

/**
 * Created by Landolsi on 20/10/2015.
 */
public class GameScreen extends AbstractGameScreen {

    private com.thugcoder.game.loop.WorldController worldController;
    private com.thugcoder.game.loop.WorldRenderer worldRenderer;
    private boolean paused;
    private com.thugcoder.game.ads.AdsController adsController;

    public GameScreen(DirectedGame game) {
        super(game);
    }

    public GameScreen(DirectedGame game, com.thugcoder.game.ads.AdsController adsController) {
        super(game);
        this.adsController = adsController;
    }

    @Override
    public void render(float deltaTime) {

        // Do not update game world when paused.
        if (!paused) {
            // Update game world by the time that has passed
            // since last rendered frame.
            worldController.update(deltaTime);
        }
        // Sets the clear screen color to: Cornflower Blue
        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed /
                255.0f, 0xff / 255.0f);
        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Render game world to screen
        worldRenderer.render();

    }

    @Override
    public InputProcessor getInputProcessor() {
        return worldController;
    }

    @Override
    public void resize(int width, int height) {
        worldRenderer.resize(width, height);
    }

    @Override
    public void show() {
        com.thugcoder.game.utils.GamePreferences.instance.load();
        worldController = new com.thugcoder.game.loop.WorldController(game, adsController);
        worldRenderer = new com.thugcoder.game.loop.WorldRenderer(worldController);
        Gdx.input.setCatchBackKey(true);

    }

    @Override
    public void hide() {
        worldRenderer.dispose();
        worldController.dispose();
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        super.resume();
        //Cette Methode sera appelé uniquement si le jeu tourne sous Android
        paused = false;
    }
}
