package sprites;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpriteUtils {

    /**
     * Sets the opacity of the provided image
     * @param originalImage image that we're changing the opacity of
     * @param opacity the opacity we are setting the image to
     */
    public static BufferedImage setImageOpacity(BufferedImage originalImage, float opacity) {
        // Create a new BufferedImage with transparency support
        BufferedImage newImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        // Get graphics context for the new image
        Graphics2D g2d = newImage.createGraphics();

        // Set up alpha composite
        AlphaComposite alphaChannel = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER,
                opacity
        );
        g2d.setComposite(alphaChannel);

        // Draw the original image with new opacity
        g2d.drawImage(originalImage, 0, 0, null);

        // Clean up
        g2d.dispose();

        return newImage;
    }
}
