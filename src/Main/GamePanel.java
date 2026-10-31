package Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.security.KeyStore.Entry;

import javax.sound.sampled.Clip;
import javax.swing.JPanel;

import Enemy.Slime;
import Entity.Entity;
import Entity.Player;
import Map.PengelolaMap;
import Object.SupperObject;

public class GamePanel extends JPanel implements Runnable {

    public static final String SupperObject = null;
    Clip clip;
    long clipTimePosition;
    // Setinggan screen
    public final int ukuranKotak = 64; // 64 x 64
    final int scale = 1;
    int windowedWidth;
    int windowedHeight;

    public final int perbesarKotak = ukuranKotak * scale; // 64 x 64
    public final int maxScreenColumn = 16;
    public final int maxScreenRow = 9;
    public final int screenWidth = perbesarKotak * maxScreenColumn; // 1920
    public final int screenHeight = perbesarKotak * maxScreenRow; // 1080

    // fullscreen
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;

    // world
    public final int maxWorldCol = 100;
    public final int maxWorldRow = 100;
    public final int maxMap = 3;
    public int currentMap = 0;
    private int lastPlayedMap = -1; // track map terakhir yg dimainkan musiknya

    // FPS
    int FPS = 60;
    PengelolaMap pengelolaMap = new PengelolaMap(this);
    public Keybind keyBind = new Keybind(this);
    Sound music = new Sound();
    Sound soundEffect = new Sound();

    public CollisioinCheck collisioinCheck = new CollisioinCheck(this);
    public AssetObject assetObject = new AssetObject(this);
    Thread gameThread;

    public Player player = new Player(this, keyBind);
    public SupperObject obj[] = new SupperObject[100]; // 10 objek kayak key, weapon, pintu
    public Entity npc[] = new Entity[10];
    public Entity enemy[] = new Entity[20];
    public Ui ui = new Ui(this);
    public DebugMode debugMode = new DebugMode(this);

    // kondisi game
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int inventoryState = 4;
    public final int gameOverState = 5;
    public final int gameWinState = 6;

    public SupperObject[][] mapObject = new SupperObject[maxMap][100];
    public Entity[][] mapEnemy = new Entity[maxMap][50];
    public Entity[][] mapNPC = new Entity[maxMap][10];

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        windowedWidth = screenWidth;
        windowedHeight = screenHeight;

        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyBind);
        this.setFocusable(true);
    }

    public void setupGame() {
        // Inisialisasi semua map
        for (int i = 0; i < maxMap; i++) {
            assetObject.setObject(i);
            assetObject.setNPC(i);
            assetObject.setEnemy(i);
        }

        // playMusic(0);
        // else if (currentMap == 1)
        // playMusic(1);
        // else if (currentMap == 2)
        // playMusic(2);
        // Set objek aktif sesuai map sekarang
        obj = mapObject[currentMap];
        npc = mapNPC[currentMap];
        enemy = mapEnemy[currentMap];

        // playMusic(0);

        gameState = titleState;
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double jeda = 1000000000 / FPS;
        double selisihFrame = 0;
        long waktuTerakhir = System.nanoTime();
        long waktuSekarang;
        long waktu = 0;
        int jumlahTampilan = 0;
        while (gameThread != null) {

            waktuSekarang = System.nanoTime();

            selisihFrame += (waktuSekarang - waktuTerakhir) / jeda;
            waktu += (waktuSekarang - waktuTerakhir);
            waktuTerakhir = waktuSekarang;

            if (selisihFrame >= 1) {
                update();
                repaint();
                selisihFrame--;
                jumlahTampilan++;
            }

            if (waktu >= 1000000000) {
                // System.out.println("FPS=" + jumlahTampilan);
                jumlahTampilan = 0;
                waktu = 0;
            }
        }
    }

    public void update() {
        // Play background music sesuai map
        // Musik tetap jalan saat playState, pauseState, gameOverState, gameWinState
        // Hanya berhenti saat title, dialogue, atau inventory state
        if (gameState != titleState && gameState != dialogueState && gameState != inventoryState) {
            if (currentMap != lastPlayedMap) {
                lastPlayedMap = currentMap;
                stopMusic(); // Stop musik lama dulu
                System.out.println("============= ");
                if (currentMap == 0) {
                    System.out.println("Pindah ke map " + currentMap + " (atas)");
                    System.out.println("Lagu: Map Atas");
                    playMusic(0, 0.5f); // Map Atas - 50% volume
                } else if (currentMap == 1) {
                    System.out.println("Pindah ke map " + currentMap + " (dungeon)");
                    System.out.println("Lagu: Map Dungeon");
                    playMusic(12, 2.0f); // Map Dungeon - 200% volume
                } else if (currentMap == 2) {
                    System.out.println("Pindah ke map " + currentMap + " (boss)");
                    System.out.println("Lagu: Map Boss");
                    playMusic(11, 2.0f); // Map Boss - 200% volume
                }
            }
        } else if ((gameState == titleState || gameState == dialogueState || gameState == inventoryState)
                && lastPlayedMap != -1) {
            stopMusic();
            lastPlayedMap = -1;
        }

        if (gameState == titleState) {
            // System.out.println("gameState: 1");
            ui.updateTitleScreen();
        }

        else if (gameState == playState) {
            player.update();

            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null)
                    npc[i].update();
            }

            for (int i = 0; i < enemy.length; i++) {
                if (enemy[i] != null)
                    enemy[i].update();
            }
        }

        else if (gameState == inventoryState) {
            player.update();
        }

        else if (gameState == pauseState) {
            // pause → semua entity diam
        }
    }

    public void gantiMap(int mapBaru, int posX, int posY) {
        if (mapBaru < 0 || mapBaru >= maxMap)
            return;

        currentMap = mapBaru;

        // 🔄 Update referensi ke map baru
        obj = mapObject[currentMap];
        npc = mapNPC[currentMap];
        enemy = mapEnemy[currentMap];

        // Force musik ganti dengan stop dulu
        lastPlayedMap = -1;
        // if (currentMap == 0) {
        // playMusic(0); // Musik Map 0 (misal: Hutan)
        // } else if (currentMap == 1) {
        // playMusic(1); // Musik Map 1 (misal: Dungeon) - Pastikan ada file sound index
        // 1
        // } else if (currentMap == 2) {
        // playMusic(2); // Musik Map 2 (Boss) - Pastikan ada file sound index 2
        // }

        player.worldX = posX * ukuranKotak;
        player.worldY = posY * ukuranKotak;

        player.collisionOn = false;

    }

    public void drawToTempScreen() {
        // menu
        if (gameState == titleState) {
            ui.draw(g2);
        } else {
            pengelolaMap.gambar(g2);
            // load objek
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    obj[i].draw(g2, this);
                }
            }
            // npc
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].draw(g2);
                }
            }

            // enemy
            for (int i = 0; i < enemy.length; i++) {
                if (enemy[i] != null) {
                    enemy[i].draw(g2);
                }
            }

            // load player
            player.draw(g2);
            ui.draw(g2);

            // Debug Mode - draw info panel and hitboxes
            debugMode.draw(g2);
            debugMode.drawAllHitboxes(g2);
        }
    }

    public void drawToScreen(Graphics g) {
        if (Main.window.isUndecorated()) {
            // fullscreen
            g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        } else {
            // windowed
            g.drawImage(tempScreen, 0, 0, screenWidth, screenHeight, null);
        }
    }

    public void setFullScreen() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        // Ambil resolusi layar penuh
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth2 = (int) screenSize.getWidth();
        screenHeight2 = (int) screenSize.getHeight();

        Main.window.dispose();
        Main.window.setUndecorated(true);
        gd.setFullScreenWindow(Main.window);
        Main.window.setVisible(true);

        // 🔥 Buat ulang buffer sesuai ukuran baru
        tempScreen = new BufferedImage(screenWidth2, screenHeight2, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();

        // System.out.println(">> FULLSCREEN AKTIF: " + screenWidth2 + "x" +
        // screenHeight2);
    }

    public void setWindowedMode() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(null);

        Main.window.dispose();
        Main.window.setUndecorated(false);
        Main.window.setResizable(false);
        Main.window.add(this);
        Main.window.pack();
        Main.window.setLocationRelativeTo(null);
        Main.window.setVisible(true);

        // 🔥 Kembalikan ukuran normal
        screenWidth2 = windowedWidth;
        screenHeight2 = windowedHeight;
        tempScreen = new BufferedImage(windowedWidth, windowedHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();

        System.out.println(">> WINDOWED MODE: " + screenWidth2 + "x" + screenHeight2);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (tempScreen == null || g2 == null) {
            tempScreen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            g2 = (Graphics2D) tempScreen.getGraphics();
        }

        drawToTempScreen();

        // scaling proporsional jika fullscreen
        if (Main.window.isUndecorated()) {
            double scaleX = (double) getWidth() / windowedWidth;
            double scaleY = (double) getHeight() / windowedHeight;
            ((Graphics2D) g).scale(scaleX, scaleY);
        }

        drawToScreen(g);
    }

    public void playMusic(int i) {
        playMusic(i, 1.0f);
    }

    public void playMusic(int i, float volume) {
        music.setFile(i);
        music.play();
        music.loop();
        // Set background music volume
        try {
            music.setVolume(volume);
        } catch (Exception e) {
            // ignore if not supported on platform
        }
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        soundEffect.setFile(i);
        soundEffect.play();
    }

    public void pause() {
        if (clip != null && clip.isRunning()) {
            clipTimePosition = clip.getMicrosecondPosition();
            clip.stop();
        }
    }

    public void resume() {
        if (clip != null) {
            clip.setMicrosecondPosition(clipTimePosition);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
}
