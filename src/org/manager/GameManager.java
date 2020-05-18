package org.manager;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class GameManager extends JFrame implements Runnable, KeyListener {
    private static int fps = 30;
    private static double targetFps = 1000000000 / fps;
    private boolean running;
    private Thread gameThread;
    private Canvas canvas;
    private BufferStrategy bs;
    private Graphics2D g;
    private StageManager stage;

    public GameManager(String title, int width, int height) {
        super(title);
        createFrame(width, height);
        start();
    }

    private void createFrame(int width, int height) {
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        createCanvas();
    }

    private void createCanvas() {
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(getWidth(), getHeight()));
        canvas.setMaximumSize(new Dimension(getWidth(), getHeight()));
        canvas.setMinimumSize(new Dimension(getWidth(), getHeight()));
        canvas.setFocusable(true);
        canvas.setBackground(java.awt.Color.black);

        add(canvas);
        pack();
    }

    private void init() {
        canvas.addKeyListener(this);
        stage = new StageManager(this);
    }

    private synchronized void start() {
        if (running) {
            return;
        }

        running = true;
        gameThread = new Thread(this, "Game Thread");
        gameThread.start();
    }

    private void render() {
        bs = canvas.getBufferStrategy();

        if (bs == null) {
            canvas.createBufferStrategy(3);
            return;
        }

        g = (Graphics2D)bs.getDrawGraphics();
        g.clearRect(0, 0, getWidth(), getHeight());

        stage.render(g);

        bs.show();
        g.dispose();
    }

    private void update() {
        stage.update();
    }

    private void gameLoop() {
        update();
        render();
    }

    @Override
    public void run() {
        long now = 0;
        long last = System.nanoTime();
        double delta = 0;

        init();

        while (running) {
            now = System.nanoTime();
            delta += (now - last) / targetFps;
            last = now;

            if (delta >= 1) {
                gameLoop();
                --delta;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        stage.keyPressed(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        stage.keyReleased(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        stage.keyTyped(e.getKeyCode());
    }

    public boolean isRunning() {
        return running;
    }
}
