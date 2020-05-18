package org.manager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import org.engine.Launcher;
import org.entity.block.Block;
import org.entity.block.JBlock;
import org.entity.block.LBlock;
import org.entity.block.OBlock;
import org.entity.block.IBlock;
import org.entity.block.TBlock;
import org.entity.block.SBlock;
import org.entity.block.ZBlock;

public class StageManager {
    public static final int WIDTH           = 10;
    public static final int HEIGHT          = 20;
    public static final int NONE            = 0;
    public static final int BLOCK           = 1;
    public static final int FIX             = 2;
    public static final int CLONE           = 3;

    private Random rand = new Random();
    private ArrayList<Block> blocks = new ArrayList<>();
    private GameManager game;
    private int[][] bufData;
    private int[][] drawData;
    private Block current;
    private long dropTime = 500000000;
    private long last;
    private double timer;
    private boolean[] alignCells = new boolean[HEIGHT];

    public StageManager(GameManager game) {
        init();
    }

    private void init() {
        last = System.nanoTime();
        timer = 0;
        registerBlock();
        buildStage();
        selectRandomBlock();
    }

    private void registerBlock() {
        blocks.add(new OBlock(this, (WIDTH/2) - (OBlock.SIZE / 2), -1));
        blocks.add(new IBlock(this, (WIDTH/2) - (IBlock.SIZE / 2), -1));
        blocks.add(new TBlock(this, (WIDTH/2) - (TBlock.SIZE / 2), -1));
        blocks.add(new LBlock(this, (WIDTH/2) - (LBlock.SIZE / 2), -1));
        blocks.add(new JBlock(this, (WIDTH/2) - (JBlock.SIZE / 2), -1));
        blocks.add(new SBlock(this, (WIDTH/2) - (SBlock.SIZE / 2), -1));
        blocks.add(new ZBlock(this, (WIDTH/2) - (ZBlock.SIZE / 2), -1));
    }

    private void selectRandomBlock() {
        int result = rand.nextInt(blocks.size());
        current = blocks.get(result);
    }

    private void reset() {
        selectRandomBlock();
        current.reset();
    }

    private void buildStage() {
        bufData = new int[HEIGHT][WIDTH];
        drawData = new int[HEIGHT][WIDTH];
    }

    public void update() {
        long now = System.nanoTime();
        timer += now - last;
        last = now;
        if (timer > dropTime) {
            if (!isCollisionBottom()) {
                current.update();
            } else {
                blockFix();
                removeAlignCells();
                reset();
            }
            timer = 0;
        }
        margeStage();
        margeBlock();
    }

    public void render(Graphics2D g) {
        drawStage(g);
        drawClone(g);
    }

    private void drawStage(Graphics2D g) {
        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                switch (drawData[i][j]) {
                case NONE:
                    g.setColor(Color.white);
                    g.fillRect(j * Block.WIDTH_SCALE, i * Block.HEIGHT_SCALE, Block.WIDTH_SCALE, Block.HEIGHT_SCALE);
                    g.setColor(Color.black);
                    g.drawRect(j * Block.WIDTH_SCALE, i * Block.HEIGHT_SCALE, Block.WIDTH_SCALE, Block.HEIGHT_SCALE);
                    break;

                case BLOCK:
                    g.setColor(current.getColor());
                    g.fillRect(j * Block.WIDTH_SCALE, i * Block.HEIGHT_SCALE, Block.WIDTH_SCALE, Block.HEIGHT_SCALE);
                    g.setColor(Color.black);
                    g.drawRect(j * Block.WIDTH_SCALE, i * Block.HEIGHT_SCALE, Block.WIDTH_SCALE, Block.HEIGHT_SCALE);
                    break;

                case FIX:
                    g.setColor(Color.gray);
                    g.fillRect(j * Block.WIDTH_SCALE, i * Block.HEIGHT_SCALE, Block.WIDTH_SCALE, Block.HEIGHT_SCALE);
                    g.setColor(Color.black);
                    g.drawRect(j * Block.WIDTH_SCALE, i * Block.HEIGHT_SCALE, Block.WIDTH_SCALE, Block.HEIGHT_SCALE);
                    break;

                default:
                    g.setColor(Color.red);
                    g.fillRect(j * Block.WIDTH_SCALE, i * Block.HEIGHT_SCALE, Block.WIDTH_SCALE, Block.HEIGHT_SCALE);
                    g.setColor(Color.black);
                    g.drawRect(j * Block.WIDTH_SCALE, i * Block.HEIGHT_SCALE, Block.WIDTH_SCALE, Block.HEIGHT_SCALE);
                    break;
                }
            }
        }
    }

    private void drawClone(Graphics2D g) {
        int cx = current.getX();
        int cy = current.getY();
        int width = current.getWidth();
        int height = current.getHeight();

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (current.isBlockCell(j, i)) {
                    while (true) {
                        if (!isCollisionBottom(cx, cy)) {
                            ++cy;
                        } else {
                            break;
                        }
                    }
                }
            }
        }

        if (current.getY() != cy) {
            for (int i = 0; i< height; ++i) {
                for (int j = 0; j < width; ++j) {
                    if (current.isBlockCell(j, i)) {
                        g.setColor(Color.darkGray);
                        g.fillRect((cx + j) * Block.WIDTH_SCALE, (cy + i) * Block.HEIGHT_SCALE, Block.WIDTH_SCALE, Block.HEIGHT_SCALE);
                        g.setColor(Color.black);
                        g.drawRect((cx + j) * Block.WIDTH_SCALE, (cy + i) * Block.HEIGHT_SCALE, Block.WIDTH_SCALE, Block.HEIGHT_SCALE);
                    }
                }
            }
        }
    }

    public void keyPressed(int key) {
        switch (key) {
        case KeyEvent.VK_UP:
            if (canBlockRotate()) {
                current.rotate();
            }
            break;

        case KeyEvent.VK_DOWN:
            hardDrop();
            break;

        case KeyEvent.VK_LEFT:
            if (!isCollisionLeft()) {
                current.moveLeft();
            }
            break;

        case KeyEvent.VK_RIGHT:
            if (!isCollisionRight()) {
                current.moveRight();
            }
            break;

        case KeyEvent.VK_SPACE:
            hold();
            break;
        }
    }

    public void keyReleased(int key) {
    }

    public void keyTyped(int key) {
    }

    private void hold() {

    }

    private void margeStage() {
        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                drawData[i][j] = bufData[i][j];
            }
        }
    }

    private void margeBlock() {
        int x = current.getX();
        int y = current.getY();

        for (int i = 0; i < current.getWidth(); ++i) {
            for (int j = 0; j < current.getHeight(); ++j) {
                if (isOutOfStage(j+x, i+y)) continue;
                drawData[i+y][j+x] |= current.getBlockCellData(j, i);
            }
        }
    }

    private boolean isCollisionLeft() {
        int x = current.getX();
        int y = current.getY();

        for (int i = 0; i < current.getWidth(); ++i) {
            for (int j = 0; j < current.getHeight(); ++j) {
                if (current.isBlockCell(j, i)) {
                    if (isOutOfStage(j+x-1, i+y)) return true;
                    if (!isNoneCell(j+x-1, i+y)) return true;
                }
            }
        }
        return false;
    }

    private boolean isCollisionRight() {
        int x = current.getX();
        int y = current.getY();

        for (int i = 0; i < current.getWidth(); ++i) {
            for (int j = 0; j < current.getHeight(); ++j) {
                if (current.isBlockCell(j, i)) {
                    if (isOutOfStage(j+x+1, i+y)) return true;
                    if (!isNoneCell(j+x+1, i+y)) return true;
                }
            }
        }
        return false;
    }

    private boolean isCollisionBottom() {
        int x = current.getX();
        int y = current.getY();

        for (int i = 0; i < current.getWidth(); ++i) {
            for (int j = 0; j < current.getHeight(); ++j) {
                if (current.isBlockCell(j, i)) {
                    if (isOutOfStage(j+x, i+y+1)) return true;
                    if (!isNoneCell(j+x, i+y+1)) return true;
                }
            }
        }
        return false;
    }

    private boolean isCollisionBottom(int x, int y) {
        for (int i = 0; i < current.getWidth(); ++i) {
            for (int j = 0; j < current.getHeight(); ++j) {
                if (current.isBlockCell(j, i)) {
                    if (isOutOfStage(j+x, i+y+1)) return true;
                    if (!isNoneCell(j+x, i+y+1)) return true;
                }
            }
        }
        return false;
    }

    private void blockFix() {
        int x = current.getX();
        int y = current.getY();

        for (int i = 0; i < current.getWidth(); ++i) {
            for (int j = 0; j < current.getHeight(); ++j) {
                if (current.isBlockCell(j, i)) {
                    if (isOutOfStage(j+x, i+y)) continue;
                    bufData[i+y][j+x] = FIX;
                }
            }
        }
    }

    private void hardDrop() {
        while (true) {
            if (!isCollisionBottom()) {
                current.moveBottom();
            } else {
                break;
            }
        }
    }

    private boolean canBlockRotate() {
        int rot = current.getRot();
        if (++rot >= current.getData().size()) {
            rot = 0;
        }
        Integer[][] next = current.getBlock(rot);
        int x = current.getX();
        int y = current.getY();

        for (int i = 0; i < current.getWidth(); ++i) {
            for (int j = 0; j < current.getHeight(); ++j) {
                if (next[i][j] == BLOCK) {
                    if (isOutOfStage(j+x, i+y)) return false;
                    if (!isNoneCell(j+x, i+y)) return false;
                }
            }
        }

        return true;
    }

    private boolean isAlignCell(int y) {
        boolean flg = true;

        for (int x = 0; x < WIDTH; ++x) {
            if (bufData[y][x] == NONE) {
                flg = false;
            }
        }

        return flg;
    }

    private void removeAlignCells() {
        for (int y = 0; y < HEIGHT; ++y) {
            if (isAlignCell(y)) {
                alignCells[y] = true;
                for (int x = 0; x < WIDTH; ++x) {
                    bufData[y][x] = NONE;
                }
            }
        }

        int idx = HEIGHT-1;
        for (int y = HEIGHT-1; y > 0; --y) {
            if (!alignCells[y]) {
                for (int x = 0; x < WIDTH; ++x) {
                    bufData[idx][x] = bufData[y][x];
                }
                --idx;
            }
        }

        for (int y = 0; y < HEIGHT; ++y) {
            alignCells[y] = false;
        }
    }

    private boolean isNoneCell(int x, int y) {
        return bufData[y][x] == NONE;
    }

    private boolean isOutOfStage(int x, int y) {
        return x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT;
    }
}
