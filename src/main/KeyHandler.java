package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class KeyHandler implements KeyListener, MouseListener {

    public boolean keyDown, leftPressed, rightPressed, upPressed, downPressed, mousePressed;
    public boolean upArrowPressed, downArrowPressed;
    public boolean previousUpArrowPressed, previousDownArrowPressed;
    public boolean ePressed, previousEPressed;

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

        if (code == KeyEvent.VK_UP) {
            upArrowPressed = true;
            keyDown = true;
        }

        if (code == KeyEvent.VK_DOWN) {
            downArrowPressed = true;
            keyDown = true;
        }

        if (code == KeyEvent.VK_E) {
            ePressed = true;
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

        if (code == MouseEvent.BUTTON1){
            mousePressed = false;
        }

        if (code == KeyEvent.VK_UP) {
            upArrowPressed = false;
        }

        if (code == KeyEvent.VK_DOWN) {
            downArrowPressed = false;
        }

        if (code == KeyEvent.VK_E) {
            ePressed = false;
        }

        if (!rightPressed && !leftPressed && !downPressed && !upPressed && !upArrowPressed && !downArrowPressed && !ePressed) {
            keyDown = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Will not implement
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            mousePressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            mousePressed = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Will not implement
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Will not implement
    }

    public void update() {
        previousDownArrowPressed = downArrowPressed;
        previousUpArrowPressed = upArrowPressed;
        previousEPressed = ePressed;
    }
}
