package com.thugcoder.game.loop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.thugcoder.game.Assets;
import com.thugcoder.game.Level;
import com.thugcoder.game.ads.AdsController;
import com.thugcoder.game.objects.BadCheese;
import com.thugcoder.game.objects.FinalCheese;
import com.thugcoder.game.objects.GoldenCheese;
import com.thugcoder.game.objects.Kenny;
import com.thugcoder.game.objects.Rock;
import com.thugcoder.game.objects.Star;
import com.thugcoder.game.screens.DirectedGame;
import com.thugcoder.game.screens.GameOverScreen;
import com.thugcoder.game.screens.HighScoreScreen;
import com.thugcoder.game.screens.MenuScreen;
import com.thugcoder.game.utils.AudioManager;
import com.thugcoder.game.utils.CameraHelper;
import com.thugcoder.game.utils.Constants;
import com.thugcoder.game.utils.GamePreferences;


/**
 * Created by Landolsi on 09/10/2015.
 */
public class WorldController extends InputAdapter implements Disposable {

    private static final String TAG = WorldController.class.getName();

    public CameraHelper cameraHelper;
    public Level level;
    public World b2world;
    public int lives;
    public int score = 0;
    //animations variables for the change in the score
    // state and the loss of life event !!
    public float livesVisual;
    public float scoreVisual;
    public boolean isFistLevel = true;
    public boolean isLevelTwo = false;
    public boolean isLevelThree = false;
    public boolean isFinalLevel = false;
    //High scores Table
    int[] highScores;
    private boolean goalReached;
    private float timeLeftGameOverDelay;
    private DirectedGame game;
    // Rectangles for collision detection
    private Rectangle r1 = new Rectangle();
    private Rectangle r2 = new Rectangle();
    private AdsController adsController ;

    public WorldController(DirectedGame game) {
        this.game = game;
        init();
    }

    public WorldController(DirectedGame game,AdsController adsController) {
        this.game = game;
        this.adsController = adsController ;
        init();
    }


    public int getScore() {
        return score;
    }

    private void backToMenu() {
        // switch to menu screen
        game.setScreen(new MenuScreen(game,adsController));

    }

    public boolean isGameOver() {
        return lives < 0;
    }

    public boolean isPlayerInWater() {

        return level.kennyHead.position.y < -6;


    }

    private void onCollisionKennyHeadWithRock(Rock rock) {
        Kenny kennyHead = level.kennyHead;
        float heightDifference = Math.abs(kennyHead.position.y
                - (rock.position.y + rock.bounds.height));
        if (heightDifference > 0.25f) {
            boolean hitRightEdge = kennyHead.position.x > (
                    rock.position.x + rock.bounds.width / 2.0f);
            if (hitRightEdge) {
                kennyHead.position.x = rock.position.x + rock.bounds.width;
            } else {
                kennyHead.position.x = rock.position.x -
                        kennyHead.bounds.width;
            }
            return;
        }
        switch (kennyHead.jumpState) {
            case GROUNDED:
                break;
            case FALLING:
            case JUMP_FALLING:
                kennyHead.position.y = rock.position.y +
                        kennyHead.bounds.height + kennyHead.origin.y;
                kennyHead.jumpState = Kenny.JUMP_STATE.GROUNDED;
                break;
            case JUMP_RISING:
                kennyHead.position.y = rock.position.y +
                        kennyHead.bounds.height + kennyHead.origin.y;
                break;
        }


    }

    private void onCollisionKennyWithGoodCheese(com.thugcoder.game.objects.GoodCheese goodCheese) {
        AudioManager.instance.play(Assets.instance.sounds.pickupCheese);
        goodCheese.collected = true;
        score += goodCheese.getScore();
        Gdx.app.log(TAG, "Cheese collected");
    }

    private void onCollisionkennyWithBadCheese(BadCheese badCheese) {
        AudioManager.instance.play(Assets.instance.sounds.pickupPowerUp);
        badCheese.collected = true;
        score += badCheese.getScore();
        level.kennyHead.setFeatherPowerup(true);

    }


    private void onCollisionKennyWithFinalCheese(FinalCheese goodCheese) {
        AudioManager.instance.play(Assets.instance.sounds.pickupCheese);
        goodCheese.collected = true;
        score = getScore() + goodCheese.getScore();


        //set the test variable to true
        if (!isLevelTwo && !isLevelThree && !isFinalLevel && isFistLevel) {
            isLevelTwo = true;
            isLevelThree = false;
            isFistLevel = false;
            isFinalLevel = false;
            // Clears the screen
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            initSecondLevel();
        } else if (!isFistLevel && !isLevelThree && isLevelTwo && !isFinalLevel) {
            isFistLevel = false;
            isLevelTwo = false;
            isLevelThree = true;
            isFinalLevel = false;
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            initThirdLevel();

        } else {
            isFistLevel = false;
            isLevelTwo = false;
            isLevelThree = false;
            isFinalLevel = true;
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            initFinaleLevel();
        }


    }


    private void spawnStars(Vector2 pos, int numCarrots,
                            float radius) {
        float carrotShapeScale = 0.5f;
        // create carrots with box2d body and fixture
        for (int i = 0; i < numCarrots; i++) {
            Star star = new Star();
            // calculate random spawn position, rotation, and scale
            float x = MathUtils.random(-radius, radius);
            float y = MathUtils.random(5.0f, 15.0f);
            float rotation = MathUtils.random(0.0f, 360.0f)
                    * MathUtils.degreesToRadians;
            float carrotScale = MathUtils.random(0.5f, 1.5f);
            star.scale.set(carrotScale, carrotScale);
// create box2d body for carrot with start position
// and angle of rotation
            BodyDef bodyDef = new BodyDef();
            bodyDef.position.set(pos);
            bodyDef.position.add(x, y);
            bodyDef.angle = rotation;
            Body body = b2world.createBody(bodyDef);
            body.setType(BodyType.DynamicBody);
            star.body = body;
            // create rectangular shape for star to allow
            // interactions (collisions) with other objects
            PolygonShape polygonShape = new PolygonShape();
            float halfWidth = star.bounds.width / 2.0f * carrotScale;
            float halfHeight = star.bounds.height / 2.0f * carrotScale;
            polygonShape.setAsBox(halfWidth * carrotShapeScale,
                    halfHeight * carrotShapeScale);
            // set physics attributes
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = polygonShape;
            fixtureDef.density = 50;
            fixtureDef.restitution = 0.5f;
            fixtureDef.friction = 0.5f;
            body.createFixture(fixtureDef);
            polygonShape.dispose();
            // finally, add new star to list for updating/rendering
            level.stars.add(star);
        }
    }


    private void initPhysics() {
        if (b2world != null) b2world.dispose();
        b2world = new World(new Vector2(0, -9.81f), true);
        // Rocks
        Vector2 origin = new Vector2();
        for (Rock rock : level.rocks) {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyType.KinematicBody;
            bodyDef.position.set(rock.position);
            Body body = b2world.createBody(bodyDef);
            rock.body = body;
            PolygonShape polygonShape = new PolygonShape();
            origin.x = rock.bounds.width / 2.0f;
            origin.y = rock.bounds.height / 2.0f;
            polygonShape.setAsBox(rock.bounds.width / 2.0f,
                    rock.bounds.height / 2.0f, origin, 0);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
            polygonShape.dispose();
        }
    }


    private void onCollisionkennyWithGoldenCheese(GoldenCheese goldenCheese) {
        goalReached = true;
        level.kennyHead.isGoalReached = true;
        Vector2 centerPosBunnyHead =
                new Vector2(level.kennyHead.position);
        centerPosBunnyHead.x += level.kennyHead.bounds.width;
        spawnStars(centerPosBunnyHead, Constants.STARS_SPAWN_MAX,
                Constants.STARS_SPAWN_RADIUS);
        timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_FINISHED;


    }

    private void testCollisions() {
        r1.set(level.kennyHead.position.x, level.kennyHead.position.y,
                level.kennyHead.bounds.width, level.kennyHead.bounds.height);
// Test collision: Kenny Head <-> Rocks
        for (Rock rock : level.rocks) {
            r2.set(rock.position.x, rock.position.y, rock.bounds.width,
                    rock.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionKennyHeadWithRock(rock);
// IMPORTANT: must do all collisions for valid
// edge testing on rocks.
        }
// Test collision: Kenny Head <-> Good Cheese
        for (com.thugcoder.game.objects.GoodCheese goldcoin : level.goodCheeses) {
            if (goldcoin.collected) continue;
            r2.set(goldcoin.position.x, goldcoin.position.y,
                    goldcoin.bounds.width, goldcoin.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionKennyWithGoodCheese(goldcoin);
            break;
        }
        // Test collision: Kenny Head <-> Final Cheese
        for (FinalCheese finalCheese : level.finalCheeses) {
            if (finalCheese.collected) continue;
            r2.set(finalCheese.position.x, finalCheese.position.y,
                    finalCheese.bounds.width, finalCheese.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionKennyWithFinalCheese(finalCheese);
            break;
        }

// Test collision: Kennny Head <-> Bad Cheese
        for (BadCheese feather : level.badCheeses) {
            if (feather.collected) continue;
            r2.set(feather.position.x, feather.position.y,
                    feather.bounds.width, feather.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionkennyWithBadCheese(feather);
            break;
        }


        // Test collision: Kennny Head <-> Golden Cheese
        if (!goalReached) {
            r2.set(level.goal.bounds);
            r2.x += level.goal.position.x;
            r2.y += level.goal.position.y;
            if (r1.overlaps(r2)) onCollisionkennyWithGoldenCheese(level.goal);
        }

    }


    private void initLevel() {
        level = new Level(Constants.LEVEL_01);
        cameraHelper.setTarget(level.kennyHead);
        isLevelTwo = false;
        isLevelThree = false;
        isFistLevel = true;
        isFinalLevel = false;
        initPhysics();
    }

    private void initSecondLevel() {
        level = new Level(Constants.LEVEL_02);
        cameraHelper.setTarget(level.kennyHead);
        isLevelTwo = true;
        isLevelThree = false;
        isFistLevel = false;
        isFinalLevel = false;
        initPhysics();

    }

    private void initThirdLevel() {
        level = new Level(Constants.LEVEL_03);
        cameraHelper.setTarget(level.kennyHead);
        isLevelTwo = false;
        isLevelThree = true;
        isFistLevel = false;
        isFinalLevel = false;
        initPhysics();
    }


    private void initFinaleLevel() {
        level = new Level(Constants.LEVEL_04);
        cameraHelper.setTarget(level.kennyHead);
        isLevelTwo = false;
        isLevelThree = false;
        isFistLevel = false;
        isFinalLevel = true;
        initPhysics();
    }


    private void init() {
        cameraHelper = new CameraHelper();
        lives = Constants.LIVES_START;
        timeLeftGameOverDelay = 0;
        initLevel();


    }


    public void update(float deltaTime) {
        handleDebugInput(deltaTime);


        if (isGameOver() || goalReached) {
            timeLeftGameOverDelay -= deltaTime;
            if (timeLeftGameOverDelay < 0) {
                GamePreferences preferences = GamePreferences.instance;
                highScores = new int[5];
                highScores = preferences.getScores();
                //If the score acheived is NOT a high score the player is directed to the game over screen.
                if (score < highScores[0] && !goalReached) {
                    goToGameOver(score);
                } else if (score < highScores[0] && goalReached) {
                    goToMenu();
                } else {
                    //Now We know for sure that the player has a new high score we will update the list
                    //and pass to the congratulation Scene :)
                    highScores = preferences.insertNewHighScore(score);
                    preferences.saveScores(highScores);
                    goToCongratScene();
                }

            }
        } else {
            handleInputGame(deltaTime);
        }
        level.update(deltaTime);
        testCollisions();
        b2world.step(deltaTime, 8, 3);
        cameraHelper.update(deltaTime);
        if (!isGameOver() && isPlayerInWater()) {
            AudioManager.instance.play(Assets.instance.sounds.liveLost);
            lives--;
            if (isGameOver())
                timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
            else {
                if (isFistLevel) {
                    initLevel();
                } else if (isLevelTwo) {
                    initSecondLevel();
                } else if (isLevelThree) {
                    initThirdLevel();
                } else {
                    initFinaleLevel();
                }

            }


        }
        level.mountains.updateScrollPosition
                (cameraHelper.getPosition());
        if (livesVisual > lives)
            livesVisual = Math.max(lives, livesVisual - 1 * deltaTime);
        if (scoreVisual < score)
            scoreVisual = Math.min(score, scoreVisual
                    + 250 * deltaTime);

    }


    private void goToGameOver(int aScore) {
        game.setScreen(new GameOverScreen(game, aScore,adsController));
    }

    private void goToCongratScene() {
        game.setScreen(new HighScoreScreen(game, getScore(),adsController));
    }

    private void goToMenu() {
        game.setScreen(new MenuScreen(game,adsController));
    }


    private void handleDebugInput(float deltaTime) {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) return;

        if (!cameraHelper.hasTarget(level.kennyHead)) {


            // Camera Controls (move)
            float camMoveSpeed = 5 * deltaTime;
            float camMoveSpeedAccelerationFactor = 5;
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) camMoveSpeed *=
                    camMoveSpeedAccelerationFactor;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveCamera(-camMoveSpeed,
                    0);
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveCamera(camMoveSpeed,
                    0);
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) moveCamera(0, camMoveSpeed);
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) moveCamera(0,
                    -camMoveSpeed);
            if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE))
                cameraHelper.setPosition(0, 0);

            // Camera Controls (zoom)
            float camZoomSpeed = 1 * deltaTime;
            float camZoomSpeedAccelerationFactor = 5;
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) camZoomSpeed *=
                    camZoomSpeedAccelerationFactor;
            if (Gdx.input.isKeyPressed(Input.Keys.COMMA))
                cameraHelper.addZoom(camZoomSpeed);
            if (Gdx.input.isKeyPressed(Input.Keys.PERIOD)) cameraHelper.addZoom(
                    -camZoomSpeed);
            if (Gdx.input.isKeyPressed(Input.Keys.SLASH)) cameraHelper.setZoom(1);
        }
    }

    private void moveCamera(float x, float y) {
        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x, y);
    }


    private void handleInputGame(float deltaTime) {
        if (cameraHelper.hasTarget(level.kennyHead)) {
// Player Movement
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                level.kennyHead.velocity.x =
                        -level.kennyHead.terminalVelocity.x;
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                level.kennyHead.velocity.x =
                        level.kennyHead.terminalVelocity.x;
            } else {
// Execute auto-forward movement on non-desktop platform
                if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
                    level.kennyHead.velocity.x =
                            level.kennyHead.terminalVelocity.x;
                }
            }
// Kenny Jump
            if (Gdx.input.isTouched() ||
                    Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                level.kennyHead.setJumping(true);
            } else {
                level.kennyHead.setJumping(false);
            }
        }
    }


    @Override
    public boolean keyUp(int keycode) {
// Reset game world
        if (keycode == Input.Keys.R) {
            init();
        }

        // Toggle camera follow
        else if (keycode == Input.Keys.ENTER) {
            cameraHelper.setTarget(cameraHelper.hasTarget()
                    ? null : level.kennyHead);

        }

        // Back to Menu
        else if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            backToMenu();
        }
        return false;
    }


    @Override
    public void dispose() {
        if (b2world != null) b2world.dispose();
    }


}
