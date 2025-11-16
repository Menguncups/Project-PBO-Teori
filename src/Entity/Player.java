package Entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Enemy.Crow;
import Enemy.Demon;
import Enemy.Hog;
import Enemy.Orc;
import Enemy.Skeleton;
import Enemy.Slime;
import Main.GamePanel;
import Main.Keybind;
import Object.ObjectHealthDrop;
import Object.ObjectLadder;
import Object.ObjectRedPotion;
import Object.SupperObject;
import javafx.scene.paint.Color;

public class Player extends Entity {
    int guardFrame = 0; // frame saat block
    int guardCounter = 0; // counter animasi guard
    boolean guarding = false; // flag guard aktif
    Keybind keyBind;
    public boolean invincible = false;
    public int invincibleCounter = 0;
    public boolean invincible2 = false;
    public int invincibleCounter2 = 0;
    public int interactCooldown = 0;
    boolean canAttack = true;
    public ArrayList<SupperObject> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;
    int ladderCooldown = 0;
    public final int screenX, screenY;
    int hasKey = 0;
    int useKey = 0;

    private long lastTeleportTime = 0; // waktu teleport terakhir (dalam ms)
    private final long teleportCooldown = 1000; // delay 1 detik antar teleport

    public Player(GamePanel gamePanel, Keybind keyBind) {
        super(gamePanel);
        this.keyBind = keyBind;
        screenX = gamePanel.screenWidth / 2 - (gamePanel.perbesarKotak / 2);
        screenY = gamePanel.screenHeight / 2 - (gamePanel.perbesarKotak / 2);
        name = "Player";

        solidArea = new Rectangle();
        solidArea.x = 47;
        solidArea.y = 47;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 34;
        solidArea.height = 49;

        attackArea.width = 47;
        attackArea.height = 47;

        posisiAwal();
        getJalanPlayer();
        getAttackPlayer();
        getShieldPlayer();
        setItems();
    }

    public void posisiAwal() {
        worldX = gamePanel.ukuranKotak * 49; // untuk meletakan posisi awal player
        worldY = gamePanel.ukuranKotak * 36;
        kecepatanBergerak = 4;
        arah = "kanan";

        // status player
        maxLife = 12;
        life = maxLife;
    }

    public void setItems() {

    }

    public void getShieldPlayer() {
        try {
            for (int i = 0; i < 7; i++) {
                // Gunakan list baru
                shieldAtas.add(ImageIO.read(
                        getClass().getResourceAsStream(
                                "/Gambar/Player/Fighter/Shield/Atas/Fighter_Shield_Atas_" + i + ".png")));
                shieldBawah.add(ImageIO.read(
                        getClass().getResourceAsStream(
                                "/Gambar/Player/Fighter/Shield/Bawah/Fighter_Shield_Bawah_" + i + ".png")));

                shieldKiri.add(ImageIO.read(
                        getClass().getResourceAsStream(
                                "/Gambar/Player/Fighter/Shield/Kiri/Fighter_Shield_Kiri_" + i + ".png")));

                shieldKanan.add(ImageIO.read(
                        getClass().getResourceAsStream(
                                "/Gambar/Player/Fighter/Shield/Kanan/Fighter_Shield_Kanan_" + i + ".png")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getJalanPlayer() {
        try {
            for (int i = 0; i < 9; i++) {
                jalanAtas.add(ImageIO.read(
                        getClass().getResourceAsStream(
                                "/Gambar/Player/Fighter/Jalan/Jalan_Atas_/Fighter_Jalan_Atas_" + i + ".png")));
                // /Gambar/Player/Fighter/Jalan/Jalan_Atas_/Fighter_Jalan_Atas_0.png
                jalanBawah.add(ImageIO.read(
                        getClass().getResourceAsStream(
                                "/Gambar/Player/Fighter/Jalan/Jalan_Bawah_/Fighter_Jalan_Bawah_" + i + ".png")));

                jalanKiri.add(ImageIO.read(
                        getClass().getResourceAsStream(
                                "/Gambar/Player/Fighter/Jalan/Jalan_Kiri_/Fighter_Jalan_Kiri_" + i + ".png")));

                jalanKanan.add(ImageIO.read(
                        getClass().getResourceAsStream(
                                "/Gambar/Player/Fighter/Jalan/Jalan_Kanan_/Fighter_Jalan_Kanan_" + i + ".png")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAttackPlayer() {
        try {
            for (int i = 0; i < 5; i++) {
                attackAtas.add(ImageIO.read(
                        getClass().getResourceAsStream(
                                "/Gambar/Player/Fighter/Attack/Atas/Fighter_Attack_Atas_" + i + ".png")));
                attackBawah.add(ImageIO.read(
                        getClass().getResourceAsStream(
                                "/Gambar/Player/Fighter/Attack/Bawah/Fighter_Attack_Bawah_" + i + ".png")));
                attackKiri.add(ImageIO.read(
                        getClass().getResourceAsStream(
                                "/Gambar/Player/Fighter/Attack/Kiri/Fighter_Attack_Kiri_" + i + ".png")));
                attackKanan.add(ImageIO.read(
                        getClass().getResourceAsStream(
                                "/Gambar/Player/Fighter/Attack/Kanan/Fighter_Attack_Kanan_" + i + ".png")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int attackCounter = 0;
    int attackFrame = 0;
    int attackDelay = 0;

    public void attack() {
        attackCounter++;

        if (attackCounter > 8) { // kecepatan animasi
            attackFrame++;
            if (attackFrame == 3) {
                gamePanel.playSE(4); // ganti 4 dengan index efek suara kamu
            }
            if (attackFrame >= 5) {

                attackFrame = 0;
                attack = false;
                canAttack = true; // buka kunci serangan
                attackDelay = 10; // delay sebelum boleh serang lagi

                // hitbox pas nyerang
                int currentWorldX = worldX;
                int currentWorldY = worldY;
                int solidAreaWidth = solidArea.width;
                int solidAreaHeight = solidArea.height;

                switch (arah) {
                    case "atas":
                        worldY -= attackArea.height;
                        break;
                    case "bawah":
                        worldY += attackArea.height;
                        break;
                    case "kiri":
                        worldX -= attackArea.width;
                        break;
                    case "kanan":
                        worldX += attackArea.width;
                        break;
                }

                solidArea.width = attackArea.width;
                solidArea.height = attackArea.height;

                int enemyIndex = gamePanel.collisioinCheck.checkEntity(this, gamePanel.enemy);
                damageEnemy(enemyIndex);

                worldX = currentWorldX;
                worldY = currentWorldY;
                solidArea.width = solidAreaWidth;
                solidArea.height = solidAreaHeight;
            }

            attackCounter = 0;
        }
    }

    public void update() {

        // ================= Guard =================
        // ================= Guard/block =================
        if (keyBind.enterDitekan) {
            // --- Tombol DITAHAN (Guard Aktif: Frame 0-5) ---
            guarding = true;

            // 🔥 AKTIFKAN KEBALLAN SEGERA SETELAH TOMBOL DITEKAN
            invincible2 = true;

            invincibleCounter2 = 0;// Reset counter agar efek 'flash' kekebalan terlihat

            // Memastikan animasi tidak melebihi frame 5 saat ditahan
            if (guardFrame <= 5) {
                guardCounter++;
                // 🔥 KECEPATAN ANIMASI (Misalnya, 2 FPS/frame/tick, ini sangat cepat)
                if (guardCounter > 1) {
                    if (guardFrame < 5) { // Hanya naik sampai frame 5
                        guardFrame++;
                    }
                    guardCounter = 0;
                }
            }
        } else {
            // --- Tombol DILEPAS (Animasi Selesai: Frame 6) ---

            // 🔥 NONAKTIFKAN KEBALLAN SAAT TOMBOL DILEPAS
            invincible2 = false;

            if (guarding && guardFrame >= 5 && guardFrame < 6) {
                // Lanjutkan animasi (Release Animation: Frame 6)
                guardCounter++;
                if (guardCounter > 8) { // Kecepatan normal untuk animasi pelepasan
                    guardFrame++; // Akan mencapai 6 (indeks aman terakhir)
                    guardCounter = 0;
                }
            } else if (guarding && guardFrame >= 6) {
                // Animasi selesai
                guarding = false;
                guardFrame = 0;
                guardCounter = 0;
            } else {
                // Reset cepat jika player tidak dalam fase hold/release yang valid
                guarding = false;
                guardFrame = 0;
                guardCounter = 0;
            }
        }
        // checkLadderCollision();
        if (ladderCooldown > 0)
            ladderCooldown--;
        if (attackDelay > 0) {
            attackDelay--;
        }
        if (invincible2) {
            invincibleCounter2++;
            // Hapus batasan waktu 60, biarkan hanya dikontrol oleh tombol Enter
            // if (invincibleCounter2 > 60) {
            // invincible2 = false;
            // invincibleCounter2 = 0;
            // }
        }

        // if (keyBind.enterDitekan) {
        // guarding = true;
        // }

        if (attack) {
            attack();
        }

        // ========== INVENTORY STATE ==========
        // Proses inventory PERTAMA, sebelum logic lainnya
        if (gamePanel.gameState == gamePanel.inventoryState) {
            if (keyBind.spasiDitekan && interactCooldown == 0) {
                selectItem();
                keyBind.spasiDitekan = false; // reset flag manual
                interactCooldown = 20;
            }
            return; // STOP di sini, jangan proses logic playState
        }

        // ========== PLAY STATE ==========
        boolean bergerak = false;

        // arah gerak
        if (keyBind.keAtas) {
            arah = "atas";
            bergerak = true;
        } else if (keyBind.keBawah) {
            arah = "bawah";
            bergerak = true;
        } else if (keyBind.keKiri) {
            arah = "kiri";
            bergerak = true;
        } else if (keyBind.keKanan) {
            arah = "kanan";
            bergerak = true;
        }

        // --- Cek interaksi / serangan ---
        if (interactCooldown > 0) {
            interactCooldown--;
        }

        int npcIndex = gamePanel.collisioinCheck.checkEntity(this, gamePanel.npc);

        // tekan spasi: bicara atau serang (hanya di playState)
        if (keyBind.spasiDitekan && interactCooldown == 0 && canAttack) {
            interactNPC(npcIndex);
            keyBind.spasiDitekan = false; // reset setelah digunakan
            interactCooldown = 20;
        }

        // --- Gerak player ---
        if (bergerak && !attack && !guarding) {
            collisionOn = false;
            gamePanel.collisioinCheck.checkTile(this);

            int objIndex = gamePanel.collisioinCheck.checkObject(this, true);
            pickUpObject(objIndex);
            if (objIndex != 999) {
                interactObject(objIndex);
            }

            int enemyIndex = gamePanel.collisioinCheck.checkEntity(this, gamePanel.enemy);
            contactEnemy(enemyIndex);

            if (!collisionOn) {
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

            guarding = false;

            spriteCounter++;
            if (spriteCounter > 10) {
                spriteNumber = (spriteNumber + 1) % 9;
                spriteCounter = 0;
            }
        } else if (!bergerak && !attack && !guarding) { // Tambahkan `!guarding`
            spriteNumber = 0;
        }

        // invincible logic
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if (life <= 0) {
            gamePanel.playSE(9);
            gamePanel.gameState = gamePanel.gameOverState;
        }

        int col = worldX / gamePanel.ukuranKotak;
        int row = worldY / gamePanel.ukuranKotak;

        // contoh: portal dari map 0 ke 1
        if (gamePanel.currentMap == 0 && col == 5 && row == 5) {
            gamePanel.gantiMap(1, 10, 10);
        }

        // portal balik dari map 1 ke 0
        if (gamePanel.currentMap == 1 && col == 10 && row == 10) {
            gamePanel.gantiMap(0, 5, 5);
        }
        // === TELEPORT OTOMATIS DENGAN DELAY ===
        int tileX = worldX / gamePanel.ukuranKotak;
        int tileY = worldY / gamePanel.ukuranKotak;

        // waktu sekarang (ms)
        long now = System.currentTimeMillis();

        // cek apakah teleport sudah bisa lagi (lebih dari 1 detik sejak terakhir
        // teleport)
        if (now - lastTeleportTime > teleportCooldown) {
            if (gamePanel.currentMap == 0 && tileX == 20 && tileY == 82) {
                gamePanel.gantiMap(1, 57, 57);
                lastTeleportTime = now;
            }
            // Teleport balik dari Map 1 ke Map 0
            else if (gamePanel.currentMap == 1 && tileX == 56 && tileY == 54) {
                gamePanel.gantiMap(0, 21, 83);
                lastTeleportTime = now;
            }

            // --- LOGIKA BARU (MAP 1 <-> MAP 2) ---

            // 1. Pindah dari Map 1 ke Map 2
            // Trigger: Player injak x=55, y=49 di Map 1
            else if (gamePanel.currentMap == 1 && tileX == 55 && tileY == 46) {
                // gantiMap(MapTujuan, SpawnX, SpawnY)
                // Saya atur spawn di Map 2 tepat di titik 16,29 (atau geser sedikit jika perlu)
                gamePanel.gantiMap(2, 15, 33);
                lastTeleportTime = now;
            }

            // 2. Pindah dari Map 2 ke Map 1
            // Trigger: Player injak x=16, y=29 di Map 2
            else if (gamePanel.currentMap == 2 && tileX == 15 && tileY == 36) {
                // Balik ke Map 1, spawn di 55,49
                gamePanel.gantiMap(1, 55, 48);
                lastTeleportTime = now;
            }
        }

    }

    public void knocBack(Entity entity) {
        entity.arah = arah;
        entity.kecepatanBergerak += 1;
        entity.knocBack = true;
    }

    public void interactObject(int i) {
        if (i != 999 && gamePanel.obj[i] != null) {
            SupperObject obj = gamePanel.obj[i];
            if (obj.name.equals("Ladder")) {
                System.out.println("🪜 Naik/turun tangga ke map " + obj.targetMap);
                gamePanel.gantiMap(obj.targetMap, obj.targetX, obj.targetY);
            }
        }
    }

    public void contactEnemy(int i) {
        // Cek tabrakan dengan musuh
        if (i != 999) {
            Entity enemy = gamePanel.enemy[i];

            // 🛡️ LOGIKA BLOCKING/GUARDING: JIKA SEDANG GUARDING, KEMBALI TANPA DAMAGE
            // Kita tidak perlu cek frame lagi, cukup cek `guarding` karena `invincible`
            // sudah diaktifkan di update().
            if (invincible2) {
                System.out.println("Block/Guard berhasil!");
                // gamePanel.playSE(7);

                if (enemy.life > 0) {
                    knocBack(enemy);
                    enemy.actionLockCounter = 60;
                }
                return; // TIDAK KENAL DAMAGE
            }
            if (guarding) {
                System.out.println("Block/Guard berhasil!");
                // gamePanel.playSE(7);

                // Tambahkan knockback musuh terlepas dari apakah kita menggunakan invincible
                // flag
                if (enemy.life > 0) {
                    knocBack(enemy);
                    enemy.actionLockCounter = 60;
                }
                return; // TIDAK KENAL DAMAGE KARENA SEDANG GUARD
            }

            // LOGIKA NORMAL: Menerima damage jika TIDAK sedang Guard dan TIDAK sedang
            // Invincible akibat damage sebelumnya
            if (!invincible) { // Menggunakan flag invincible bawaan untuk damage-flash.
                if (enemy instanceof Slime) {
                    life -= 1;
                    invincible = true; // Aktifkan invincibility (damage flash)
                    knocBack(enemy);
                    System.out.println("Terkena slime! Sisa darah: " + life);
                    gamePanel.playSE(3);
                } else if (enemy instanceof Hog) {
                    life -= 1;
                    invincible = true; // Aktifkan invincibility (damage flash)
                    knocBack(enemy);
                    System.out.println("Terkena slime! Sisa darah: " + life);
                    gamePanel.playSE(3);
                } else {
                    if (enemy.attack) {
                        life -= 1;
                        invincible = true; // Aktifkan invincibility (damage flash)
                        knocBack(enemy);
                        System.out.println("Terkena serangan musuh! Sisa darah: " + life);
                        gamePanel.playSE(3);
                    }
                }
            }
        }
    }

    public void damageEnemy(int i) {
        if (i != 999) {
            Entity enemy = gamePanel.enemy[i];

            // hanya serang kalau belum invincible
            if (!enemy.invincible) {
                knocBack(gamePanel.enemy[i]);
                gamePanel.playSE(5);
                enemy.life -= 1;
                enemy.invincible = true; // kasih waktu kebal setelah kena hit

                if (enemy.life <= 0) {
                    if (enemy.life <= 0) {

                        // 🔥 FIX: CEK APAKAH MUSUH YANG MATI ADALAH BOSS?
                        if (enemy.name.equals("BossRock")) {
                            // gamePanel.stopMusic();
                            gamePanel.gameState = gamePanel.gameWinState; // Pindah ke The End
                            gamePanel.playSE(10); // (Opsional) Efek suara menang
                        }

                        // Baru hapus musuh dari array
                        gamePanel.enemy[i] = null;
                    }
                    if (enemy instanceof Slime) {
                        ((Slime) enemy).checkDrop(); // drop di posisi mati
                    }
                    if (enemy instanceof Crow) {
                        ((Crow) enemy).checkDrop(); // drop di posisi mati
                    }
                    if (enemy instanceof Demon) {
                        ((Demon) enemy).checkDrop(); // drop di posisi mati
                    }
                    if (enemy instanceof Hog) {
                        ((Hog) enemy).checkDrop(); // drop di posisi mati
                    }
                    if (enemy instanceof Orc) {
                        ((Orc) enemy).checkDrop(); // drop di posisi mati
                    }
                    if (enemy instanceof Skeleton) {
                        ((Skeleton) enemy).checkDrop(); // drop di posisi mati
                    }
                    gamePanel.enemy[i] = null; // hapus slime dari array enemy
                }

            }
        }
    }

    public void pickUpObject(int i) {
        if (i != 999) {
            SupperObject obj = gamePanel.obj[i];
            // hanya objek yang bisa diambil
            if (obj.pickupable) {
                // Cek jenis objek
                if (obj instanceof ObjectHealthDrop) {
                    // Tambahkan darah langsung
                    life += 3; // jumlah darah yang ingin ditambahkan
                    if (life > maxLife) {
                        life = maxLife; // batasi agar tidak lebih dari maxLife
                    }
                    gamePanel.playSE(2); // efek suara
                    gamePanel.obj[i] = null; // hapus dari dunia
                    System.out.println("Mengambil heart! Darah sekarang: " + life + "/" + maxLife);
                } else {
                    // objek biasa masuk inventory
                    if (inventory.size() < maxInventorySize) {
                        inventory.add(obj);
                        gamePanel.playSE(2);
                        gamePanel.obj[i] = null;
                        System.out.println("Menambahkan ke inventory: " + obj.name);
                    } else {
                        System.out.println("Inventory penuh!");
                    }
                }
            } else {
                // --- LOGIKA OBJEK YANG TIDAK BISA DIAMBIL (INTERAKSI) ---

                // health drop
                if (obj instanceof ObjectHealthDrop) {
                    ((ObjectHealthDrop) obj).use(this);
                    gamePanel.obj[i] = null; // hapus dari dunia
                    System.out.println("Heart diambil! Darah sekarang: " + life + "/" + maxLife);
                    return;
                }

                String objectName = obj.name;

                // 🔥 LOGIKA KUNCI GABUNGAN UNTUK DOOR11 dan DOOR12 🔥
                // 🔥 LOGIKA KUNCI BERSAMA UNTUK DOOR11 dan DOOR12 🔥
                if (objectName.equals("Door11") || objectName.equals("Door12")) {

                    // 1. Cek apakah player punya Key2 di inventory
                    int keyIndex = -1;
                    for (int j = 0; j < inventory.size(); j++) {
                        if (inventory.get(j).name.equals("Key2")) {
                            keyIndex = j;
                            break;
                        }
                    }

                    if (keyIndex != -1) {
                        // Kunci ditemukan. Buka pintu saat ini.
                        gamePanel.obj[i] = null; // Hapus pintu dari dunia
                        gamePanel.playSE(1);
                        System.out.println("Pintu " + obj.name + " terbuka!");

                        // 2. CEK APAKAH PINTU LAIN SUDAH DIBUKA (Pintu di dunia sudah dihapus)
                        boolean otherDoorStillExists = false;

                        // Pintu yang sedang diinteraksi sudah dianggap null di sini (gamePanel.obj[i] =
                        // null)

                        for (int k = 0; k < gamePanel.obj.length; k++) {
                            // Cek semua slot objek di map
                            if (gamePanel.obj[k] != null) {
                                String remainingName = gamePanel.obj[k].name;

                                // Jika ada objek lain yang merupakan Door11 atau Door12,
                                // artinya pintu kedua masih ada.
                                if (remainingName.equals("Door11") || remainingName.equals("Door12")) {
                                    otherDoorStillExists = true;
                                    break;
                                }
                            }
                        }

                        if (!otherDoorStillExists) {
                            // Tidak ada Door11 atau Door12 yang tersisa di peta (keduanya sudah dibuka).
                            inventory.remove(keyIndex); // 🔥 Hapus kunci dari inventory
                            System.out.println("Kunci Key2 digunakan dan dihapus karena kedua pintu terbuka.");
                        }

                    } else {
                        // Kunci tidak ditemukan
                        System.out.println("Anda memerlukan Key2 untuk membuka " + obj.name + ".");
                    }
                    return; // Selesai memproses pintu gabungan
                }

                // Logika objek lain yang tidak bisa diambil (Door lama, dll.)
                switch (objectName) {
                    case "Door":
                        // cek apakah player punya key di inventory
                        boolean hasKeyItem = false;
                        int keyIndexDefault = -1;
                        for (int j = 0; j < inventory.size(); j++) {
                            if (inventory.get(j).name.equals("Key")) {
                                hasKeyItem = true;
                                keyIndexDefault = j;
                                break;
                            }
                        }
                        if (hasKeyItem) {
                            gamePanel.obj[i] = null;
                            gamePanel.playSE(1);
                            System.out.println("Pintu terbuka!");
                            inventory.remove(keyIndexDefault);
                            System.out.println("Kunci digunakan dan dihapus dari inventory.");
                        } else {
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void selectItem() {
        int itemIndex = gamePanel.ui.getItemIndexOnSlot();

        if (itemIndex < inventory.size()) {
            SupperObject selectedItem = inventory.get(itemIndex);

            if (selectedItem.name.equals("Red Potion")) {
                if (selectedItem instanceof ObjectRedPotion) {
                    ((ObjectRedPotion) selectedItem).use(this);
                    inventory.remove(itemIndex);
                    System.out.println("Menggunakan Red Potion! Darah sekarang: " + life + "/" + maxLife);
                }
            }
        }
    }

    public void interactNPC(int i) {
        if (i != 999) {
            // bicara dengan NPC
            gamePanel.npc[i].speak();
            gamePanel.gameState = gamePanel.dialogueState;
            gamePanel.ui.currentDialogIndex = 0;
            gamePanel.ui.currentDialog = gamePanel.npc[i].dialouges[0];
        } else {
            // tidak ada NPC → serang
            if (canAttack && !attack) {
                attack = true;
                canAttack = false; // kunci agar tidak bisa diserang ulang
                attackFrame = 0;
                attackCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage gambar = null;

        // --- Tentukan sprite yang akan digambar
        if (attack) {
            switch (arah) {
                case "atas":
                    gambar = attackAtas.get(attackFrame);
                    break;
                case "bawah":
                    gambar = attackBawah.get(attackFrame);
                    break;
                case "kiri":
                    gambar = attackKiri.get(attackFrame);
                    break;
                case "kanan":
                    gambar = attackKanan.get(attackFrame);
                    break;
            }
        } else if (guarding) {
            switch (arah) {
                case "atas":
                    gambar = shieldAtas.get(guardFrame);
                    break;
                case "bawah":
                    gambar = shieldBawah.get(guardFrame);
                    break;
                case "kiri":
                    gambar = shieldKiri.get(guardFrame);
                    break;
                case "kanan":
                    gambar = shieldKanan.get(guardFrame);
                    break;
            }

        } else {
            switch (arah) {
                case "atas":
                    gambar = jalanAtas.get(spriteNumber);
                    break;
                case "bawah":
                    gambar = jalanBawah.get(spriteNumber);
                    break;
                case "kiri":
                    gambar = jalanKiri.get(spriteNumber);
                    break;
                case "kanan":
                    gambar = jalanKanan.get(spriteNumber);
                    break;
            }
        }

        // --- efek transparan saat invincible
        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }

        // --- gambar player (normal)
        g2.drawImage(gambar, screenX, screenY, gamePanel.perbesarKotak * 2, gamePanel.perbesarKotak * 2, null);

        // --- kembalikan opasitas normal
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // ==========================================================
        // Debug: hitbox serangan + collider
        // ==========================================================

        // // 🔥 simpan transformasi sebelum scaling otomatis dari fullscreen
        // java.awt.geom.AffineTransform oldTransform = g2.getTransform();

        // // 🔥 hitung scaleX dan scaleY (jika fullscreen aktif)
        // double scaleX = (double) gamePanel.getWidth() / gamePanel.screenWidth;
        // double scaleY = (double) gamePanel.getHeight() / gamePanel.screenHeight;

        // // 🔥 normalisasi (balikkan scaling biar hitbox tidak ikut membesar)
        // g2.scale(1.0 / scaleX, 1.0 / scaleY);

        // --- hitbox serangan
        if (attack) {
            // g2.setColor(java.awt.Color.ORANGE);

            int attackBoxX = screenX + solidArea.x;
            int attackBoxY = screenY + solidArea.y;
            int attackBoxWidth = attackArea.width;
            int attackBoxHeight = attackArea.height;

            switch (arah) {
                case "atas":
                    attackBoxY -= attackArea.height;
                    break;
                case "bawah":
                    attackBoxY += solidArea.height;
                    break;
                case "kiri":
                    attackBoxX -= attackArea.width;
                    break;
                case "kanan":
                    attackBoxX += solidArea.width;
                    break;
            }
            // g2.drawRect(attackBoxX, attackBoxY, attackBoxWidth, attackBoxHeight);
        }
        // // --- hitbox player
        // g2.setColor(java.awt.Color.RED);
        // int collisionX = screenX + solidArea.x;
        // int collisionY = screenY + solidArea.y;
        // g2.drawRect(collisionX, collisionY, solidArea.width, solidArea.height);

        // // --- ukuran sprite (debug hijau)
        // g2.setColor(java.awt.Color.GREEN);
        // g2.drawRect(screenX, screenY, gamePanel.perbesarKotak * 2,
        // gamePanel.perbesarKotak * 2);

        // 🔥 kembalikan transformasi agar elemen lain tetap normal
        // g2.setTransform(oldTransform);
    }

}
