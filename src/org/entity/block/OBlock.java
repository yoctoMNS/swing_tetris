package org.entity.block;

import org.manager.StageManager;

public class OBlock extends Block {
    public static final int SIZE = 4;

    public OBlock(StageManager stage, int x, int y) {
        super(stage, x, y, SIZE, SIZE);
    }

    @Override
    public void init() {
        super.init();
        setColor(java.awt.Color.orange);
        addBlockData("0000011001100000");
        addBlockData("0000011001100000");
        addBlockData("0000011001100000");
        addBlockData("0000011001100000");
        rot = rand.nextInt(data.size());
    }
}
