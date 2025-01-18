package main;

import entity.Entity;
import entity.EntityUtil;
import tile.Tile;

public class CollisionHandler {
    GamePanel gp;

    /**
     * Creates a collision handler
     * @param gp The Game Panel
     */
    public CollisionHandler (GamePanel gp) {this.gp = gp;}

    /**
     * Checks adjacent tiles in the direction entity is moving in to see if there's a collision
     * @param entity The entity we're checking for a collision
     */
    public void checkTile(Entity entity) {
        double entityLeftWorldX = entity.worldX + entity.solidArea.x;
        double entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        double entityTopWorldY = entity.worldY + entity.solidArea.y;
        double entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = (int) entityLeftWorldX/ GamePanel.tileSize;
        int entityRightCol = (int) entityRightWorldX/ GamePanel.tileSize;
        int entityTopRow = (int) entityTopWorldY/GamePanel.tileSize;
        int entityBottomRow = (int) entityBottomWorldY/GamePanel.tileSize;

        Tile tileNum1, tileNum2, tileNum3;
        boolean isColliding;

        switch (entity.direction) {
            case EntityUtil.Direction.UP:
                entityTopRow = (int)((entityTopWorldY - entity.speed)/GamePanel.tileSize);
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];

                isColliding = (tileNum1 != null && tileNum1.collision) || (tileNum2 != null && tileNum2.collision);

                if (isColliding){
                    entity.collisionOn = true;
                }
                break;

            case UP_LEFT:
                entityTopRow = (int)((entityTopWorldY - entity.speed)/GamePanel.tileSize);
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                entityLeftCol = (int)((entityLeftWorldX - entity.speed)/GamePanel.tileSize);
                tileNum3 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];

                isColliding = (tileNum1 != null && tileNum1.collision) || (tileNum2 != null && tileNum2.collision || (tileNum2 != null && tileNum3.collision));

                if (isColliding){
                    entity.collisionOn = true;
                }
                break;
            case UP_RIGHT:
                entityTopRow = (int)((entityTopWorldY - entity.speed)/GamePanel.tileSize);
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                entityRightCol = (int)((entityRightWorldX + entity.speed)/GamePanel.tileSize);
                tileNum3 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];

                isColliding = (tileNum1 != null && tileNum1.collision) || (tileNum2 != null && tileNum2.collision || (tileNum2 != null && tileNum3.collision));

                if (isColliding){
                    entity.collisionOn = true;
                }
                break;
            case EntityUtil.Direction.DOWN:
                entityBottomRow = (int)((entityBottomWorldY + entity.speed)/GamePanel.tileSize);
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];

                isColliding = (tileNum1 != null && tileNum1.collision) || (tileNum2 != null && tileNum2.collision);

                if (isColliding){
                    entity.collisionOn = true;
                }
                break;
            case DOWN_LEFT:
                entityBottomRow = (int)((entityBottomWorldY + entity.speed)/GamePanel.tileSize);
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                entityLeftCol = (int)((entityLeftWorldX - entity.speed)/GamePanel.tileSize);
                tileNum3 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];

                isColliding = (tileNum1 != null && tileNum1.collision) || (tileNum2 != null && tileNum2.collision || (tileNum2 != null && tileNum3.collision));

                if (isColliding){
                    entity.collisionOn = true;
                }
                break;
            case DOWN_RIGHT:
                entityBottomRow = (int)((entityBottomWorldY + entity.speed)/GamePanel.tileSize);
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                entityRightCol = (int)((entityRightWorldX + entity.speed)/GamePanel.tileSize);
                tileNum3 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];

                isColliding = (tileNum1 != null && tileNum1.collision) || (tileNum2 != null && tileNum2.collision || (tileNum2 != null && tileNum3.collision));

                if (isColliding){
                    entity.collisionOn = true;
                }
                break;
            case EntityUtil.Direction.LEFT:
                entityLeftCol = (int)((entityLeftWorldX - entity.speed)/GamePanel.tileSize);
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];

                isColliding = (tileNum1 != null && tileNum1.collision) || (tileNum2 != null && tileNum2.collision);

                if (isColliding){
                    entity.collisionOn = true;
                }
                break;
            case EntityUtil.Direction.RIGHT:
                entityRightCol = (int)((entityRightWorldX + entity.speed)/GamePanel.tileSize);
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];

                isColliding = (tileNum1 != null && tileNum1.collision) || (tileNum2 != null && tileNum2.collision);

                if (isColliding){
                    entity.collisionOn = true;
                }
                break;
        }
    }

}
