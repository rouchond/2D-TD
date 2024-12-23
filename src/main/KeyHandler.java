package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean keyDown, leftPressed, rightPressed, upPressed, downPressed;

    @Override
    public void keyTyped(KeyEvent e) {
        // Will not implement
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_A) {
            leftPressed = true;
            keyDown = true;
        }

        if (code == KeyEvent.VK_D) {
            rightPressed = true;
            keyDown = true;
        }

        if (code == KeyEvent.VK_W) {
            upPressed = true;
            keyDown = true;
        }

        if (code == KeyEvent.VK_S) {
            downPressed = true;
            keyDown = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }

        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }

        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }

        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }

        if (!rightPressed && !leftPressed && !downPressed && !upPressed) {
            keyDown = false;
        }
    }
}
