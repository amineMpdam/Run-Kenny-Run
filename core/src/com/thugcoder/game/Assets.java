package com.thugcoder.game;

/**
 * Created by Landolsi on 09/10/2015.
 */


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {

    public static final Assets instance = new Assets();
    public AssetKenny kenny;
    public AssetRock rock;
    public AssetRockLevelTwo anOtherRock;
    public AssetGoodCheese goodCheese;
    public AssetBadCheese badCheese;
    public AssetFinalCheese finalCheese;
    public AssetGoldenCheese goldenCheese;
    public AssetLevelDecoration levelDecoration;
    public AssetFonts fonts;
    public AssetSounds sounds;
    public AssetMusic music;
    private AssetManager assetManager;

    // singleton: prevent instantiation from other classes
    private Assets() {
    }

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        // set asset manager error handler
        assetManager.setErrorListener(this);
        // load texture atlas
        assetManager.load(com.thugcoder.game.utils.Constants.TEXTURE_ATLAS_OBJECTS,
                TextureAtlas.class);
        // start loading assets and wait until finished
        assetManager.finishLoading();
        //loading the image resources
        TextureAtlas atlas =
                assetManager.get(com.thugcoder.game.utils.Constants.TEXTURE_ATLAS_OBJECTS);
// enable texture filtering for pixel smoothing
        for (Texture t : atlas.getTextures()) {
            t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
        // load sounds
        assetManager.load("sound/jump.wav", Sound.class);
        assetManager.load("sound/jump_with_feather.wav", Sound.class);
        assetManager.load("sound/pickup_coin.wav", Sound.class);
        assetManager.load("sound/pickup_feather.wav", Sound.class);
        assetManager.load("sound/live_lost.wav", Sound.class);
// load music
        assetManager.load("music/keith303_-_brand_new_highscore.mp3",
                Music.class);

// create game resource objects
        fonts = new AssetFonts();
        kenny = new AssetKenny(atlas);
        rock = new AssetRock(atlas);
        anOtherRock = new AssetRockLevelTwo(atlas);
        goodCheese = new AssetGoodCheese(atlas);
        badCheese = new AssetBadCheese(atlas);
        levelDecoration = new AssetLevelDecoration(atlas);
        goldenCheese = new AssetGoldenCheese(atlas);
        finalCheese = new AssetFinalCheese(atlas);
        sounds = new AssetSounds();
        music = new AssetMusic();


    }

    @Override
    public void dispose() {
        assetManager.dispose();
        fonts.defaultSmall.dispose();
        fonts.defaultNormal.dispose();
        fonts.defaultBig.dispose();
    }

    public void error(String filename, Class type,
                      Throwable throwable) {
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {

    }

    public class AssetFonts {
        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultBig;

        public AssetFonts() {
// create three fonts using Libgdx's 15px bitmap font
            defaultSmall = new BitmapFont(
                    Gdx.files.internal("images/sunshineFont.fnt"), true);
            defaultNormal = new BitmapFont(
                    Gdx.files.internal("images/sunshineFont.fnt"), true);
            defaultBig = new BitmapFont(
                    Gdx.files.internal("images/myFont.fnt"), true);
// enable linear texture filtering for smooth fonts
            defaultSmall.getRegion().getTexture().setFilter(
                    TextureFilter.Linear, TextureFilter.Linear);
            defaultNormal.getRegion().getTexture().setFilter(
                    TextureFilter.Linear, TextureFilter.Linear);
            defaultBig.getRegion().getTexture().setFilter(
                    TextureFilter.Linear, TextureFilter.Linear);
        }
    }

    public class AssetKenny {
        public final AtlasRegion head;

        public final Animation animNormal;


        public AssetKenny(TextureAtlas atlas) {
            head = atlas.findRegion("kenny");

            // Animation: Kenny's walk
            Array<AtlasRegion> regions = atlas.findRegions("kenny");
            animNormal = new Animation(1.0f / 10.0f, regions, Animation.PlayMode.LOOP);
        }
    }


    public class AssetRock {
        public final AtlasRegion edge;
        public final AtlasRegion middle;

        public AssetRock(TextureAtlas atlas) {
            edge = atlas.findRegion("terrain2");
            middle = atlas.findRegion("terrain1");
        }
    }

    public class AssetRockLevelTwo {
        public final AtlasRegion edge;
        public final AtlasRegion middle;

        public AssetRockLevelTwo(TextureAtlas atlas) {
            edge = atlas.findRegion("terrain22");
            middle = atlas.findRegion("terrain11");
        }
    }


    public class AssetGoodCheese {
        public final AtlasRegion goodCheese;

        public AssetGoodCheese(TextureAtlas atlas) {
            goodCheese = atlas.findRegion("cheese_good");
        }
    }


    public class AssetFinalCheese {
        public final AtlasRegion finalCheese;

        public AssetFinalCheese(TextureAtlas atlas) {
            finalCheese = atlas.findRegion("final_cheese");
        }
    }

    public class AssetGoldenCheese {
        public final AtlasRegion goldenCheese;

        public AssetGoldenCheese(TextureAtlas atlas) {
            goldenCheese = atlas.findRegion("cup");
        }
    }


    public class AssetBadCheese {
        public final AtlasRegion badCheese;

        public AssetBadCheese(TextureAtlas atlas) {
            badCheese = atlas.findRegion("cheese_bad");
        }
    }


    public class AssetLevelDecoration {
        public final AtlasRegion cloud01;
        public final AtlasRegion cloud02;
        public final AtlasRegion cloud03;
        public final AtlasRegion mountainLeft;
        public final AtlasRegion mountainRight;
        public final AtlasRegion waterOverlay;
        public final AtlasRegion star;

        public AssetLevelDecoration(TextureAtlas atlas) {
            cloud01 = atlas.findRegion("cloud1");
            cloud02 = atlas.findRegion("cloud2");
            cloud03 = atlas.findRegion("cloud3");
            mountainLeft = atlas.findRegion("mountain_left");
            mountainRight = atlas.findRegion("mountain_right");
            waterOverlay = atlas.findRegion("water_ditch");
            star = atlas.findRegion("star");
        }
    }


    public class AssetSounds {
        public final Sound jump;
        public final Sound jumpWithPowerUp;
        public final Sound pickupCheese;
        public final Sound pickupPowerUp;
        public final Sound liveLost;

        public AssetSounds() {
            jump = Gdx.audio.newSound(Gdx.files.internal("sound/jump.wav"));
            jumpWithPowerUp = Gdx.audio.newSound(Gdx.files.internal("sound/jump_with_bad_cheese.wav"));
            pickupCheese = Gdx.audio.newSound(Gdx.files.internal("sound/pickup_cheese.wav"));
            pickupPowerUp = Gdx.audio.newSound(Gdx.files.internal("sound/pickup_other_cheese.wav"));
            liveLost = Gdx.audio.newSound(Gdx.files.internal("sound/live_lost.wav"));
            //TODO : add an other sound for the final piece of cheese !! tonight.

        }
    }


    public class AssetMusic {
        public final Music song01;

        public AssetMusic() {
            song01 = Gdx.audio.newMusic(Gdx.files.internal("music/keith303_-_brand_new_highscore.mp3"));
        }
    }

}