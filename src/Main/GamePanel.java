package Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.security.KeyStore.Entry;

import javax.swing.JPanel;

import Entity.Entity;
import Entity.Player;
import Map.PengelolaMap;
import Object.SupperObject;

public class GamePanel extends JPanel implements Runnable {

    // Setinggan screen
    public final int ukuranKotak = 64; // 64 x 64
    final int scale = 1;

    public final int perbesarKotak = ukuranKotak * scale; // 64 x 64
    public final int maxScreenColumn = 16;
    public final int maxScreenRow = 9;
    public final int screenWidth = perbesarKotak * maxScreenColumn; // 1920
    public final int screenHeight = perbesarKotak * maxScreenRow; // 1080

    // world
    public final int maxWorldCol = 100;
    public final int maxWorldRow = 100;

    // FPS
    int FPS = 60;
    PengelolaMap pengelolaMap = new PengelolaMap(this);
    Keybind keyBind = new Keybind(this);
    Sound music = new Sound();
    Sound soundEffect = new Sound();

    public CollisioinCheck collisioinCheck = new CollisioinCheck(this);
    public AssetObject assetObject = new AssetObject(this);
    Thread gameThread;

    public Player player = new Player(this, keyBind);
    public SupperObject obj[] = new SupperObject[10]; // 10 objek kayak key, weapon, pintu
    public Entity npc[] = new Entity[10];
    public Ui ui = new Ui(this);

    // kondisi game
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyBind);
        this.setFocusable(true);
    }

    public void setupGame() {
        assetObject.setObject();
        assetObject.setNPC();
        playMusic(0);

        gameState = playState;
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
                System.out.println("FPS=" + jumlahTampilan);
                jumlahTampilan = 0;
                waktu = 0;
            }
        }
    }

    public void update() {

        if (gameState == playState) {
            player.update();
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].update();
                }
            }
        }

        if (gameState == pauseState) {
            // pause
        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // load map
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

        // load player
        player.draw(g2);
        ui.draw(g2);

        g2.dispose();

    }

    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        soundEffect.setFile(i);
        soundEffect.play();
    }

}
