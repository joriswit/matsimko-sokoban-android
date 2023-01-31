package com.vsb.sim0323.sokoban;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
simple usage in MainActivity:

DBHelper dbHelper = new DBHelper(this);
dbHelper.insertContact("RADOVAN FUSEK");
ArrayList<String> myList = dbHelper.getAllContacts();

ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, myList);
ListView myL = findViewById(R.id.myList);
myL.setAdapter(arrayAdapter);

<ListView
    android:id="@+id/myList"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>

*/


public class SokoDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SokoDB.db";

    private static final String SQL_CREATE_LEVEL_TABLE =
            "CREATE TABLE level ( " +
                    "id INTEGER PRIMARY KEY, " +
                    "name TEXT, " +
                    "data BLOB NOT NULL, " +
                    "width INTEGER NOT NULL, " +
                    "height INTEGER NOT NULL " +
                    "); ";
    private static final String SQL_CREATE_SCORE_TABLE =
            "CREATE TABLE score (" +
                    "id INTEGER PRIMARY KEY, " +
                    "score INTEGER NOT NULL, " +
                    "is_completed INTEGER DEFAULT 0, " +
                    "level_id INTEGER NOT NULL, " +
                    "FOREIGN KEY(level_id) REFERENCES level(id) " +
                    ") ";

    private static final String SQL_GET_ITEM_DATA_FOR_ALL_LEVELS =
            "SELECT level.id, level.name, level.data, level.width, level.height," +
                    " max(score.score) AS highscore, sum(score.is_completed) AS is_completed " + //if the sum is > 0, it was completed
                    "FROM level " +
                    "LEFT JOIN score ON level.id = score.level_id " +
                    "GROUP BY level.id, level.name, level.data ";

    private static final String SQL_GET_LEVEL_BY_ID =
            "SELECT id, name, data, width, height " +
                    "FROM level " +
                    "WHERE id = ? ";

    private static final String SQL_GET_SCORES_BY_LEVEL_ID =
            "SELECT id, score, is_completed " +
                    "FROM score " +
                    "WHERE level_id = ? ";

    private final LevelLoader levelLoader;


    public SokoDB(Context context, LevelLoader levelLoader){
        super(context,DATABASE_NAME,null,1);
        this.levelLoader = levelLoader;
    }

    /*On an INSERT, if the ROWID or INTEGER PRIMARY KEY column is not explicitly given a value,
     then it will be filled automatically with an unused integer, usually one more than the largest ROWID currently in use.
     This is true regardless of whether or not the AUTOINCREMENT keyword is used.*/



    private byte[] intArrayToByteArray ( int[] arr ) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            for(int n : arr) {
                dos.writeInt(n);
            }
            dos.flush();
            return bos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private int[] byteArrayToIntArray ( byte[] arr ) {
        ByteArrayInputStream bis = new ByteArrayInputStream(arr);
        DataInputStream dis = new DataInputStream(bis);
        List<Integer> list = new LinkedList();
        int n;
        try {
            while(true) {
                n = dis.readInt();
                list.add(n);
            }
        } catch (IOException e) {
            //ending reading at end of "file"
        }

        int[] intArr = new int[list.size()];
        for(int i = 0; i < list.size(); i++) {
            intArr[i] = list.get(i);
        }
        return intArr;
    }

    public void insertLevel(Level level, SQLiteDatabase db) {
        //can't call getWrtableDatabase in onCreate, so I send it as paramater in these cases
        if(db == null) {
            db = this.getWritableDatabase();
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", level.getId());
        contentValues.put("name", level.getName());
        contentValues.put("data", intArrayToByteArray(level.getData()));
        contentValues.put("width", level.getWidth());
        contentValues.put("height", level.getHeight());
        db.insert("level", null, contentValues);
    }

    public void insertScore(int levelId, int score, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("level_id", levelId);
        contentValues.put("score", score);
        contentValues.put("is_completed", isCompleted ? 1 : 0);
        db.insert("score", null, contentValues);
    }

    private Level createLevelFromCursor(Cursor c) {
        Level level = new Level();
        level.setId(c.getInt(c.getColumnIndex("id")));
        level.setName(c.getString(c.getColumnIndex("name")));
        level.setWidth(c.getInt(c.getColumnIndex("width")));
        level.setHeight(c.getInt(c.getColumnIndex("height")));
        int[] levelData = byteArrayToIntArray(c.getBlob(c.getColumnIndex("data")));
        level.setData(levelData);

        return level;
    }

    public List<LevelRecycleViewAdapter.ItemData> getItemDataForAllLevels() {
        List<LevelRecycleViewAdapter.ItemData> itemsData = new ArrayList<LevelRecycleViewAdapter.ItemData>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res =  db.rawQuery( SQL_GET_ITEM_DATA_FOR_ALL_LEVELS, null );
        res.moveToFirst();
        while(res.isAfterLast() == false){

            Level level = createLevelFromCursor(res);
            int score = res.getInt(res.getColumnIndex("highscore"));
            boolean isCompleted = res.getInt(res.getColumnIndex("is_completed")) > 0 ? true : false;
            LevelRecycleViewAdapter.ItemData itemData =
                    new LevelRecycleViewAdapter.ItemData(level, score, isCompleted);
            itemsData.add(itemData);
            res.moveToNext();
        }
        return itemsData;
    }

    public Level getLevel(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(SQL_GET_LEVEL_BY_ID, new String[] {String.valueOf(id)});
        if(res.moveToFirst()) {
            Level level = createLevelFromCursor(res);
            return level;
        }
        return null;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_LEVEL_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SCORE_TABLE);

        try {
            List<Level> levels = levelLoader.loadLevels("levels.txt");
            for(Level level : levels) {
                insertLevel(level, sqLiteDatabase);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public List<Score> getScoresForLevel(int level_id) {
        List<Score> scores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res =  db.rawQuery( SQL_GET_SCORES_BY_LEVEL_ID, new String[] {String.valueOf(level_id)} );
        res.moveToFirst();
        while(res.isAfterLast() == false){

            int id = res.getInt(res.getColumnIndex("id"));
            int scoreValue = res.getInt(res.getColumnIndex("score"));
            boolean isCompleted = res.getInt(res.getColumnIndex("is_completed")) == 1 ? true : false;

            Score score = new Score(id, scoreValue, isCompleted);
            scores.add(score);
            res.moveToNext();
        }
        return scores;
    }
}