package org.entity.block;

import org.manager.StageManager;

public class IBlock extends Block {
    public static final int SIZE = 4;

    public IBlock(StageManager stage, int x, int y) {
        super(stage, x, y, SIZE, SIZE);
    }

    @Override
    public void init() {
        super.init();
        setColor(java.awt.Color.pink);
        addBlockData("0010001000100010");
        addBlockData("0000111100000000");
        addBlockData("0100010001000100");
        addBlockData("0000000011110000");
        rot = rand.nextInt(data.size());
    }
}
