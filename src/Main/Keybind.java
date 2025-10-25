package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keybind implements KeyListener {

    public boolean keAtas, keBawah, keKanan, keKiri;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            keAtas = true;
        }
        if (code == KeyEvent.VK_S) {
            keBawah = true;
        }
        if (code == KeyEvent.VK_A) {
            keKiri = true;
        }
        if (code == KeyEvent.VK_D) {
            keKanan = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            keAtas = false;
        }
        if (code == KeyEvent.VK_S) {
            keBawah = false;
        }
        if (code == KeyEvent.VK_A) {
            keKiri = false;
        }
        if (code == KeyEvent.VK_D) {
            keKanan = false;
        }
    }

}
