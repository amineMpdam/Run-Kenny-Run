package com.thugcoder.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.thugcoder.game.Assets;

/**
 * Created by Landolsi on 13/11/2015.
 */
public class GoldenCheese extends AbstractGameObject {

    public boolean collected;
    private TextureRegion regGoldCoin;

    public GoldenCheese() {
        init();
    }


    private void init() {
        dimension.set(0.5f, 0.5f);
        regGoldCoin = Assets.instance.goldenCheese.goldenCheese;
        // Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
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
        return 1500;
    }
}


