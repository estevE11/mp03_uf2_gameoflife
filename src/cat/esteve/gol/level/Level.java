package cat.esteve.gol.level;

import cat.esteve.gol.gfx.Camera;
import cat.esteve.gol.gfx.MainCanvas;
import cat.esteve.gol.main.Main;

import java.awt.*;
import java.util.Random;

public class Level {
    public static int TILE_W = 32, D_TILE_W = 32;
    public static int WIDTH = 100, HEIGHT = 100;

    private Random r = new Random();

    private Main main;
    private Camera camera;

    private boolean[][] tiles;

    private int tickCount = 0, tickRate = 15;
    private boolean running = true;

    private int mx = 0, my = 0, mbtn = 0;

    public Level(Main main, Camera camera) {
        this.main = main;
        this.camera = camera;

        this.init();
    }

    public void init() {
        this.loadLevel();
    }

    public void loadLevel() {
        this.tiles = new boolean[WIDTH][HEIGHT];
        for(int y = 0; y < HEIGHT; y++) {
            for(int x = 0; x < WIDTH; x++) {
                this.tiles[x][y] = r.nextBoolean();
            }
        }
    }

    public void autoGen() {

    }

    public void emptyLevel() {
        this.tiles = new boolean[WIDTH][HEIGHT];
    }

    public void update() {
        if(running) {
            if(this.tickCount % tickRate == 0) this.updateBoard();
            this.tickCount++;
        }
    }

    private void updateBoard() {
        boolean[][] new_board = new boolean[WIDTH][HEIGHT];
        for(int y = 0; y < this.tiles[0].length; y++) {
            for (int x = 0; x < this.tiles.length; x++) {
                new_board[x][y] = updateCell(x, y);
            }
        }
        this.tiles = new_board;
    }

    public boolean updateCell(int x, int y) {
        int count = 0;
        for(int yy = y-1; yy < y+2; yy++) {
            for (int xx = x-1; xx < x+2; xx++) {
                if((xx == x && yy == y)) continue;
                if(this.getTile(xx, yy)) count++;
            }
        }
        if(!this.tiles[x][y] && count == 3) return true;
        if(this.tiles[x][y] && (count == 2 || count == 3)) return true;
        return false;
    }

    public void render(MainCanvas canvas) {
        //Tiles renders
        for(int y = 0; y < this.tiles[0].length; y++) {
            for (int x = 0; x < this.tiles.length; x++) {
                this.renderTile(canvas, x, y, this.tiles[x][y]);
            }
        }

        this.renderHUD(canvas);
    }

    private void renderHUD(MainCanvas canvas) {
        // Se que aquest codi es basicament una merda, pero demoment esta fet perque funcioni :D
        //Back
        int w = 200, h = 50;
        canvas.HUD_fill(this.main.getFrameCurrentSize().x/2 - w/2, this.main.getFrameCurrentSize().y-h, w, h, Color.black, .4f);
        //Pause indicator
        w = 30;
        canvas.HUD_fill(this.main.getFrameCurrentSize().x/2 - w/2, this.main.getFrameCurrentSize().y-w - 10, w, w, this.running ? Color.green : Color.red);
        //Tick rate indicator
        canvas.HUD_renderText(this.getTickRate()+"", this.main.getFrameCurrentSize().x/2 - w/2 + 60, this.main.getFrameCurrentSize().y-w + 15, w, Color.white);
    }

    private void renderTile(MainCanvas canvas, int x, int y, boolean alive) {
        int stroke = 2;
        int xx = x*TILE_W;
        int yy = y*TILE_W;
        if(alive) {
            canvas.fill(xx, yy, D_TILE_W, D_TILE_W, new Color(133, 133, 0));
            canvas.fill(xx + stroke, yy + stroke, D_TILE_W - stroke*2, D_TILE_W - stroke*2, new Color(255, 255, 0));
        } else {
            canvas.fill(xx, yy, D_TILE_W, D_TILE_W, new Color(53, 53, 53));
            canvas.fill(xx + stroke, yy + stroke, D_TILE_W - stroke*2, D_TILE_W - stroke*2, new Color(126, 126, 126));
        }
    }

    public void onKeyDown(int key) {
        if(key == 32) this.running = !this.running;
        if(key == 39 && !this.running) this.updateBoard();
        if(key == 69) this.emptyLevel();
    }

    public void onKeyUp(int key) {
    }

    public void mousePressed(int btn, int mx, int my) {
        this.mbtn = btn;
        switch (btn) {
            case 1:
            case 3:
                mouseSetTile(mx, my);
        }
    }

    public void mouseReleassed(int x, int y) {
        this.mbtn = 0;
    }

    public void mouseMoved(int mx, int my) {
        this.mx = mx;
        this.my = my;
    }

    public void mouseDragged(int b, int mx, int my) {
        this.mx = mx;
        this.my = my;
        switch (this.mbtn) {
            case 1:
            case 3:
                this.mouseSetTile(mx, my);
        }
    }

    public void mouseWheelMoved(int steps, boolean ctrl) {
        if (ctrl) {
            this.tickRate += steps > 0 ? -1 : 1;
            if (this.tickRate > 20) this.tickRate = 20;
            if (this.tickRate < 1) this.tickRate = 1;
        }
    }

    public void mouseSetTile(int mx, int my) {
        Point m = camera.screenToWorld(mx, my);
        Point t = this.worldToTile(m.x, m.y);
        this.setTile(t.x, t.y, this.mbtn == 1);
    }

    public void setTile(int x, int y, boolean a) {
        if(x >= this.tiles.length || x < 0 || y >= this.tiles[0].length || y < 0) {
            return;
        }
        this.tiles[x][y] = a;
    }

    public boolean getTile(int x, int y) {
        int rx = x;
        int ry = y;


        if(x >= this.tiles.length || x < 0) rx = this.tiles.length-Math.abs(x);
        if(y >= this.tiles[0].length || y < 0) ry = this.tiles[0].length-Math.abs(y);

        return this.tiles[rx][ry];
    }

    public boolean getTileAt(int x, int y) {
        int xx = x/D_TILE_W;
        int yy = y/D_TILE_W;
        int rx = xx;
        int ry = yy;


        if(xx >= this.tiles.length || xx < 0) rx = this.tiles.length-xx;
        if(yy >= this.tiles[0].length || yy < 0) ry = this.tiles[0].length-yy;

        return this.tiles[rx][ry];
    }

    public Point worldToTile(int x, int y) {
        return new Point(x/D_TILE_W, y/D_TILE_W);
    }

    public int getTickRate() {
        return 60/this.tickRate;
    }
}
