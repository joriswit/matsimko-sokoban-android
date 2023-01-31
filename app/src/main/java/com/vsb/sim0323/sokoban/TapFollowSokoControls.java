package com.vsb.sim0323.sokoban;

import android.view.MotionEvent;

public class TapFollowSokoControls implements SokoControls {
    private Player player;
    private boolean hasMovedX;
    private boolean hasMovedY;
    private int dx;
    private int dy;

    public TapFollowSokoControls() {
        hasMovedY = hasMovedX = false;
        dx = dy = 0;
        this.player = null;
    }

    @Override
    public boolean handle(MotionEvent e, int tileWidth, int tileHeight) {
        hasMovedX = hasMovedY = false;
        dx = dy = 0;

        if(e.getAction() == MotionEvent.ACTION_DOWN) {
            float touchX = e.getX();
            float touchY = e.getY();
            float playerX = player.getX() * tileWidth;
            float playerY = player.getY() * tileHeight;

            //if the touch is vertically within the player's tile bounds
            if(touchX >= playerX && touchX < playerX + tileWidth) {
                //the player's bound is the bottom or the top of his tile, depending on which is closer to the touch
                float boundY = (touchY - playerY) > 0 ? playerY + tileHeight : playerY;
                float floatDy = ((touchY - boundY) / (float) tileHeight);
                //round away from zero (there are other ways to do this, like Math.ceil(Math.abs(floatDy)) * ((floatDy > 0) ? 1 : -1);
                dy = (int) (floatDy < 0 ? Math.floor(floatDy) : Math.ceil(floatDy));
                if(dy != 0) {
                    hasMovedY = true;
                }
            }
            else if(touchY >= playerY && touchY < playerY + tileHeight) {
                //the player's bound is the left or the right of his tile, depending on which is closer to the touch
                float boundX = (touchX - playerX) > 0 ? playerX + tileWidth : playerX;
                float floatDx = (touchX - boundX) / (float) tileWidth;
                //round away from zero
                dx =  (int) (floatDx < 0 ? Math.floor(floatDx) : Math.ceil(floatDx));
                if (dx != 0) {
                    hasMovedX = true;
                }
            }
        }

        return hasMovedX || hasMovedY;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public boolean hasMovedX() {
        return hasMovedX;
    }

    @Override
    public boolean hasMovedY() {
        return hasMovedY;
    }


    @Override
    public int getDx() {
        return dx;
    }

    @Override
    public int getDy() {
        return dy;
    }
}
