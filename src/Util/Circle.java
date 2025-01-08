package Util;

import main.GamePanel;

import java.awt.*;

public class Circle {
    private double centerX;
    private double centerY;
    private double radius;

    public Circle(double x, double y, double radius) {
        this.centerX = x + ((double) GamePanel.tileSize / 2);
        this.centerY = y + ((double) GamePanel.tileSize / 2);
        this.radius = ((double) GamePanel.tileSize / 2) + radius;
    }

    // Check if a point is inside the circle
    public boolean contains(double x, double y) {
        // Using distance formula: d = √((x₂-x₁)² + (y₂-y₁)²)
        double distance = Math.sqrt(
                Math.pow(x - centerX, 2) +
                        Math.pow(y - centerY, 2)
        );
        return distance <= radius;
    }

    // Check if a rectangle intersects with the circle
    public boolean intersects(Rectangle rect) {
        // Find the closest point on the rectangle to the circle's center
        double closestX = Math.max(rect.getX(), Math.min(centerX, rect.getX() + rect.getWidth()));
        double closestY = Math.max(rect.getY(), Math.min(centerY, rect.getY() + rect.getHeight()));

        // Calculate the distance between the circle's center and this closest point
        double distance = Math.sqrt(
                Math.pow(closestX - centerX, 2) +
                        Math.pow(closestY - centerY, 2)
        );

        return distance <= radius;
    }

    // Check if another circle intersects with this circle
    public boolean intersects(Circle other) {
        double distance = Math.sqrt(
                Math.pow(other.centerX - this.centerX, 2) +
                        Math.pow(other.centerY - this.centerY, 2)
        );
        return distance <= (this.radius + other.radius);
    }

    // Getters and setters
    public double getCenterX() { return centerX; }
    public double getCenterY() { return centerY; }
    public double getRadius() { return radius; }
    public void setCenterX(double x) { this.centerX = x; }
    public void setCenterY(double y) { this.centerY = y; }
    public void setRadius(double r) { this.radius = r; }
}
