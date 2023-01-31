package com.vsb.sim0323.sokoban;

import java.util.HashMap;
import java.util.Map;

public enum TileType {
    EMPTY(0), WALL(1), BOX(2), GOAL(3), HERO(4), BOX_OK(5), HERO_OK(6), OUTSIDE(7);

    private static Map<Integer, TileType> mapByNumber = new HashMap<>();
    public final int n;

    //load the map on class creation
    static {
        for(TileType value : values()) {
            mapByNumber.put(value.n, value);
        }
    }

    TileType(int n) {
        this.n = n;
    }

    public static TileType valueOfNumber(int n) {
        return mapByNumber.get(n);
    }
}
