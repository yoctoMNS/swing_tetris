package org.entity.block;

import org.manager.StageManager;

public class ZBlock extends Block {
    public static final int SIZE = 5;

    public ZBlock(StageManager stage, int x, int y) {
        super(stage, x, y, SIZE, SIZE);
    }

    @Override
    public void init() {
        super.init();
        setColor(new java.awt.Color(0.2f, 0.6f, 0.5f));
        addBlockData("0000001100001100000000000");
        addBlockData("0000000100011000100000000");
        addBlockData("0000001100001100000000000");
        addBlockData("0000000100011000100000000");
        rot = rand.nextInt(data.size());
    }
}
