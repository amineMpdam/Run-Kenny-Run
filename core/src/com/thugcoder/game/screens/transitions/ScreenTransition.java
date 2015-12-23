package com.thugcoder.game.screens.transitions;

/**
 * Created by Landolsi on 31/10/2015.
 */

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface ScreenTransition {


    public float getDuration ();

    public void render (SpriteBatch batch, Texture currScreen,
                        Texture nextScreen, float alpha);


}
