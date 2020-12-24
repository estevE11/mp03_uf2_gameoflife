package cat.esteve.gol.main;

import cat.esteve.gol.gfx.Camera;
import cat.esteve.gol.gfx.MainCanvas;
import cat.esteve.gol.input.KeyboardInput;
import cat.esteve.gol.input.MouseInput;
import cat.esteve.gol.level.Level;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class Main implements Runnable {

    private JFrame frame;
    private final String frame_name = "Game of Life";
    private int WIDTH = 400, HEIGHT = 300;
    private boolean frame_resizeable = true;

    private Camera camera;
    private MainCanvas canvas;
    private Level level;
    private KeyboardInput kb;
    private MouseInput mouse;

    private int mx = 0, my = 0;

    private int fps = 0, tps = 0, ffps = 0, ftps = 0;

    private void start_thread(String thread_name) {
        Thread thread = new Thread(this, thread_name);
        thread.start();
    }

    private void init() {
        this.frame.setSize(1080, 720);
        this.level = new Level(this, this.camera);
        this.kb = new KeyboardInput(this);
        canvas.addKeyListener(this.kb);
        this.mouse = new MouseInput(this);
        canvas.addMouseListener(this.mouse);
        canvas.addMouseMotionListener(this.mouse);
        canvas.addMouseWheelListener(this.mouse);
   }

    @Override
    public void run() {
        this.canvas.requestFocus();
        init();
        int fps = 0;
        long lastTime = System.nanoTime();
        double unprocessed = 0;
        double nsPerTick = 1000000000.0 / 60;
        long lastTimer1 = System.currentTimeMillis();

        while (true) {
            long now = System.nanoTime();
            unprocessed += (now - lastTime) / nsPerTick;
            lastTime = now;
            while (unprocessed >= 1) {
                tps++;
                update();
                unprocessed -= 1;
            }

            fps++;
            render();

            if (System.currentTimeMillis() - lastTimer1 > 1000) {
                lastTimer1 += 1000;
                this.frame.setTitle("FPS: " + fps + ", TPS: " + tps);
                ffps = fps;
                ftps = tps;
                fps = 0;
                tps = 0;
            }
        }
    }

    private void update() {
        this.level.update();

        this.camera.update();
    }

    private void render() {
        if(!canvas.start()) return;
        canvas.clear();
        this.level.render(canvas);
        renderHUD();
        canvas.end();
    }

    private void renderHUD() {
    }

    public void onKeyDown(int key) {
        this.level.onKeyDown(key);
    }

    public void onKeyUp(int key) {
        this.level.onKeyUp(key);
    }

    public void mousePressed(MouseEvent evt) {
        this.level.mousePressed(evt.getButton(), evt.getX(), evt.getY());
        this.camera.mousePressed(evt.getButton(), evt.getX(), evt.getY());
    }

    public void mouseReleassed(MouseEvent evt) {
        this.level.mouseReleassed(evt.getX(), evt.getY());
        this.camera.mouseReleassed(evt.getX(), evt.getY());
    }

    public void mouseMoved(MouseEvent evt) {
        this.camera.mouseMoved(evt.getX(), evt.getY());
        this.level.mouseMoved(evt.getX(), evt.getY());
        this.mx = evt.getX();
        this.my = evt.getY();
    }

    public void mouseDragged(MouseEvent evt) {
        this.camera.mouseDragged(evt.getButton(), evt.getX(), evt.getY());
        this.level.mouseDragged(evt.getButton(), evt.getX(), evt.getY());
        this.mx = evt.getX();
        this.my = evt.getY();
    }

    public void mouseWheelMoved(MouseWheelEvent evt) {
        this.camera.mouseWheelMoved(evt.getUnitsToScroll()*-1, evt.isControlDown());
        this.level.mouseWheelMoved(evt.getUnitsToScroll()*-1, evt.isControlDown());
    }

    public Point getFrameCurrentSize() {
        return new Point(this.canvas.getWidth(), this.canvas.getHeight());
    }

    public void start() {
        this.frame = new JFrame(this.frame_name);
        this.camera = new Camera();
        this.canvas = new MainCanvas(this.camera);

        this.frame.setPreferredSize(new Dimension(this.WIDTH, this.HEIGHT));
        this.frame.setMinimumSize(new Dimension(this.WIDTH, this.HEIGHT));

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLocation(10, 10);
        this.frame.setResizable(this.frame_resizeable);
        this.frame.setVisible(true);

        this.frame.add(canvas);

        this.frame.requestFocus();

        this.start_thread("Main Thread");
    }
}

