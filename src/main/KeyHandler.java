package main;

import java.awt.event.*;

public class KeyHandler implements KeyListener, MouseListener, MouseMotionListener {

    public boolean keyDown, leftPressed, rightPressed, upPressed, downPressed, mousePressed;
    public boolean upArrowPressed, downArrowPressed;
    public boolean previousUpArrowPressed, previousDownArrowPressed;
    public boolean ePressed, previousEPressed;
    public boolean mPressed, previousMPressed;
    public boolean previousMousePressed;

    public boolean gravePressed, previousGravePressed;

    public int mouseX, mouseY;

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

        if (code == KeyEvent.VK_M) {
            mPressed = true;
            keyDown = true;
        }

        if (code == 192) {
            gravePressed = true;
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

        if (code == KeyEvent.VK_M) {
            mPressed = false;
        }

        if (code == KeyEvent.VK_BACK_QUOTE) {
            gravePressed = false;
        }

        if (!rightPressed && !leftPressed && !downPressed && !upPressed && !upArrowPressed && !downArrowPressed && !ePressed && !mPressed && !gravePressed) {
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

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public void update() {
        previousDownArrowPressed = downArrowPressed;
        previousUpArrowPressed = upArrowPressed;
        previousEPressed = ePressed;
        previousMPressed = mPressed;
        previousGravePressed = gravePressed;
        previousMousePressed = mousePressed;
    }
}
