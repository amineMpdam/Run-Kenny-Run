package com.thugcoder.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Landolsi on 16/10/2015.
 */
public class Kenny extends AbstractGameObject {


    public static final String TAG = Kenny.class.getName();
    private final float JUMP_TIME_MAX = 0.3f;
    private final float JUMP_TIME_MIN = 0.1f;
    private final float JUMP_TIME_OFFSET_FLYING =
            JUMP_TIME_MAX - 0.018f;
    public ParticleEffect dustParticles = new ParticleEffect();
    public int cal = 0;
    public VIEW_DIRECTION viewDirection;
    public float timeJumping;
    public JUMP_STATE jumpState;
    public boolean hasFeatherPowerup;
    public float stateTime;
    public float timeLeftFeatherPowerup;
    public Boolean isGoalReached = false;
    private Animation animNormal;
    private int level;

    public Kenny() {
        init();
    }

    public Kenny(int level) {
        init();
        this.level = level;
    }


    private void init() {
        dimension.set(1, 1);

        animNormal = com.thugcoder.game.Assets.instance.kenny.animNormal;
        stateTime = 0f;


        // Center image on game object
        origin.set(dimension.x / 2, dimension.y / 2);
        // Bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
        // Set physics values

        terminalVelocity.set(3.0f, 4.0f);
        friction.set(12.0f, 0.0f);
        acceleration.set(0.0f, -25.0f);
        // View direction
        viewDirection = VIEW_DIRECTION.RIGHT;
        // Jump state
        jumpState = JUMP_STATE.FALLING;
        timeJumping = 0;
        // Power-ups
        hasFeatherPowerup = false;
        timeLeftFeatherPowerup = 0;
        //Dust Effect
        dustParticles.load(Gdx.files.internal("particles/dust.pfx"), Gdx.files.internal("particles"));
    }

    public void setJumping(boolean jumpKeyPressed) {
        switch (jumpState) {
            case GROUNDED: // Character is standing on a platform
                if (jumpKeyPressed) {
                    // Start counting jump time from the beginning
                    com.thugcoder.game.utils.AudioManager.instance.play(com.thugcoder.game.Assets.instance.sounds.jump);
                    timeJumping = 0;
                    jumpState = JUMP_STATE.JUMP_RISING;
                }
                break;
            case JUMP_RISING: // Rising in the air
                if (!jumpKeyPressed)
                    jumpState = JUMP_STATE.JUMP_FALLING;
                break;
            case FALLING:// Falling down
            case JUMP_FALLING: // Falling down after jump
                if (jumpKeyPressed && hasFeatherPowerup) {
                    timeJumping = JUMP_TIME_OFFSET_FLYING;
                    jumpState = JUMP_STATE.JUMP_RISING;
                }
                break;
        }
    }

    public void setFeatherPowerup(boolean pickedUp) {

        hasFeatherPowerup = pickedUp;
        if (pickedUp) {
            timeLeftFeatherPowerup =
                    com.thugcoder.game.utils.Constants.ITEM_FEATHER_POWERUP_DURATION;
        }
    }

    public boolean hasFeatherPowerup() {
        return hasFeatherPowerup && timeLeftFeatherPowerup > 0;
    }

    @Override
    public void render(SpriteBatch batch) {

        TextureRegion reg = null;

        // Draw Particles
        dustParticles.draw(batch);

        // Apply Skin Color
        batch.setColor(
                com.thugcoder.game.utils.CharacterSkin.values()[com.thugcoder.game.utils.GamePreferences.instance.charSkin]
                        .getColor());


        // Set special color when game object has a feather power-up
        if (hasFeatherPowerup) {
            batch.setColor(2.0f, 1.8f, 1.0f, 1.0f);
        }
        // Draw image
        float dimCorrectionX = 0;
        float dimCorrectionY = 0;
        stateTime += Gdx.graphics.getDeltaTime();
        reg = animNormal.getKeyFrame(stateTime, true);
        batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x + dimCorrectionX, dimension.y + dimCorrectionY,
                scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
                viewDirection == VIEW_DIRECTION.LEFT, false);
        // Reset color to white
        batch.setColor(1, 1, 1, 1);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        switch (level) {
            case 1:
                terminalVelocity.set(3.0f, 4.0f);
                velocity.x = 2.8f;
                break;
            case 2:
                terminalVelocity.set(4.0f, 4.0f);
                velocity.x = 4.0f;
                break;
            case 3:
                terminalVelocity.set(4.5f, 4.0f);
                velocity.x = 5.5f;
                break;
            case 4:
                if (isGoalReached) {

                    terminalVelocity.set(-10.0f, 4.0f);
                    velocity.x = 0.0f;
                } else {

                    terminalVelocity.set(5.0f, 4.0f);
                    velocity.x = 6.0f;
                }

                break;

        }


        if (velocity.x != 0) {
            viewDirection = velocity.x < 0 ? VIEW_DIRECTION.LEFT :
                    VIEW_DIRECTION.RIGHT;
        }
        if (timeLeftFeatherPowerup > 0) {
            timeLeftFeatherPowerup -= deltaTime;
            if (timeLeftFeatherPowerup < 0) {
                // disable power-up
                timeLeftFeatherPowerup = 0;
                setFeatherPowerup(false);
            }
        }

        dustParticles.update(deltaTime);
    }

    @Override
    protected void updateMotionY(float deltaTime) {
        switch (jumpState) {
            case GROUNDED:
                jumpState = JUMP_STATE.FALLING;
                if (velocity.x != 0) {
                    dustParticles.setPosition(position.x + dimension.x / 2,
                            position.y);
                    dustParticles.start();
                }
                break;
            case JUMP_RISING:
                // Keep track of jump time
                timeJumping += deltaTime;
                // Jump time left?
                if (timeJumping <= JUMP_TIME_MAX) {
                    // Still jumping
                    velocity.y = terminalVelocity.y;
                }
                break;
            case FALLING:
                break;
            case JUMP_FALLING:
                // Add delta times to track jump time
                timeJumping += deltaTime;
                // Jump to minimal height if jump key was pressed too short
                if (timeJumping > 0 && timeJumping <= JUMP_TIME_MIN) {
                    // Still jumping
                    velocity.y = terminalVelocity.y;
                }
        }
        if (jumpState != JUMP_STATE.GROUNDED) {
            dustParticles.allowCompletion();
            super.updateMotionY(deltaTime);
        }

    }


    public enum VIEW_DIRECTION {LEFT, RIGHT}


    public enum JUMP_STATE {
        GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING
    }


}
