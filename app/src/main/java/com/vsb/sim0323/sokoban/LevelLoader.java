package com.vsb.sim0323.sokoban;

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelLoader {
    private Map<Character, Integer> converterMap;
    private int levelDescriptionLineCount;

    private final AssetManager assetManager;


    public LevelLoader(AssetManager assetManager) {
        this.assetManager = assetManager;
        configure(' ', '#', '.', '$', '*', '@', '+', 1);
    }

    public void configure(char empty, char wall, char goal, char box, char box_ok, char hero, char hero_ok,
            int levelDescriptionLineCount) {
        converterMap = new HashMap<>();
        converterMap.put(empty, TileType.EMPTY.n);
        converterMap.put(wall, TileType.WALL.n);
        converterMap.put(goal, TileType.GOAL.n);
        converterMap.put(box, TileType.BOX.n);
        converterMap.put(box_ok, TileType.BOX_OK.n);
        converterMap.put(hero, TileType.HERO.n);
        converterMap.put(hero_ok, TileType.HERO_OK.n);

        this.levelDescriptionLineCount = levelDescriptionLineCount;
    }

    public List<Level> loadLevels(String filename) throws IOException {
        InputStream input = assetManager.open(filename);
        LevelParser parser = new LevelParser(converterMap, levelDescriptionLineCount);
        List<Level> levels = parser.parseLevels(input);
        return levels;
    }

    //close the IS or use enhanced try
}
