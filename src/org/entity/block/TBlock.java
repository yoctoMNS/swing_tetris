package org.entity.block;

import org.manager.StageManager;

public class TBlock extends Block {
    public static final int SIZE = 5;

    public TBlock(StageManager stage, int x, int y) {
        super(stage, x, y, SIZE, SIZE);
    }

    @Override
    public void init() {
        super.init();
        setColor(java.awt.Color.blue);
        addBlockData("000000000001110001000000000000");
        addBlockData("000000010000110001000000000000");
        addBlockData("000000010001110000000000000000");
        addBlockData("000000010001100001000000000000");
        rot = rand.nextInt(data.size());
    }
}
