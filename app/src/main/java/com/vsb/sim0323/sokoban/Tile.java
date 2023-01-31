package com.vsb.sim0323.sokoban;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Tile {
    private int x, y;

    private boolean isGoal; //isStorage

    //state design pattern
    private GameObject gameObject;


    public Tile(int x, int y, boolean isGoal, GameObject gameObject) {
        this.x = x;
        this.y = y;
        this.isGoal = isGoal;
        this.gameObject = gameObject;
    }

    public boolean isEmpty() {
        return gameObject == null;
    }

    public boolean isGoal() {
        return isGoal;
    }

    public boolean canMoveX(int dx) {
        return isEmpty() ? false : gameObject.canMoveX(x, y, dx);
    }

    public boolean canMoveY(int dy) {
        return isEmpty() ? false : gameObject.canMoveY(x, y, dy);
    }

    public void moveX(int dx) {
        if(!isEmpty()) {
            gameObject.moveX(x, y, dx);
            gameObject = null;
        }
    }

    public void moveY(int dy) {
        if(!isEmpty()) {
            gameObject.moveY(x, y, dy);
            gameObject = null;
        }
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public void draw(Canvas canvas, Bitmap[] bmp, int width, int height) {
        TileType type = null;
        if(isEmpty()) {
            if(isGoal()) {
                type = TileType.GOAL;
            }
            else {
                type = TileType.EMPTY;
            }
        }
        else {
            if(isGoal() && gameObject instanceof Box) { //or check if type equals BOX
                type = TileType.BOX_OK;
            }
            else if(isGoal() && gameObject instanceof Player) {
                type = TileType.HERO_OK;
            }
            else {
                type = gameObject.getType();
            }
        }

        Rect rect = new Rect(width * x, height * y, width * (x + 1), height * (y + 1));
        canvas.drawBitmap(bmp[type.n], null, rect, null);

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean hasBox() {
        return gameObject instanceof Box;
    }
}
