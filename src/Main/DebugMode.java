package Main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import Entity.Entity;
import Object.SupperObject;

public class DebugMode {
    GamePanel gamePanel;
    public boolean debugOn = false;

    // Drop chance history tracking
    public ArrayList<String> dropHistory = new ArrayList<>();
    private int rightScrollOffset = 0;
    private final int maxHistorySize = 100;

    // Left panel scroll tracking
    private int leftScrollOffset = 0;
    private static final int LEFT_PANEL_MAX_LINES = 28; // Approx lines that fit in left panel

    public DebugMode(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * Log drop chance result to history
     * Called from enemy checkDrop() methods
     */
    public void logDropChance(String enemyName, int roll, int threshold, String result) {
        String logEntry = String.format("[%s] %s: rolled %d/%d -> %s",
                System.currentTimeMillis() % 10000, enemyName, roll, threshold, result);
        dropHistory.add(logEntry);

        // Keep history size manageable
        if (dropHistory.size() > maxHistorySize) {
            dropHistory.remove(0);
        }
        // Auto-scroll to latest entries when a new log arrives
        resetScroll();
    }

    /**
     * Reset scroll offset (called when new entries added)
     */
    public void resetScroll() {
        rightScrollOffset = Math.max(0, dropHistory.size() - 15);
    }

    public void toggleDebug() {
        debugOn = !debugOn;
        System.out.println("Debug Mode: " + (debugOn ? "ON" : "OFF"));
    }

    /**
     * Handle scroll input for left panel (UP/DOWN arrow keys)
     */
    public void scrollLeft(int direction) {
        int maxScroll = Math.max(0, getTotalLeftPanelLines() - LEFT_PANEL_MAX_LINES);
        leftScrollOffset += direction * 3; // Scroll 3 lines per input
        leftScrollOffset = Math.max(0, Math.min(leftScrollOffset, maxScroll));
    }

    /**
     * Calculate total lines in left panel content
     */
    private int getTotalLeftPanelLines() {
        int lines = 2; // Title
        lines += 2; // Player section header
        lines += 6; // Player info (pos, hp, dir, hitbox, attack, shield)
        lines += 1; // Enemy section header
        lines += gamePanel.enemy.length * 4; // Each enemy: name, tile, hitbox, drop
        lines += 1; // NPC section header
        lines += gamePanel.npc.length * 4; // Each NPC: name, tile, hitbox
        return lines;
    }

    public void draw(Graphics2D g2) {
        if (!debugOn)
            return;

        // Semi-transparent background untuk info panel KIRI
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(10, 10, 400, gamePanel.screenHeight - 20);

        // Semi-transparent background untuk history panel KANAN
        g2.fillRect(gamePanel.screenWidth - 310, 10, 300, gamePanel.screenHeight - 20);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 14));

        // Draw left panel with scroll
        drawLeftPanel(g2);

        // Draw right panel (drop history)
        drawDropHistoryPanel(g2);
    }

    /**
     * Draw left panel content with scroll support
     */
    private void drawLeftPanel(Graphics2D g2) {
        int panelX = 20;
        int panelStartY = 30;
        int lineHeight = 18;
        int maxDisplayLines = LEFT_PANEL_MAX_LINES;
        int panelHeight = gamePanel.screenHeight - 40;

        // Create a list of all content lines
        ArrayList<String> contentLines = new ArrayList<>();
        ArrayList<Color> lineColors = new ArrayList<>();

        // Build content
        contentLines.add("=== DEBUG MODE (F12) ===");
        lineColors.add(Color.WHITE);

        contentLines.add(""); // Blank line
        lineColors.add(Color.WHITE);

        contentLines.add("[ PLAYER ]");
        lineColors.add(new Color(100, 200, 255));

        Entity player = gamePanel.player;
        contentLines.add("Pos: (" + (player.worldX / gamePanel.ukuranKotak) + ", "
                + (player.worldY / gamePanel.ukuranKotak) + ") Tile");
        lineColors.add(Color.WHITE);
        contentLines.add("HP: " + player.life + "/" + player.maxLife);
        lineColors.add(Color.WHITE);
        contentLines.add("Dir: " + player.arah);
        lineColors.add(Color.WHITE);
        contentLines.add("Hitbox: X=" + player.solidArea.x + " Y=" + player.solidArea.y
                + " W=" + player.solidArea.width + " H=" + player.solidArea.height);
        lineColors.add(Color.WHITE);
        contentLines.add("Attack: " + (player.attack ? "YES" : "NO") + " | Invincible: "
                + (player.invincible ? "YES" : "NO"));
        lineColors.add(Color.WHITE);
        contentLines.add("Shield: " + (player.invincible2 ? "ACTIVE" : "inactive"));
        lineColors.add(Color.WHITE);

        contentLines.add("");
        lineColors.add(Color.WHITE);

        contentLines.add("[ ENEMIES ON MAP ]");
        lineColors.add(new Color(255, 100, 100));

        int enemyCount = 0;
        for (int i = 0; i < gamePanel.enemy.length; i++) {
            if (gamePanel.enemy[i] != null) {
                Entity enemy = gamePanel.enemy[i];
                enemyCount++;

                contentLines.add(enemyCount + ". " + enemy.name + " (Life: " + enemy.life + ")");
                lineColors.add(new Color(255, 200, 100));

                contentLines.add("  Tile: (" + (enemy.worldX / gamePanel.ukuranKotak) + ", "
                        + (enemy.worldY / gamePanel.ukuranKotak) + ")");
                lineColors.add(Color.WHITE);

                contentLines.add("  Hitbox: X=" + enemy.solidArea.x + " Y=" + enemy.solidArea.y
                        + " W=" + enemy.solidArea.width + " H=" + enemy.solidArea.height);
                lineColors.add(Color.WHITE);

                String dropInfo = getDropChanceInfo(enemy);
                if (!dropInfo.isEmpty()) {
                    contentLines.add("  " + dropInfo);
                    lineColors.add(new Color(200, 255, 100));
                }

                if (!enemy.lastDropChance.isEmpty()) {
                    contentLines.add("  LAST DROP: " + enemy.lastDropChance);
                    lineColors.add(new Color(0, 255, 100));
                }
            }
        }

        if (enemyCount == 0) {
            contentLines.add("(No enemies on this map)");
            lineColors.add(Color.WHITE);
        }

        contentLines.add("");
        lineColors.add(Color.WHITE);

        contentLines.add("[ NPCs ON MAP ]");
        lineColors.add(new Color(150, 255, 150));

        int npcCount = 0;
        for (int i = 0; i < gamePanel.npc.length; i++) {
            if (gamePanel.npc[i] != null) {
                Entity npc = gamePanel.npc[i];
                npcCount++;

                contentLines.add(npcCount + ". " + npc.name);
                lineColors.add(new Color(200, 255, 150));

                contentLines.add("  Tile: (" + (npc.worldX / gamePanel.ukuranKotak) + ", "
                        + (npc.worldY / gamePanel.ukuranKotak) + ")");
                lineColors.add(Color.WHITE);

                contentLines.add("  Hitbox: X=" + npc.solidArea.x + " Y=" + npc.solidArea.y
                        + " W=" + npc.solidArea.width + " H=" + npc.solidArea.height);
                lineColors.add(Color.WHITE);
            }
        }

        if (npcCount == 0) {
            contentLines.add("(No NPCs on this map)");
            lineColors.add(Color.WHITE);
        }

        // Render with scroll offset
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        int displayY = panelStartY;
        int startLine = leftScrollOffset;
        int endLine = Math.min(startLine + maxDisplayLines, contentLines.size());

        for (int i = startLine; i < endLine; i++) {
            g2.setColor(lineColors.get(i));
            g2.drawString(contentLines.get(i), panelX, displayY);
            displayY += lineHeight;
        }

        // Draw scroll indicator
        if (contentLines.size() > maxDisplayLines) {
            g2.setColor(new Color(150, 150, 150));
            g2.setFont(new Font("Arial", Font.PLAIN, 9));
            int scrollPercent = (int) ((double) leftScrollOffset / (contentLines.size() - maxDisplayLines) * 100);
            g2.drawString("Scroll: " + scrollPercent + "% (Arrow Up/Down to scroll)", 20, gamePanel.screenHeight - 5);
        }
    }

    /**
     * Draw scrollable drop history panel on the right side
     */
    private void drawDropHistoryPanel(Graphics2D g2) {
        int panelX = gamePanel.screenWidth - 310;
        int panelY = 10;
        int panelHeight = gamePanel.screenHeight - 20;
        int lineHeight = 14;

        // Title
        g2.setColor(new Color(100, 200, 255));
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.drawString("DROP CHANCE HISTORY", panelX + 10, panelY + 25);

        // Scroll info
        g2.setColor(new Color(150, 255, 150));
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        g2.drawString("Total: " + dropHistory.size(), panelX + 10, panelY + 40);

        // Draw history entries
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Courier New", Font.PLAIN, 11));

        int displayY = panelY + 55;
        int startIdx = Math.max(0, dropHistory.size() - 20); // Show last 20 entries

        for (int i = startIdx; i < dropHistory.size(); i++) {
            if (displayY + lineHeight > panelY + panelHeight - 10) {
                g2.setColor(new Color(150, 150, 150));
                g2.drawString("... (" + (dropHistory.size() - i) + " more)", panelX + 10, displayY);
                break;
            }

            String entry = dropHistory.get(i);

            // Color code based on result
            if (entry.contains("DROPPED")) {
                g2.setColor(new Color(0, 255, 100)); // Bright green for drops
            } else if (entry.contains("NO DROP")) {
                g2.setColor(new Color(255, 100, 100)); // Red for no drops
            } else {
                g2.setColor(new Color(200, 200, 200)); // Gray for others
            }

            // Draw entry with text wrapping if needed
            if (entry.length() > 35) {
                g2.drawString(entry.substring(0, 35), panelX + 10, displayY);
                g2.drawString(entry.substring(35), panelX + 15, displayY + lineHeight);
                displayY += lineHeight * 2;
            } else {
                g2.drawString(entry, panelX + 10, displayY);
                displayY += lineHeight;
            }
        }

        // Footer
        g2.setColor(new Color(150, 150, 150));
        g2.setFont(new Font("Arial", Font.ITALIC, 9));
        g2.drawString("[Latest entries at bottom]", panelX + 10, panelY + panelHeight - 5);
    }

    /**
     * Helper method to get drop chance info dari musuh
     * Return string kosong jika enemy tidak punya drop atau jika tidak diketahui
     */
    private String getDropChanceInfo(Entity enemy) {
        // Cek apakah enemy punya method atau field dropChance
        // Untuk sekarang, kita bisa menggunakan reflection atau hardcode per enemy type

        String enemyName = enemy.name.toLowerCase();

        // Hardcode drop chances (sesuaikan dengan logika game kamu)
        if (enemyName.contains("slime")) {
            return "Drop: 50% Chance Drop Hearth";
        } else if (enemyName.contains("orc")) {
            return "Drop: 50% Chance Drop Hearth";
        } else if (enemyName.contains("hog")) {
            return "Drop: 50% Chance Drop Hearth";
        } else if (enemyName.contains("crow")) {
            return "Drop: 50% Chance Drop Hearth";
        } else if (enemyName.contains("skeleton")) {
            return "Drop: 50% Chance Drop Hearth";
        } else if (enemyName.contains("demon")) {
            return "Drop: 50% Chance Drop Hearth";
        } else if (enemyName.contains("bossrock")) {
            return "Drop: 50% Chance Drop Hearth";

        }

        return "";
    }

    /**
     * Draw all hitboxes on screen (collision areas, attack areas, detect radius)
     * This is called separately to overlay hitboxes during debug mode
     */
    public void drawAllHitboxes(Graphics2D g2) {
        if (!debugOn)
            return;

        // ================= PLAYER HITBOX =================
        drawEntityHitbox(g2, gamePanel.player, Color.CYAN, "Player");

        // ================= ENEMY HITBOXES =================
        for (int i = 0; i < gamePanel.enemy.length; i++) {
            if (gamePanel.enemy[i] != null) {
                drawEntityHitbox(g2, gamePanel.enemy[i], Color.RED, "Enemy");
            }
        }

        // ================= NPC HITBOXES =================
        for (int i = 0; i < gamePanel.npc.length; i++) {
            if (gamePanel.npc[i] != null) {
                drawEntityHitbox(g2, gamePanel.npc[i], new Color(100, 255, 100), "NPC");
            }
        }

        // ================= OBJECT HITBOXES =================
        for (int i = 0; i < gamePanel.obj.length; i++) {
            if (gamePanel.obj[i] != null) {
                drawObjectHitbox(g2, gamePanel.obj[i]);
            }
        }
    }

    private void drawEntityHitbox(Graphics2D g2, Entity entity, Color color, String type) {
        int screenX = entity.worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = entity.worldY - gamePanel.player.worldY + gamePanel.player.screenY;

        // Cek apakah entity terlihat di screen
        if (screenX + gamePanel.perbesarKotak * 2 < 0 || screenX > gamePanel.screenWidth ||
                screenY + gamePanel.perbesarKotak * 2 < 0 || screenY > gamePanel.screenHeight) {
            return;
        }

        // Draw collision hitbox (solid area)
        g2.setColor(color);
        g2.setStroke(new BasicStroke(2));
        int collisionX = screenX + entity.solidArea.x;
        int collisionY = screenY + entity.solidArea.y;
        g2.drawRect(collisionX, collisionY, entity.solidArea.width, entity.solidArea.height);

        // Draw label
        g2.setColor(color);
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        g2.drawString(entity.name, collisionX + 3, collisionY - 3);

        // Draw attack area jika entity sedang attack (ATAU untuk semua enemy)
        if (entity.attackArea != null) {
            // Untuk MUSUH: selalu tampilkan attack area (aktif atau tidak)
            // Untuk PLAYER: hanya tampilkan saat attack
            boolean shouldShowAttack = entity != gamePanel.player || entity.attack;

            if (shouldShowAttack) {
                int attackX = screenX + entity.solidArea.x;
                int attackY = screenY + entity.solidArea.y;

                switch (entity.arah) {
                    case "atas":
                        attackY -= entity.attackArea.height;
                        break;
                    case "bawah":
                        attackY += entity.solidArea.height;
                        break;
                    case "kiri":
                        attackX -= entity.attackArea.width;
                        break;
                    case "kanan":
                        attackX += entity.solidArea.width;
                        break;
                }

                // Highlight jika sedang attack
                if (entity.attack) {
                    g2.setColor(new Color(255, 255, 0, 150)); // Bright yellow
                    g2.fillRect(attackX, attackY, entity.attackArea.width, entity.attackArea.height);
                    g2.setColor(new Color(255, 255, 0));
                    g2.setStroke(new BasicStroke(2));
                } else {
                    g2.setColor(new Color(255, 200, 0, 80)); // Dim orange
                    g2.setStroke(new BasicStroke(1));
                }
                g2.drawRect(attackX, attackY, entity.attackArea.width, entity.attackArea.height);
            }
        }

        // Draw detect radius untuk semua enemy dengan radius detection
        if (entity.type == 2) {
            drawEnemyDetectRadius(g2, entity, screenX, screenY);
        }
    }

    /**
     * Draw enemy's detect radius (colored circle based on enemy type)
     */
    private void drawEnemyDetectRadius(Graphics2D g2, Entity entity, int screenX, int screenY) {
        int radiusPixels = 0;
        Color radiusColor = new Color(200, 100, 255); // Default purple
        Color radiusColorFill = new Color(200, 100, 255, 80);

        // Determine radius based on enemy type
        switch (entity.name.toLowerCase()) {
            case "orc":
            case "slime":
            case "hog":
            case "demon":
                radiusPixels = 2 * gamePanel.ukuranKotak; // 2 tiles
                radiusColor = new Color(200, 100, 255); // Purple
                radiusColorFill = new Color(200, 100, 255, 80);
                break;
            case "skeleton":
            case "crow":
                radiusPixels = 2 * gamePanel.ukuranKotak; // 2 tiles (assumed same)
                radiusColor = new Color(100, 200, 255); // Cyan
                radiusColorFill = new Color(100, 200, 255, 80);
                break;
            case "bossrock":
                radiusPixels = 5 * gamePanel.ukuranKotak; // 5 tiles
                radiusColor = new Color(255, 100, 100); // Red
                radiusColorFill = new Color(255, 100, 100, 80);
                break;
            default:
                return; // No radius for other entities
        }

        int centerX = screenX + entity.solidArea.x + entity.solidArea.width / 2;
        int centerY = screenY + entity.solidArea.y + entity.solidArea.height / 2;

        g2.setColor(radiusColorFill);
        g2.fillOval(centerX - radiusPixels, centerY - radiusPixels, radiusPixels * 2, radiusPixels * 2);

        g2.setColor(radiusColor);
        g2.setStroke(new BasicStroke(1));
        g2.drawOval(centerX - radiusPixels, centerY - radiusPixels, radiusPixels * 2, radiusPixels * 2);
    }

    private void drawObjectHitbox(Graphics2D g2, SupperObject obj) {
        int screenX = obj.worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = obj.worldY - gamePanel.player.worldY + gamePanel.player.screenY;

        // Cek apakah object terlihat di screen
        if (screenX + gamePanel.perbesarKotak < 0 || screenX > gamePanel.screenWidth ||
                screenY + gamePanel.perbesarKotak < 0 || screenY > gamePanel.screenHeight) {
            return;
        }

        // Draw object hitbox
        g2.setColor(new Color(200, 100, 200)); // Purple
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(screenX, screenY, gamePanel.perbesarKotak, gamePanel.perbesarKotak);

        g2.setColor(new Color(200, 100, 200));
        g2.setFont(new Font("Arial", Font.PLAIN, 9));
        g2.drawString(obj.name, screenX + 3, screenY + 12);
    }
}
