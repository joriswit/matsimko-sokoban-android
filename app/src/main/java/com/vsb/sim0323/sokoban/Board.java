package com.vsb.sim0323.sokoban;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Board implements Iterable<Tile> {
    private final Tile[] board;
    private final int width;
    private final int height;
    private Player player;
    private List<Tile> goalTiles = new ArrayList<>();

    Board(Level level) {
        this.height = level.getHeight();
        this.width = level.getWidth();
        int[] data = level.getData();
        this.board = new Tile[data.length];

        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {

                GameObject gameObject = null;
                boolean isGoal = false;
                TileType type = TileType.valueOfNumber(data[y * width + x]);
                switch(type) {
                    case WALL:
                    case OUTSIDE: //the out of map tiles work the same way as walls, though their methods won't ever be invoked anyway
                        gameObject = new Wall(type, this);
                        break;
                    case BOX:
                    case BOX_OK:
                        gameObject = new Box(type, this);
                        break;
                    case HERO:
                    case HERO_OK:
                        gameObject = new Player(type, this);
                        break;
                }
                switch(type) {
                    case GOAL:
                    case BOX_OK:
                    case HERO_OK:
                        isGoal = true;
                        break;
                }
                Tile tile = new Tile(x, y, isGoal, gameObject);
                if(gameObject instanceof Player) {
                    player = (Player) gameObject;
                    player.setTile(tile);
                }
                if(isGoal) {
                    goalTiles.add(tile);
                }
                board[y * width + x] = tile;
            }
        }
    }



    Tile getTile(int x, int y) {
        return board[y * width + x];
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isLevelWon() {
        for(Tile tile : goalTiles) {
            if(!tile.hasBox()) {
                return false;
            }
        }
        return true;
    }

    public int getReachedGoalCount() {
        int count = 0;
        for(Tile tile : goalTiles) {
            if(tile.hasBox()) {
                count++;
            }
        }
        return count;
    }


    public static class BoardIterator implements Iterator<Tile> {
        private Tile board[];
        private int idx;

        public BoardIterator(Tile board[]) {
            this.board = board;
            this.idx = 0;
        }

        @Override
        public boolean hasNext() {
            return idx < board.length;
        }

        @Override
        public Tile next() {
            Tile next = board[idx];
            idx++;
            return next;
        }

        @Override
        public void remove() {

        }
    }

    @NonNull
    @Override
    public Iterator<Tile> iterator() {
        return new BoardIterator(board);
    }
}
