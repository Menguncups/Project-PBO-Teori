package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Main.GamePanel;
import javafx.scene.paint.Color;

public class Entity {
    public GamePanel gamePanel;
    public int worldX;
    public int worldY;
    public int kecepatanBergerak;
    private int moveCounter = 0;
    public String name;
    public boolean invincible = false;
    public int invincibleCounter = 0;
    public boolean invincible2 = false;
    public int invincibleCounter2 = 0;
    int interactCooldown = 0;
    public boolean alive = true;
    public boolean die = false;

    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public Rectangle solidArea = new Rectangle(15, 30, 34, 34);

    public ArrayList<BufferedImage> jalanAtas = new ArrayList<>();
    public ArrayList<BufferedImage> jalanBawah = new ArrayList<>();
    public ArrayList<BufferedImage> jalanKiri = new ArrayList<>();
    public ArrayList<BufferedImage> jalanKanan = new ArrayList<>();

    public ArrayList<BufferedImage> attackAtas = new ArrayList<>();
    public ArrayList<BufferedImage> attackBawah = new ArrayList<>();
    public ArrayList<BufferedImage> attackKiri = new ArrayList<>();
    public ArrayList<BufferedImage> attackKanan = new ArrayList<>();

    public ArrayList<BufferedImage> shieldAtas = new ArrayList<>();
    public ArrayList<BufferedImage> shieldBawah = new ArrayList<>();
    public ArrayList<BufferedImage> shieldKiri = new ArrayList<>();
    public ArrayList<BufferedImage> shieldKanan = new ArrayList<>();

    public String dialouges[] = new String[3];

    public String arah;
    public int spriteCounter = 0;
    public int spriteNumber = 0;
    public int dieCounter = 0;
    public boolean isTalking = false;
    public int type; // 0 player 1 npc 2 enemy
    public BufferedImage image;
    public boolean attack = false;
    public boolean knocBack = false;
    public int defaultSpeed;
    int knocBackCounter = 0;
    // Jika false, `Entity.update()` tidak akan memanggil `setAction()` sehingga
    // subclass dapat memanggil `setAction()` pada urutan yang mereka butuhkan.
    public boolean useBaseSetAction = true;
    public String lastDropChance = ""; // debug: track drop result saat enemy mati

    // status character
    public int maxLife;
    public int life;

    public int solidAreaDefaultX;
    public int solidAreaDefaultY;

    public boolean collisionOn = false;
    public int actionLockCounter = 0;
    public int attackFrame = 0; // untuk animasi attack

    public Entity(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void draw(Graphics2D g2) {
        // Pilih animasi
        ArrayList<BufferedImage> animasi = null;

        if (attack) {
            switch (arah) {
                case "atas":
                    animasi = attackAtas;
                    break;
                case "bawah":
                    animasi = attackBawah;
                    break;
                case "kiri":
                    animasi = attackKiri;
                    break;
                case "kanan":
                    animasi = attackKanan;
                    break;
            }
        } else {
            switch (arah) {
                case "atas":
                    animasi = jalanAtas;
                    break;
                case "bawah":
                    animasi = jalanBawah;
                    break;
                case "kiri":
                    animasi = jalanKiri;
                    break;
                case "kanan":
                    animasi = jalanKanan;
                    break;
            }
        }

        // Ambil frame saat ini
        BufferedImage currentImage = null;
        if (animasi != null && !animasi.isEmpty()) {
            int frame = attack ? attackFrame % animasi.size() : spriteNumber % animasi.size();
            currentImage = animasi.get(frame);
        }

        // Gambar ke layar
        if (currentImage != null) {
            // g2.drawImage(currentImage, worldX, worldY, gamePanel.ukuranKotak,
            // gamePanel.ukuranKotak, null);
        } else {
            // fallback: gambar kotak merah kalau gambar tidak ada
            // g2.setColor(java.awt.Color.RED);
            // g2.fillRect(worldX, worldY, gamePanel.ukuranKotak, gamePanel.ukuranKotak);
        }
    }

    public void use(Entity entity) {

    }

    public void checkDrop() {
        // default kosong
    }

    public void speak() {

    }

    public void setAction() {

    }

    public void update() {
        if (knocBack) {
            if (collisionOn) {
                knocBackCounter = 0;
                knocBack = false;
                kecepatanBergerak -= 1;
            } else if (collisionOn == false) {
                switch (gamePanel.player.arah) {
                    case "atas":
                        worldY -= kecepatanBergerak;
                        break;
                    case "bawah":
                        worldY += kecepatanBergerak;
                        break;
                    case "kiri":
                        worldX -= kecepatanBergerak;
                        break;
                    case "kanan":
                        worldX += kecepatanBergerak;
                        break;
                }
                knocBackCounter++;
                if (knocBackCounter == 10) {
                    knocBackCounter = 0;
                    knocBack = false;
                }
            }
        } else {
            if (useBaseSetAction) {
                setAction();
            }
            collisionOn = false;

            // cek tabrakan
            gamePanel.collisioinCheck.checkTile(this);
            gamePanel.collisioinCheck.checkObject(this, false);
            gamePanel.collisioinCheck.checkEntity(this, gamePanel.enemy);
            boolean contactPlayer = gamePanel.collisioinCheck.checkPlayer(this);

            // if (this.type == 2 && contactPlayer == true) {
            // if (gamePanel.player.invincible == false) {
            // gamePanel.player.life -= 1;
            // gamePanel.player.invincible = true;
            // System.out.println("Sisa darah: " + gamePanel.player.life);
            // gamePanel.playSE(3);
            // }

            // }

            if (!collisionOn) {
                moveCounter++;
                if (moveCounter % 3 == 0) { // gerak tiap 3 frame sekali
                    switch (arah) {
                        case "atas":
                            worldY -= kecepatanBergerak;
                            break;
                        case "bawah":
                            worldY += kecepatanBergerak;
                            break;
                        case "kiri":
                            worldX -= kecepatanBergerak;
                            break;
                        case "kanan":
                            worldX += kecepatanBergerak;
                            break;
                    }
                }
            }
        }

        // === Update animasi aman ===
        spriteCounter++;
        if (spriteCounter > 5) { // ganti angka ini untuk kecepatan animasi (lebih besar = lebih lambat)
            spriteNumber++;

            // hitung jumlah frame sesuai arah
            int maxFrame = 1;
            switch (arah) {
                case "atas":
                    maxFrame = jalanAtas.size();
                    break;
                case "bawah":
                    maxFrame = jalanBawah.size();
                    break;
                case "kiri":
                    maxFrame = jalanKiri.size();
                    break;
                case "kanan":
                    maxFrame = jalanKanan.size();
                    break;
            }

            // cegah index keluar batas array
            if (spriteNumber >= maxFrame) {
                spriteNumber = 0;
            }

            spriteCounter = 0;
        }
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

    }

}
