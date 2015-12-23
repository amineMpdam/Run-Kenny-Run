package com.thugcoder.game.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Landolsi on 09/10/2015.
 */
public class CameraHelper {


    private final float MAX_ZOOM_IN = 0.25f;
    private final float MAX_ZOOM_OUT = 10.0f;
    private final float FOLLOW_SPEED = 4.0f;
    private Vector2 position;
    private float zoom;
    private com.thugcoder.game.objects.AbstractGameObject target;


    public CameraHelper() {
        position = new Vector2();
        zoom = 1.0f;
    }


    public void update(float deltaTime) {
        if (!hasTarget()) return;
        position.x = target.position.x + target.origin.x;
        position.y = target.position.y + target.origin.y;

        position.lerp(target.position, FOLLOW_SPEED * deltaTime);
        // Prevent camera from moving down too far
        position.y = Math.max(-1f, position.y);
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void addZoom(float amount) {
        setZoom(zoom + amount);
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
    }

    public com.thugcoder.game.objects.AbstractGameObject getTarget() {
        return target;
    }

    public void setTarget(com.thugcoder.game.objects.AbstractGameObject target) {
        this.target = target;
    }

    public boolean hasTarget() {
        return target != null;
    }

    public boolean hasTarget(com.thugcoder.game.objects.AbstractGameObject target) {
        return hasTarget() && this.target.equals(target);
    }

    public void applyTo(OrthographicCamera camera) {
        camera.position.x = position.x;
        camera.position.y = position.y;
        camera.zoom = zoom;
        camera.update();
    }
}
