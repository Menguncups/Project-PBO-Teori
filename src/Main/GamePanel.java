package Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import Entity.Player;
import Map.PengelolaMap;

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
    public final int worldWidth = ukuranKotak * maxScreenColumn;
    public final int worldHeight = ukuranKotak * maxScreenRow;

    // FPS
    int FPS = 60;
    PengelolaMap pengelolaMap = new PengelolaMap(this);
    Keybind keyBind = new Keybind();
    Thread gameThread;
    public CollisioinCheck collisioinCheck = new CollisioinCheck(this);
    public Player player = new Player(this, keyBind);

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyBind);
        this.setFocusable(true);
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
        player.update();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        pengelolaMap.gambar(g2);
        player.draw(g2);

        g2.dispose();

    }
}
