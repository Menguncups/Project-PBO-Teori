package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import Entity.Entity;

public class Keybind implements KeyListener {
    GamePanel gamePanel;
    public boolean keAtas, keBawah, keKanan, keKiri, spasiDitekan, enterDitekan;
    int i = 1;

    public Keybind(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // ========== F12 DEBUG TOGGLE ==========
        if (code == KeyEvent.VK_F12) {
            gamePanel.debugMode.toggleDebug();
            return;
        }

        // ========== DEBUG SCROLL CONTROL (Arrow Up/Down) ==========
        if (gamePanel.debugMode.debugOn) {
            if (code == KeyEvent.VK_UP) {
                gamePanel.debugMode.scrollLeft(-1);
                return;
            } else if (code == KeyEvent.VK_DOWN) {
                gamePanel.debugMode.scrollLeft(1);
                return;
            }
        }

        // ========== TITLE STATE ==========
        if (gamePanel.gameState == gamePanel.titleState) {
            // System.out.println("gameState: 1");
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gamePanel.playSE(6);
                gamePanel.ui.commandNum--;
                if (gamePanel.ui.commandNum < 0)
                    gamePanel.ui.commandNum = 1;
            } else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gamePanel.playSE(6);
                gamePanel.ui.commandNum++;
                if (gamePanel.ui.commandNum > 1)
                    gamePanel.ui.commandNum = 0;
            } else if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                gamePanel.playSE(6);
                if (gamePanel.ui.commandNum == 0) {
                    gamePanel.gameState = gamePanel.playState;
                    // gamePanel.playMusic(0); // Nyalakan musik game
                } else if (gamePanel.ui.commandNum == 1) {
                    System.exit(0);
                }
            }
        }
        // ========== PLAY STATE ==========
        else if (gamePanel.gameState == gamePanel.playState) {
            // System.out.println("gameState: 1");

            if (code == KeyEvent.VK_W) {
                keAtas = true;
                // System.out.println("W ditekan bergerak ke atas");
            } else {
                keAtas = false;
                // System.out.println("W dilepas ");
            }

            if (code == KeyEvent.VK_S) {
                // System.out.println("S ditekan begerak ke bawah");
                keBawah = true;
            } else {
                keBawah = false;
                // System.out.println("S dilepas ");
            }

            if (code == KeyEvent.VK_A) {
                keKiri = true;
                // System.out.println("A ditekan bergerak ke kiri");
            } else {
                keKiri = false;
                // System.out.println("A dilepas ");
            }

            if (code == KeyEvent.VK_D) {
                keKanan = true;
                // System.out.println("D ditekan begerak ke kanan");
            } else {
                keKanan = false;
                // System.out.println("D dilepas ");
            }

            if (code == KeyEvent.VK_SPACE) {
                System.out.println("Spasi Ditekan " + i + 'x');
                spasiDitekan = true;
                i++;
            }

            if (code == KeyEvent.VK_ENTER)
                enterDitekan = true;

            // Tombol Menu
            if (code == KeyEvent.VK_C)
                gamePanel.gameState = gamePanel.inventoryState;
            if (code == KeyEvent.VK_ESCAPE)
                gamePanel.gameState = gamePanel.pauseState;
        }

        // ========== PAUSE STATE ==========
        else if (gamePanel.gameState == gamePanel.pauseState) {
            if (code == KeyEvent.VK_ESCAPE) {
                gamePanel.gameState = gamePanel.playState;
            }
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gamePanel.playSE(6);
                gamePanel.ui.commandNum--;
                if (gamePanel.ui.commandNum < 0)
                    gamePanel.ui.commandNum = 2;
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gamePanel.playSE(6);
                gamePanel.ui.commandNum++;
                if (gamePanel.ui.commandNum > 2)
                    gamePanel.ui.commandNum = 0;
            }
            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                gamePanel.playSE(6);
                if (gamePanel.ui.commandNum == 0) { // Resume
                    gamePanel.gameState = gamePanel.playState;
                } else if (gamePanel.ui.commandNum == 1) { // Back to Title
                    gamePanel.gameState = gamePanel.titleState;
                    resetGame();
                } else if (gamePanel.ui.commandNum == 2) { // Quit
                    System.exit(0);
                }
            }
        }

        // ========== GAME OVER STATE ==========
        else if (gamePanel.gameState == gamePanel.gameOverState) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gamePanel.playSE(6);
                gamePanel.ui.commandNum--;
                if (gamePanel.ui.commandNum < 0)
                    gamePanel.ui.commandNum = 1;
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gamePanel.playSE(6);
                gamePanel.ui.commandNum++;
                if (gamePanel.ui.commandNum > 1)
                    gamePanel.ui.commandNum = 0;
            }
            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                gamePanel.playSE(6);
                if (gamePanel.ui.commandNum == 0) { // Retry
                    // Reset the whole game state (objects, NPCs, enemies) and
                    // return to map 0 so leftover enemies from other maps don't persist.
                    resetGame();
                    gamePanel.gameState = gamePanel.playState;
                } else if (gamePanel.ui.commandNum == 1) { // Quit to Title
                    gamePanel.gameState = gamePanel.titleState;
                    resetGame();
                }
            }
        }

        // ========== GAME WIN STATE (THE END) ==========
        else if (gamePanel.gameState == gamePanel.gameWinState) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gamePanel.playSE(6);
                gamePanel.ui.commandNum--;
                if (gamePanel.ui.commandNum < 0)
                    gamePanel.ui.commandNum = 1;
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gamePanel.playSE(6);
                gamePanel.ui.commandNum++;
                if (gamePanel.ui.commandNum > 1)
                    gamePanel.ui.commandNum = 0;
            }
            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                gamePanel.playSE(6);
                if (gamePanel.ui.commandNum == 0) { // Back to Title
                    gamePanel.gameState = gamePanel.titleState;
                    gamePanel.currentMap = 0;
                    resetGame();
                } else if (gamePanel.ui.commandNum == 1) { // Quit Game
                    System.exit(0);
                }
            }
        }

        // ========== INVENTORY STATE ==========
        else if (gamePanel.gameState == gamePanel.inventoryState) {
            if (code == KeyEvent.VK_C || code == KeyEvent.VK_ESCAPE) {
                gamePanel.gameState = gamePanel.playState;
            }
            if (code == KeyEvent.VK_W) {
                gamePanel.ui.slotRow--;
                gamePanel.playSE(6);
            }
            if (code == KeyEvent.VK_S) {
                gamePanel.ui.slotRow++;
                gamePanel.playSE(6);
            }
            if (code == KeyEvent.VK_A) {
                gamePanel.ui.slotCol--;
                gamePanel.playSE(6);
            }
            if (code == KeyEvent.VK_D) {
                gamePanel.ui.slotCol++;
                gamePanel.playSE(6);
            }
            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                spasiDitekan = true; // Gunakan Item
            }
        }

        // ========== DIALOGUE STATE ==========
        else if (gamePanel.gameState == gamePanel.dialogueState) {
            if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
                // Maju ke dialog berikutnya
                gamePanel.ui.currentDialogIndex++;

                // Pastikan tidak melebihi jumlah dialog NPC
                if (gamePanel.ui.currentDialogIndex < gamePanel.npc[0].dialouges.length) {
                    // Update teks di UI sesuai index
                    gamePanel.ui.currentDialog = gamePanel.npc[0].dialouges[gamePanel.ui.currentDialogIndex];
                } else {
                    // Jika dialog habis, kembali ke play state
                    gamePanel.gameState = gamePanel.playState;
                    gamePanel.ui.currentDialogIndex = 0; // reset index
                }
            }
        }

        // ========== GLOBAL KEYS ==========
        // Fullscreen
        if (code == KeyEvent.VK_F11) {
            if (Main.window.isUndecorated())
                gamePanel.setWindowedMode();
            else
                gamePanel.setFullScreen();
        }
    }

    // Method Helper untuk Reset Game Bersih
    private void resetGame() {
        gamePanel.player.posisiAwal();
        gamePanel.player.life = gamePanel.player.maxLife;
        gamePanel.player.inventory.clear();
        gamePanel.currentMap = 0; // Balik ke map awal

        // Reset Semua Objek & Musuh
        for (int i = 0; i < gamePanel.maxMap; i++) {
            gamePanel.assetObject.setObject(i);
            gamePanel.assetObject.setNPC(i);
            gamePanel.assetObject.setEnemy(i);
        }

        // Update referensi array ke map 0
        gamePanel.obj = gamePanel.mapObject[0];
        gamePanel.npc = gamePanel.mapNPC[0];
        gamePanel.enemy = gamePanel.mapEnemy[0];

        // gamePanel.playMusic(0); // Putar musik title
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W)
            keAtas = false;
        if (code == KeyEvent.VK_S)
            keBawah = false;
        if (code == KeyEvent.VK_A)
            keKiri = false;
        if (code == KeyEvent.VK_D)
            keKanan = false;
        if (code == KeyEvent.VK_SPACE)
            spasiDitekan = false;
        if (code == KeyEvent.VK_ENTER)
            enterDitekan = false;
    }
}