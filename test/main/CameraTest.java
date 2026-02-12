package main;

import entity.Entity;
import org.junit.
        jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CameraTest {

    private Camera camera;
    private Entity target;

    @BeforeEach
    void setUp() {
        camera = new Camera(800, 600);
        target = new Entity();
        target.worldX = 0;
        target.worldY = 0;
    }

    @Test
    void constructorSetsScreenDimensions() {
        assertEquals(800, camera.screenWidth);
        assertEquals(600, camera.screenHeight);
    }

    @Test
    void initialPositionIsZero() {
        assertEquals(0f, camera.getX());
        assertEquals(0f, camera.getY());
    }

    @Test
    void updateSetsTargetBasedOnEntityPosition() {
        target.worldX = 500;
        target.worldY = 400;

        camera.update(target);

        // target should be entity position minus half screen
        assertEquals(500 - 400f, camera.targetX); // 500 - 800/2
        assertEquals(400 - 300f, camera.targetY); // 400 - 600/2
    }

    @Test
    void updateMovesPositionTowardTarget() {
        target.worldX = 500;
        target.worldY = 400;

        camera.update(target);

        // After one update, camera should have moved 10% toward the target (lerpSpeed = 0.1)
        float expectedTargetX = 500 - 400f; // 100
        float expectedTargetY = 400 - 300f; // 100
        float expectedX = expectedTargetX * 0.1f; // 10
        float expectedY = expectedTargetY * 0.1f; // 10

        assertEquals(expectedX, camera.getX(), 0.001f);
        assertEquals(expectedY, camera.getY(), 0.001f);
    }

    @Test
    void multipleUpdatesConvergeOnTarget() {
        target.worldX = 800;
        target.worldY = 600;

        // Run many updates so the camera converges
        for (int i = 0; i < 200; i++) {
            camera.update(target);
        }

        float expectedTargetX = 800 - 400f; // 400
        float expectedTargetY = 600 - 300f; // 300

        assertEquals(expectedTargetX, camera.getX(), 0.5f);
        assertEquals(expectedTargetY, camera.getY(), 0.5f);
    }

    @Test
    void toScreenXConvertsWorldToScreen() {
        target.worldX = 500;
        target.worldY = 400;

        // Run enough updates for the camera to settle
        for (int i = 0; i < 200; i++) {
            camera.update(target);
        }

        // Camera position should be approximately (100, 100)
        // toScreenX(500) should be approximately 500 - 100 = 400 (center of screen)
        int screenX = camera.toScreenX(500);
        assertEquals(400, screenX, 1);
    }

    @Test
    void toScreenYConvertsWorldToScreen() {
        target.worldX = 500;
        target.worldY = 400;

        for (int i = 0; i < 200; i++) {
            camera.update(target);
        }

        int screenY = camera.toScreenY(400);
        assertEquals(300, screenY, 1);
    }

    @Test
    void cameraTracksMovingTarget() {
        // Simulate a target moving over time
        for (int i = 0; i < 100; i++) {
            target.worldX = i * 5;
            target.worldY = i * 3;
            camera.update(target);
        }

        // Last iteration: i=99, worldX=495, worldY=297
        float finalTargetX = 495 - 400f; // 95
        float finalTargetY = 297 - 300f; // -3

        // Camera X should be positive since target moved far enough on X
        assertTrue(camera.getX() > 0);
        // Camera Y stays negative since worldY never exceeds screenHeight/2
        assertTrue(camera.getY() < 0);

        // Target values should match the last update
        assertEquals(finalTargetX, camera.targetX, 0.001f);
        assertEquals(finalTargetY, camera.targetY, 0.001f);
    }

    @Test
    void toScreenCoordinatesAreConsistentWithCameraPosition() {
        target.worldX = 1000;
        target.worldY = 800;
        camera.update(target);

        int worldX = 1000;
        int worldY = 800;
        int screenX = camera.toScreenX(worldX);
        int screenY = camera.toScreenY(worldY);

        // Screen coordinate = world coordinate - camera position
        assertEquals(Math.round(worldX - camera.getX()), screenX);
        assertEquals(Math.round(worldY - camera.getY()), screenY);
    }
}