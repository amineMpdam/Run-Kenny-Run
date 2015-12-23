package com.thugcoder.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.thugcoder.game.Assets;

/**
 * Created by Landolsi on 27/11/2015.
 */
public class Star extends AbstractGameObject {


    private TextureRegion regStar;

    public Star() {
        init();
    }

    private void init() {
        dimension.set(0.25f, 0.5f);
        regStar = Assets.instance.levelDecoration.star;
// Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
        origin.set(dimension.x / 2, dimension.y / 2);
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion reg = null;
        reg = regStar;
        batch.draw(reg.getTexture(), position.x - origin.x,
                position.y - origin.y, origin.x, origin.y, dimension.x,
                dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
                reg.getRegionY(), reg.getRegionWidth(),
                reg.getRegionHeight(), false, false);
    }


}
