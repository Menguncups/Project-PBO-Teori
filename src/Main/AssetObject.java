package Main;

import Entity.NPCTua;
import Object.ObjectChest;
import Object.ObjectDoorLeft;
import Object.ObjectDoorRight;
import Object.ObjectKey;

public class AssetObject {

    GamePanel gamePanel;

    public AssetObject(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void setObject() {
        gamePanel.obj[0] = new ObjectKey();
        gamePanel.obj[0].worldX = gamePanel.ukuranKotak * 26;
        gamePanel.obj[0].worldY = gamePanel.ukuranKotak * 81;

        gamePanel.obj[1] = new ObjectDoorLeft();
        gamePanel.obj[1].worldX = gamePanel.ukuranKotak * 20;
        gamePanel.obj[1].worldY = gamePanel.ukuranKotak * 86;

        gamePanel.obj[2] = new ObjectDoorRight();
        gamePanel.obj[2].worldX = gamePanel.ukuranKotak * 21;
        gamePanel.obj[2].worldY = gamePanel.ukuranKotak * 86;

        gamePanel.obj[3] = new ObjectChest();
        gamePanel.obj[3].worldX = gamePanel.ukuranKotak * 13;
        gamePanel.obj[3].worldY = gamePanel.ukuranKotak * 18;

        gamePanel.obj[4] = new ObjectChest();
        gamePanel.obj[4].worldX = gamePanel.ukuranKotak * 75;
        gamePanel.obj[4].worldY = gamePanel.ukuranKotak * 38;

        gamePanel.obj[5] = new ObjectChest();
        gamePanel.obj[5].worldX = gamePanel.ukuranKotak * 78;
        gamePanel.obj[5].worldY = gamePanel.ukuranKotak * 55;

        gamePanel.obj[6] = new ObjectChest();
        gamePanel.obj[6].worldX = gamePanel.ukuranKotak * 52;
        gamePanel.obj[6].worldY = gamePanel.ukuranKotak * 88;
    }

    public void setNPC(){
        gamePanel.npc[0]=new NPCTua(gamePanel);
        gamePanel.npc[0].worldX = gamePanel.ukuranKotak*48;
        gamePanel.npc[0].worldY = gamePanel.ukuranKotak*35;
    }
}
