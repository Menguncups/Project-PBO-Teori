package Main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class Ui {

    GamePanel gamePanel;
    Graphics2D g2;
    Sound sound;

    public String currentDialog = "";
    public int currentDialogIndex = 0;

    public Ui(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        // play
        if (gamePanel.gameState == gamePanel.playState) {
        }

        // stop
        if (gamePanel.gameState == gamePanel.pauseState) {
        }

        // lagi ngobrol
        if (gamePanel.gameState == gamePanel.dialogueState) {
            drawDialogScreen();
        }
    }

    public void drawDialogScreen() {
        // window
        int width = gamePanel.screenWidth - (gamePanel.ukuranKotak * 1);
        int height = gamePanel.ukuranKotak * 2;
        // border
        int x = (gamePanel.screenWidth - width) / 2;
        int y = gamePanel.ukuranKotak - 50;
        drawSubWindow(x, y, width, height);

        x += 25;
        y += 55;

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 28)); // font & ukuran teks
        g2.setColor(Color.white);

        int textWidth = width - 50; // area teks di dalam box
        drawWrappedText(currentDialog, x, y, textWidth);
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        Color color = new Color(0, 0, 0, 200);
        g2.setColor(color);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        color = new Color(255, 255, 255);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
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

            // kalau teks melebihi lebar box, pindah baris
            if (lineWidth > maxWidth) {
                g2.drawString(line.toString(), x, currentY);
                line = new StringBuilder(word + " ");
                currentY += lineHeight;
            } else {
                line = new StringBuilder(testLine);
            }
        }
        // gambar sisa teks
        g2.drawString(line.toString(), x, currentY);
    }
}
