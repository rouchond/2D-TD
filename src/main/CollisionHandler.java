package main;

import Util.Vector2;
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
                    entity.tileCollisionOn = true;
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
                    entity.tileCollisionOn = true;
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
                    entity.tileCollisionOn = true;
                }
                break;
            case EntityUtil.Direction.DOWN:
                entityBottomRow = (int)((entityBottomWorldY + entity.speed)/GamePanel.tileSize);
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];

                isColliding = (tileNum1 != null && tileNum1.collision) || (tileNum2 != null && tileNum2.collision);

                if (isColliding){
                    entity.tileCollisionOn = true;
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
                    entity.tileCollisionOn = true;
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
                    entity.tileCollisionOn = true;
                }
                break;
            case EntityUtil.Direction.LEFT:
                entityLeftCol = (int)((entityLeftWorldX - entity.speed)/GamePanel.tileSize);
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];

                isColliding = (tileNum1 != null && tileNum1.collision) || (tileNum2 != null && tileNum2.collision);

                if (isColliding){
                    entity.tileCollisionOn = true;
                }
                break;
            case EntityUtil.Direction.RIGHT:
                entityRightCol = (int)((entityRightWorldX + entity.speed)/GamePanel.tileSize);
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];

                isColliding = (tileNum1 != null && tileNum1.collision) || (tileNum2 != null && tileNum2.collision);

                if (isColliding){
                    entity.tileCollisionOn = true;
                }
                break;
        }
    }

    /**
     * Checks how far an entity can move before colliding or reaching an empty tile
     * returns a float array that contains the x and y positions the entity can go to
     * @param entity
     * @return
     */
    public float[] checkIncremental(Entity entity, Vector2 dir) {
        float xComp = 0, yComp = 0;

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
                for (int i = 0; i < entity.speed; i += 2) {
                    float incY = i + 2;
                    entityTopRow = (int) ((entityTopWorldY - incY) / GamePanel.tileSize);
                    tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];

                    isColliding = (tileNum1 != null && tileNum1.collision) || (tileNum2 != null && tileNum2.collision);

                    if (isColliding) {
                        entity.tileCollisionOn = true;
                        break;
                    }

                    yComp = -incY;
                }

                if (yComp != 0) {
                    yComp = -entity.speed;
                }
                break;

            case UP_LEFT:
                for (int i = 0; i < entity.speed; i += 2) {
                    float inc = i + 2;
                    entityTopRow = (int) ((entityTopWorldY - inc) / GamePanel.tileSize);
                    tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                    entityLeftCol = (int) ((entityLeftWorldX - inc) / GamePanel.tileSize);
                    tileNum3 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];

                    isColliding = (tileNum1 != null && tileNum1.collision) || (tileNum2 != null && tileNum2.collision || (tileNum2 != null && tileNum3.collision));

                    if (isColliding) {
                        entity.tileCollisionOn = true;
                        break;
                    }

                    xComp = -inc;
                    yComp = -inc;
                }

                if (yComp != 0) {
                    yComp = -entity.speed;
                }

                if (xComp != 0) {
                    xComp = -entity.speed;
                }
                break;
            case UP_RIGHT:
                for (int i = 0; i < entity.speed; i += 2) {
                    float inc = i + 2;
                    entityTopRow = (int) ((entityTopWorldY - inc) / GamePanel.tileSize);
                    tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                    entityRightCol = (int) ((entityRightWorldX + inc) / GamePanel.tileSize);
                    tileNum3 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];

                    isColliding = (tileNum1 != null && tileNum1.collision) || (tileNum2 != null && tileNum2.collision || (tileNum2 != null && tileNum3.collision));

                    if (isColliding) {
                        entity.tileCollisionOn = true;
                        break;
                    }

                    xComp = inc;
                    yComp = -inc;
                }

                if (yComp != 0) {
                    yComp = -entity.speed;
                }

                if (xComp != 0) {
                    xComp = entity.speed;
                }
                break;
            case EntityUtil.Direction.DOWN:
                for (int i = 0; i < entity.speed; i += 2) {
                    float inc = i + 2;
                    entityBottomRow = (int) ((entityBottomWorldY + inc) / GamePanel.tileSize);
                    tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                    tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];

                    isColliding = (tileNum1 != null && tileNum1.collision) || (tileNum2 != null && tileNum2.collision);

                    if (isColliding) {
                        entity.tileCollisionOn = true;
                        break;
                    }
                    yComp = inc;
                }
                if (yComp != 0) {
                    yComp = entity.speed;
                }

                break;
            case DOWN_LEFT:
                for (int i = 0; i < entity.speed; i += 2) {
                    float inc = i + 2;
                    entityBottomRow = (int) ((entityBottomWorldY + inc) / GamePanel.tileSize);
                    tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                    tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                    entityLeftCol = (int) ((entityLeftWorldX - inc) / GamePanel.tileSize);
                    tileNum3 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];

                    isColliding = (tileNum1 != null && tileNum1.collision) || (tileNum2 != null && tileNum2.collision || (tileNum2 != null && tileNum3.collision));

                    if (isColliding) {
                        entity.tileCollisionOn = true;
                        break;
                    }

                    xComp = -inc;
                    yComp = inc;
                }

                if (yComp != 0) {
                    yComp = entity.speed;
                }

                if (xComp != 0) {
                    xComp = -entity.speed;
                }
                break;
            case DOWN_RIGHT:
                for (int i = 0; i < entity.speed; i += 2) {
                    float inc = i + 2;
                    entityBottomRow = (int) ((entityBottomWorldY + inc) / GamePanel.tileSize);
                    tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                    tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                    entityRightCol = (int) ((entityRightWorldX + inc) / GamePanel.tileSize);
                    tileNum3 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];

                    isColliding = (tileNum1 != null && tileNum1.collision) || (tileNum2 != null && tileNum2.collision || (tileNum2 != null && tileNum3.collision));

                    if (isColliding) {
                        entity.tileCollisionOn = true;
                        break;
                    }

                    xComp = inc;
                    yComp = inc;
                }

                if (yComp != 0) {
                    yComp = -entity.speed;
                }

                if (xComp != 0) {
                    xComp = entity.speed;
                }
                break;
            case EntityUtil.Direction.LEFT:
                for (int i = 0; i < entity.speed; i += 2) {
                    float inc = i + 2;
                    entityLeftCol = (int) ((entityLeftWorldX - inc) / GamePanel.tileSize);
                    tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];

                    isColliding = (tileNum1 != null && tileNum1.collision) || (tileNum2 != null && tileNum2.collision);

                    if (isColliding) {
                        entity.tileCollisionOn = true;
                        break;
                    }

                    xComp = -inc;
                }

                if (xComp != 0) {
                    xComp = -entity.speed;
                }
                break;
            case EntityUtil.Direction.RIGHT:
                for (int i = 0; i < entity.speed; i += 2) {
                    float inc = i + 2;
                    entityRightCol = (int) ((entityRightWorldX + entity.speed) / GamePanel.tileSize);
                    tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];

                    isColliding = (tileNum1 != null && tileNum1.collision) || (tileNum2 != null && tileNum2.collision);

                    if (isColliding) {
                        entity.tileCollisionOn = true;
                    }

                    xComp = inc;
                }
                if (xComp != 0) {
                    xComp = entity.speed;
                }
                break;
        }

        float[] newPos = new float[2];
        newPos[0] = entity.worldX + xComp;
        newPos[1] = entity.worldY + yComp;

        if (entity.tileCollisionOn){
            System.out.println("Collided");
        }

        return newPos;
    }

}
