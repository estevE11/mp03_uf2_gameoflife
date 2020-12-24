package cat.esteve.gol.gfx;

import cat.esteve.gol.level.Level;

import java.awt.*;

public class Camera {
    private int x, y;
    private int offsetX, offsetY;
    private double zoom = 1;

    private int mbtn = 0;

    private int oldMx, oldMy, dx, dy;

    public Camera() {
        this.x = 0;
        this.y = 0;
    }

    public void update() {

        // Final calc
        this.offsetX = this.x*-1;
        this.offsetY = this.y*-1;
    }

    public Point screenToWorld(int sx, int sy) {
        int x = sx-this.offsetX;
        int y = sy-this.offsetY;
        return new Point((int)(x/this.zoom), (int)(y/this.zoom));
    }

    public void mousePressed(int btn, int x, int y) {
        this.mbtn = btn;
    }

    public void mouseReleassed(int x, int y) {
        this.mbtn = 0;
    }

    public void mouseMoved(int mx, int my) {
        this.dx = this.oldMx - mx;
        this.dy = this.oldMy - my;
        this.oldMx = mx;
        this.oldMy = my;
    }

    public void mouseDragged(int btn, int mx, int my) {
        this.dx = this.oldMx - mx;
        this.dy = this.oldMy - my;

        if(this.mbtn == 2) {
            if (this.dx != 0) {
                this.move(dx, 0);
            }
            if (this.dy != 0) {
                this.move(0, dy);
            }
        }

        this.oldMx = mx;
        this.oldMy = my;
    }

    public void mouseWheelMoved(int steps, boolean ctrl) {
        if(ctrl) return;
        this.zoom((this.zoom/100)*steps);
    }

    public void move(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void zoom(double steps) {
        this.zoom += steps;
    }

    public double getZoom() {
        return this.zoom;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getOffsetX() {
        return this.offsetX;
    }

    public int getOffsetY() {
        return this.offsetY;
    }
}
