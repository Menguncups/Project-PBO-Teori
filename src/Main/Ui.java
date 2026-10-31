package Main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import Object.ObjectHearth;

public class Ui {

    GamePanel gamePanel;
    Sound sound;
    Font jersey;
    Font pixel;
    Graphics2D g2; // ✅ tambahkan variabel global untuk g2

    public String currentDialog = "";
    public int currentDialogIndex = 0;
    public int commandNum = 0;
    public int tittleScreenState = 0; // milih karakter
    public int slotCol = 0;
    public int slotRow = 0;
    ObjectHearth heartObj = new ObjectHearth();

    public Ui(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.commandNum = 0; // ✅ PASTIKAN INIT
        // System.out.println("✅ UI CREATED: commandNum = " + commandNum);

        try {
            InputStream is = getClass().getResourceAsStream("/Gambar/Font/Jersey15-Regular.ttf");
            jersey = Font.createFont(Font.TRUETYPE_FONT, is);
            InputStream iss = getClass().getResourceAsStream("/Gambar/Font/PixelifySans-VariableFont_wght.ttf");
            pixel = Font.createFont(Font.TRUETYPE_FONT, iss);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    private int getXForCenteredText(String text) {
        int textWidth = g2.getFontMetrics().stringWidth(text);
        return (gamePanel.screenWidth - textWidth) / 2;
    }

    public int uiOptionIndex = 0;

    // ===========================================================
    // DRAW UTAMA
    // ===========================================================
    public void draw(Graphics2D g2) {
        this.g2 = g2;

        if (gamePanel.gameState == gamePanel.titleState) {
            drawTitleScreen(g2);
        } else if (gamePanel.gameState == gamePanel.playState) {
            drawPlayerLife(g2);
        } else if (gamePanel.gameState == gamePanel.pauseState) {
            drawPauseScreen(g2);
        } else if (gamePanel.gameState == gamePanel.dialogueState) {
            drawDialogScreen();
        } else if (gamePanel.gameState == gamePanel.inventoryState) {
            drawInventory();
        } else if (gamePanel.gameState == gamePanel.gameOverState) {
            drawGameOverScreen();
        } else if (gamePanel.gameState == gamePanel.gameWinState) { // 🔥 TAMBAHKAN INI
            drawGameWinScreen();
        }
    }

    public void drawGameWinScreen() {

        // Latar belakang hitam semi-transparan
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);

        // Teks Utama: THE END
        if (pixel != null)
            g2.setFont(pixel.deriveFont(Font.BOLD, 80f));
        g2.setColor(Color.white); // Warna emas/kuning
        String text = "THE END";
        int x = getXForCenteredText(text);
        int y = gamePanel.screenHeight / 2 - 100;
        g2.drawString(text, x, y);

        // Sub-teks: Congratulations
        if (pixel != null)
            g2.setFont(pixel.deriveFont(Font.PLAIN, 40f));
        g2.setColor(Color.WHITE);
        text = "Congratulations!";
        x = getXForCenteredText(text);
        y += 60;
        g2.drawString(text, x, y);

        // Pilihan Menu
        if (pixel != null)
            g2.setFont(pixel.deriveFont(Font.PLAIN, 36f));
        y += 100;

        String[] options = { "Back to Title", "Quit Game" };

        for (int i = 0; i < options.length; i++) {
            String opt = options[i];
            x = getXForCenteredText(opt);

            g2.setColor(Color.WHITE);
            // Jika opsi dipilih, ubah warna & tambah panah
            if (commandNum == i) {
                g2.setColor(Color.white);
                g2.drawString(">", x - 40, y + (i * 50));
            }
            g2.drawString(opt, x, y + (i * 50));
        }
    }

    // ===========================================================
    // TITLE SCREEN
    // ===========================================================
    public void drawTitleScreen(Graphics2D g2) {
        
        // 🔥 Bersihkan layar dulu
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);

        g2.setFont(pixel.deriveFont(Font.PLAIN, 48f));
        g2.setColor(Color.white);

        // Judul
        String title = "RPG TOP DOWN";
        int x = getXForCenteredText(title);
        int y = gamePanel.ukuranKotak * 3;
        g2.drawString(title, x, y);

        // Menu
        g2.setFont(pixel.deriveFont(Font.PLAIN, 28f));
        String[] options = { "Play", "Quit" };
        y += gamePanel.ukuranKotak * 2;

        for (int i = 0; i < options.length; i++) {
            String text = options[i];
            x = getXForCenteredText(text);
            g2.drawString(text, x, y + (i * 40));

            // 🔹 Gambar panah hanya di pilihan aktif
            if (commandNum == i) {
                g2.drawString(">", x - 30, y + (i * 40));
            }
        }
    }

    // ===========================================================
    // HEALTH BAR / HEART
    // ===========================================================
    public void drawPlayerLife(Graphics2D g2) {
        BufferedImage[] heart = heartObj.heart;
        int x = 25;
        int y = 20;

        // pastikan darah dalam batas
        if (gamePanel.player.life < 0)
            gamePanel.player.life = 0;
        if (gamePanel.player.life > 12)
            gamePanel.player.life = 12;

        int width = heart[0].getWidth() * 2;
        int height = heart[0].getHeight() * 2;

        // jantung kosong
        g2.drawImage(heart[12], x, y, width, height, null);

        // index jantung penuh
        int index = 12 - gamePanel.player.life;
        g2.drawImage(heart[index], x, y, width, height, null);
    }

    public void drawGameOverScreen() {
        // Latar belakang gelap transparan
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);

        // Teks "Game Over"
        g2.setFont(pixel.deriveFont(Font.BOLD, 80f));
        g2.setColor(Color.WHITE);
        String text = "GAME OVER";
        int x = getXForCenteredText(text);
        int y = gamePanel.screenHeight / 2 - 100;
        g2.drawString(text, x, y);

        // Opsi menu
        g2.setFont(pixel.deriveFont(Font.PLAIN, 36f));
        String[] options = { "Retry", "Quit" };
        y += 100;

        for (int i = 0; i < options.length; i++) {
            String opt = options[i];
            x = getXForCenteredText(opt);
            g2.setColor(Color.WHITE);
            g2.drawString(opt, x, y + (i * 50));

            // panah di opsi aktif
            if (commandNum == i) {
                g2.drawString(">", x - 40, y + (i * 50));
                g2.drawString("<", x + g2.getFontMetrics().stringWidth(opt) + 20, y + (i * 50));
            }
        }
    }

    // ===========================================================
    // PAUSE SCREEN
    // ===========================================================
    public void drawPauseScreen(Graphics2D g2) {
        // Ukuran frame yang lebih besar
        int frameX = gamePanel.screenWidth / 2 - 220;
        int frameY = gamePanel.screenHeight / 2 - 220;
        int frameWidth = 440;
        int frameHeight = 480;

        // Latar belakang semi-transparan
        Color bgColor = new Color(0, 0, 0, 220);
        g2.setColor(bgColor);
        g2.fillRoundRect(frameX, frameY, frameWidth, frameHeight, 25, 25);

        // Border putih
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(frameX, frameY, frameWidth, frameHeight, 25, 25);

        // Font
        Font titleFont = pixel.deriveFont(Font.BOLD, 36f);
        Font textFont = pixel.deriveFont(Font.PLAIN, 22f);
        Font optionFont = pixel.deriveFont(Font.PLAIN, 28f);

        // Judul "Control"
        g2.setFont(titleFont);
        g2.setColor(Color.WHITE);
        String title = "Control";
        int titleX = getXForCenteredText(title);
        int titleY = frameY + 60;
        g2.drawString(title, titleX, titleY);

        // Daftar kontrol
        g2.setFont(textFont);
        String[][] controls = {
                { "Move", "W / A / S / D" },
                { "Attack / Interact", "SPACE" },
                { "Guard", "ENTER" },
                { "Inventory", "C" },
                { "Pause", "ESC" },
                { "Full Screen", "F11" }
        };

        int startY = frameY + 110;
        int lineHeight = 32;
        int leftX = frameX + 50;
        int rightX = frameX + frameWidth - 130;

        for (int i = 0; i < controls.length; i++) {
            g2.drawString(controls[i][0], leftX, startY + (i * lineHeight));
            g2.drawString(controls[i][1], rightX, startY + (i * lineHeight));
        }

        // Garis pemisah
        g2.setColor(new Color(255, 255, 255, 150));
        g2.setStroke(new BasicStroke(2));
        int lineY = frameY + frameHeight - 140;
        g2.drawLine(frameX + 30, lineY, frameX + frameWidth - 30, lineY);

        // Menu Options - DITENGAH
        g2.setFont(optionFont);
        g2.setColor(Color.WHITE);

        String[] options = { "Back to Game", "Back to Title", "Quit Game" };
        int menuStartY = frameY + frameHeight - 100;
        int menuLineHeight = 40;

        for (int i = 0; i < options.length; i++) {
            String text = options[i];

            // Hitung posisi tengah untuk setiap opsi
            int textWidth = g2.getFontMetrics().stringWidth(text);
            int textX = frameX + (frameWidth - textWidth) / 2;

            // Tambahkan panah di kiri dan kanan jika terpilih
            if (commandNum == i) {
                String arrow = ">";
                int arrowWidth = g2.getFontMetrics().stringWidth(arrow);
                g2.drawString(arrow, textX - arrowWidth - 15, menuStartY + (i * menuLineHeight));
                g2.drawString("<", textX + textWidth + 15, menuStartY + (i * menuLineHeight));
            }

            g2.drawString(text, textX, menuStartY + (i * menuLineHeight));
        }
    }

    // ===========================================================
    // INVENTORY
    // ===========================================================
    public void drawInventory() {
        int frameX = gamePanel.ukuranKotak * 5;
        int frameY = gamePanel.ukuranKotak * 2;
        int frameWidth = gamePanel.ukuranKotak * 6;
        int frameHeight = gamePanel.ukuranKotak * 5;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // grid
        int maxCol = 5;
        int maxRow = 4;
        int slotSize = gamePanel.ukuranKotak + 8;
        int gridWidth = slotSize * maxCol;
        int gridHeight = slotSize * maxRow;
        int slotXStart = frameX + (frameWidth - gridWidth) / 2;
        int slotYStart = frameY + (frameHeight - gridHeight) / 2;

        // gambar item
        for (int i = 0; i < gamePanel.player.inventory.size(); i++) {
            int col = i % maxCol;
            int row = i / maxCol;
            int slotX = slotXStart + (col * slotSize);
            int slotY = slotYStart + (row * slotSize);
            g2.drawImage(gamePanel.player.inventory.get(i).image, slotX, slotY,
                    gamePanel.ukuranKotak, gamePanel.ukuranKotak, null);
        }

        // batas gerak cursor
        slotCol = Math.max(0, Math.min(slotCol, maxCol - 1));
        slotRow = Math.max(0, Math.min(slotRow, maxRow - 1));

        // posisi cursor
        int cursorX = slotXStart + (slotCol * slotSize);
        int cursorY = slotYStart + (slotRow * slotSize);
        int cursorWidth = gamePanel.ukuranKotak;
        int cursorHeight = gamePanel.ukuranKotak;

        // gambar kursor
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);
    }

    // ===========================================================
    // DIALOG
    // ===========================================================
    public void drawDialogScreen() {
        int width = gamePanel.screenWidth - (gamePanel.ukuranKotak * 1);
        int height = gamePanel.ukuranKotak * 2;
        int x = (gamePanel.screenWidth - width) / 2;
        int y = gamePanel.ukuranKotak - 50;

        drawSubWindow(x, y, width, height);

        x += 25;
        y += 55;

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        g2.setColor(Color.white);

        int textWidth = width - 50;
        drawWrappedText(currentDialog, x, y, textWidth);
    }

    // ===========================================================
    // SUBWINDOW & TEXT WRAP
    // ===========================================================
    public void drawSubWindow(int x, int y, int width, int height) {
        Color color = new Color(0, 0, 0, 200);
        g2.setColor(color);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        color = new Color(255, 255, 255);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    // Variabel animasi di class Ui
    int titleAnimCounter = 0;
    boolean arrowVisible = true;

    public void updateTitleScreen() {
        titleAnimCounter++;

        // setiap 20 frame, ubah visibilitas panah (efek kedip)
        if (titleAnimCounter > 20) {
            arrowVisible = !arrowVisible;
            titleAnimCounter = 0;
        }
    }

    public void drawWrappedText(String text, int x, int y, int maxWidth) {
        FontMetrics fm = g2.getFontMetrics();
        int lineHeight = fm.getHeight();

        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        int currentY = y;

        for (String word : words) {
            String testLine = line + word + " ";
            int lineWidth = fm.stringWidth(testLine);

            if (lineWidth > maxWidth) {
                g2.drawString(line.toString(), x, currentY);
                line = new StringBuilder(word + " ");
                currentY += lineHeight;
            } else {
                line = new StringBuilder(testLine);
            }
        }
        g2.drawString(line.toString(), x, currentY);
    }

    // ===========================================================
    // UTIL
    // ===========================================================
    public int getItemIndexOnSlot() {
        int maxCol = 5;
        return slotRow * maxCol + slotCol;
    }
}
