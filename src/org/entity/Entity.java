package org.entity;

import org.manager.StageManager;

public abstract class Entity {
    protected StageManager stage;
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public Entity(StageManager stage, int x, int y, int width, int height) {
        this.stage = stage;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        init();
    }

    public abstract void init();

    public abstract void update();

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
