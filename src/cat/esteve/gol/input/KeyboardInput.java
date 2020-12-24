package cat.esteve.gol.input;

import cat.esteve.gol.main.Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {
    private Main main;

    public KeyboardInput(Main main) {
        this.main = main;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        this.main.onKeyDown(keyEvent.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        this.main.onKeyUp(keyEvent.getKeyCode());
    }
}
