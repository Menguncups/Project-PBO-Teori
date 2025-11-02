package Main;

import Entity.Entity;

public class CollisioinCheck {
    GamePanel gamePanel;

    public CollisioinCheck(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void checkTile(Entity entity) {
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gamePanel.ukuranKotak;
        int entityRightCol = entityRightWorldX / gamePanel.ukuranKotak;
        int entityTopRow = entityTopWorldY / gamePanel.ukuranKotak;
        int entityBottomRow = entityBottomWorldY / gamePanel.ukuranKotak;

        int tileNum1, tileNum2;
        
        switch (entity.arah) {
            case "atas":
                // Cek tile di atas player (kurangi Y)
                entityTopRow = (entityTopWorldY - entity.kecepatanBergerak) / gamePanel.ukuranKotak;
                tileNum1 = gamePanel.pengelolaMap.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gamePanel.pengelolaMap.mapTileNum[entityRightCol][entityTopRow];

                if (gamePanel.pengelolaMap.map[tileNum1].bertabrakan == true
                        || gamePanel.pengelolaMap.map[tileNum2].bertabrakan == true) {
                    entity.collisionOn = true;
                }
                break;
                
            case "bawah":
                // ✅ FIXED: Cek tile di bawah player (tambah Y)
                entityBottomRow = (entityBottomWorldY + entity.kecepatanBergerak) / gamePanel.ukuranKotak;
                tileNum1 = gamePanel.pengelolaMap.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gamePanel.pengelolaMap.mapTileNum[entityRightCol][entityBottomRow];

                if (gamePanel.pengelolaMap.map[tileNum1].bertabrakan == true
                        || gamePanel.pengelolaMap.map[tileNum2].bertabrakan == true) {
                    entity.collisionOn = true;
                }
                break;
                
            case "kiri":
                // ✅ FIXED: Cek tile di kiri player (kurangi X)
                entityLeftCol = (entityLeftWorldX - entity.kecepatanBergerak) / gamePanel.ukuranKotak;
                tileNum1 = gamePanel.pengelolaMap.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gamePanel.pengelolaMap.mapTileNum[entityLeftCol][entityBottomRow];

                if (gamePanel.pengelolaMap.map[tileNum1].bertabrakan == true
                        || gamePanel.pengelolaMap.map[tileNum2].bertabrakan == true) {
                    entity.collisionOn = true;
                }
                break;
                
            case "kanan":
                // Cek tile di kanan player (tambah X)
                entityRightCol = (entityRightWorldX + entity.kecepatanBergerak) / gamePanel.ukuranKotak;
                tileNum1 = gamePanel.pengelolaMap.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gamePanel.pengelolaMap.mapTileNum[entityRightCol][entityBottomRow];

                if (gamePanel.pengelolaMap.map[tileNum1].bertabrakan == true
                        || gamePanel.pengelolaMap.map[tileNum2].bertabrakan == true) {
                    entity.collisionOn = true;
                }
                break;
                
            default:
                break;
        }
    }
}