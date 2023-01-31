package com.vsb.sim0323.sokoban;

public class Box extends GameObject {

    public Box(TileType type, Board board) {
        super(type, board);
    }

    @Override
    public boolean canMoveX(int x, int y, int dx) {
        Tile nextTile = getBoard().getTile(x + dx, y);
        //Box can move only if the next tile is empty
        return nextTile.isEmpty();
    }

    @Override
    public void moveX(int x, int y, int dx) {
        Tile nextTile = getBoard().getTile(x + dx, y);
        nextTile.setGameObject(this);
    }

    @Override
    public boolean canMoveY(int x, int y, int dy) {
        Tile nextTile = getBoard().getTile(x, y + dy);
        //Box can move only if the next tile is empty
        return nextTile.isEmpty();
    }

    @Override
    public void moveY(int x, int y, int dy) {
        Tile nextTile = getBoard().getTile(x, y + dy);
        nextTile.setGameObject(this);
    }
}
