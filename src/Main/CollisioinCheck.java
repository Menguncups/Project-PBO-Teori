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

    public int checkObject(Entity entity, boolean player) {
        int index = 999;
        for (int i = 0; i < gamePanel.obj.length; i++) {
            if (gamePanel.obj[i] != null) {
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                gamePanel.obj[i].solidArea.x = gamePanel.obj[i].worldX + gamePanel.obj[i].solidArea.x;
                gamePanel.obj[i].solidArea.y = gamePanel.obj[i].worldY + gamePanel.obj[i].solidArea.y;
                switch (entity.arah) {
                    case "atas":
                        entity.solidArea.y -= entity.kecepatanBergerak;
                        if (entity.solidArea.intersects(gamePanel.obj[i].solidArea)) {
                            if (gamePanel.obj[i].collision == true) {
                                entity.collisionOn = true;
                            }
                            if (player == true) {
                                index = i;
                            }
                        }
                        break;

                    case "bawah":
                        entity.solidArea.y += entity.kecepatanBergerak;
                        if (entity.solidArea.intersects(gamePanel.obj[i].solidArea)) {
                            if (gamePanel.obj[i].collision == true) {
                                entity.collisionOn = true;
                            }
                            if (player == true) {
                                index = i;
                            }
                        }
                        break;

                    case "kiri":
                        entity.solidArea.x -= entity.kecepatanBergerak;
                        if (entity.solidArea.intersects(gamePanel.obj[i].solidArea)) {
                            if (gamePanel.obj[i].collision == true) {
                                entity.collisionOn = true;
                            }
                            if (player == true) {
                                index = i;
                            }
                        }
                        break;

                    case "kanan":
                        entity.solidArea.x += entity.kecepatanBergerak;
                        if (entity.solidArea.intersects(gamePanel.obj[i].solidArea)) {
                            if (gamePanel.obj[i].collision == true) {
                                entity.collisionOn = true;
                            }
                            if (player == true) {
                                index = i;
                            }
                        }
                        break;

                    default:
                        break;
                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gamePanel.obj[i].solidArea.x = gamePanel.obj[i].solidAreaDefaultX;
                gamePanel.obj[i].solidArea.y = gamePanel.obj[i].solidAreaDefaultY;
            }
        }
        return index;
    }

    // NPC atau monster
    public int checkEntity(Entity entity, Entity[] target) {
        int index = 999;
        for (int i = 0; i < target.length; i++) {
            if (target[i] != null) {
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;
                target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
                switch (entity.arah) {
                    case "atas":
                        entity.solidArea.y -= entity.kecepatanBergerak;
                        if (entity.solidArea.intersects(target[i].solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;

                    case "bawah":
                        entity.solidArea.y += entity.kecepatanBergerak;
                        if (entity.solidArea.intersects(target[i].solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;

                    case "kiri":
                        entity.solidArea.x -= entity.kecepatanBergerak;
                        if (entity.solidArea.intersects(target[i].solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;

                    case "kanan":
                        entity.solidArea.x += entity.kecepatanBergerak;
                        if (entity.solidArea.intersects(target[i].solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;

                    default:
                        break;
                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
            }
        }
        return index;
    }

    public void checkPlayer(Entity entity) {
        if (gamePanel.player != null) {
            entity.solidArea.x = entity.worldX + entity.solidArea.x;
            entity.solidArea.y = entity.worldY + entity.solidArea.y;

            gamePanel.player.solidArea.y = gamePanel.player.worldY + gamePanel.player.solidArea.y;
            gamePanel.player.solidArea.x = gamePanel.player.worldX + gamePanel.player.solidArea.x;
            switch (entity.arah) {
                case "atas":
                    entity.solidArea.y -= entity.kecepatanBergerak;
                    if (entity.solidArea.intersects(gamePanel.player.solidArea)) {
                        entity.collisionOn = true;
                    }
                    break;

                case "bawah":
                    entity.solidArea.y += entity.kecepatanBergerak;
                    if (entity.solidArea.intersects(gamePanel.player.solidArea)) {
                        entity.collisionOn = true;
                    }
                    break;

                case "kiri":
                    entity.solidArea.x -= entity.kecepatanBergerak;
                    if (entity.solidArea.intersects(gamePanel.player.solidArea)) {
                        entity.collisionOn = true;
                    }
                    break;

                case "kanan":
                    entity.solidArea.x += entity.kecepatanBergerak;
                    if (entity.solidArea.intersects(gamePanel.player.solidArea)) {
                        entity.collisionOn = true;
                    }
                    break;

                default:
                    break;
            }
            entity.solidArea.x = entity.solidAreaDefaultX;
            entity.solidArea.y = entity.solidAreaDefaultY;
            gamePanel.player.solidArea.x = gamePanel.player.solidAreaDefaultX;
            gamePanel.player.solidArea.y = gamePanel.player.solidAreaDefaultY;
        }
    }
}