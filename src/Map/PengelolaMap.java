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
    public Map[] map;
    public int mapTileNum[][];

    public PengelolaMap(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        map = new Map[66];
        mapTileNum = new int[gamePanel.maxWorldCol][gamePanel.maxWorldRow];
        getKotakGambar();
        loadMap();
    }

    public void loadMap() {
        try {
            // Pastikan path sesuai dengan lokasi file
            InputStream is = getClass().getResourceAsStream("/Gambar/Map/Map_Atas/Map_Atas_Ngetes.txt");
            // src\Asset\Map\Map_Atas\Map_Atas_Ngetes.txt
            if (is == null) {
                System.out.println("ERROR: File Map_tes.txt tidak ditemukan!");
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int row = 0;
            String line;

            // Baca baris per baris
            while ((line = br.readLine()) != null && row < gamePanel.maxWorldRow) {
                String[] numbers = line.trim().split(" ");

                // Isi setiap kolom di baris ini
                for (int col = 0; col < gamePanel.maxWorldCol && col < numbers.length; col++) {
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                }
                row++;
            }

            br.close();
            System.out.println("Map berhasil di-load: " + row + " baris");

        } catch (IOException e) {
            System.out.println("ERROR saat load map:");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Format angka tidak valid di map file");
            e.printStackTrace();
        }
    }

    public void getKotakGambar() {
        try {
            // daftar tile yang punya collision (ubah sesuai kebutuhanmu)
            int[] tileCollision = { 0, 2, 3, 14, 15, 27, 28, 29, 30, 31, 32, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 53,
                    56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 48, 49, 50, 51, 52 };

            for (int i = 0; i <= 65; i++) {
                map[i] = new Map();
                String namaFile = String.format("/Gambar/Map/Map_Atas/Asset_Map_Atas/%03d.png", i);
                InputStream is = getClass().getResourceAsStream(namaFile);
                // src\Asset\Map\Map_Atas\Asset_Map_Atas\000.png
                // src\Asset\Map\Map_Atas\Asset_Map_Atas\000.png
                if (is == null) {
                    System.out.println("File tidak ditemukan: " + namaFile);
                    continue;
                }

                map[i].gambar = ImageIO.read(is);

                // Cek apakah tile ini termasuk dalam daftar collision
                map[i].bertabrakan = false;
                for (int coll : tileCollision) {
                    if (i == coll) {
                        map[i].bertabrakan = true;
                        break;
                    }
                }
            }
            System.out.println("Semua tile berhasil di-load");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gambar(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gamePanel.maxWorldCol && worldRow < gamePanel.maxWorldRow) {
            int tilenum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gamePanel.ukuranKotak;
            int worldY = worldRow * gamePanel.ukuranKotak;
            int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
            int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

            if (worldX + gamePanel.ukuranKotak > gamePanel.player.worldX - gamePanel.player.screenX &&
                    worldX - gamePanel.ukuranKotak < gamePanel.player.worldX + gamePanel.player.screenX &&
                    worldY + gamePanel.ukuranKotak > gamePanel.player.worldY - gamePanel.player.screenY &&
                    worldY - gamePanel.ukuranKotak < gamePanel.player.worldY + gamePanel.player.screenY) {

                if (tilenum >= 0 && tilenum < map.length && map[tilenum] != null && map[tilenum].gambar != null) {
                    g2.drawImage(map[tilenum].gambar, screenX, screenY, gamePanel.ukuranKotak, gamePanel.ukuranKotak,
                            null);
                }
            }

            worldCol++;

            if (worldCol == gamePanel.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}