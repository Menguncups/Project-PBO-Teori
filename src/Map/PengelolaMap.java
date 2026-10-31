package Map;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import Main.GamePanel;

public class PengelolaMap {
    GamePanel gamePanel;
    public Map[][] map; // [mapIndex][tileIndex]
    public int[][][] mapTileNum; // [mapIndex][col][row]

    // 🔥 PERBAIKAN 1: Perbesar kapasitas array agar aman dari crash
    // Jika map.txt kamu punya angka '90', array size 66 akan error.
    final int MAX_TILES = 100;

    public PengelolaMap(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        map = new Map[gamePanel.maxMap][MAX_TILES];
        mapTileNum = new int[gamePanel.maxMap][gamePanel.maxWorldCol][gamePanel.maxWorldRow];

        // ✅ Load MAP 0 (Map Atas)
        getKotakGambar("Map_Atas", 0);
        loadMap("/Gambar/Map/Map_Atas/Map_Atas_Ngetes.txt", 0);

        // ✅ Load MAP 1 (Map Dungeon)
        getKotakGambar("Map_Dungeon", 1);
        loadMap("/Gambar/Map/Map_Dungeon/Map_Dungeon.txt", 1);

        // ✅ Load MAP 2 (Map Boss)
        getKotakGambar("Map_Boss", 2);
        loadMap("/Gambar/Map/Map_Boss/boss.txt", 2);
    }

    public void getKotakGambar(String folderMap, int mapIndex) {
        try {
            // 🔥 PERBAIKAN 2: Jangan batasi loop secara manual (limitTile).
            // Biarkan loop jalan sampai MAX_TILES.
            // Jika file gambar tidak ada, sistem akan otomatis skip (continue).

            for (int i = 0; i < MAX_TILES; i++) {

                // Format nama file: 000.png, 001.png ... 099.png
                String namaFile = String.format("/Gambar/Map/%s/Asset_%s/%03d.png",
                        folderMap, folderMap, i);

                InputStream is = getClass().getResourceAsStream(namaFile);

                // Jika gambar tidak ditemukan, lewati saja (jangan crash)
                if (is == null) {
                    continue;
                }

                // Jika gambar ditemukan, baru inisialisasi object Map
                map[mapIndex][i] = new Map();
                map[mapIndex][i].gambar = ImageIO.read(is);
            }

            // ✅ Setup Collision (Tabrakan)
            setupCollision(mapIndex);

            // System.out.println("Asset untuk map " + folderMap + " selesai di-load.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Saya pisahkan setup collision agar lebih rapi
    public void setupCollision(int mapIndex) {
        int[] tileCollision = new int[0]; // default kosong

        if (mapIndex == 0) {
            // MAP 0 (Map Atas)
            tileCollision = new int[] {
                    0, 2, 3, 14, 15, 27, 28, 29, 30, 31, 32, 36, 37, 38, 39, 40, 41,
                    42, 43, 44, 45, 53, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65,
                    48, 49, 50, 51, 52
            };
        } else if (mapIndex == 1) {
            // MAP 1 (Map Dungeon)
            tileCollision = new int[] { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 22, 24, 25 };
        } else if (mapIndex == 2) {
            // MAP 2 (Map Boss)
            tileCollision = new int[] { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 20, 21 };
        }

        // Set collision flag
        for (int i : tileCollision) {
            // Cek bounds agar tidak error jika salah ketik angka collision
            if (i >= 0 && i < MAX_TILES && map[mapIndex][i] != null) {
                map[mapIndex][i].bertabrakan = true;
            }
        }
    }

    public void loadMap(String filePath, int mapIndex) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            // Cek jika file txt map tidak ketemu
            if (is == null) {
                System.out.println("❌ ERROR: File map tidak ditemukan: " + filePath);
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int row = 0;
            String line;
            while ((line = br.readLine()) != null && row < gamePanel.maxWorldRow) {
                String[] numbers = line.trim().split(" ");
                for (int col = 0; col < gamePanel.maxWorldCol && col < numbers.length; col++) {
                    try {
                        int num = Integer.parseInt(numbers[col]);
                        mapTileNum[mapIndex][col][row] = num;
                    } catch (NumberFormatException e) {
                        System.out.println("⚠️ Warning: Ada angka aneh di map file baris " + row);
                    }
                }
                row++;
            }
            br.close();
            // System.out.println("Layout map " + mapIndex + " berhasil di-load!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gambar(Graphics2D g2) {
        int mapIndex = gamePanel.currentMap;

        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gamePanel.maxWorldCol && worldRow < gamePanel.maxWorldRow) {
            int tileNum = mapTileNum[mapIndex][worldCol][worldRow];

            int worldX = worldCol * gamePanel.ukuranKotak;
            int worldY = worldRow * gamePanel.ukuranKotak;
            int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
            int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

            // 🔥 PERBAIKAN 3: Anti-Crash saat Menggambar
            // 1. Cek apakah tileNum ada di dalam batas array (0 - 99)
            // 2. Cek apakah tile tersebut sudah di-load (tidak null)

            if (tileNum >= 0 && tileNum < MAX_TILES && map[mapIndex][tileNum] != null
                    && map[mapIndex][tileNum].gambar != null) {
                g2.drawImage(map[mapIndex][tileNum].gambar, screenX, screenY,
                        gamePanel.ukuranKotak, gamePanel.ukuranKotak, null);
            } else {
                // Jika tileNum valid tapi gambarnya null (belum diload), bisa gambar kotak
                // hitam/kosong
                // Ini mencegah error jika di txt ada angka '25' tapi gambar 025.png tidak ada
            }

            worldCol++;
            if (worldCol == gamePanel.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}