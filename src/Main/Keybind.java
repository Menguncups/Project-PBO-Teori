package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Entity.Entity;

public class Keybind implements KeyListener {
    Entity entity;
    GamePanel gamePanel;
    Sound sound;
    public boolean keAtas, keBawah, keKanan, keKiri;

    public Keybind(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

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
        if (code == KeyEvent.VK_ESCAPE) {
            if (gamePanel.gameState == gamePanel.playState) {
                gamePanel.music.pause();
                gamePanel.gameState = gamePanel.pauseState;

            } else if (gamePanel.gameState == gamePanel.pauseState) {
                gamePanel.music.resume();
                gamePanel.gameState = gamePanel.playState;
            }
        }
        if (code == KeyEvent.VK_SPACE) {
            if (gamePanel.gameState == gamePanel.dialogueState) {
                Ui ui = gamePanel.ui;
                Entity npc = gamePanel.npc[0]; // atau NPC yang sedang diajak bicara

                ui.currentDialogIndex++;

                if (ui.currentDialogIndex < npc.dialouges.length && npc.dialouges[ui.currentDialogIndex] != null) {
                    ui.currentDialog = npc.dialouges[ui.currentDialogIndex];
                } else {
                    // kalau sudah habis, kembali ke playState
                    gamePanel.gameState = gamePanel.playState;
                }
            }
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
