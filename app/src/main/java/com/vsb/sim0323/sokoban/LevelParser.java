package com.vsb.sim0323.sokoban;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LevelParser {

    private final Map<Character, Integer> converterMap;
    private final int levelDescriptionLineCount;

    LevelParser(Map<Character, Integer> converterMap, int levelDescriptionLineCount) {
        this.converterMap = converterMap;
        this.levelDescriptionLineCount = levelDescriptionLineCount;
    }

    public List<Level> parseLevels(InputStream levelsIS) throws IOException {
        List<Level> levels = new ArrayList<>();

        BufferedReader levelsReader = new BufferedReader(new InputStreamReader(levelsIS));
        String line;
        String levelDesc;
        Level level;
        while((line = levelsReader.readLine()) != null) {
            levelDesc = line;
            level = new Level(levelDesc);

            for(int i = 1; i < levelDescriptionLineCount; i++) {
                line = levelsReader.readLine(); //ignore other descriptions
            }

            try {
                boolean moreLevels = readLevelData(levelsReader, level);
                levels.add(level);
                level.setId(levels.size());
                if(!moreLevels) {
                    break;
                }
            } catch (InvalidLevelException e) {
                continue; //just ignore this level
            }


        }

        return levels;
    }

    /**
     * @return true if there are more levels to read
     */
    private boolean readLevelData(BufferedReader levelsReader, Level level) throws IOException, InvalidLevelException {
        List<int[]> convertedRows = new ArrayList<>();

        String line = levelsReader.readLine();
        //level ends with empty line or end of file
        while(line != null && !line.isEmpty()) { //.equals() instead of == for object comparison!
            convertedRows.add(convertRow(line));
            line = levelsReader.readLine();
        }

        int height = convertedRows.size();
        if(height == 0) {
            throw new InvalidLevelException();
        }
        //find out the actual width
        //convertedRows.stream(); //api 24+...
        int width = convertedRows.get(0).length;
        for(int i = 1; i < convertedRows.size(); i++) {
            int length = convertedRows.get(i).length;
            if(length > width) {
                width = length;
            }
        }
        //merge rows into one array and pad all rows up to width
        int[] levelData = new int[width * height];
        for(int r = 0; r < convertedRows.size(); r++) {
            int[] row = convertedRows.get(r);
            for(int c = 0; c < row.length; c++) {
                levelData[r * width + c] = row[c];
            }
            //pad the rest of the width
            for(int c = row.length; c < width; c++) {
                //TileType.OUTSIDE.n would require an algorithm to find out which empty spaces
                //at the start of rows are outside the walls and which aren't
                levelData[r * width + c] = TileType.EMPTY.n;
            }
        }

        level.setWidth(width);
        level.setHeight(height);
        level.setData(levelData);

        boolean moreLevels = (line == null) ? false : true;
        return moreLevels;
    }

    private int[] convertRow(String line) {
        int[] row = new int[line.length()];
        for(int i = 0; i < line.length(); i++) {
            row[i] = converterMap.get(line.charAt(i));
        }

        return row;
    }


}
