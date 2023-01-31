package com.vsb.sim0323.sokoban;

import java.io.Serializable;

public class Level implements Serializable {
    private int id;
    private int width, height;
    private int[] data;
    private String name;

    public Level(String name) {
        this.name = name;
    }

    public Level() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
