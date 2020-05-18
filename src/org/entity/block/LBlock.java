package org.entity.block;

import org.manager.StageManager;

public class LBlock extends Block {
    public static final int SIZE = 5;

    public LBlock(StageManager stage, int x, int y) {
        super(stage, x, y, SIZE, SIZE);
    }

    @Override
    public void init() {
        super.init();
        setColor(java.awt.Color.cyan);
        addBlockData("0010000100001100000000000");
        addBlockData("0000000100111000000000000");
        addBlockData("0000000000011000010000100");
        addBlockData("0000000000001110010000000");
        rot = rand.nextInt(data.size());
    }
}
