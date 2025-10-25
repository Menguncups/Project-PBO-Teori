package Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import Entity.Player;

public class GamePanel extends JPanel implements Runnable {

    // Setinggan screen
    final int ukuranKotak = 64; // 64 x 64
    final int scale = 2;

    public final int perbesarKotak = ukuranKotak * scale; // 128 x 128
    final int maxScreenColumn = 16;
    final int maxScreenRow = 9;
    final int screenWidth = (perbesarKotak * 2) * maxScreenColumn; // 1920
    final int screenHeight = (perbesarKotak * 2) * maxScreenRow; // 1080

    // FPS
    int FPS = 60;

    Thread gameThread;
    Keybind keyBind = new Keybind();
    Player player = new Player(this, keyBind);

    // * posisi awal player
    int posisiYPlayer = 100;
    int posisiXPlayer = 100;
    int kecepatanPlayer = 4;

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

        player.draw(g2);

        g2.dispose();

    }
}
