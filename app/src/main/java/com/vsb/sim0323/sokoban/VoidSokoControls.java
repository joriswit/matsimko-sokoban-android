package com.vsb.sim0323.sokoban;

import android.view.MotionEvent;

//useless controls
public class VoidSokoControls implements SokoControls{

    @Override
    public boolean handle(MotionEvent e, int tileWidth, int tileHeight) {
        return false;
    }

    @Override
    public void setPlayer(Player player) { }

    @Override
    public boolean hasMovedX() {
        return false;
    }

    @Override
    public boolean hasMovedY() {
        return false;
    }

    @Override
    public int getDx() {
        return 0;
    }

    @Override
    public int getDy() {
        return 0;
    }
}
