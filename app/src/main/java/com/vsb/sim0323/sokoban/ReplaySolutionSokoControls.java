package com.vsb.sim0323.sokoban;

import android.view.MotionEvent;

public class ReplaySolutionSokoControls implements SokoControls {
    private final String solution;

    private int replayPosition = 0;

    public ReplaySolutionSokoControls(String solution) {
        this.solution = solution;
    }

    public Character getNextMove() {
        return solution.charAt(replayPosition++);
    }

    public boolean isAtEndOfSolution() {
        return solution.length() - 1 < replayPosition;
    }

    public int getSolutionLength() {
        return solution.length();
    }

    @Override
    public boolean handle(MotionEvent e, int tileWidth, int tileHeight) {
        return true;
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
