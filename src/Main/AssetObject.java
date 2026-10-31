package Main;

import java.rmi.server.Skeleton;

import Enemy.Crow;
import Enemy.Hog;
import Enemy.Orc;
import Enemy.Slime;
import Entity.NPCTua;
import Object.ObejctDoor11;
import Object.ObejctDoor12;
import Object.ObjectChest;
import Object.ObjectDoor;
import Object.ObjectHealthDrop;
import Object.ObjectKey;
import Object.ObjectKey2;
import Object.ObjectLadder;
import Object.ObjectRedPotion;

public class AssetObject {

    GamePanel gamePanel;

    public AssetObject(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void setObject(int mapIndex) {
        int i = 0;

        if (mapIndex == 0) {
            gamePanel.mapObject[0][i] = new ObjectKey(gamePanel);
            gamePanel.mapObject[0][i].worldX = gamePanel.ukuranKotak * 33;
            gamePanel.mapObject[0][i].worldY = gamePanel.ukuranKotak * 87;
            i++;    

            gamePanel.mapObject[0][i] = new ObjectRedPotion(gamePanel);
            gamePanel.mapObject[0][i].worldX = gamePanel.ukuranKotak * 27;
            gamePanel.mapObject[0][i].worldY = gamePanel.ukuranKotak * 82;
            i++;

            gamePanel.mapObject[0][i] = new ObjectRedPotion(gamePanel);
            gamePanel.mapObject[0][i].worldX = gamePanel.ukuranKotak * 74;
            gamePanel.mapObject[0][i].worldY = gamePanel.ukuranKotak * 37;
            i++;

            gamePanel.mapObject[0][i] = new ObjectRedPotion(gamePanel);
            gamePanel.mapObject[0][i].worldX = gamePanel.ukuranKotak * 79;
            gamePanel.mapObject[0][i].worldY = gamePanel.ukuranKotak * 62;
            i++;

            gamePanel.mapObject[0][i] = new ObjectRedPotion(gamePanel);
            gamePanel.mapObject[0][i].worldX = gamePanel.ukuranKotak * 83;
            gamePanel.mapObject[0][i].worldY = gamePanel.ukuranKotak * 87;
            i++;

            gamePanel.mapObject[0][i] = new ObjectRedPotion(gamePanel);
            gamePanel.mapObject[0][i].worldX = gamePanel.ukuranKotak * 52;
            gamePanel.mapObject[0][i].worldY = gamePanel.ukuranKotak * 86;
            i++;

            gamePanel.mapObject[0][i] = new ObjectRedPotion(gamePanel);
            gamePanel.mapObject[0][i].worldX = gamePanel.ukuranKotak * 36;
            gamePanel.mapObject[0][i].worldY = gamePanel.ukuranKotak * 85;
            i++;

            gamePanel.mapObject[0][i] = new ObjectRedPotion(gamePanel);
            gamePanel.mapObject[0][i].worldX = gamePanel.ukuranKotak * 23;
            gamePanel.mapObject[0][i].worldY = gamePanel.ukuranKotak * 65;
            i++;

            gamePanel.mapObject[0][i] = new ObjectRedPotion(gamePanel);
            gamePanel.mapObject[0][i].worldX = gamePanel.ukuranKotak * 25;
            gamePanel.mapObject[0][i].worldY = gamePanel.ukuranKotak * 43;
            i++;

            gamePanel.mapObject[0][i] = new ObjectRedPotion(gamePanel);
            gamePanel.mapObject[0][i].worldX = gamePanel.ukuranKotak * 22;
            gamePanel.mapObject[0][i].worldY = gamePanel.ukuranKotak * 32;
            i++;

            gamePanel.mapObject[0][i] = new ObjectDoor();
            gamePanel.mapObject[0][i].worldX = gamePanel.ukuranKotak * 21;
            gamePanel.mapObject[0][i].worldY = gamePanel.ukuranKotak * 86;
            i++;

            // gamePanel.mapObject[0][i] = new ObjectChest();
            // gamePanel.mapObject[0][i].worldX = gamePanel.ukuranKotak * 13;
            // gamePanel.mapObject[0][i].worldY = gamePanel.ukuranKotak * 18;
            // i++;

            // gamePanel.mapObject[0][i] = new ObjectChest();
            // gamePanel.mapObject[0][i].worldX = gamePanel.ukuranKotak * 75;
            // gamePanel.mapObject[0][i].worldY = gamePanel.ukuranKotak * 38;
            // i++;

            // gamePanel.mapObject[0][i] = new ObjectChest();
            // gamePanel.mapObject[0][i].worldX = gamePanel.ukuranKotak * 78;
            // gamePanel.mapObject[0][i].worldY = gamePanel.ukuranKotak * 55;
            // i++;

            // gamePanel.mapObject[0][i] = new ObjectHealthDrop(gamePanel);
            // gamePanel.mapObject[0][i].worldX = gamePanel.ukuranKotak * 25;
            // gamePanel.mapObject[0][i].worldY = gamePanel.ukuranKotak * 82;
            // i++;

            // gamePanel.mapObject[0][i] = new ObjectChest();
            // gamePanel.mapObject[0][i].worldX = gamePanel.ukuranKotak * 52;
            // gamePanel.mapObject[0][i].worldY = gamePanel.ukuranKotak * 88;
            // i++;

            // // Tangga dari map 0 ke map 1
            // gamePanel.mapObject[0][i] = new ObjectLadder(gamePanel);
            // gamePanel.mapObject[0][i].worldX = gamePanel.ukuranKotak * 21;
            // gamePanel.mapObject[0][i].worldY = gamePanel.ukuranKotak * 83;
            // gamePanel.mapObject[0][i].targetMap = 1;
            // gamePanel.mapObject[0][i].targetX = 57;
            // gamePanel.mapObject[0][i].targetY = 57;
            // i++;
        }

        if (mapIndex == 1) {
            gamePanel.mapObject[1][i] = new ObjectKey2(gamePanel);
            gamePanel.mapObject[1][i].worldX = gamePanel.ukuranKotak * 32;
            gamePanel.mapObject[1][i].worldY = gamePanel.ukuranKotak * 71;
            i++;

            gamePanel.mapObject[1][i] = new ObjectRedPotion(gamePanel);
            gamePanel.mapObject[1][i].worldX = gamePanel.ukuranKotak * 57;
            gamePanel.mapObject[1][i].worldY = gamePanel.ukuranKotak * 70;
            i++;

            gamePanel.mapObject[1][i] = new ObjectRedPotion(gamePanel);
            gamePanel.mapObject[1][i].worldX = gamePanel.ukuranKotak * 57;
            gamePanel.mapObject[1][i].worldY = gamePanel.ukuranKotak * 70;
            i++;

            gamePanel.mapObject[1][i] = new ObjectRedPotion(gamePanel);
            gamePanel.mapObject[1][i].worldX = gamePanel.ukuranKotak * 28;
            gamePanel.mapObject[1][i].worldY = gamePanel.ukuranKotak * 63;
            i++;

            gamePanel.mapObject[1][i] = new ObjectRedPotion(gamePanel);
            gamePanel.mapObject[1][i].worldX = gamePanel.ukuranKotak * 43;
            gamePanel.mapObject[1][i].worldY = gamePanel.ukuranKotak * 63;
            i++;

            gamePanel.mapObject[1][i] = new ObjectRedPotion(gamePanel);
            gamePanel.mapObject[1][i].worldX = gamePanel.ukuranKotak * 33;
            gamePanel.mapObject[1][i].worldY = gamePanel.ukuranKotak * 51;
            i++;

            gamePanel.mapObject[1][i] = new ObjectRedPotion(gamePanel);
            gamePanel.mapObject[1][i].worldX = gamePanel.ukuranKotak * 47;
            gamePanel.mapObject[1][i].worldY = gamePanel.ukuranKotak * 55;
            i++;

            gamePanel.mapObject[1][i] = new ObjectRedPotion(gamePanel);
            gamePanel.mapObject[1][i].worldX = gamePanel.ukuranKotak * 56;
            gamePanel.mapObject[1][i].worldY = gamePanel.ukuranKotak * 51;
            i++;

            gamePanel.mapObject[1][i] = new ObejctDoor11();
            gamePanel.mapObject[1][i].worldX = gamePanel.ukuranKotak * 36;
            gamePanel.mapObject[1][i].worldY = gamePanel.ukuranKotak * 68;
            i++;

            gamePanel.mapObject[1][i] = new ObejctDoor11();
            gamePanel.mapObject[1][i].worldX = gamePanel.ukuranKotak * 36;
            gamePanel.mapObject[1][i].worldY = gamePanel.ukuranKotak * 57;
            i++;

            gamePanel.mapObject[1][i] = new ObejctDoor11();
            gamePanel.mapObject[1][i].worldX = gamePanel.ukuranKotak * 45;
            gamePanel.mapObject[1][i].worldY = gamePanel.ukuranKotak * 52;
            i++;

            gamePanel.mapObject[1][i] = new ObejctDoor12();
            gamePanel.mapObject[1][i].worldX = gamePanel.ukuranKotak * 35;
            gamePanel.mapObject[1][i].worldY = gamePanel.ukuranKotak * 68;
            i++;

            gamePanel.mapObject[1][i] = new ObejctDoor12();
            gamePanel.mapObject[1][i].worldX = gamePanel.ukuranKotak * 35;
            gamePanel.mapObject[1][i].worldY = gamePanel.ukuranKotak * 57;
            i++;

            gamePanel.mapObject[1][i] = new ObejctDoor12();
            gamePanel.mapObject[1][i].worldX = gamePanel.ukuranKotak * 44;
            gamePanel.mapObject[1][i].worldY = gamePanel.ukuranKotak * 52;
            i++;
            // // Tangga dari map 1 kembali ke map 0
            // gamePanel.mapObject[1][i] = new ObjectLadder(gamePanel);
            // gamePanel.mapObject[1][i].worldX = gamePanel.ukuranKotak * 57;
            // gamePanel.mapObject[1][i].worldY = gamePanel.ukuranKotak * 57;
            // gamePanel.mapObject[1][i].targetMap = 0;
            // gamePanel.mapObject[1][i].targetX = 21;
            // gamePanel.mapObject[1][i].targetY = 83;
            // i++;
        }
        if (mapIndex == 2) {

            gamePanel.mapObject[2][i] = new ObjectRedPotion(gamePanel);
            gamePanel.mapObject[2][i].worldX = gamePanel.ukuranKotak * 10;
            gamePanel.mapObject[2][i].worldY = gamePanel.ukuranKotak * 8;
            i++;

            gamePanel.mapObject[2][i] = new ObjectRedPotion(gamePanel);
            gamePanel.mapObject[2][i].worldX = gamePanel.ukuranKotak * 24;
            gamePanel.mapObject[2][i].worldY = gamePanel.ukuranKotak * 9;
            i++;

            gamePanel.mapObject[2][i] = new ObjectRedPotion(gamePanel);
            gamePanel.mapObject[2][i].worldX = gamePanel.ukuranKotak * 25;
            gamePanel.mapObject[2][i].worldY = gamePanel.ukuranKotak * 25;
            i++;

            gamePanel.mapObject[2][i] = new ObjectRedPotion(gamePanel);
            gamePanel.mapObject[2][i].worldX = gamePanel.ukuranKotak * 8;
            gamePanel.mapObject[2][i].worldY = gamePanel.ukuranKotak * 25;
            i++;
        }
    }

    public void setNPC(int mapIndex) {
        if (mapIndex == 0) {

            gamePanel.mapNPC[0][0] = new NPCTua(gamePanel);
            gamePanel.mapNPC[0][0].worldX = gamePanel.ukuranKotak * 48;
            gamePanel.mapNPC[0][0].worldY = gamePanel.ukuranKotak * 35;
        }

        if (mapIndex == 1) {

        }
    }

    public void setEnemy(int mapIndex) {
        int i = 0;

        // ================= MAP 0 =================
        if (mapIndex == 0) {
            // Slime
            gamePanel.mapEnemy[0][i] = new Slime(gamePanel);
            gamePanel.mapEnemy[0][i].worldX = gamePanel.ukuranKotak * 24;
            gamePanel.mapEnemy[0][i].worldY = gamePanel.ukuranKotak * 15;
            i++;
            gamePanel.mapEnemy[0][i] = new Slime(gamePanel);
            gamePanel.mapEnemy[0][i].worldX = gamePanel.ukuranKotak * 44;
            gamePanel.mapEnemy[0][i].worldY = gamePanel.ukuranKotak * 67;
            i++;
            gamePanel.mapEnemy[0][i] = new Slime(gamePanel);
            gamePanel.mapEnemy[0][i].worldX = gamePanel.ukuranKotak * 50;
            gamePanel.mapEnemy[0][i].worldY = gamePanel.ukuranKotak * 66;
            i++;

            gamePanel.mapEnemy[0][i] = new Slime(gamePanel);
            gamePanel.mapEnemy[0][i].worldX = gamePanel.ukuranKotak * 37;
            gamePanel.mapEnemy[0][i].worldY = gamePanel.ukuranKotak * 68;
            i++;

            gamePanel.mapEnemy[0][i] = new Slime(gamePanel);
            gamePanel.mapEnemy[0][i].worldX = gamePanel.ukuranKotak * 29;
            gamePanel.mapEnemy[0][i].worldY = gamePanel.ukuranKotak * 19;
            i++;

            gamePanel.mapEnemy[0][i] = new Slime(gamePanel);
            gamePanel.mapEnemy[0][i].worldX = gamePanel.ukuranKotak * 19;
            gamePanel.mapEnemy[0][i].worldY = gamePanel.ukuranKotak * 13;
            i++;

            gamePanel.mapEnemy[0][i] = new Slime(gamePanel);
            gamePanel.mapEnemy[0][i].worldX = gamePanel.ukuranKotak * 24;
            gamePanel.mapEnemy[0][i].worldY = gamePanel.ukuranKotak * 19;
            i++;

            // Hog
            gamePanel.mapEnemy[0][i] = new Hog(gamePanel);
            gamePanel.mapEnemy[0][i].worldX = gamePanel.ukuranKotak * 45;
            gamePanel.mapEnemy[0][i].worldY = gamePanel.ukuranKotak * 77;
            i++;

            gamePanel.mapEnemy[0][i] = new Hog(gamePanel);
            gamePanel.mapEnemy[0][i].worldX = gamePanel.ukuranKotak * 46;
            gamePanel.mapEnemy[0][i].worldY = gamePanel.ukuranKotak * 80;
            i++;

            gamePanel.mapEnemy[0][i] = new Hog(gamePanel);
            gamePanel.mapEnemy[0][i].worldX = gamePanel.ukuranKotak * 50;
            gamePanel.mapEnemy[0][i].worldY = gamePanel.ukuranKotak * 84;
            i++;

            gamePanel.mapEnemy[0][i] = new Hog(gamePanel);
            gamePanel.mapEnemy[0][i].worldX = gamePanel.ukuranKotak * 50;
            gamePanel.mapEnemy[0][i].worldY = gamePanel.ukuranKotak * 86;
            i++;

            gamePanel.mapEnemy[0][i] = new Hog(gamePanel);
            gamePanel.mapEnemy[0][i].worldX = gamePanel.ukuranKotak * 41;
            gamePanel.mapEnemy[0][i].worldY = gamePanel.ukuranKotak * 84;
            i++;

            // Orc
            gamePanel.mapEnemy[0][i] = new Orc(gamePanel);
            gamePanel.mapEnemy[0][i].worldX = gamePanel.ukuranKotak * 25;
            gamePanel.mapEnemy[0][i].worldY = gamePanel.ukuranKotak * 78;
            i++;

            gamePanel.mapEnemy[0][i] = new Orc(gamePanel);
            gamePanel.mapEnemy[0][i].worldX = gamePanel.ukuranKotak * 20;
            gamePanel.mapEnemy[0][i].worldY = gamePanel.ukuranKotak * 77;
            i++;

            gamePanel.mapEnemy[0][i] = new Orc(gamePanel);
            gamePanel.mapEnemy[0][i].worldX = gamePanel.ukuranKotak * 27;
            gamePanel.mapEnemy[0][i].worldY = gamePanel.ukuranKotak * 85;
            i++;
        }

        // ================= MAP 1 =================
        else if (mapIndex == 1) {

            gamePanel.mapEnemy[1][i] = new Crow(gamePanel);
            gamePanel.mapEnemy[1][i].worldX = gamePanel.ukuranKotak * 54;
            gamePanel.mapEnemy[1][i].worldY = gamePanel.ukuranKotak * 70;
            i++;
            gamePanel.mapEnemy[1][i] = new Crow(gamePanel);
            gamePanel.mapEnemy[1][i].worldX = gamePanel.ukuranKotak * 45;
            gamePanel.mapEnemy[1][i].worldY = gamePanel.ukuranKotak * 71;
            i++;
            gamePanel.mapEnemy[1][i] = new Crow(gamePanel);
            gamePanel.mapEnemy[1][i].worldX = gamePanel.ukuranKotak * 35;
            gamePanel.mapEnemy[1][i].worldY = gamePanel.ukuranKotak * 70;
            i++;

            gamePanel.mapEnemy[1][i] = new Enemy.Skeleton(gamePanel);
            gamePanel.mapEnemy[1][i].worldX = gamePanel.ukuranKotak * 28;
            gamePanel.mapEnemy[1][i].worldY = gamePanel.ukuranKotak * 63;
            i++;

            gamePanel.mapEnemy[1][i] = new Enemy.Skeleton(gamePanel);
            gamePanel.mapEnemy[1][i].worldX = gamePanel.ukuranKotak * 36;
            gamePanel.mapEnemy[1][i].worldY = gamePanel.ukuranKotak * 63;
            i++;

            gamePanel.mapEnemy[1][i] = new Enemy.Skeleton(gamePanel);
            gamePanel.mapEnemy[1][i].worldX = gamePanel.ukuranKotak * 43;
            gamePanel.mapEnemy[1][i].worldY = gamePanel.ukuranKotak * 63;
            i++;

            gamePanel.mapEnemy[1][i] = new Enemy.Demon(gamePanel);
            gamePanel.mapEnemy[1][i].worldX = gamePanel.ukuranKotak * 35;
            gamePanel.mapEnemy[1][i].worldY = gamePanel.ukuranKotak * 52;
            i++;

            gamePanel.mapEnemy[1][i] = new Enemy.Demon(gamePanel);
            gamePanel.mapEnemy[1][i].worldX = gamePanel.ukuranKotak * 47;
            gamePanel.mapEnemy[1][i].worldY = gamePanel.ukuranKotak * 55;
            i++;

            gamePanel.mapEnemy[1][i] = new Enemy.Demon(gamePanel);
            gamePanel.mapEnemy[1][i].worldX = gamePanel.ukuranKotak * 45;
            gamePanel.mapEnemy[1][i].worldY = gamePanel.ukuranKotak * 44;
            i++;
        } else {
            gamePanel.mapEnemy[2][i] = new Enemy.BossRock(gamePanel);
            gamePanel.mapEnemy[2][i].worldX = gamePanel.ukuranKotak * 15;
            gamePanel.mapEnemy[2][i].worldY = gamePanel.ukuranKotak * 17;
            i++;
        }
    }
}
