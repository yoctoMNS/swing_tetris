package org.entity.block;

import org.manager.StageManager;

public class SBlock extends Block {
    public static final int SIZE = 5;

    public SBlock(StageManager stage, int x, int y) {
        super(stage, x, y, SIZE, SIZE);
    }

    @Override
    public void init() {
        super.init();
        setColor(new java.awt.Color(0.7f, 0.1f, 0.2f));
        addBlockData("0000000110011000000000000");
        addBlockData("0000000100001100001000000");
        addBlockData("0000000110011000000000000");
        addBlockData("0000000100001100001000000");
        rot = rand.nextInt(data.size());
    }
}
