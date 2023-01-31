package com.vsb.sim0323.sokoban;

public abstract class GameObject {
    private TileType type;
    private Board board;

    public GameObject(TileType type, Board board) {
        this.type = type;
        this.board = board;
    }

    //move() expects that the correctness of the move was verified with canMove()
    public abstract boolean canMoveX(int x, int y, int dx);
    public abstract void moveX(int x, int y, int dx);
    public abstract boolean canMoveY(int x, int y, int dy);
    public abstract void moveY(int x, int y, int dy);

    public TileType getType() {
        return type;
    }

    public Board getBoard() {
        return board;
    }
}
