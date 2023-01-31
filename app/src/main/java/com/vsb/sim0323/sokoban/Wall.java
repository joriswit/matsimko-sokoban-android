package com.vsb.sim0323.sokoban;

public class Wall extends GameObject {
    public Wall(TileType type, Board board) {
        super(type, board);
    }

    @Override
    public boolean canMoveX(int x, int y, int dx) {
        return false;
    }

    @Override
    public void moveX(int x, int y, int dx) {

    }

    @Override
    public boolean canMoveY(int x, int y, int dy) {
        return false;
    }

    @Override
    public void moveY(int x, int y, int dy) {

    }
}
