package com.vsb.sim0323.sokoban;

public class Player extends GameObject {

    //the player's position is often wanted so I will keep his current tile (other GameObject will make do with position sent in methods)
    private Tile tile;

    public Player(TileType type, Board board) {
        super(type, board);
    }

    @Override
    public boolean canMoveX(int x, int y, int dx) {
        Tile nextTile = getBoard().getTile(x + dx, y);
        return nextTile.isEmpty() || nextTile.canMoveX(dx);
    }

    @Override
    public void moveX(int x, int y, int dx) {
        Tile nextTile = getBoard().getTile(x + dx, y);
        if(!nextTile.isEmpty()) {
            nextTile.moveX(dx);
        }
        nextTile.setGameObject(this);
        setTile(nextTile);
    }

    @Override
    public boolean canMoveY(int x, int y, int dy) {
        Tile nextTile = getBoard().getTile(x, y + dy);
        return nextTile.isEmpty() || nextTile.canMoveY(dy);
    }

    @Override
    public void moveY(int x, int y, int dy) {
        Tile nextTile = getBoard().getTile(x, y + dy);
        if(!nextTile.isEmpty()) {
            nextTile.moveY(dy);
        }
        nextTile.setGameObject(this);
        setTile(nextTile);
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public int getX() {
        return tile.getX();
    }

    public int getY() {
        return tile.getY();
    }


    public boolean canMoveX(int dx) {
        return tile.canMoveX(dx);
    }

    public boolean canMoveY(int dy) {
        return tile.canMoveY(dy);
    }

    public void moveX(int dx) {
        tile.moveX(dx);
    }

    public void moveY(int dy) {
        tile.moveY(dy);
    }
}
