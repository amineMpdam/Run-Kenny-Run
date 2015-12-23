package com.thugcoder.game;

/**
 * Created by Landolsi on 14/10/2015.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.thugcoder.game.objects.Kenny;
import com.thugcoder.game.objects.Mountains;
import com.thugcoder.game.objects.Rock;


public class Level {

    public static final String TAG = Level.class.getName();

    public Kenny kennyHead;
    public Array<com.thugcoder.game.objects.GoodCheese> goodCheeses;
    public Array<com.thugcoder.game.objects.FinalCheese> finalCheeses;
    public Array<com.thugcoder.game.objects.BadCheese> badCheeses;
    public com.thugcoder.game.objects.GoldenCheese goal;
    public Array<com.thugcoder.game.objects.Star> stars;
    // objects
    public Array<Rock> rocks;
    public Array<com.thugcoder.game.objects.SecondRock> otherRocks;
    // decoration
    public com.thugcoder.game.objects.Clouds clouds;
    public Mountains mountains;
    public com.thugcoder.game.objects.WaterOverlay waterOverlay;

    public Level(String filename) {
        init(filename);
    }

    private void init(String filename) {
        // player character
        kennyHead = null;
        // objects
        rocks = new Array<Rock>();
        otherRocks = new Array<com.thugcoder.game.objects.SecondRock>();
        goodCheeses = new Array<com.thugcoder.game.objects.GoodCheese>();
        badCheeses = new Array<com.thugcoder.game.objects.BadCheese>();
        finalCheeses = new Array<com.thugcoder.game.objects.FinalCheese>();
        goal = new com.thugcoder.game.objects.GoldenCheese();
        stars = new Array<com.thugcoder.game.objects.Star>();
        // load image file that represents the level data
        Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
        // scan pixels from top-left to bottom-right
        int lastPixel = -1;
        for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
            for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
                com.thugcoder.game.objects.AbstractGameObject obj = null;
                float offsetHeight = 0;
                // height grows from bottom to top
                float baseHeight = pixmap.getHeight() - pixelY;
                // get color of current pixel as 32-bit RGBA value
                int currentPixel = pixmap.getPixel(pixelX, pixelY);
                // find matching color value to identify block type at (x,y)
                // point and create the corresponding game object if there is
                // a match


                // empty space
                if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
                    // do nothing
                }
                // rock
                else if (BLOCK_TYPE.ROCK.sameColor(currentPixel)) {

                    if (lastPixel != currentPixel) {
                        obj = new Rock();
                        float heightIncreaseFactor = 0.25f;
                        offsetHeight = -2.5f;
                        obj.position.set(pixelX, baseHeight * obj.dimension.y
                                * heightIncreaseFactor + offsetHeight);
                        rocks.add((Rock) obj);
                    } else {
                        rocks.get(rocks.size - 1).increaseLength(1);

                    }


                }

                // player spawn point
                else if
                        (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) {
                    if (filename.equals(com.thugcoder.game.utils.Constants.LEVEL_01)) {
                        obj = new Kenny(1);
                    } else if (filename.equals(com.thugcoder.game.utils.Constants.LEVEL_02)) {
                        obj = new Kenny(2);
                    } else if (filename.equals(com.thugcoder.game.utils.Constants.LEVEL_03)) {
                        obj = new Kenny(3);
                    } else {
                        obj = new Kenny(4);
                    }

                    offsetHeight = -3.0f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y +
                            offsetHeight);
                    kennyHead = (Kenny) obj;
                }
                // bad chesse
                else if (BLOCK_TYPE.ITEM_BAD_CHEESE.sameColor(currentPixel)) {
                    obj = new com.thugcoder.game.objects.BadCheese();
                    offsetHeight = -1.5f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y
                            + offsetHeight);
                    badCheeses.add((com.thugcoder.game.objects.BadCheese) obj);
                }
                // good cheese
                else if (BLOCK_TYPE.ITEM_GOOD_CHEESE.sameColor(currentPixel)) {
                    obj = new com.thugcoder.game.objects.GoodCheese();
                    offsetHeight = -1.5f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y
                            + offsetHeight);
                    goodCheeses.add((com.thugcoder.game.objects.GoodCheese) obj);
                } else if (BLOCK_TYPE.ITEM_FINAL_CHEESE.sameColor(currentPixel)) {
                    obj = new com.thugcoder.game.objects.FinalCheese();
                    offsetHeight = -1.5f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y
                            + offsetHeight);
                    finalCheeses.add((com.thugcoder.game.objects.FinalCheese) obj);
                } else if (BLOCK_TYPE.ITEM_Golden_CHEESE.sameColor(currentPixel)) {
                    obj = new com.thugcoder.game.objects.GoldenCheese();
                    offsetHeight = -5.0f;
                    obj.position.set(pixelX, baseHeight + offsetHeight);
                    goal = (com.thugcoder.game.objects.GoldenCheese) obj;

                } else {
                    int r = 0xff & (currentPixel >>> 24); //red color channel
                    int g = 0xff & (currentPixel >>> 16); //green color channel
                    int b = 0xff & (currentPixel >>> 8); //blue color channel
                    int a = 0xff & currentPixel; //alpha channel
                    Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<"
                            + pixelY + ">: r<" + r + "> g<" + g + "> b<" + b + "> a<" + a + ">");
                }
                lastPixel = currentPixel;
            }
        }
// decoration
        clouds = new com.thugcoder.game.objects.Clouds(pixmap.getWidth());
        clouds.position.set(0, 2);
        mountains = new Mountains(pixmap.getWidth());
        mountains.position.set(-1, -1);
        waterOverlay = new com.thugcoder.game.objects.WaterOverlay(pixmap.getWidth());
        waterOverlay.position.set(0, -3.75f);
// free memory
        pixmap.dispose();
        Gdx.app.debug(TAG, "level '" + filename + "' loaded");

    }

    public void render(SpriteBatch batch) {
        // Draw Mountains
        mountains.render(batch);
        // Draw Rocks
        if (!rocks.equals(null)) {
            for (Rock rock : rocks)
                rock.render(batch);
        } else {
            for (com.thugcoder.game.objects.SecondRock aRock : otherRocks)
                aRock.render(batch);
        }
        // Draw yellow cheese
        for (com.thugcoder.game.objects.GoodCheese goldCoin : goodCheeses)
            goldCoin.render(batch);
        // Draw green cheese
        for (com.thugcoder.game.objects.BadCheese feather : badCheeses)
            feather.render(batch);
        //Draw Golden Cheese
        // Draw Goal
        goal.render(batch);
        // Draw Red cheese
        for (com.thugcoder.game.objects.FinalCheese finalCheese : finalCheeses)
            finalCheese.render(batch);
        //Draw Stars
        for (com.thugcoder.game.objects.Star star : stars)
            star.render(batch);
        // Draw Player Character
        kennyHead.render(batch);
        // Draw Water Overlay
        waterOverlay.render(batch);
        // Draw Clouds
        clouds.render(batch);
    }

    public void update(float deltaTime) {
        kennyHead.update(deltaTime);

        for (Rock rock : rocks)
            rock.update(deltaTime);
        for (com.thugcoder.game.objects.GoodCheese goldCoin : goodCheeses)
            goldCoin.update(deltaTime);
        for (com.thugcoder.game.objects.BadCheese feather : badCheeses)
            feather.update(deltaTime);
        for (com.thugcoder.game.objects.SecondRock secondRock : otherRocks)
            secondRock.update(deltaTime);
        for (com.thugcoder.game.objects.FinalCheese finalCheese : finalCheeses)
            finalCheese.update(deltaTime);
        for (com.thugcoder.game.objects.Star star : stars)
            star.update(deltaTime);
        clouds.update(deltaTime);
    }


    public enum BLOCK_TYPE {
        EMPTY(0, 0, 0), // black
        ROCK(0, 255, 0), // green
        PLAYER_SPAWNPOINT(255, 255, 255), // white
        ITEM_BAD_CHEESE(255, 0, 255), // purple
        ITEM_GOOD_CHEESE(255, 255, 0), // yellow
        ITEM_FINAL_CHEESE(0, 0, 217), //Blue
        ITEM_Golden_CHEESE(100, 100, 100); //Red
        private int color;

        private BLOCK_TYPE(int r, int g, int b) {
            color = r << 24 | g << 16 | b << 8 | 0xff;
        }

        public boolean sameColor(int color) {
            return this.color == color;
        }

        public int getColor() {
            return color;
        }
    }

}
