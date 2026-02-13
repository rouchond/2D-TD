# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A Java Swing 2D Tower Defense game. Plain Java project (no Maven/Gradle), built and run via IntelliJ IDEA with OpenJDK 21.

## Game Vision
Player will be constantly running around, defending themselves from enemies, dodging attacks, sometimes 
stationary to upgrade/build towers.

## Build & Run

```bash
# Compile source
javac -d out/production -cp src src/main/Main.java

# Run game (768x576 window, 60 FPS)
java -cp out/production main.Main

# Run tile map editor
java -cp out/production tile.Editor

# Compile tests
javac -d out/test -cp "src:lib/junit-jupiter-api-5.10.2.jar:lib/junit-platform-console-standalone-1.10.2.jar" test/**/*.java

# Run all tests
java -jar lib/junit-platform-console-standalone-1.10.2.jar --class-path "out/test:src:lib/junit-jupiter-api-5.10.2.jar:lib/junit-jupiter-engine-5.10.2.jar" --scan-class-path="out/test"

# Run a single test class
java -jar lib/junit-platform-console-standalone-1.10.2.jar --class-path "out/test:src:lib/junit-jupiter-api-5.10.2.jar:lib/junit-jupiter-engine-5.10.2.jar" --select-class=entity.pathfinding.PathfinderTest
```

## Architecture

### Game Loop (GamePanel.java)
`GamePanel` is the central hub. It owns all systems and runs a 60 FPS loop. Update order matters:
1. Player → 2. Towers → 3. Enemies → 4. Camera → 5. TileManager → 6. KeyHandler

Render order: Tiles → Enemies → Player → Debug overlays.

### Coordinate Systems
- **World coords**: Pixel-based (float). Entity positions use these.
- **Tile coords**: `row = worldY / tileSize`, `col = worldX / tileSize`. Grid is `Tile[row][col]` (row-major).
- **Screen coords**: `camera.toScreenX(worldX)` / `camera.toScreenY(worldY)`.
- `tileSize = 48` (16px × 3 scale). World is 100×100 tiles.

### State Machine Pattern
All entities use a generic `State<T extends EntityController<T>>` interface with `enterState`, `updateState`, `exitState`. Each entity type has its own controller (e.g., `PlayerController`, `PlaceholderController`) that manages state transitions via `changeState()`.

- **Player states**: `Idle` (waits for input) → `Moving` (physics-based movement)
- **Enemy states**: `Moving` (A* pathfinding + direct pursuit) → `Attack` (dash, currently WIP) → `Knockback` (recoil)

### Physics & Collision
`PhysicsHandler` applies velocity with deltaTime scaling. `CollisionHandler.checkIncremental()` implements wall-sliding: attempts full movement first, then steps pixel-by-pixel on each axis independently so entities slide along walls rather than stopping.

Tile collision checks the 4 corners of an entity's `solidArea` Rectangle against `mapTileNum[row][col].collision`.

### Pathfinding (A*)
`Pathfinder` operates on a node grid derived from the tile map. Supports 8-directional movement (diagonal cost = 1.41, cardinal = 1.0). Diagonal moves are blocked if either adjacent cardinal tile is unwalkable (no corner cutting). Enemies recalculate paths every 20 frames or when the player changes tiles, and switch to direct pursuit within 4 tiles.

### Tile System
`TileManager` loads maps from `res/maps/world.txt` (format: `tileSet,tileIndex,row,col`). Tile index 16 in "dungeon" set creates a `TowerPlacer` instead of a regular tile. `TileLoader` reads PNGs from `res/tiles/dungeon/`; indices 2-15 are walls.

### Camera
Lerps toward the player at 10% per frame. All rendering uses `camera.toScreenX/Y()` for world-to-screen conversion. `TileManager.draw()` culls tiles outside the viewport.

## Test Structure

Tests live in `test/` mirroring source packages. JUnit 5 JARs are in `lib/`. Test directory must be marked as Test Sources Root in IntelliJ.

Current test files:
- `test/main/CameraTest.java` — camera positioning, lerp convergence, coordinate conversion
- `test/entity/pathfinding/PathfindingNodeTest.java` — node costs, heuristic, equality
- `test/entity/pathfinding/PathfinderTest.java` — A* paths, wall avoidance, corridors, blocked grids

## Work Flows
We want to focus on test-driven development
When working on a new feature, follow this workflow:
1. Plan out the features, list out relevant algorithms, give pros and cons to an approach, talk about scalability
2. Confirm proposed changes to user (and relevant files that will be added/modified)
3. Plan out JUnit tests for each feature, and list out tests to try in game
4. Confirm proposed tests with user and make a MD file of the plan
5. Create the tests 
6. Implement the feature
7. Run the tests
8. Fix any bugs that may be exposed through the tests
9. Reiterate what in game tests the user should do to confirm functionality