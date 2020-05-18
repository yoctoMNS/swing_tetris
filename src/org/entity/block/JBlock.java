package org.entity.block;

import org.manager.StageManager;

public class JBlock extends Block {
    public static final int SIZE = 5;

    public JBlock(StageManager stage, int x, int y) {
        super(stage, x, y, SIZE, SIZE);
    }

    @Override
    public void init() {
        super.init();
        setColor(new java.awt.Color(0.5f, 0.4f, 0.7f));
        addBlockData("0010000100011000000000000");
        addBlockData("0000000000111000010000000");
        addBlockData("0000000000001100010000100");
        addBlockData("0000000100001110000000000");
        rot = rand.nextInt(data.size());
    }
}
