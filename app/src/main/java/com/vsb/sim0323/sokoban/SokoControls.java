package com.vsb.sim0323.sokoban;

import android.view.MotionEvent;

public interface SokoControls {

    //returns true if user completed a motion command
    boolean handle(MotionEvent e, int tileWidth, int tileHeight);
    void setPlayer(Player player);

    boolean hasMovedX();
    boolean hasMovedY();
    int getDx();
    int getDy();
}
