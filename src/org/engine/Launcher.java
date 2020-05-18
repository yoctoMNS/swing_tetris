package org.engine;

import org.manager.GameManager;

public class Launcher {
    public static final String TITLE = "Tetris";
    public static final int WIDTH = 400;
    public static final int HEIGHT = 800;

    public static void main(String... args) {
        new GameManager(TITLE, WIDTH, HEIGHT);
    }
}
