package com.thugcoder.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.thugcoder.game.Assets;

/**
 * Created by Landolsi on 07/11/2015.
 */
public class FinalCheese extends AbstractGameObject {
    public boolean collected;
    private TextureRegion regGoldCoin;

    public FinalCheese() {
        init();
    }


    private void init() {
        dimension.set(0.5f, 0.5f);
        regGoldCoin = Assets.instance.finalCheese.finalCheese;
        // Set bounding box for collision detection
        bounds.set(1, Float.MIN_VALUE, 10, Float.MAX_VALUE);
        origin.set(dimension.x / 2.0f, 0.0f);
        collected = false;
    }


    @Override
    public void render(SpriteBatch batch) {
        if (collected) return;
        TextureRegion reg = null;
        reg = regGoldCoin;
        batch.draw(reg.getTexture(), position.x, position.y,
                origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
                rotation, reg.getRegionX(), reg.getRegionY(),
                reg.getRegionWidth(), reg.getRegionHeight(), false, false);
    }


    public int getScore() {
        return 500;
    }
}
