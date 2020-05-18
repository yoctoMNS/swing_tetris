package org.entity.block;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import org.entity.Entity;
import org.manager.StageManager;

public class Block extends Entity {
    public static final int WIDTH_SCALE = 40;
    public static final int HEIGHT_SCALE = 40;

    protected Random rand;
    protected ArrayList<Integer[][]> data;
    protected Color color;
    protected int defaultX;
    protected int defaultY;
    protected int rot;

    public Block(StageManager stage, int x, int y, int width, int height) {
        super(stage, x, y, width, height);
        defaultX = x;
        defaultY = y;
    }

    @Override
    public void init() {
        rand = new Random();
        data = new ArrayList<>();
    }

    public void reset() {
        rot = rand.nextInt(data.size());
        x = defaultX;
        y = defaultY;
    }

    protected void addBlockData(String buf) {
        char[] cData = buf.toCharArray();
        Integer[][] tmp = new Integer[height][width];

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                tmp[i][j] = Character.getNumericValue(cData[i * width + j]);
            }
        }

        data.add(tmp);
    }

    @Override
    public void update() {
        moveBottom();
    }

    public void moveLeft() {
        --x;
    }

    public void moveRight() {
        ++x;
    }

    public void moveBottom() {
        ++y;
    }

    public void rotate() {
        if (++rot >= data.size()) {
            rot = 0;
        }
    }

    public int getBlockCellData(int x, int y) {
        Integer[][] select = data.get(rot);
        return select[y][x];
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public Integer[][] getBlock(int idx) {
        return data.get(idx);
    }

    public int getRot() {
        return rot;
    }

    public ArrayList<Integer[][]> getData() {
        return data;
    }

    public boolean isBlockCell(int x, int y) {
        Integer[][] select = data.get(rot);
        return select[y][x] == StageManager.BLOCK;
    }
}
