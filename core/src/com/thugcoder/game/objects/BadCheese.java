package com.thugcoder.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.thugcoder.game.Assets;

/**
 * Created by Landolsi on 16/10/2015.
 */
public class BadCheese extends AbstractGameObject {

    public boolean collected;
    private TextureRegion regFeather;

    public BadCheese() {
        init();
    }

    private void init() {
        dimension.set(0.5f, 0.5f);
        regFeather = Assets.instance.badCheese.badCheese;
        // Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
        collected = false;
    }


    @Override
    public void render(SpriteBatch batch) {
        if (collected) return;
        TextureRegion reg = null;
        reg = regFeather;
        batch.draw(reg.getTexture(), position.x, position.y,
                origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
                rotation, reg.getRegionX(), reg.getRegionY(),
                reg.getRegionWidth(), reg.getRegionHeight(), false, false);
    }


    public int getScore() {
        return 250;
    }
}
